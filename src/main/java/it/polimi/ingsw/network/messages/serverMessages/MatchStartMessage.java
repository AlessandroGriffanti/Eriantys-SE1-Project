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
     * This attribute is the number of players of the match
     */
    private int numPlayer;
    /**
     * This attribute is true if the match is played in expert mode, false otherwise
     */
    private boolean expertMode;
    /**
     * This attribute is the initial position of mother nature
     */
    private int motherNaturePosition;
    /**
     * This attribute is the list of students initially positioned on each island
     */
    private ArrayList<Creature> studentsOnIslands;
    //il primo studente nella lista è lo studente sull'isola successiva alla posizione di madre natura, il secondo sulla seconda dopo
    //la posizione di madre natura e così via. N.B. sulla sesta dopo madre natura(quella opposta) non ci sono studenti quindi è da saltare.

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
    /**
     * This attribute is the list of students on the character monk;
     * it's NULL if the character monk was not chosen or if the match is not in
     * expert mode
     */
    private ArrayList<Creature> monkStudents = null;
    /**
     * This attribute is the list of students on the character jester;
     * it's NULL if the character monk was not chosen or if the match is not in
     * expert mode
     */
    private ArrayList<Creature> jesterStudents = null;
    /**
     * This attribute is the list of students on the character princess;
     * it's NULL if the character monk was not chosen or if the match is not in
     * expert mode
     */
    private ArrayList<Creature> princessStudents = null;

    public MatchStartMessage(){
        this.object = "start";
    }

    public MatchStartMessage(int firstPlayer_ID, int motherNaturePosition, int numPlayer, boolean expertMode){
        this.numPlayer = numPlayer;
        this.expertMode = expertMode;
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



    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }


    public Set<String> getCharacters() {
        return characters;
    }

    public HashMap<Integer, ArrayList<Creature>> getStudentsInEntrance() {
        return studentsInEntrance;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public void setStudentsOnIslands(ArrayList<Creature> studentsOnIslands) {
        this.studentsOnIslands = studentsOnIslands;
    }
    public ArrayList<Creature> getStudentsOnIslands() {
        return studentsOnIslands;
    }

    public void setMonkStudents(ArrayList<Creature> monkStudents) {
        this.monkStudents = monkStudents;
    }

    public ArrayList<Creature> getMonkStudents() {
        return monkStudents;
    }

    public void setJesterStudents(ArrayList<Creature> jesterStudents) {
        this.jesterStudents = jesterStudents;
    }

    public ArrayList<Creature> getJesterStudents() {
        return jesterStudents;
    }

    public void setPrincessStudents(ArrayList<Creature> princessStudents) {
        this.princessStudents = princessStudents;
    }

    public ArrayList<Creature> getPrincessStudents() {
        return princessStudents;
    }
}
