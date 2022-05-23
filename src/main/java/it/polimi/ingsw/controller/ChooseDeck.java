package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.ChosenDeckMessage;

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

    @Override
    public void stateExecution(Controller controller) {
        playersCounter++;

        String jsonMsg = controller.getMsg();

        ChosenDeckMessage request = gson.fromJson(jsonMsg, ChosenDeckMessage.class);

        if(!(request.getObjectOfMessage().equals("deck"))){
            System.out.println("CHOOSE_ASSISTANT_CARD STATE: \nexpected message with object [deck]\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }else{

            /*IMPORTANT:
            The control on the deck chosen , that is a deck is not legit if it has already been chosen by another player,
            is made inside the client, in fact when we send the ack message we attach also the list of not available
            decks, useful for this control.*/

            //adds the chosen deck to the list of not available decks anymore
            notAvailableDecks.add(request.getDeck());

            //notify the match with the deck chosen by the current player
            controller.getMatch().setCurrentPlayer(request.getSender_ID());
            controller.getMatch().playerChoosesDeck(request.getDeck());

            /*sends an ack message to all the clients, who is waiting for an ack then knows that its choice is legit
            while is not waiting for an ack can just read the not available decks to perform the control on the deck chosen
            and modify the appearance of the GUI/CLI*/
            AckMessage response = new AckMessage();
            response.setSubObject("deck");
            response.setRecipient(request.getSender_ID());

            int nextPlayer = controller.nextPlayer(request.getSender_ID());
            response.setNextPlayer(nextPlayer);

            response.setNotAvailableDecks(notAvailableDecks);
            controller.sendMessageAsBroadcast(response);

            //controls if all the players have chosen a deck
            if(playersCounter == controller.getNumberOfPlayers()){
                controller.nextState();
            }
        }
    }
}
