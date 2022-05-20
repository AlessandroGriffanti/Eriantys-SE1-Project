package it.polimi.ingsw.network.messages;

public class NoLobbyAvailableMessage extends Message {
    private int playerID;

    public NoLobbyAvailableMessage(int playerID) {
        this.object = "no lobby available";
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

}
