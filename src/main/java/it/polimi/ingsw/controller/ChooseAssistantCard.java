package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.AckMessage;
import it.polimi.ingsw.network.messages.ChosenAssistantCardMessage;
import it.polimi.ingsw.network.messages.NackMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseAssistantCard implements ControllerState{
    /**
     * This attribute is the counter of players that has already chosen their assistant
     */
    private int playersCounter = 0;
    /**
     * This attribute is the list of assistants already chosen by the players
     * key: order value that identifies the card
     * value: ID of the player that used it
     */
    private HashMap<Integer, Integer> usedCards;
    /**
     * This attribute tells if the card chosen is legit or not
     */
    private boolean cardLegit = true;

    @Override
    public void nextState(Controller controller) {

    }

    @Override
    public void stateExecution(Controller controller) {

        //operations for a single player
        ChosenAssistantCardMessage request = gson.fromJson(controller.getMsg(), ChosenAssistantCardMessage.class);

        if(!(request.getObjectOfMessage().equals("assistant_chosen"))){
            System.out.println("CHOOSE_ASSISTANT_CARD STATE: \nexpected message with object [assistant_chosen]\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }

        //set current player
        controller.getMatch().setCurrentPlayer(request.getSenderID());

        /*CONTROL:
        can the card be used? if it can be used we set cardLegit to 'true'*/
        if(usedCards.containsKey(request.getAssistantChosen())){
            //get the number of cards left
            int numberOfRemainingCards = controller.getMatch().numberOfRemainingCardsOfPlayer(request.getSenderID());

            //if the player has more cards than the already chosen ones then he can choose another assistant
            if(numberOfRemainingCards > usedCards.size()){
                cardLegit = false;
                NackMessage nack = new NackMessage();
                nack.setSubObject("assistant");

                controller.sendMessageToPlayer(request.getSenderID(), nack);
            }
        }

        if(cardLegit){
            //modify the model
            controller.getMatch().useCard(request.getAssistantChosen());

            //add to used cards only if this is the first player using the card
            if(!usedCards.containsKey(request.getAssistantChosen())){
                usedCards.put(request.getAssistantChosen(), request.getSenderID());
            }

            //count that another player has ended its turn
            playersCounter++;

            //create and send an ack message
            AckMessage response = new AckMessage();
            response.setSubObject("assistant");
            response.setRecipient(request.getSenderID());
            response.setAssistantAlreadyUsedInThisRound(new ArrayList<>(usedCards.keySet()));


            //if all the players choose their assistant then we can go to the next state -> ACTION phase
            if(playersCounter == controller.getNumberOfPlayers()){

                response.setNextPlayer(defineActionPhaseOrder(controller));
                controller.nextState();
            }else{
                response.setNextPlayer(controller.nextPlayer(request.getSenderID()));
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
        ArrayList<Integer> arr = new ArrayList<Integer>(usedCards.keySet());

        int n = arr.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++){
                if (arr.get(j) > arr.get(j + 1)) {
                    // swap arr[j+1] and arr[j]
                    int temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                }
            }
        }

        // substitute the cards' values with players' ID
        for(int k = 0; k < arr.size(); k++){
            arr.set(k, usedCards.get(arr.get(k)));
        }

        controller.setActionPhaseOrder(arr);
        return arr.get(0);
    }
}
