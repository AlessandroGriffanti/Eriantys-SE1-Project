package it.polimi.ingsw.network.messages;

public class LoginSuccessMessage extends Message{
    private int playerID;

    private boolean newMatchNeeded;
    //TRUE  -> to create
    //FALSE -> already created

    public LoginSuccessMessage(int playerID, boolean newMatchNeeded){
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
