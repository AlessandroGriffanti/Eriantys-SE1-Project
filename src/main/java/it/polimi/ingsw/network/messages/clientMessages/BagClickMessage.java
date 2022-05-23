package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class BagClickMessage extends Message {
    public BagClickMessage() {
        this.object = "draw";
    }
}
