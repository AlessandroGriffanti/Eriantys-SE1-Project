package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Creature;

import java.util.ArrayList;

/**
 *
 */
public class MatchStartMessage extends Message{
    private int firstPlayer;
    private int motherNaturePosition;
    private ArrayList<Creature> studentsInEntrance;

    public MatchStartMessage(int firstPlayer_ID, int motherNaturePosition, ArrayList<Creature> studentInEntrance){
        this.object = "start";
        this.firstPlayer = firstPlayer_ID;
        this.studentsInEntrance = studentInEntrance;
        this.motherNaturePosition = motherNaturePosition;
    }
}
