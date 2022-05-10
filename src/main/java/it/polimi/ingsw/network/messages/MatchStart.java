package it.polimi.ingsw.network.messages;

public class MatchStart extends Message{
    private int firstPlayer;

    public MatchStart(int firstPlayer_ID){
        this.firstPlayer = firstPlayer_ID;
        this.object = "start";
    }
}
