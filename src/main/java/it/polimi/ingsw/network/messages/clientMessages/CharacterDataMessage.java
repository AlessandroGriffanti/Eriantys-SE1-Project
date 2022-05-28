package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

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


    public CharacterDataMessage(){
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

}

/*POSSIBLE VALUES OF cardName:
        For every message there is the sender_ID attribute set

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
            - no further attributes*/
