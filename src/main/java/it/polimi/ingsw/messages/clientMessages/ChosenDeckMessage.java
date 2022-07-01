package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.messages.Message;

/**
 * This class represent the message sent by the client to the server containing the deck chosen.
 */
public class ChosenDeckMessage extends Message {
    /**
     * This attribute represents the deck chosen by the player.
     */
    Wizard deck = null;

    public ChosenDeckMessage() {
        this.object = "deck";
    }

    public void setDeck(Wizard deck) {
        this.deck = deck;
    }

    public Wizard getDeck() {
        return deck;
    }
}
