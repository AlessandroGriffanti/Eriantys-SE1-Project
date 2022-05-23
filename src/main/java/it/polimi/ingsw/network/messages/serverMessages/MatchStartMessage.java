package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

/**
 *This class represents the message sent by the server when a match starts to each one of the players
 */
public class MatchStartMessage extends Message {
    private int firstPlayer;
    private int motherNaturePosition;
    private ArrayList<Creature> studentsInEntrance;
    private ArrayList<String> characters;

    public MatchStartMessage(){}

    public MatchStartMessage(int firstPlayer_ID, int motherNaturePosition, ArrayList<Creature> studentInEntrance){
        this.object = "start";
        this.firstPlayer = firstPlayer_ID;
        this.studentsInEntrance = studentInEntrance;
        this.motherNaturePosition = motherNaturePosition;
    }

    public void setCharacters(ArrayList<String> characters) {
        this.characters = characters;
    }
}
