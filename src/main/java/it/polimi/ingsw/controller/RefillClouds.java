package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.AckMessage;
import it.polimi.ingsw.network.messages.BagClickMessage;

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
     * This method fills up the clouds on the table
     */
    @Override
    public void stateExecution(Controller controller) {
        BagClickMessage request = gson.fromJson(controller.getMsg(), BagClickMessage.class);

        if(!(request.getObjectOfMessage().equals("draw"))){
            System.out.println("REFILL_CLOUDS STATE: \nexpected object [draw]\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }else{
            //refill every cloud on the table
            ArrayList<Creature> studentsPutOnEachCloud = controller.getMatch().moveStudentsFromBagToCloudsEveryRound();

            //send an ack message with the drawn students in order to update the Client' s data
            AckMessage response = new AckMessage();
            response.setSubObject("refillClouds");
            response.setStudentsAddedToTheClouds(studentsPutOnEachCloud);


            controller.sendMessageAsBroadcast(response);

            //takes the controller to the next state
            controller.nextState();
        }
    }
}
