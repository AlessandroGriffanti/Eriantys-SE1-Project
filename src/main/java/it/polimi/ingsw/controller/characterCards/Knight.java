package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;

public class Knight extends Character {
    public Knight() {
        this.price = 2;
    }

    //TODO: influenza + 2 con questo personaggio durante il turno


    @Override
    public boolean effect(CharacterDataMessage request) {
return true;
    }
}
