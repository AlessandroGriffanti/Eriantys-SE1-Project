package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.serverMessages.AckMessage;
import it.polimi.ingsw.messages.clientMessages.ChosenTowerColorMessage;
import it.polimi.ingsw.messages.serverMessages.NackMessage;

import java.util.ArrayList;

public class ChooseTowerColor implements ControllerState{
    /**
     * This attribute is the list of already used towers' color
     */
    private ArrayList<Tower> usedColors = new ArrayList<Tower>();
    /**
     * This attribute is counter of the number of players who have already chosen their towers' color
     */
    private int playersCounter = 0;


    @Override
    public void nextState(Controller controller) {
        controller.setState(new ChooseDeck());
    }

    /**
     * This method controls that the message received has an object 'tower_color'
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
        String json = controller.getMsg();
        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        if (object.equals("tower_color")) {
            stateExecution(controller);
        } else {
            System.out.println("CHOOSE_TOWER_COLOR STATE: \nexpected message with object [tower_color]" +
                    "\nreceived message with object[" + message.getObjectOfMessage() + "]");
        }
    }

    @Override
    public void stateExecution(Controller controller) {
        playersCounter++;

        String jsonRequest = controller.getMsg();
        ChosenTowerColorMessage request = gson.fromJson(jsonRequest, ChosenTowerColorMessage.class);

        /*IMPORTANT:
        The control on the color chosen , that is a color is not legit if it has already been chosen by another player,
        is made inside the client, in fact when we send the ack message we attach also the list of not available
        color, useful for this control.*/
        if(colorAlreadyUsed(request)){
            NackMessage nack = new NackMessage("tower_color");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }

        // add the chosen color to the already used towers' color list
        usedColors.add(request.getColor());

        // set the current player
        controller.getMatch().setCurrentPlayer(request.getSender_ID());
        // notify the match with the color chosen by the current player
        controller.getMatch().playerChoosesTowerColor(request.getColor());

        // send an ack message
        AckMessage response = new AckMessage();
        response.setSubObject("tower_color");
        response.setRecipient(request.getSender_ID());

        int nextPlayer = controller.nextPlayer(request.getSender_ID());
        response.setNextPlayer(nextPlayer);

        response.setNotAvailableTowerColors(usedColors);
        controller.sendMessageAsBroadcast(response);

        //controls if all the players have chosen a deck
        if(playersCounter == controller.getNumberOfPlayers()){
            controller.nextState();
        }
    }

    /**
     * This method controls if the tower-color chosen by the player is still available or not
     * @param request message received from the client
     * @return true if the color has already been chosen (not available anymore),
     *         false if the color is still available
     */
    private boolean colorAlreadyUsed(ChosenTowerColorMessage request){
        return usedColors.contains(request.getColor());
    }
}
