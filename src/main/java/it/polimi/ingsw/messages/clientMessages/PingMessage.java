package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

public class PingMessage extends Message {
    public PingMessage (){
        this.object = "ping";
    }
}
