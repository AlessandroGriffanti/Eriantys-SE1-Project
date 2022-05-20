package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.AckMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MovedMotherNatureMessage;
import it.polimi.ingsw.network.messages.NackMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class Action_2 implements ControllerState{
    /**
     * This attribute tells us if the character card showing a centaur has been played,
     * in this case the computation of the influence won't take into account the towers
     * on the island
     */
    boolean centaurEffect = false;

    @Override
    public void nextState(Controller controller) {

    }

    @Override
    public void stateExecution(Controller controller) {

        Match match = controller.getMatch();
        String json = controller.getMsg();

        Message request = gson.fromJson(json, Message.class);
        if(request.getObjectOfMessage().equals("action_2")){
            executeAction_2(controller, json);
        }else if(request.getObjectOfMessage().equals("character")){
            // TODO
        }else{
            System.out.println("ACTION_2: \nexpected message with object [action_2] or [character]\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }
    }

    /**
     * todo -> test
     * This method takes care of all the controls and actions needed when the player moves mother nature
     * @param controller reference to the controller of this match
     * @param json message received by the player, not yet deserialized
     */
    private void executeAction_2(Controller controller, String json){
        Match match = controller.getMatch();

        MovedMotherNatureMessage request = gson.fromJson(json, MovedMotherNatureMessage.class);
        int destinationIsland = request.getDestinationIsland_ID();
        Archipelago archipelago = match.getRealmOfTheMatch().getArchipelagos().get(destinationIsland);

        AckMessage ack = new AckMessage();
        ack.setSubObject("action_2");
        ack.setDestinationIsland_ID(destinationIsland);

        // control if the movement is legit
        if(!isMovementValid(match, destinationIsland, request.getSender_ID())){
            NackMessage nack = new NackMessage();
            nack.setSubObject("invalid_mother_nature_movement");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);

            System.out.println("Nack sent for invalid mother nature movement");
        }else{
            // move mother nature
            match.getRealmOfTheMatch().setDestinationOfMotherNature(destinationIsland);

            // if there is at least one noEntryTile on the island we do not compute the influence on it
            if(archipelago.getNoEntryTiles() > 0){
                //remove one no entry tile
                archipelago.removeNoEntryTile();
                ack.setRemovedNoEntryTile(true);
                ack.setIslandThatLostNoEntryTile(destinationIsland);
                ack.setIslandsUnified("none");
            }else{
                // in the case there are no towers yet...
                if(archipelago.getMasterOfArchipelago() == null){

                    // compute influence
                    int newMaster = influenceComputation(match, destinationIsland);

                    // if there is a valid new master then we apply the modification
                    if(newMaster != -1){
                        match.newMasterOnIsland(newMaster);

                        ack.setMasterChanged(true);
                        ack.setNewMaster_ID(newMaster);
                    }
                }
                // in the case there is already an island-master...
                else{
                    int previousMaster = archipelago.getMasterOfArchipelago().getID();
                    int newMaster = influenceComputation(match, destinationIsland);

                    if(newMaster != -1){

                        if(newMaster != previousMaster){
                            match.newMasterOnIsland(newMaster);

                            ack.setMasterChanged(true);
                            ack.setPreviousMaster_ID(previousMaster);
                            ack.setNewMaster_ID(newMaster);
                        }
                    }
                }
                // control if the islands can be unified
                int unificationControl = match.islandUnifyControl();
                int previousIsland_ID = match.getRealmOfTheMatch().previousIsland(destinationIsland);
                int nextIsland_ID = match.getRealmOfTheMatch().nextIsland(destinationIsland);

                switch (unificationControl){
                    case 1:
                        ack.setIslandsUnified("previous");
                        // if the previous island has at least one noEntryTile we remove it
                        if(match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).getNoEntryTiles() > 0){
                            match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).removeNoEntryTile();

                            ack.setRemovedNoEntryTile(true);
                            ack.setIslandThatLostNoEntryTile(previousIsland_ID);
                        }
                        // unify islands
                        match.getRealmOfTheMatch().unifyArchipelago(destinationIsland, previousIsland_ID);

                    case 2:
                        ack.setIslandsUnified("next");
                        // if the next island has at least one noEntryTile we remove it
                        if(match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).getNoEntryTiles() > 0){
                            match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).removeNoEntryTile();

                            ack.setRemovedNoEntryTile(true);
                            ack.setIslandThatLostNoEntryTile(nextIsland_ID);
                        }
                        // unify islands
                        match.getRealmOfTheMatch().unifyArchipelago(destinationIsland, nextIsland_ID);

                    case 3:
                        ack.setIslandsUnified("both");
                        // if the next or the previous island has at least one noEntryTile we remove only one of them
                        boolean alreadyRemovedTile = false;

                        if(match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).getNoEntryTiles() > 0){
                            match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).removeNoEntryTile();
                            alreadyRemovedTile = true;

                            ack.setRemovedNoEntryTile(true);
                            ack.setIslandThatLostNoEntryTile(previousIsland_ID);
                        }

                        if(match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).getNoEntryTiles() > 0 && !alreadyRemovedTile){
                            match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).removeNoEntryTile();

                            ack.setRemovedNoEntryTile(true);
                            ack.setIslandThatLostNoEntryTile(nextIsland_ID);
                        }
                        // unify islands
                        match.getRealmOfTheMatch().unifyArchipelago(destinationIsland, previousIsland_ID);
                        match.getRealmOfTheMatch().unifyArchipelago(destinationIsland, nextIsland_ID);
                }
            }

            // final set up of the message and shipping
            ack.setRecipient(request.getSender_ID());
            controller.sendMessageAsBroadcast(ack);
        }

        controller.nextState();
    }

    /**
     * This method computes the influence of all players on the island and returns the player who is the
     * master of the island
     * @param match
     * @param island_ID
     * @return ID of the player or if the there are two players with the same influence value, which is the maximum
     *         value, then -1 is returned
     */
    private int influenceComputation(Match match, int island_ID){
        // in this variable we store for each player the influence on the island : HashMap<player_ID, influence>
        HashMap<Integer, Integer> allPlayersInfluence = new HashMap<Integer, Integer>();

        Archipelago island = match.getRealmOfTheMatch().getArchipelagos().get(island_ID);
        ArrayList<Creature> playerProfessors;

        for(int player_ID = 0; player_ID < match.getPlayers().size(); player_ID++){
            Player player = match.getPlayers().get(player_ID);
            playerProfessors = player.getMyProfessors();

            // count the number of students of each type on the island
            int playerInfluence = 0;
            for(Creature creature: playerProfessors){
                playerInfluence += island.getStudentsOfType(creature);
            }

            if(!centaurEffect){
                // if the players owns the tower(s) then we also count them in the influence
                playerInfluence += match.numberOfTowersOnTheIsland(player_ID, island_ID);
            }

            allPlayersInfluence.put(player_ID, playerInfluence);
        }

        // control who has the higher influence
        int maxInfluence = 0;
        int playerWithmaxInfluence = -1;
        int repetitions = 0;

        for(int i = 0; i < match.getPlayers().size(); i++){
            if(allPlayersInfluence.get(i) > maxInfluence){
                repetitions = 1;
                playerWithmaxInfluence = i;
                maxInfluence = allPlayersInfluence.get(i);
            }else if(allPlayersInfluence.get(i) == maxInfluence){
                repetitions++;
            }
        }

        /* if we found a max value more than once then the influence is not valid, meaning no one is getting the
        control over the island */
        if(repetitions > 1){
            return -1;
        }else {
            return playerWithmaxInfluence;
        }
    }

    /**
     * This method controls if the movement made by mother nature is valid accordingly to the last assistant card
     * played by the player
     * @param match reference to the Match
     * @param destinationIsland_ID ID of the island that is the destination of mother nature's movement
     * @return true if the movement is legit, false otherwise
     */
    private boolean isMovementValid(Match match, int destinationIsland_ID, int player_ID){
        int currentMotherNaturePosition = match.getPositionOfMotherNature();
        int steps = 0;
        int futurePosition_ID;
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
        return steps <= lastPlayedCard.getMotherNatureMovementValue();
    }
}
