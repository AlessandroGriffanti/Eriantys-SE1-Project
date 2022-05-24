package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;

public class Centaur extends Character {
    public Centaur() {
        this.price = 3;
    }

    /** the idea is that we change the tower color to all the players in the game to a constant called "TRANSPARENT" and, in the
     * controller, where we manage the influence computing, we check if the tower color of the player is TRANSPARENT and , if so,
     * we do not count its influence.
     *
     * Another Idea could be to delete the "TRANSPARENT" costant in the Tower enum and 'delete' this card and handle it all in the
     * controller where the influence is computed.
     * @param request
     *
     */
    @Override
    public void effect(ChosenCharacterMessage request) {

    }
}
