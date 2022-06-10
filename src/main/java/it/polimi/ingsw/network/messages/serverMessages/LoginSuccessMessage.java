package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class LoginSuccessMessage extends Message {
    private int playerID;

    private boolean newMatchNeeded; // todo: not needed -> can be removed
    //TRUE  -> to create
    //FALSE -> already created

    public LoginSuccessMessage(int playerID, boolean newMatchNeeded){
        this.object = "LoginSuccess";
        this.newMatchNeeded = newMatchNeeded;
        this.playerID = playerID;
    }

    public LoginSuccessMessage(){}

    public int getPlayerID() {
        return playerID;
    }

    public boolean getNewMatchNeeded() {
        return newMatchNeeded;
    }

}
