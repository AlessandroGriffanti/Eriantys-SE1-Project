package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;

import java.util.ArrayList;
import java.util.HashMap;

public class AckCharactersMessage extends AckMessage {


    /**
     * This attribute is the ID of the player after whose action this message is sent
     */
    private int recipient;
    /**
     * This attribute is the name of the character card used
     */
    private String character = "";
    /**
     * This attribute is the amount of coins currently kept in the general reserve
     * (at the center of the table)
     */
    private int coinReserve;
    /**
     * This attribute is a single student taken/put because of the effect of some character cards
     */
    private Creature student;
    /**
     * This attribute is the island chosen for the effect of the character card (if necessary)
     */
    private int island_ID;
    /**
     * This attribute is the list of students on the card (if necessary)
     */
    private ArrayList<Creature> studentsOnCard;
    /**
     * This attribute is the number of elements (e.g. students / no-entry-tiles) on a character
     */
    private int numberOfElementsOnTheCard;
    /**
     * This attribute is the list of students belonging to the player
     * (for example in the entrance of his school-board)
     */
    private ArrayList<Creature> entranceOfPlayer;
    /**
     * This attribute is the color of the tower of the new master on the island
     * chosen with the ambassador character card
     */
    private Tower towerColor = null;
    /**
     * This attribute is true if the match has come to its end or false otherwise
     */
    private boolean endOfMatch = false;
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
     * This attribute tells who the new master of the island where mother nature arrived is, only if
     * the master changed
     */
    private int newMaster_ID = -1;
    /**
     * This attribute tells if some islands have been unified
     * - none: current island unified with no other island
     * - previous: current island unified with the previous one
     * - next: current island unified with the next one
     * - both: current island unified with both the next and the previous
     */
    private String islandsUnified = "none";
    /**
     * This attribute is the kind of student chosen by the player or used during the
     * effect of a character
     */
    private Creature creature;
    /**
     * This attribute is the dining room of the player, for each type of students
     * the number of students on the table
     * key: type of students
     * value: number of student of the table
     */
    private HashMap<Creature, Integer> playerDiningRoom;
    /**
     * This attribute is the correspondence between player_ID and its professors
     * as list of creature
     * key: player's ID
     * value: array of Creature corresponding to the professors controlled by the player
     */
    private HashMap<Integer, ArrayList<Creature>> allPlayersProfessors = new HashMap<Integer, ArrayList<Creature>>();
    /**
     * This attribute represents the dining room of each player
     * key: ID of the player
     * value: hashMap representing the dining room of the player
     */
    private HashMap<Integer, HashMap<Creature, Integer>> allPlayersDiningRoom;


    public AckCharactersMessage(){this.object = "character_ack";}

    public AckCharactersMessage(int recipient_ID, String card, int coinReserve){
        this.object = "character_ack";
        this.recipient = recipient_ID;
        this.character = card;
        this.coinReserve = coinReserve;
    }

    // GETTER FOR recipient
    public int getRecipient() {
        return recipient;
    }

    // GETTER FOR card
    public String getCharacter() {
        return character;
    }

    // GETTER FOR coinReserve
    public int getCoinReserve() {
        return coinReserve;
    }

    // SETTER AND GETTER FOR student
    public void setStudent(Creature student) {
        this.student = student;
    }

    public Creature getStudent() {
        return student;
    }

    // SETTER AND GETTER FOR studentsOnCard [array]
    public void setStudentsOnCard(ArrayList<Creature> studentsOnCard) {
        this.studentsOnCard = studentsOnCard;
    }

    public ArrayList<Creature> getStudentsOnCard() {
        return studentsOnCard;
    }

    // SETTER AND GETTER FOR studentsOfPlayer
    public void setEntranceOfPlayer(ArrayList<Creature> entranceOfPlayer) {
        this.entranceOfPlayer = entranceOfPlayer;
    }

    public ArrayList<Creature> getEntranceOfPlayer() {
        return entranceOfPlayer;
    }

    // SETTER AND GETTER FOR island_ID

    public void setIsland_ID(int island_ID) {
        this.island_ID = island_ID;
    }

    public int getIsland_ID() {
        return island_ID;
    }

    // SETTER AND GETTER FOR numberOfElementsOnTheCard
    public void setNumberOfElementsOnTheCard(int numberOfElementsOnTheCard) {
        this.numberOfElementsOnTheCard = numberOfElementsOnTheCard;
    }

    public int getNumberOfElementsOnTheCard() {
        return numberOfElementsOnTheCard;
    }

    // SETTER AND GETTER FOR endOfMatch
    public void setEndOfMatch(boolean endOfMatch) {
        this.endOfMatch = endOfMatch;
    }

    public boolean isEndOfMatch() {
        return endOfMatch;
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

    // SETTER AND GETTER FOR islandsUnified
    public void setIslandsUnified(String islandsUnified) {
        this.islandsUnified = islandsUnified;
    }

    public String getIslandsUnified() {
        return islandsUnified;
    }

    // SETTER AND GETTER FOR creature
    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    public Creature getCreature() {
        return creature;
    }

    // SETTER AND GETTER FOR playerDiningRoom
    public void setPlayerDiningRoom(HashMap<Creature, Integer> playerDiningRoom) {
        this.playerDiningRoom = playerDiningRoom;
    }

    public HashMap<Creature, Integer> getPlayerDiningRoom() {
        return playerDiningRoom;
    }

    // ADDER AND GETTER FOR allPlayersProfessors
    public void setPlayerProfessors(int player_ID, ArrayList<Creature> playerProfessors) {
        allPlayersProfessors.put(player_ID, playerProfessors);
    }

    public HashMap<Integer, ArrayList<Creature>> getAllPlayersProfessors() {
        return allPlayersProfessors;
    }

    // ADDER AND GETTER FOR allPlayersDiningRoom
    public HashMap<Integer, HashMap<Creature, Integer>> getAllPlayersDiningRoom() {
        return allPlayersDiningRoom;
    }

    public void addPlayerDiningRoom(int player_ID, HashMap<Creature, Integer> playerDiningRoom){
        allPlayersDiningRoom.put(player_ID, playerDiningRoom);
    }

    @Override
    public void setTowerColor(Tower towerColor) {
        this.towerColor = towerColor;
    }

    @Override
    public Tower getTowerColor() {
        return towerColor;
    }
}

/*THE CLIENTS NEED TO READ DIFFERENT ATTRIBUTES BASED ON THE VALUE OF 'card':
        For every message must be set:
        - coinReserve: the current amount of money in the reserve
        - recipient: the player who used the character

        1. monk:
           - student: the type of student taken from the card and put on the island
           - studentsOnCard

        2. cook: the card will be taken into account during action_1, movement to dining room
           - no more attributes set

        3.1 ambassador_influence:
           - endOfMatch
           - masterChanged
           - previousMaster_ID
           - newMaster_ID

        3.2 ambassador_union:
           - islandsUnified
           - endOfMatch

        4. messenger:
           - no more attributes set, it just means that during the movement of mother nature
           (only of the player who used the card)it will be taken into account the messenger card effect

        5. herbalist:
           - island_ID: ID of the island where the no-entry-tile was put
           - getNumberOfElementsOnTheCard: number of no-entry-tiles remained on the character card

        6. centaur: the card will be taken into account during influence computation
           - no more attributes set

        7. jester:
           - studentsOnCard: all the students on the character card
           - entranceOfPlayer: the new entrance of the player

        8. knight:
           - no more attributes set

        9. mushroomsMerchant:
           - creature: type of student that won't be counted in the influence

        10. bard:
           - entranceOfPlayer: the new entrance of the player that used the character
           - playerDiningRoom: the new dining room of the player that used the character
           - allPlayersProfessors: for each player his professors table as array of Creature

        11. princess:
           - studentsOnCard: students on the princess character card
           - playerDiningRoom: the new diningRoomOfThePlayer
           - allPlayersProfessors: for each player his professors table as array of Creature

        12. trafficker:
           - creature: type of students chosen by the user of the character
           - allPlayersProfessors: for each player his professors table as array of Creature*/