package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * This class represents the message sent by the client to the server providing the information needed to creat the new match.
 */
public class MatchSpecsMessage extends Message {
    /**
     * It represents the number of player of the new lobby.
     */
    int numOfPlayers;

    /**
     * It tells if the match will be played in expert mode or not.
     */
    boolean expertMode;

    public MatchSpecsMessage(){
        this.object = "creation";
    }

    public MatchSpecsMessage(int numberOfPlayers, boolean expertMode){
        this.object = "creation";
        this.numOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}
