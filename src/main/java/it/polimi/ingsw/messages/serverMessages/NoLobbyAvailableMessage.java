package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

/**
 * This class represents the message sent from the server to the client when a player asks to join a match,
 * but there is no one available (because full or no one created), and so the client is asked to provide
 * the specs for a new match (The nickname sent before is accepted).
 */
public class NoLobbyAvailableMessage extends Message {
    /**
     * This attribute represents the player ID (it will be 0 since a new match must be created).
     */
    private int playerID;

    public NoLobbyAvailableMessage(int playerID){
        this.playerID = playerID;
        this.object = "no lobby available";
    }
    public int getPlayerID() {
        return playerID;
    }
}
