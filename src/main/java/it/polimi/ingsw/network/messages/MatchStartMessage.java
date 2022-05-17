package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Player;

public class MatchStartMessage extends Message{
    private int firstPlayer;

    public MatchStartMessage(int firstPlayer_ID){
        this.firstPlayer = firstPlayer_ID;
        this.object = "start";
    }

    public MatchStartMessage(){}

    public int getFirstPlayer() {
        return firstPlayer;
    }

}
