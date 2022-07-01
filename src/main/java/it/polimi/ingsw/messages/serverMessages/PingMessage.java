package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

public class PingMessage extends Message {
    public PingMessage (){
        this.object = "ping";
    }
}
