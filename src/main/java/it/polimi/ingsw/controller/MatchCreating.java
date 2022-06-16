package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;

/**
 * This class represents the state of the controller in which it's waiting for other players to join the match
 * RECEIVED: MatchSpecsMessage
 * RESPONSE: MatchWaiting
 */
public class MatchCreating implements ControllerState{
    @Override
    public void nextState(Controller controller) {
        controller.setState(new ChooseTowerColor());
    }

    @Override
    public void controlMessageAndExecute(Controller controller) {
        String json = controller.getMsg();

        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        if(object.equals("creation")){
            stateExecution(controller);
        }else{
            System.out.println("MATCH_CREATING: \nexpected message with object [creation]" +
                               "\nreceived message with object["+ message.getObjectOfMessage() + "]");
            }
        }

    /**
     *  This method initializes the specifics of the match inside the model
     * @param controller reference of the controller of the match
     */
    @Override
    public void stateExecution(Controller controller) {
        //read the message in controller and map it in MatchSpecs
        String jsonMessage = controller.getMsg();
        MatchSpecsMessage msgMapped = gson.fromJson(jsonMessage, MatchSpecsMessage.class);

        //initialize numberOfPlayers (in controller and model) and expertMode (only inside the model)
        controller.setNumberOfPlayers(msgMapped.getNumOfPlayers());
        controller.setExpertMode(msgMapped.isExpertMode());

        //notify the player with a MatchWaiting message
        AckMessage ack = new AckMessage();
        ack.setSubObject("waiting");
        controller.sendMessageToPlayer(0, ack);

        if(controller.getPlayersAddedCounter() == controller.getNumberOfPlayers()){
            controller.startMatch();
        }
        //N.B. There is no nextState call because we call it inside the startMatch methode of Controller class
    }
}
