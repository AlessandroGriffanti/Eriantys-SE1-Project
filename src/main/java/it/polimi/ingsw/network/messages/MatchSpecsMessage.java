package it.polimi.ingsw.network.messages;

/**
 * This class represents the message sent by the client to the server providing the information needed to creat the new match
 */
public class MatchSpecsMessage extends Message{
    int numOfPlayers;
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
