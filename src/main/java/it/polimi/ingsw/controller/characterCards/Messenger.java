package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.messages.serverMessages.AckCharactersMessage;

/**
 * This class represents the character card called 'messenger' (the fourth in the rules file).
 * It allows the players to move mother nature two steps farther than the movement value
 * written on each assistant card.
 */
public class Messenger extends Character {

    public Messenger(Controller controller){
        this.controller = controller;
        this.price = 1;
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
     * This method set to true the attribute in CharactersManager instance that tells if the
     * messenger card has been used or not
     * @param request message sent by the client
     */
    public void effect(CharacterDataMessage request) {
        increasePrice();

        controller.getCharactersManager().setMessengerActive(true);

        int coinsInReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "messenger", coinsInReserve);

        int coinsOfPlayer = controller.getMatch().getPlayerByID(request.getSender_ID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }
}
