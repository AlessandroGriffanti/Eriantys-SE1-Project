package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class NoLobbyAvailableMessage extends Message {
    private int playerID;

    public NoLobbyAvailableMessage(int playerID){
        this.playerID = playerID;
        this.object = "no lobby available";
    }
    public int getPlayerID() {
        return playerID;
    }
}
