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
     * This method sets the cookUsed attribute of the CharactersManager object to true so that
     * during Action_1 state the player can take control over the professors even if he has the same number
     * of students of the previous owner of professor.
     * @param request message containing island_ID and student_ID chosen by the player
     * @return true if the effect was activated, false otherwise
     */
    @Override
    public boolean effect(CharacterDataMessage request) {
        increasePrice();

        controller.getCharactersManager().setCookUsed(true);

        int coinsInReserve = controller.getMatch().getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "cook", coinsInReserve);

        controller.sendMessageAsBroadcast(ack);

        return true;
    }
}
