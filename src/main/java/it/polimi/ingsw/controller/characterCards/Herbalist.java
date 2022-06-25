package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

/**
 * This class represents the character card called 'herbalist' (the fifth in the rules file).
 * It allows players to put one no-entry-tile on an island of their choice.
 */
public class Herbalist extends Character {
    /**
     * This attribute is the number of entry tiles currently on the character card
     */
    private int tilesOnTheCard;

    public Herbalist(Controller controller) {
        this.controller = controller;
        this.price = 2;
        this.tilesOnTheCard = 4;
    }

    /**
     * This method controls if there are any tiles left on the character
     * @return true if there are one or more tiles on the card, false otherwise
     */
    @Override
    public boolean checkCharacterAvailability() {
        return tilesOnTheCard > 0;
    }

    /**
     * This method controls if there are sufficient tiles on the character, if so it takes one of them and puts it
     * on the island specified by the player
     * @param request message containing the ID of the chosen island
     */
    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        int island_ID = request.getIsland_ID();
        Archipelago island = controller.getMatch().getRealmOfTheMatch().getArchipelagos().get(island_ID);

        // the island mustn't be null (it would mean an error)
        assert island != null;

        // take a tile from the character
        takeOneTile();
        // put the tile on the island chosen by the player
        island.addNoEntryTile();

        int coinsInReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "herbalist", coinsInReserve);
        ack.setIsland_ID(island_ID);
        ack.setNumberOfElementsOnTheCard(tilesOnTheCard);

        int coinsOfPlayer = controller.getMatch().getPlayerByID(request.getSender_ID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }

    /**
     * This method add one tile to the character card's tiles
     */
    public void addOneTile(){
        tilesOnTheCard++;
        assert  (tilesOnTheCard >= 0) : "ERROR: there are less than zero noEntryTiles on the herbalist character";
        assert (tilesOnTheCard <= 4) : "ERROR: there are more than four noEntryTiles on the herbalistCharacter";
    }

    /**
     * This method removes one tile from the character card's tiles
     */
    public void takeOneTile(){
        tilesOnTheCard --;
    }

    public int getTilesOnTheCard() {
        return tilesOnTheCard;
    }
}
