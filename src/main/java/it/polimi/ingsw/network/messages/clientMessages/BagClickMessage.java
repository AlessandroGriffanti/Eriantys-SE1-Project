package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent by the client to the server when the students have been drawn from the bag.
 */
public class BagClickMessage extends Message {
    public BagClickMessage() {
        this.object = "draw";
    }
}
