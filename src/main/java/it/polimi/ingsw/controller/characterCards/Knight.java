package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;

public class Knight extends Character {
    public Knight() {
        this.price = 2;
    }

    //TODO: influenza + 2 con questo personaggio durante il turno


    @Override
    public boolean effect(ChosenCharacterMessage request) {
return true;
    }
}
