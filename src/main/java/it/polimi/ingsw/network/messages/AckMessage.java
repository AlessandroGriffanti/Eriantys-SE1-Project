package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.Wizard;

import java.util.ArrayList;

/**
 * This class represents the message sent by the server to the client to notify about the correct receiving of the message (client -> server)
 */
public class AckMessage extends Message{
    /**
     * This attribute is the second object of the message, and it tells which data are stored in this message
     */
    private String subObject = "";
    /**
     * This attribute is the ID of the Client to whom this ack message is addressed;
     * if NULL there is no recipient
     */
    private int recipient;
    /**
     * This attribute indicates who is the next player to make his move
     */
    private int nextPlayer;
    /**
     * This attribute is the list of all the students added to the clouds
     */
    private ArrayList<Creature> studentsAddedToTheClouds;
    /**
     * This attribute is the list of already chosen tower colors
     */
    private ArrayList<Tower> notAvailableTowerColors;
    /**
     * This attribute is the list of already chosen decks
     */
    private ArrayList<Wizard> notAvailableDecks;
    /**
     * This attribute is the list already chosen assistant cards in this round
     */
    private ArrayList<Integer> assistantAlreadyUsedInThisRound;
    /**
     * This attribute is the ID of the moved student
     */
    private int studentMoved_ID;
    /**
     * This attribute is the type of the student that has just been moved
     */
    private Creature typeOfStudentMoved = null;
    /**
     * This attribute tells if the player took control on the professor after
     * moving a student into the dining room
     */
    private boolean professorTaken = false;
    /**
     * This attribute is the ID of the previouse player who had the control over the professor
     * taken by the current player (recipient of this message)
     */
    private int previousOwnerOfProfessor;
    /**
     * This attribute is the ID of the island where the student has been moved, in case the player chose to move
     * the student on an island;
     * or it can also be the ID of the island where mother nature was moved during action_2
     */
    private int destinationIsland_ID;
    /**
     * This attribute tells if some islands have been unified
     * - none: current island unified with no other island
     * - previous: current island unified woth the previous one
     * - next: current island unified with the next one
     * - both: current island unified with both the next and the previous
     */
    private String islandsUnified = "none";
    /**
     * This attribute tells if a noEntryTile was removed from an island
     * (specified by the attribute)
     */
    private boolean removedNoEntryTile = false;
    /**
     * This attribute tells from which island the noEntryTile was taken
     */
    private int islandThatLostNoEntryTile;
    /**
     * This attribute tells if the master on the island reached by mother nature changed
     */
    private boolean masterChanged = false;
    /**
     * This attribute tells, if the master changed, who was the previous one;
     * the current master could anyone of the players not necessarily the one who moved mother nature or
     * could be no one if there wasn't any tower yet
     */
    private int previousMaster_ID = -1;
    /**
     * This attribute tells who is the new master of the island where mother nature arrived, only if
     * the master changed
     */
    private int newMaster_ID = -1;


    public AckMessage(){
        this.object = "ack";
        this.recipient = -1;
        this.nextPlayer = -1;

        notAvailableDecks = new ArrayList<Wizard>();
        studentsAddedToTheClouds = new ArrayList<Creature>();
        assistantAlreadyUsedInThisRound = new ArrayList<Integer>();
    }

    public void setSubObject(String subObject){
        this.subObject = subObject;
    }

    public String getSubObject() {
        return subObject;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    //SETTER AND GETTER FOR studentsAddedToTheClouds
    public void setStudentsAddedToTheClouds(ArrayList<Creature> studentsAddedToTheClouds) {
        this.studentsAddedToTheClouds = studentsAddedToTheClouds;
    }

    public ArrayList<Creature> getStudentsAddedToTheClouds() {
        return studentsAddedToTheClouds;
    }

    //SETTER AND GETTER FOR notAvailableDecks
    public void setNotAvailableDecks(ArrayList<Wizard> notAvailableDecks) {
        this.notAvailableDecks = notAvailableDecks;
    }

    public ArrayList<Wizard> getNotAvailableDecks() {
        return notAvailableDecks;
    }

    //SETTER AND GETTER FOR assistantAlreadyUsedInThisRound
    public void setAssistantAlreadyUsedInThisRound(ArrayList<Integer> assistantAlreadyUsedInThisRound) {
        this.assistantAlreadyUsedInThisRound = assistantAlreadyUsedInThisRound;
    }

    public ArrayList<Integer> getAssistantAlreadyUsedInThisRound() {
        return assistantAlreadyUsedInThisRound;
    }

    //SETTER AND GETTER FOR notAvailableTowerColors

    public void setNotAvailableTowerColors(ArrayList<Tower> notAvailableTowerColors) {
        this.notAvailableTowerColors = notAvailableTowerColors;
    }

    public ArrayList<Tower> getNotAvailableTowerColors() {
        return notAvailableTowerColors;
    }

    // SETTER AND GETTER FOR studentMoved_ID
    public void setStudentMoved_ID(int studentMoved_ID) {
        this.studentMoved_ID = studentMoved_ID;
    }

    public int getStudentMoved_ID() {
        return studentMoved_ID;
    }

    // SETTER AND GETTER FOR typeOfStudentMoved
    public void setTypeOfStudentMoved(Creature typeOfStudentMoved) {
        this.typeOfStudentMoved = typeOfStudentMoved;
    }

    public Creature getTypeOfStudentMoved() {
        return typeOfStudentMoved;
    }

    // SETTER AND GETTER FOR professorTaken
    public void setProfessorTaken(boolean professorTaken) {
        this.professorTaken = professorTaken;
    }

    public boolean isProfessorTaken() {
        return professorTaken;
    }

    // SETTER AND GETTER FOR previousOwnerOfProfessor
    public void setPreviousOwnerOfProfessor(int previousOwnerOfProfessor) {
        this.previousOwnerOfProfessor = previousOwnerOfProfessor;
    }

    public int getPreviousOwnerOfProfessor() {
        return previousOwnerOfProfessor;
    }

    // SETTER AND GETTER FOR destinationIsland_ID
    public void setDestinationIsland_ID(int destinationIsland_ID) {
        this.destinationIsland_ID = destinationIsland_ID;
    }

    public int getDestinationIsland_ID() {
        return destinationIsland_ID;
    }

    // SETTER AND GETTER FOR islandsUnified
    public void setIslandsUnified(String islandsUnified) {
        this.islandsUnified = islandsUnified;
    }

    public String getIslandsUnified() {
        return islandsUnified;
    }

    // SETTER AND GETTER FOR removedNoEntryTile
    public void setRemovedNoEntryTile(boolean removedNoEntryTile) {
        this.removedNoEntryTile = removedNoEntryTile;
    }

    public boolean isRemovedNoEntryTile() {
        return removedNoEntryTile;
    }

    // SETTER AND GETTER FOR islandThatLostNoEntryTile
    public void setIslandThatLostNoEntryTile(int islandThatLostNoEntryTile) {
        this.islandThatLostNoEntryTile = islandThatLostNoEntryTile;
    }

    public int getIslandThatLostNoEntryTile() {
        return islandThatLostNoEntryTile;
    }

    // SETTER AND GETTER FOR masterChanged
    public void setMasterChanged(boolean masterChanged) {
        this.masterChanged = masterChanged;
    }

    public boolean isMasterChanged() {
        return masterChanged;
    }

    // SETTER AND GETTER FOR previousMaster_ID
    public void setPreviousMaster_ID(int previousMaster_ID) {
        this.previousMaster_ID = previousMaster_ID;
    }

    public int getPreviousMaster_ID() {
        return previousMaster_ID;
    }

    // SETTER AND GETTER FOR newMaster_ID

    public void setNewMaster_ID(int newMaster_ID) {
        this.newMaster_ID = newMaster_ID;
    }

    public int getNewMaster_ID() {
        return newMaster_ID;
    }
}

/*POSSIBLE VALUES OF "subObject":
   0. waiting:
      it means that the match has been just created and is waiting to start; no more data required.

   1. fillClouds:
      it means that all the clouds have been refilled and 'studentsAddedToTheClouds' contains all te students
      added on each cloud
      [ex. 2 players => 2 clouds with 3 students each => in the attribute there will be 6 objects Creature, the first 3 belong to the cloud0 the last 3 to the cloud1]

   2. tower_color:
      it means that the color chosen is legit and 'notAvailableTowerColors' contains the already chosen colors

   3. deck:
      it means that the deck chosen by the player is legit and 'notAvailableDecks' contains the decks that are no more available (useful for the next players)

   4. assistant:
      it means that the assistant chosen by the player is legit and 'assistantAlreadyUsedInThisRound' contains the assistants already chosen in this round's planning phase

   5. action_1_dining_room:
      it means that the student has been moved into the dining room and
        - 'nextPlayer' = -1
        - 'studentMoved_ID' contains the ID of the student in the entrance of the recipient of this message
        - 'typeOfStudentMoved' contains the kind of student moved [Creature]  <-- maybe useless
        - 'professorTaken' tell us if the player has now control over the professor
        - 'previousOwnerOfProfessor' contains the ID of the previous owner of the professor

   6. action_1_island:
      it means that the student has been moved to the island and
        - 'nextPlayer' = -1
        - 'studentMoved_ID' contains the ID of the student in the entrance of the recipient of this message
        - 'typeOfStudentMoved' contains the kind of student moved [Creature]  <-- maybe useless
        - 'destinationIsland_ID' contains the ID of the island where the student has been moved

   7. action_2:
      it means that mother nature has been moved successfully and
        - 'nextPlayer' = -1
        - 'destinationIsland_ID' the island reached by mother nature
        - 'removedNoEntryTile' tells if a no entry tile has been removed
        - 'islandThatLostNoEntryTile' tells form which island the no entry tile (if one has been removed) was taken
        - 'masterChanged' true if the master has changed, false otherwise
        - 'previousMaster_ID' the ID of the previous player master of the island or
                              -1 if the master did not change
        - 'newMaster_ID' the ID of the new master on the island reached by mother nature or
                         -1 if the master did not change
        - 'islandsUnified' contains a String that says if two or more islands have been unified
                          - none -> no unification
                          - previous
                          - next
                          - both*/