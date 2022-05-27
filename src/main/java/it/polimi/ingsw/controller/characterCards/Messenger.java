package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

/**
 * This class represents the character card called 'messenger' (the fourth in the rules file).
 * It allows the players to move mother nature two steps farther than the movement value
 * written on each assistant card.
 */
public class Messenger extends Character {

    public Messenger(Controller controller){
        this.controller = controller;
    }
    /**
     * This method set to true the attribute in CharactersManager instance that tells if the
     * messenger card has been used or not
     * @param request message sent by the client
     * @return true if the effect was activated, false otherwise
     */
    public boolean effect(CharacterDataMessage request) {
        increasePrice();

        controller.getCharactersManager().setMessengerActive(true);

        int coinsInReserve = controller.getMatch().getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "messenger", coinsInReserve);

        controller.sendMessageAsBroadcast(ack);

        return true;
    }
}
