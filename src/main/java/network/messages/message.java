package network.messages;

public abstract class message {
    public String object;
    public String request;


    public String getObject() {
        return object;
    }

    public String getRequest() {
        return request;
    }
     public abstract void sentMessage();
}
