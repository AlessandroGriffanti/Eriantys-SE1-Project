package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.ChosenAssistantCardMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseAssistantCard implements ControllerState{
    /**
     * This attribute is the counter of players that has already chosen their assistant
     */
    private int playersCounter = 0;
    /**
     * This attribute is the list of used cards and for each one of them an array of players
     * that used that card in the exact order the chose the card
     */
    private HashMap<Integer, ArrayList<Integer>> usedCards = new HashMap<>();
    /**
     * This attribute tells if the card chosen is legit or not
     */
    private boolean cardLegit = true;


    @Override
    public void nextState(Controller controller) {
        controller.setState(new Action_1());
    }

    /**
     * This method controls that the message received has an object 'assistant_chosen'
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
        String json = controller.getMsg();
        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        if(object.equals("assistant_chosen")){
            stateExecution(controller);
        }else{
            System.out.println("CHOOSE_ASSISTANT_CARD STATE: \nexpected message with object [assistant_chosen]" +
                               "\nreceived message with object["+ message.getObjectOfMessage() + "]");
        }
    }

    @Override
    public void stateExecution(Controller controller) {

        //operations for a single player
        ChosenAssistantCardMessage request = gson.fromJson(controller.getMsg(), ChosenAssistantCardMessage.class);

        //set current player
        controller.getMatch().setCurrentPlayer(request.getSender_ID());

            /*CONTROL:
            can the card be used? if it can be used we set cardLegit to 'true' (already set)*/
        if(usedCards.containsKey(request.getAssistantChosen())){
            //get the number of cards left
            int numberOfRemainingCards = controller.getMatch().numberOfRemainingCardsOfPlayer(request.getSender_ID());

            //if the player has more cards than the that already chosen then he can choose another assistant
            if(numberOfRemainingCards > usedCards.size()){
                cardLegit = false;
                NackMessage nack = new NackMessage("assistant");

                controller.sendMessageToPlayer(request.getSender_ID(), nack);

                System.out.println("NACK:" + controller.getPlayersNickname().get(request.getSender_ID()) +
                        ", you chose a not valid assistant card");
            }
        }else{
            // if there is not the entry for this then we create it
            usedCards.put(request.getAssistantChosen(), new ArrayList<>());
        }

        if(cardLegit){
            //modify the model
            controller.getMatch().useCard(request.getAssistantChosen());

            //add the player to the list of users of this card
            usedCards.get(request.getAssistantChosen()).add(request.getSender_ID());

            //count that another player has ended its turn
            playersCounter++;

            //create and send an ack message
            AckMessage response = new AckMessage();
            response.setSubObject("assistant");
            response.setRecipient(request.getSender_ID());
            response.setAssistantAlreadyUsedInThisRound(new ArrayList<>(usedCards.keySet()));


            //if all the players choose their assistant then we can go to the next state -> ACTION phase
            if(playersCounter == controller.getNumberOfPlayers()){

                // the next player is the first of the action phase
                response.setNextPlayer(defineActionPhaseOrder(controller));

                controller.setActionPhase(true);
                controller.setActionPhaseCurrentPlayer(controller.getActionPhaseOrder().get(0));

                controller.nextState();
            }else{
                response.setNextPlayer(controller.nextPlayer(request.getSender_ID()));
            }

            controller.sendMessageAsBroadcast(response);
        }
    }

    /**
     * This method defines the order of the action phase and sets the controller's attribute accordingly
     * @param controller reference to the controller in order to set the attribute 'actionPhaseOrder'
     * @return the ID of the first player of the action phase
     */
    private int defineActionPhaseOrder(Controller controller) {
        ArrayList<Integer> cardsOrder = new ArrayList<Integer>(usedCards.keySet());

        int n = cardsOrder.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++){
                if (cardsOrder.get(j) > cardsOrder.get(j + 1)) {
                    // swap cardsOrder[j+1] and cardsOrder[j]
                    int temp = cardsOrder.get(j);
                    cardsOrder.set(j, cardsOrder.get(j + 1));
                    cardsOrder.set(j + 1, temp);
                }
            }
        }

        ArrayList<Integer> playerOrder = new ArrayList<Integer>();

        // substitute the cards' values with players' ID
        for(int k : cardsOrder){
            for(int j: usedCards.get(k)){
                playerOrder.add(j);
            }
        }

        controller.setActionPhaseOrder(playerOrder);
        return playerOrder.get(0);
    }
}
