package it.polimi.ingsw.network.messages;

public class Message {
    /**
     * This attribute is the object of the message
     */
    protected String object;
    /**
     * This attribute is the ID of the player who sent the message
     */
    protected int sender_ID;

    public String getObjectOfMessage() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getSender_ID() {
        return sender_ID;
    }

    public void setSender_ID(int sender_ID) {
        this.sender_ID = sender_ID;
    }
}
