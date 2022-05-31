package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.ChosenDeckMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;

public class ChooseDeck implements ControllerState{
    /**
     * This attribute is the list of already chosen decks
     */
    ArrayList<Wizard> notAvailableDecks = new ArrayList<Wizard>();
    /**
     * This attribute is the counter of players that have chosen the deck
     */
    int playersCounter = 0;

    @Override
    public void nextState(Controller controller) {
        controller.setState(new RefillClouds());
    }

    /**
     * This method controls that the message received has an object 'deck'
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
        String json = controller.getMsg();
        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        if (object.equals("deck")) {
            stateExecution(controller);
        } else {
            System.out.println("CHOOSE_ASSISTANT_CARD STATE: \nexpected message with object [deck]" +
                               "\nreceived message with object["+ message.getObjectOfMessage() + "]");
        }
    }

    @Override
    public void stateExecution(Controller controller) {
        playersCounter++;

        if(playersCounter == controller.getNumberOfPlayers()){
            controller.nextState();
        }

        String jsonMsg = controller.getMsg();
        ChosenDeckMessage request = gson.fromJson(jsonMsg, ChosenDeckMessage.class);

        // control if the deck was already chosen or not
        if(deckAlreadyChosen(request)){
            NackMessage nack = new NackMessage("deck");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }else{
            //adds the chosen deck to the list of not available decks anymore
            notAvailableDecks.add(request.getDeck());

            //notify the match with the deck chosen by the current player
            controller.getMatch().setCurrentPlayer(request.getSender_ID());
            controller.getMatch().playerChoosesDeck(request.getDeck());

            AckMessage response = new AckMessage();
            response.setSubObject("deck");
            response.setRecipient(request.getSender_ID());

            int nextPlayer = controller.nextPlayer(request.getSender_ID());
            response.setNextPlayer(nextPlayer);

            response.setNotAvailableDecks(notAvailableDecks);
            controller.sendMessageAsBroadcast(response);

            //controls if all the players have chosen a deck

        }
    }

    /**
     * This method control if the deck was already chosen by another player
     * @param request message received from the client
     * @return true if the deck was already chosen (not available anymore),
     *         false if the deck can still be chosen
     */
    private boolean deckAlreadyChosen(ChosenDeckMessage request){
        return notAvailableDecks.contains(request.getDeck());
    }
}
