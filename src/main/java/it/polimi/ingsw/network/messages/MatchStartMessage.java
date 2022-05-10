package it.polimi.ingsw.network.messages;

public class MatchStartMessage extends Message{
    private int firstPlayer;

    public MatchStartMessage(int firstPlayer_ID){
        this.firstPlayer = firstPlayer_ID;
        this.object = "start";
    }
}
