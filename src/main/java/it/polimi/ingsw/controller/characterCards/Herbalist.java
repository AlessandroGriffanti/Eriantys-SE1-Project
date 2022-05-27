package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

/**
 * This class represents the character card called 'herbalist' (the fifth in the rules file)
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
     * This method controls if there are sufficient tiles on the character, if so it takes one of them and puts it
     * on the island specified by the player
     * @param request message containing the ID of the chosen island
     */
    @Override
    public void effect(ChosenCharacterMessage request) {
        increasePrice();

        int island_ID = request.getIsland_ID();
        Archipelago island = controller.getMatch().getRealmOfTheMatch().getArchipelagos().get(island_ID);

        // the island must not be null (it would mean an error)
        assert island != null;

        /* if there are any no-entry-tiles on the character then we can put one
        * of this on an island*/
        if(tilesOnTheCard > 0){
            // take a tile from the character
            takeOneTile();
            // put the tile on the island chosen by the player
            island.addNoEntryTile();

            AckCharactersMessage ack = new AckCharactersMessage();
            ack.setRecipient(request.getSender_ID());
            ack.setCard("herbalist");
            ack.setIsland_ID(island_ID);
            ack.setNumberOfElementsOnTheCard(tilesOnTheCard);

            controller.sendMessageAsBroadcast(ack);
        }else{
            NackMessage nack = new NackMessage();
            nack.setSubObject("herbalist");

            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }
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
