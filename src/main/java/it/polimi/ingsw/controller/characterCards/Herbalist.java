package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;

public class Herbalist extends Character {
    private int remainingNoEntryTiles;
    private int islandID;

    public Herbalist() {
        this.price = 2;
        this.remainingNoEntryTiles = 4;
    }

    @Override
    public void effect(ChosenCharacterMessage request) {

    }
}
