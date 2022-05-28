package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

/**
 * This class represents the character card called 'cook' (second in the rules' file).
 * It allows players to take control over the professors even if he has the same number
 * of students of the previous owner of those professors.
 */
public class Cook  extends Character {

    public Cook(Controller controller) {
        this.controller = controller;
        this.price = 2;
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
     * This method sets the cookUsed attribute of the CharactersManager object to true so that
     * during Action_1 state the player can take control over the professors even if he has the same number
     * of students of the previous owner of professor.
     * @param request message containing island_ID and student_ID chosen by the player
     */
    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        controller.getCharactersManager().setCookActive(true);

        int coinsInReserve = controller.getMatch().getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "cook", coinsInReserve);

        controller.sendMessageAsBroadcast(ack);
    }
}
