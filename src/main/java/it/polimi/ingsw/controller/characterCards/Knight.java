package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

public class Knight extends Character {


    public Knight(Controller controller) {
        this.price = 2;
        this.controller = controller;
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
     * This method sets the ID of the player that is using the knight character
     * @param request message sent by the client
     */
    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        controller.getCharactersManager().setKnightUser(request.getSender_ID());

        int coinsInReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "knight", coinsInReserve);

        controller.sendMessageAsBroadcast(ack);
    }
}
