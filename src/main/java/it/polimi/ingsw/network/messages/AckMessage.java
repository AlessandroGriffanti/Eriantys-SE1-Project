package it.polimi.ingsw.network.messages;

/**
 * This class represents the message sent by the server to the client to notify about the correct receiving of the message (client -> server)
 */
public class AckMessage extends Message{

    public AckMessage(){
        this.object = "ack";
    }

}
