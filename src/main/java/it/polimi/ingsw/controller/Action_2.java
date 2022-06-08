package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.clientMessages.CharacterRequestMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.MovedMotherNatureMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;

public class Action_2 implements ControllerState{

    /**
     * This attribute memorizes if action_3 can be performed or not (because there are not enough students
     * on the clouds)
     */
    private boolean action3Allowed = true;


    @Override
    public void nextState(Controller controller) {
        if (!action3Allowed) {
            controller.setState(new Action_1());
        } else {
            controller.setState(new Action_3());
        }
    }

    /**
     * This method controls if the message must be handed over to the character manager or
     * to the stateExecution method
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
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
            case "action_2":
                executeAction_2_movement(controller, json);
                break;
            default:
                System.out.println("ACTION_2: \nexpected message with object [action_2] or [character_request] or [character_data]" +
                    "\nreceived message with object["+ message.getObjectOfMessage() + "]");
                break;
        }
    }

    /**
     * This method calls the execution of the first part of action_2, that is the movement of mother nature
     * @param controller reference to the controller of the match
     */
    @Override
    public void stateExecution(Controller controller){
        String json = controller.getMsg();

        executeAction_2_movement(controller, json);
    }


    /**
     * This method takes care of all the controls and actions needed when the player moves mother nature,
     * if the movement is legit it moves mother nature
     * @param controller reference to the controller of this match
     * @param json message received by the player, not yet deserialized
     */
    private void executeAction_2_movement(Controller controller, String json) {
        Match match = controller.getMatch();

        MovedMotherNatureMessage request = gson.fromJson(json, MovedMotherNatureMessage.class);
        int destinationIsland = request.getDestinationIsland_ID();
        Archipelago archipelago = match.getRealmOfTheMatch().getArchipelagos().get(destinationIsland);

        AckMessage ack = new AckMessage();
        ack.setSubObject("action_2_movement");
        ack.setDestinationIsland_ID(destinationIsland);
        ack.setRecipient(request.getSender_ID());

        // control if the movement is legit
        if (!isMovementValid(controller, destinationIsland, request.getSender_ID())) {
            NackMessage nack = new NackMessage("invalid_mother_nature_movement");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);

            System.out.println("Nack sent for invalid mother nature movement");
        } else {

            // move mother nature
            match.getRealmOfTheMatch().setDestinationOfMotherNature(destinationIsland);

            // if there is at least one noEntryTile on the island we do not compute the influence on it
            if (archipelago.getNoEntryTiles() > 0) {

                //remove one no entry tile
                archipelago.removeNoEntryTile();
                // put the tile on the character herbalist
                controller.getCharactersManager().returnTileToHerbalist();

                ack.setRemovedNoEntryTile(true);
                ack.setIslandThatLostNoEntryTile(destinationIsland);

                // control if the action_3 can be performed, if not the match should end
                ack.setAction3Valid(controlAction3Allowed(match));
                action3Allowed = controlAction3Allowed(match);

                if(!action3Allowed){
                    assert SupportFunctions.noMoreStudentsToDraw(match);

                    ack.setAction3Valid(false);

                    //if the last player is doing his move and action_3 is not legit the match will end
                    int lastPlayerOfAction = controller.getActionPhaseOrder().get(controller.getActionPhaseOrder().size() - 1);

                    if(request.getSender_ID() == lastPlayerOfAction){
                        ack.setEndOfMatch(true);
                    }else{
                        // reset characters' attributes in CharacterManager
                        controller.getCharactersManager().resetCharacterAttributes();

                        // set the next player
                        int nextPlayer = controller.nextPlayer(request.getSender_ID());
                        ack.setNextPlayer(nextPlayer);
                        controller.setActionPhaseCurrentPlayer(nextPlayer);

                        controller.nextState();
                    }
                }

            } else {
                executeAction_2_influence(controller, request);
            }

            // send the ack message for the movement
            ack.setNextPlayer(request.getSender_ID());
            controller.sendMessageAsBroadcast(ack);

            // send the endOfMatch message if needed
            if(ack.isEndOfMatch()){
                SupportFunctions.endMatch(controller,"empty_bag");
            }
        }
    }


    /**
     * This method performs the control on the influence over the island, if a new master must be declared and if
     * some islands must be unified
     * @param controller reference to the controller
     * @param request message received from the client
     */
    public void executeAction_2_influence(Controller controller, MovedMotherNatureMessage request){
        Match match = controller.getMatch();

        int currentIsland = match.getPositionOfMotherNature();
        Archipelago archipelago = match.getRealmOfTheMatch().getArchipelagos().get(currentIsland);

        AckMessage ack = new AckMessage();
        ack.setSubObject("action_2_influence");
        ack.setRecipient(request.getSender_ID());

        // in the case there are no towers yet...
        if(archipelago.getMasterOfArchipelago() == null){

            // compute influence
            int newMaster = SupportFunctions.influenceComputation(controller, currentIsland);

            // if there is a valid new master then we apply the modification
            if(newMaster != -1){
                /* if the player, while conquering the island, remains without towers then he is the winner of the match,
                   that must end*/
                if(match.newMasterOnIsland(newMaster)){
                    ack.setEndOfMatch(true);
                }

                ack.setMasterChanged(true);
                ack.setNewMaster_ID(newMaster);
                ack.setTowerColor(match.getPlayerByID(newMaster).getTowerColor());
            }
        }
        // in the case there is already an island-master...
        else{
            int previousMaster = archipelago.getMasterOfArchipelago().getID();
            int newMaster = SupportFunctions.influenceComputation(controller, currentIsland);

            if(newMaster != -1){

                if(newMaster != previousMaster){
                    /* if the player, while conquering the island, remains without towers then he is the winner of the match,
                       that must end*/
                    if(match.newMasterOnIsland(newMaster)){
                        ack.setEndOfMatch(true);
                    }

                    ack.setMasterChanged(true);
                    ack.setPreviousMaster_ID(previousMaster);
                    ack.setNewMaster_ID(newMaster);
                    ack.setTowerColor(match.getPlayerByID(newMaster).getTowerColor());
                }
            }
        }

        // send end-of-match message if needed
        if(ack.isEndOfMatch()){
            SupportFunctions.endMatch(controller, "towers_finished", request.getSender_ID());
        }else{
            // send the ack message
            controller.sendMessageAsBroadcast(ack);

            executeAction_2_union(controller, request);
        }
    }

    /**
     *This method controls if it's possible to unify two or three islands and in case there are only 3 islands left
     * in the realm then it calls the end of the match.
     * @param  controller reference to the controller of the match
     * @param  request message sent by the client containing data about the movement of mother nature
     */
    private void executeAction_2_union(Controller controller, MovedMotherNatureMessage request){
        Match match = controller.getMatch();

        int currentIsland = match.getPositionOfMotherNature();

        int unificationControl = match.islandUnifyControl();
        int previousIsland_ID = match.getRealmOfTheMatch().previousIsland(currentIsland);
        int nextIsland_ID = match.getRealmOfTheMatch().nextIsland(currentIsland);

        AckMessage ack = new AckMessage();
        ack.setSubObject("action_2_union");
        ack.setRecipient(request.getSender_ID());

        switch (unificationControl){
            case 1:
                ack.setIslandsUnified("previous");
                // if the previous island has at least one noEntryTile we remove it
                if(match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).getNoEntryTiles() > 0){
                    match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).removeNoEntryTile();
                    // return the no-entry-tile to the herbalist
                    controller.getCharactersManager().returnTileToHerbalist();

                    ack.setRemovedNoEntryTile(true);
                    ack.setIslandThatLostNoEntryTile(previousIsland_ID);
                }
                // unify islands
                match.getRealmOfTheMatch().unifyArchipelago(currentIsland, previousIsland_ID);

            case 2:
                ack.setIslandsUnified("next");
                // if the next island has at least one noEntryTile we remove it
                if(match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).getNoEntryTiles() > 0){
                    match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).removeNoEntryTile();
                    // return the no-entry-tile to the herbalist
                    controller.getCharactersManager().returnTileToHerbalist();

                    ack.setRemovedNoEntryTile(true);
                    ack.setIslandThatLostNoEntryTile(nextIsland_ID);
                }
                // unify islands
                match.getRealmOfTheMatch().unifyArchipelago(currentIsland, nextIsland_ID);

            case 3:
                ack.setIslandsUnified("both");
                // if the next or the previous island has at least one noEntryTile we remove only one of them
                boolean alreadyRemovedTile = false;

                if(match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).getNoEntryTiles() > 0){
                    match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).removeNoEntryTile();
                    // return the no-entry-tile to the herbalist
                    controller.getCharactersManager().returnTileToHerbalist();
                    alreadyRemovedTile = true;

                    ack.setRemovedNoEntryTile(true);
                    ack.setIslandThatLostNoEntryTile(previousIsland_ID);
                }

                if(match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).getNoEntryTiles() > 0 && !alreadyRemovedTile){
                    match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).removeNoEntryTile();
                    // return the no-entry-tile to the herbalist
                    controller.getCharactersManager().returnTileToHerbalist();

                    ack.setRemovedNoEntryTile(true);
                    ack.setIslandThatLostNoEntryTile(nextIsland_ID);
                }
                // unify islands
                match.getRealmOfTheMatch().unifyArchipelago(currentIsland, previousIsland_ID);
                match.getRealmOfTheMatch().unifyArchipelago(currentIsland, nextIsland_ID);
        }

        if(SupportFunctions.onlyThreeIslandsLeft(match)){

            // send the ack message and calls the end of the match because there are only 3 islands left
            ack.setEndOfMatch(true);
            controller.sendMessageAsBroadcast(ack);
            SupportFunctions.endMatch(controller, "three_islands");

        }else{
            //control if action_3 can be performed
            if(!action3Allowed){
                ack.setAction3Valid(false);

                int lastPlayerOfAction = controller.getActionPhaseOrder().get(controller.getActionPhaseOrder().size() - 1);

                // control if this is the last player of action phase
                if(request.getSender_ID() == lastPlayerOfAction){
                    ack.setEndOfMatch(true);

                    // send message and calls the end of the match for empty_bag
                    controller.sendMessageAsBroadcast(ack);
                    SupportFunctions.endMatch(controller, "empty_bag");
                }else{
                    // reset the characters' attributes in CharacterManager
                    controller.getCharactersManager().resetCharacterAttributes();

                    // find the first player of a new round
                    int nextPlayer = controller.nextPlayer(request.getSender_ID());
                    ack.setNextPlayer(nextPlayer);
                    controller.setActionPhaseCurrentPlayer(nextPlayer);

                    // change state (action_1) and send message
                    controller.nextState();
                    controller.sendMessageAsBroadcast(ack);
                }
            }else{
                // send message and change state (action_3)
                ack.setNextPlayer(request.getSender_ID());
                controller.sendMessageAsBroadcast(ack);
                controller.nextState();
            }
        }
    }


    /**
     * This method controls if the movement made by mother nature is valid accordingly to the last assistant card
     * played by the player
     * @param controller reference to the controller
     * @param destinationIsland_ID ID of the island that is the destination of mother nature's movement
     * @return true if the movement is legit, false otherwise
     */
    private boolean isMovementValid(Controller controller, int destinationIsland_ID, int player_ID){
        Match match = controller.getMatch();

        int currentMotherNaturePosition = match.getPositionOfMotherNature();
        int steps = 0;
        int tempIsland_ID = currentMotherNaturePosition;

        assert match.getRealmOfTheMatch().getArchipelagos().get(destinationIsland_ID) != null :
                "ERROR: the player choose an island [ID: " + destinationIsland_ID + "] that turned out to be null";

        // how many steps does it take to reach the destinationIsland ?
        do {
            steps++;
            tempIsland_ID = match.getRealmOfTheMatch().nextIsland(tempIsland_ID);
        }while(tempIsland_ID != destinationIsland_ID);

        // if the steps are more than the number allowed the movement is not legit
        Assistant lastPlayedCard = match.getPlayers().get(player_ID).getAssistantsDeck().getLastUsedCard();

        int maxSteps;
        // the return value depends on weather the messenger character has been used or not
        if(controller.getCharactersManager().isMessengerActive()){
            // if messenger is active we can move mother nature two islands farther than usual
            maxSteps = lastPlayedCard.getMotherNatureMovementValue() + 2;
            controller.getCharactersManager().setMessengerActive(false);
        }else{
            maxSteps = lastPlayedCard.getMotherNatureMovementValue();
        }

        return steps <= maxSteps;
    }

    /**
     * This method checks if the action_3 can be performed or not controlling the number of students on each cloud,
     * if it's smaller than the cloud's capacity, action_3 must not be performed
     * @param match reference to the match in question
     * @return true if action_3 can be performed
     *         false otherwise
     */
    private boolean controlAction3Allowed(Match match){
        ArrayList<CloudTile> clouds = match.getRealmOfTheMatch().getCloudRegion();
        boolean action3Possible = true;

        for(CloudTile c: clouds){
            if(c.getStudents().size() < c.getCapacity()){
                action3Possible = false;
            }
        }

        /* N.B. There could be no more students in the bag even if the action_3 results to be possible
                in this case there were just the right number of students in the bag*/
        if(!action3Possible){
            assert match.getBagOfTheMatch().getNumberOfRemainingStudents() == 0;
        }

        return action3Possible;
    }
}
