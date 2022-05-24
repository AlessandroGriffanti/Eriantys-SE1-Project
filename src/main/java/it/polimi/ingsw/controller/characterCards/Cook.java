package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;

/**
 * This class represents the character card called 'cook' (second one in the rules' file)
 */
public class Cook  extends Character {

    public Cook(Controller controller) {
        this.controller = controller;
        this.price = 2;
    }

    /**
     * This method sets the cookUsed attribute of the CharactersManager object to true so that
     * during Action_1 state the player can take control over the professors even if he has the same number
     * of students of the previous owner of professor
     * @param request message containing island_ID and student_ID chosen by the player
     */
    @Override
    public void effect(ChosenCharacterMessage request) {
        controller.getCharactersManager().setCookUsed(true);
    }
}
