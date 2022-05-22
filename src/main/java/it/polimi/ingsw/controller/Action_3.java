package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.AckMessage;
import it.polimi.ingsw.network.messages.ChosenCloudMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.NackMessage;

import javax.annotation.processing.SupportedOptions;
import java.util.ArrayList;

public class Action_3 implements ControllerState{


    @Override
    public void nextState(Controller controller) {
        if(controller.isActionPhase()){
            controller.setState(new Action_1());
        }else{
            controller.setState(new RefillClouds());
        }
        // TODO: we could have to add the management of the characters cards
    }

    @Override
    public void stateExecution(Controller controller) {
        Match match = controller.getMatch();
        String json = controller.getMsg();

        Message request = gson.fromJson(json, Message.class);
        if(request.getObjectOfMessage().equals("action_3")){
            executeAction_3(controller, json);
        }else if(request.getObjectOfMessage().equals("character")){
            // TODO
        }else{
            System.out.println("ACTION_3: \nexpected message with object [action_3] or [character]\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }
    }

    private void executeAction_3(Controller controller, String json){
        Match match = controller.getMatch();

        ChosenCloudMessage request = gson.fromJson(json, ChosenCloudMessage.class);
        CloudTile cloud = match.getRealmOfTheMatch().getCloudRegion().get(request.getCloud_ID());

        if(cloud.getStudents().size() == 0){
            NackMessage nack = new NackMessage();
            nack.setSubObject("invalid_cloud");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }else{
            assert cloud.getStudents().size() == cloud.getCapacity();

            // take students
            ArrayList<Creature> newEntrance = match.moveStudentsFromCloudToEntrance(request.getSender_ID());

            // create the ack message
            AckMessage ack = new AckMessage();
            ack.setSubObject("action_3");
            ack.setRecipient(request.getSender_ID());
            ack.setCloudChosen_ID(request.getCloud_ID());
            ack.setStudents(newEntrance);

            // control if the action phase has come to the end or not
            int lastPlayerOfAction_ID = controller.getActionPhaseOrder().get(controller.getActionPhaseOrder().size() - 1);
            int nextPlayer;

            if(controller.getActionPhaseCurrentPlayer() == lastPlayerOfAction_ID){
                // the action phase must end
                controller.setActionPhase(false);

                // find who is the first player of the next round
                nextPlayer = SupportFunctions.findFirstPlayerOfNewRound(controller);
                assert nextPlayer != -1 : "There are no more players connected";
                ack.setNextPlayer(nextPlayer);
                ack.setNextPlanningPhase(true);

                controller.setActionPhase(false);
            }else{
                nextPlayer = controller.nextPlayer(controller.getActionPhaseCurrentPlayer());

                ack.setNextPlayer(nextPlayer);
                ack.setNextPlanningPhase(false);
            }

            // control if a player used all of his assistants or if the bag is empty
            if(SupportFunctions.playerWithNoMoreAssistants_control(controller)){

                ack.setEndOfMatch(true);
                controller.sendMessageAsBroadcast(ack);
                SupportFunctions.endMatch(controller, "assistants_finished");

            }else if(SupportFunctions.emptyBag_control(controller)){

                ack.setEndOfMatch(true);
                controller.sendMessageAsBroadcast(ack);
                SupportFunctions.endMatch(controller, "empty_bag");
            }

            controller.nextState();
        }
    }

}
