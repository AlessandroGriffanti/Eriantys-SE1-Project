package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

public class AckCharactersMessage extends Message {


    /**
     * This attribute is the ID of the player after whose action this message is sent
     */
    private int recipient;
    /**
     * This attribute is the name of the character card used
     */
    private String card = "";
    /**
     * This attribute is the amount of coins currently kept in the general reserve (at the center of the table)
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


    public AckCharactersMessage(){
        this.object = "ack";
    }

    // SETTER AND GETTER FOR recipient
    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public int getRecipient() {
        return recipient;
    }

    // SETTER AND GETTER FOR card
    public void setCard(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }

    // SETTER AND GETTER FOR student
    public void setStudent(Creature student) {
        this.student = student;
    }

    public Creature getStudent() {
        return student;
    }

    // SETTER AND GETTER FOR students [array]
    public void setStudentsOnCard(ArrayList<Creature> studentsOnCard) {
        this.studentsOnCard = studentsOnCard;
    }

    public ArrayList<Creature> getStudentsOnCard() {
        return studentsOnCard;
    }

    // SETTER AND GETTER FOR coinReserve
    public void setCoinReserve(int coinReserve) {
        this.coinReserve = coinReserve;
    }

    public int getCoinReserve() {
        return coinReserve;
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
}

/*THE CLIENTS NEED TO READ DIFFERENT ATTRIBUTES BASED ON THE VALUE OF 'card':
        N.B. In all messages the current amount of money in the reserve is specified

        1. monk:
           - student: the type of student taken from the card and put on the island

        5. herbalist:
           - island_ID: ID of the island where the no-entry-tile was put
           - getNumberOfElementsOnTheCard: number of no-entry-tiles remained on the character card*/