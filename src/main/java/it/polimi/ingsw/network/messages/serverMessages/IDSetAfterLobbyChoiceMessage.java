package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class IDSetAfterLobbyChoiceMessage extends Message {
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
