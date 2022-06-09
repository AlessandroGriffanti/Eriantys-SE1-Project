package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

/**
 * This class represents the message sent by the server to the client to notify about the correct receiving of the message (client -> server)
 */
public class AckMessage extends Message {
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
    private ArrayList<Creature> students;
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
     * This attribute is the ID of the previous player who had the control over the professor
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
     * - previous: current island unified with the previous one
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
     * This attribute tells which player is the new master of the island where mother nature arrived, only if
     * the master changed
     */
    private int newMaster_ID = -1;
    /**
     * This attribute is true if there were enough students for refilling the clouds
     *                   false if there weren't enough students for refilling the clouds -> action_3 not played
     */
    private boolean action3Valid = true;
    /**
     * This attribute is the ID of the cloud chosen by the player to refill his entrance
     */
    private int cloudChosen_ID;
    /**
     * This attribute is true if the match has come to its end or false otherwise
     */
    private boolean endOfMatch = false;
    /**
     * This attribute is true if after action_3 there's a new round to play, false if there are other
     * players that must play their action phase yet
     */
    private boolean nextPlanningPhase = false;
    /**
     * This attribute is the color of the tower of a player or on an island
     */
    private Tower towerColor;

    public AckMessage(){
        this.object = "ack";
        this.recipient = -1;
        this.nextPlayer = -1;

        notAvailableDecks = new ArrayList<Wizard>();
        students = new ArrayList<Creature>();
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
    public void setStudents(ArrayList<Creature> students) {
        this.students = students;
    }

    public ArrayList<Creature> getStudents() {
        return students;
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

    // SETTER AND GETTER FOR cloudChosen_ID
    public void setCloudChosen_ID(int cloudChosen_ID) {
        this.cloudChosen_ID = cloudChosen_ID;
    }

    public int getCloudChosen_ID() {
        return cloudChosen_ID;
    }

    // SETTER AND GETTER FOR action3IsValid
    public void setAction3Valid(boolean action3Valid) {
        this.action3Valid = action3Valid;
    }

    public boolean isAction3Valid() {
        return action3Valid;
    }

    // SETTER AND GETTER FOR endOfMatch
    public void setEndOfMatch(boolean endOfMatch) {
        this.endOfMatch = endOfMatch;
    }

    public boolean isEndOfMatch() {
        return endOfMatch;
    }

    // SETTER AND GETTER FOR nextPlanningPhase
    public void setNextPlanningPhase(boolean nextPlanningPhase) {
        this.nextPlanningPhase = nextPlanningPhase;
    }

    public boolean isNextPlanningPhase() {
        return nextPlanningPhase;
    }

    // SETTER AND GETTER FOR towerColor
    public void setTowerColor(Tower towerColor) {
        this.towerColor = towerColor;
    }

    public Tower getTowerColor() {
        return towerColor;
    }
}

/*POSSIBLE VALUES OF "subObject":
   0. waiting:
      it means that the match has been just created and is waiting to start; no more data required.

   1. refillClouds:
      it means that all the clouds have been refilled and
      - 'students' contains all te students added on each cloud
      [ex. 2 players => 2 clouds with 3 students each => in the attribute there will be 6 objects Creature, the first 3 belong to the cloud0 the last 3 to the cloud1]

   2. tower_color:
      it means that the color chosen is legit and 'notAvailableTowerColors' contains the already chosen colors
      - 'nextPlayer'
      - notAvailableTowerColors: colors already chosen

   3. deck:
      it means that the deck chosen by the player is legit and 'notAvailableDecks' contains the decks that are no more available (useful for the next players)
      - 'nextPlayer'
      - notAvailableDecks: deck already chosen

   4. assistant:
      it means that the assistant chosen by the player is legit and 'assistantAlreadyUsedInThisRound' contains the assistants already chosen in this round's planning phase
      - 'nextPlayer'
      - assistantAlreadyUsedInThisRound: assistants already used in this round (could be useful)

   5. action_1_dining_room:
      it means that the student has been moved into the dining room and
        - 'nextPlayer': it doesn't change
        - 'studentMoved_ID' contains the ID of the student in the entrance of the recipient of this message
        - 'typeOfStudentMoved' contains the kind of student moved [Creature]  <-- maybe useless
        - 'professorTaken' tell us if the player has now control over the professor
        - 'previousOwnerOfProfessor' contains the ID of the previous owner of the professor

   6. action_1_island:
      it means that the student has been moved to the island and
        - 'nextPlayer': it doesn't change
        - 'studentMoved_ID' contains the ID of the student in the entrance of the recipient of this message
        - 'typeOfStudentMoved' contains the kind of student moved [Creature]  <-- maybe useless
        - 'destinationIsland_ID' contains the ID of the island where the student has been moved

   7.1. action_2_movement:
        it means that mother nature has been moved successfully and
        - 'nextPlayer': it doesn't change
        - 'destinationIsland_ID' the island reached by mother nature
        - 'removedNoEntryTile' tells if a no entry tile has been removed
                               if TRUE then the client can send the message for action_3
           these next attribute must be controlled only if removedNoEntryTile==true
        - 'islandThatLostNoEntryTile' tells form which island the no entry tile (if one has been removed) was taken
        - 'action3Valid' if removedNoEntryTile is true then the client must control this value otherwise he can just
                           ignore it
        - 'endOfMatch' it's true if action3IsValid=false
        - 'nextPlayer' is the next player of action phase (!= -1 only if removedNoEntry=true and action3IsValid=false
                       and endOfMatch=false)

   7.2. action_2_influence:
        it means that the influence has been computed and all changes needed were made
        - 'nextPlayer': it doesn't change
        - 'masterChanged': true if the master has changed, false otherwise
        - 'previousMaster_ID': the ID of the previous player master of the island or
                              -1 if the master did not change
        - 'newMaster_ID': the ID of the new master on the island reached by mother nature or
                         -1 if the master did not change
        - 'towerColor': the color of the tower currently on the island (where there is mother nature)
                        , that is the tower color of the new Master
        - 'endOfMatch'

   7.3 action_2_union:
       it means that the control on the union of the islands was done and the changes needed were applied
        - 'nextPlayer' + the player does not change (-> action_3)
                       + -1: if endOfMatch==true
                       + the next player of action phase (not action_3)
        - 'islandsUnified' contains a String that says if two or more islands have been unified
                          - none -> no unification
                          - previous
                          - next
                          - both
        - 'removedNoEntryTile' if an island  that was unified with the current one removed a noEntryTile
        - 'islandThatLostNoEntryTile' which one of the islands unified lost the noEntryTile
        - 'action3Valid'
        - 'endOfMatch' true if there are only three islands left, false if the match can continue

   8. action_3:
      it means that the students on the cloud were moved into the entrance of the player and
      - 'nextPlayer' contains the ID of the next player to move
      - 'cloudChosen_ID' contains the ID of the chosen cloud
      - 'students' contains the students in the recipient's entrance after the refill
      - 'nextPlanningPhase' true if the action phase ended, and it's now time to start a new round or
                            false if there are players that have not played their action phase yet
                            N.B. The planning phase starts from the 'RefillClouds' state
      - 'endOfMatch' true if there are no more students to be drawn from the bag or if a player
                     used all of his assistant cards

   9. Response to the CharacterRequestMessage:
      - subObject = <name of the character in lowercase> */