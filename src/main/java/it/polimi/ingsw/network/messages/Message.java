package it.polimi.ingsw.network.messages;

public class Message {
    /**
     * This attribute is the object of the message
     */
    protected String object;
    protected String request;
    /**
     * This attribute is the ID of the player who sent the message
     */
    protected int senderID;

    public String getObjectOfMessage() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getRequest() {
        return request;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }
}
