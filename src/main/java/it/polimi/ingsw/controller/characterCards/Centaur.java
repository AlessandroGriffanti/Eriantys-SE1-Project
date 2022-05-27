package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

/**
 * This class represents the character card called 'centaur' (sixth in the rules' file).
 * It allows the players not to count towers during influence computation.
 */
public class Centaur extends Character {


    public Centaur(Controller controller) {
        this.price = 3;
        this.controller = controller;
    }

    /** This method sets the attribute centaurUsed to true; this means that the towers on the islands
     * won't be taken into account during the influence computation
     * @param request the message containing the request of the client to use the character
     * @return true if the effect was activated, false otherwise
     */
    @Override
    public boolean effect(CharacterDataMessage request) {
        increasePrice();

        controller.getCharactersManager().setCentaurUsed(true);

        int coinsInReserve = controller.getMatch().getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "centaur", coinsInReserve);

        controller.sendMessageAsBroadcast(ack);

        return true;
    }
}
