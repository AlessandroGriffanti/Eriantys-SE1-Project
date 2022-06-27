package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent by the server when the player
 * created the match
 */
public class AckMatchCreationMessage extends Message {
    /**
     * This attribute is the ID of the player
     */
    private int playerID;

    /**
     * This attribute specifies if the match must be created (specs needed) or not
     */
    private boolean newMatchNeeded;

    public AckMatchCreationMessage(int playerID, boolean newMatchNeeded){
        this.object = "MatchCreation";
        this.newMatchNeeded = newMatchNeeded;
        this.playerID = playerID;
    }

    public AckMatchCreationMessage(){}

    public int getPlayerID() {
        return playerID;
    }

    public boolean getNewMatchNeeded() {
        return newMatchNeeded;
    }

}
