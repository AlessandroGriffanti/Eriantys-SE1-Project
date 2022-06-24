package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.clientMessages.CharacterRequestMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCloudMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;

public class Action_3 implements ControllerState{


    @Override
    public void nextState(Controller controller) {
        if(controller.isActionPhase()){
            controller.setState(new Action_1());
        }else{
            controller.setState(new RefillClouds());
        }
    }

    /**
     * This method controls if the message must be handed over to the character manager or
     * to the stateExecution method
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
        Match match = controller.getMatch();
        String json = controller.getMsg();

        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        switch (object){
            case "character_request":
                CharacterRequestMessage request = gson.fromJson(json, CharacterRequestMessage.class);
                controller.getCharactersManager().checkCard(request);
                break;
            case "character_data":
                CharacterDataMessage dataMessage = gson.fromJson(json, CharacterDataMessage.class);
                controller.getCharactersManager().useCard(dataMessage);
                break;
            case "action_3":
                stateExecution(controller);
                break;
            default:
                System.out.println("ACTION_3: \nexpected message with object [action_3] or [character]" +
                        "\nreceived message with object["+ message.getObjectOfMessage() + "]");
                break;
        }
    }

    @Override
    public void stateExecution(Controller controller) {
        Match match = controller.getMatch();
        String json = controller.getMsg();

        ChosenCloudMessage request = gson.fromJson(json, ChosenCloudMessage.class);
        CloudTile cloud = match.getRealmOfTheMatch().getCloudRegion().get(request.getCloud_ID());

        if(cloud.getStudents().size() == 0){
            NackMessage nack = new NackMessage("invalid_cloud");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }else{
            assert cloud.getStudents().size() == cloud.getCapacity();

            // take students
            ArrayList<Creature> newEntrance = match.moveStudentsFromCloudToEntrance(request.getCloud_ID());

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

                // find who is the first player of the next round
                nextPlayer = SupportFunctions.findFirstPlayerOfNewRound(controller);
                assert nextPlayer != -1 : "There are no more players connected";

                ack.setNextPlayer(nextPlayer);
                ack.setNextPlanningPhase(true);

                // the action phase must end
                controller.setActionPhase(false);
            }else{
                nextPlayer = controller.nextPlayer(controller.getActionPhaseCurrentPlayer());
                // update current player of action phase
                controller.setActionPhaseCurrentPlayer(nextPlayer);

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
            }else {
                controller.sendMessageAsBroadcast(ack);
            }

            controller.getCharactersManager().resetCharacterAttributes();
            controller.nextState();
        }
    }
}
