package it.polimi.ingsw.network.messages;

public class Message {
    public String object;
    public String request;

    /*
    public Message(String object, String request) {
        this.object = object;
        this.request = request;
    }
    */

    public String getObjectOfMessage() {
        return object;
    }

    public String getRequest() {
        return request;
    }

    public void sentMessage() {

    }
}
