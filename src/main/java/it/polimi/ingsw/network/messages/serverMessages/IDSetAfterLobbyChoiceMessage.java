package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent by the server to let the client know its
 * player ID after choosing the lobby to join.
 */
public class IDSetAfterLobbyChoiceMessage extends Message {
    /**
     * This attribute is the ID of the player inside the match
     */
    private int playerID;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public IDSetAfterLobbyChoiceMessage(int playerIDset) {
        this.object = "playerID_set";
        this.playerID = playerIDset;
    }
}
