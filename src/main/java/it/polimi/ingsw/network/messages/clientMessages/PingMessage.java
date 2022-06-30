package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class PingMessage extends Message {
    public PingMessage (){
        this.object = "ping";
    }
}
