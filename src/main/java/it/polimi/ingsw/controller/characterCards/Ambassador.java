package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;

/**
 * This class represents the character card called 'ambassador' (third in the rules file)
 */
public class Ambassador extends Character {

    @Override
    public boolean checkCharacterAvailability() {
        return false;
    }

    public void effect(CharacterDataMessage request) {
    }



}
