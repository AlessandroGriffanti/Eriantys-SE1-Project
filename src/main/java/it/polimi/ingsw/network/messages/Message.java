package it.polimi.ingsw.network.messages;

public abstract class Message {
    public String object;
    public String request;

    /*
    public Message(String object, String request) {
        this.object = object;
        this.request = request;
    }
    */

    public String getObject() {
        return object;
    }

    public String getRequest() {
        return request;
    }

    public void sentMessage() {

    }
}
