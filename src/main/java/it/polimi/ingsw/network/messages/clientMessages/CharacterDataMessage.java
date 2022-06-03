package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

public class CharacterDataMessage extends Message {


    /**
     * This attribute is the name of the character card chosen by the player
     */
    private String character = "";
    /**
     * This attribute is the ID of the student chosen by the player (used for various cards)
     */
    private int student_ID;
    /**
     * This attribute is the ID of the island chosen by the player (used for various cards)
     */
    private int island_ID;
    /**
     * This attribute is the list of IDs of the elements (students) on the character card
     * selected by the player
     */
    private ArrayList<Integer> elementsFromCard;
    /**
     * This attribute is the list of IDs of the students selected by the player
     * from his entrance
     */
    private ArrayList<Integer> studentsFromPlayerEntrance;
    /**
     * This attribute is the list of types of student selected by the player
     * from his dining room
     */
    private ArrayList<Creature> studentsFromPlayerDiningRoom;
    /**
     * This attribute is the type of student chosen by the player (ex. character MushroomsMerchant)
     */
    private Creature creature = null;


    public CharacterDataMessage(){
        this.object = "character_data";
    }

    public CharacterDataMessage(int sender_ID, String character){
        this.sender_ID = sender_ID;
        this.character = character;
        this.object = "character_data";
    }

    // SETTER AND GETTER FOR cardName
    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    // SETTER AND GETTER FOR student_ID
    public void setStudent_ID(int student_ID) {
        this.student_ID = student_ID;
    }

    public int getStudent_ID() {
        return student_ID;
    }

    //SETTER AND GETTER FOR island_ID
    public void setIsland_ID(int island_ID) {
        this.island_ID = island_ID;
    }

    public int getIsland_ID() {
        return island_ID;
    }

    // SETTER AND GETTER FOR elementsFromCard
    public void setElementsFromCard(ArrayList<Integer> elementsFromCard) {
        this.elementsFromCard = elementsFromCard;
    }

    public ArrayList<Integer> getElementsFromCard() {
        return elementsFromCard;
    }

    // SETTER AND GETTER FOR studentsFromPlayerEntrance
    public void setStudentsFromPlayerEntrance(ArrayList<Integer> studentsFromPlayerEntrance) {
        this.studentsFromPlayerEntrance = studentsFromPlayerEntrance;
    }

    public ArrayList<Integer> getStudentsFromPlayerEntrance() {
        return studentsFromPlayerEntrance;
    }

    // SETTER AND GETTER FOR creature
    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    public Creature getCreature() {
        return creature;
    }

    // SETTER AND GETTER FOR studentsFromPlayerDiningRoom
    public void setStudentsFromPlayerDiningRoom(ArrayList<Creature> studentsFromPlayerDiningRoom) {
        this.studentsFromPlayerDiningRoom = studentsFromPlayerDiningRoom;
    }

    public ArrayList<Creature> getStudentsFromPlayerDiningRoom() {
        return studentsFromPlayerDiningRoom;
    }
}

/*POSSIBLE VALUES OF cardName:
        For every message must be set:
        - sender_ID

*       1. "monk":
*           - student_ID: ID of the student on the card
*           - island_ID: ID of the island where the student will be put
        2. "cook":
            - no further attributes

        4. "messenger":
            - no further attributes

        5. "herbalist":
            - island_ID: the ID of the island where to put the noEntryTile

        6. "centaur":
            - no further attributes

        7. "jester":
            - studentsFromPlayerEntrance: the list of IDs of the students in the entrance of the player
            - elementsFromCard: the list of IDs of the students on the character card

        8. "knight":
            - no more attributes

        9. "mushroomsMerchant":
            - creature: type of student chosen by the player

        10. "bard":
            - studentsFromPlayerDiningRoom
            - studentsFromPlayerEntrance

        11. "princess":
            - student_ID: the ID of the student chosen from the character card

        12. "trafficker":
            - creature: the type of students chosen by the player*/
