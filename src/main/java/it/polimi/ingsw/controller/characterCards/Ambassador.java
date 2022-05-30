package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.SupportFunctions;
import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;

/**
 * This class represents the character card called 'ambassador' (third in the rules file)
 */
public class Ambassador extends Character {


    public Ambassador(Controller controller){
        this.controller = controller;
        this.price = 3;
    }
    /**
     * This method controls if the character can be used (in this case the card can always be used
     * because there are no restrictions)
     * @return always true
     */
    @Override
    public boolean checkCharacterAvailability() {
        return true;
    }


    /**
     * This method calls the two sub-methods effect_influence and effect_union in order to perform
     * the actions required by the influence computation of the character
     * @param request message sent by the client containing data about the island chosen by
     *                the player to compute influence using the character
     */
    public void effect(CharacterDataMessage request) {
        increasePrice();

        // compute influence on the island and add / remove towers
        effect_influence(controller, request);

        // check if some islands can be unified without caring about the no-entry-tiles
        effect_union(controller, request);
    }


    /**
     * This method finds which player is the one with higher influence on the island and if
     * necessary replaces the old master's towers with the new master's ones;
     * finally it sends an AckCharacterMessage to all the clients
     * @param controller reference to the controller of the match
     * @param request message sent by the client containing data about the island chosen by
     *                the player to compute influence using the character
     */
    private void effect_influence(Controller controller, CharacterDataMessage request){
        Match match = controller.getMatch();

        int island_ID = request.getIsland_ID();
        Archipelago archipelago = match.getRealmOfTheMatch().getArchipelagos().get(island_ID);

        int coinReserve = match.getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "ambassador_influence", coinReserve);

        // in the case there are no towers yet...
        if(archipelago.getMasterOfArchipelago() == null){

            // compute influence
            int newMaster = SupportFunctions.influenceComputation(controller, archipelago.getArchipelagoID());

            // if there is a valid new master then we apply the modification
            if(newMaster != -1){
                /* if the player, while conquering the island, remains without towers then he is the winner of the match,
                   that must end*/
                if(match.newMasterOnIsland(newMaster)){
                    ack.setEndOfMatch(true);
                }

                ack.setMasterChanged(true);
                ack.setNewMaster_ID(newMaster);
            }
        }
        // in the case there is already an island-master...
        else{
            int previousMaster = archipelago.getMasterOfArchipelago().getID();
            int newMaster = SupportFunctions.influenceComputation(controller, archipelago.getArchipelagoID());

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
                }
            }
        }

        // send end-of-match message if needed
        if(ack.isEndOfMatch()){
            SupportFunctions.endMatch(controller, "towers_finished", request.getSender_ID());
        }else{
            // send the ack message
            controller.sendMessageAsBroadcast(ack);
        }
    }


    /**
     * This method unifies the island chosen by the player with its previous and/or next island(s)
     * if possible; finally it sends an AckCharacterMessage to all the clients
     * @param controller reference to the controller of the match
     * @param request message sent by the client containing the ID of the island chosen by the player
     */
    private void effect_union(Controller controller, CharacterDataMessage request){
        Match match = controller.getMatch();

        int currentIsland = request.getIsland_ID();

        int unificationControl = match.islandUnifyControl();
        int previousIsland_ID = match.getRealmOfTheMatch().previousIsland(currentIsland);
        int nextIsland_ID = match.getRealmOfTheMatch().nextIsland(currentIsland);

        int coinReserve = match.getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "ambassador_union", coinReserve);

        switch (unificationControl){
            case 1:
                ack.setIslandsUnified("previous");
                // if the previous island has at least one noEntryTile we remove it
                if(match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).getNoEntryTiles() > 0){
                    match.getRealmOfTheMatch().getArchipelagos().get(previousIsland_ID).removeNoEntryTile();
                    // return the no-entry-tile to the herbalist
                    controller.getCharactersManager().returnTileToHerbalist();

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

                }

                if(match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).getNoEntryTiles() > 0 && !alreadyRemovedTile){
                    match.getRealmOfTheMatch().getArchipelagos().get(nextIsland_ID).removeNoEntryTile();
                    // return the no-entry-tile to the herbalist
                    controller.getCharactersManager().returnTileToHerbalist();

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
            controller.sendMessageAsBroadcast(ack);
        }
    }
}
