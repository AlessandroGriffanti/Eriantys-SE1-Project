package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *This class represents the message sent by the server when a match starts to each one of the players
 */
public class MatchStartMessage extends Message {
    /**
     * This attribute is the ID of the first player of the match
     */
    private int firstPlayer;
    /**
     * This attribute is the initial position of mother nature
     */
    private int motherNaturePosition;
    /**
     * This attribute contains the list of students in the entrance of each player
     * key: ID of the player
     * value: list of students
     */
    private HashMap<Integer, ArrayList<Creature>> studentsInEntrance = new HashMap<>();
    /**
     * This attribute contains the character randomly chosen for this match
     */
    private Set<String> characters;

    public MatchStartMessage(){
        this.object = "start";
    }

    public MatchStartMessage(int firstPlayer_ID, int motherNaturePosition){
        this.object = "start";
        this.firstPlayer = firstPlayer_ID;
        this.motherNaturePosition = motherNaturePosition;
    }

    public void setCharacters(Set<String> characters) {
        this.characters = characters;
    }

    public int getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * This method adds one entry to the HashMap of studentsInEntrance
     * @param player_ID ID of the player
     * @param students list of students in the entrance of the player
     */
    public void setStudentsInEntrance(int player_ID, ArrayList<Creature> students){
        studentsInEntrance.put(player_ID, students);
    }
}
