package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class PongMessage extends Message {
    public PongMessage (){
        this.object = "pong";
    }
}
