package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.BagClickMessage;

import java.util.ArrayList;

/**
 * This class represent the state in which the first player of the turn fill up the clouds drawing from the bag (maybe click on the bag in GUI)
 */
public class RefillClouds implements ControllerState{
    @Override
    public void nextState(Controller controller) {
        controller.setState(new ChooseAssistantCard());
    }

    /**
     * This method controls that the message received has an object 'draw'
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
        String json = controller.getMsg();
        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        if (object.equals("draw")) {
            stateExecution(controller);
        }else{
            System.out.println("REFILL_CLOUDS STATE: \nexpected object [draw]\n" +
                               "received message with object["+ message.getObjectOfMessage() + "]");
        }
    }

    /**
     * This method fills up the cloud-tiles on the table
     */
    @Override
    public void stateExecution(Controller controller) {
        BagClickMessage request = gson.fromJson(controller.getMsg(), BagClickMessage.class);

        //refill every cloud on the table
        ArrayList<Creature> studentsPutOnEachCloud = controller.getMatch().moveStudentsFromBagToCloudsEveryRound();

        //send an ack message with the drawn students in order to update the Client' s data
        AckMessage response = new AckMessage();
        response.setSubObject("refillClouds");
        response.setStudents(studentsPutOnEachCloud);


        controller.sendMessageAsBroadcast(response);

        //takes the controller to the next state
        controller.nextState();
    }
}
