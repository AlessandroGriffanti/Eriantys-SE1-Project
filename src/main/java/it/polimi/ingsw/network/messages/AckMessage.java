package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Creature;
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
     * This attribute is the list of already chosen decks
     */
    private ArrayList<Wizard> notAvailableDecks;
    /**
     * This attribute is the list already chosen assistant cards in this round
     */
    private ArrayList<Integer> assistantAlreadyUsedInThisRound;


    public AckMessage(){
        this.object = "ack";
        this.recipient = -1;
        this.nextPlayer = -1;
        studentsAddedToTheClouds = new ArrayList<Creature>();
        notAvailableDecks = new ArrayList<Wizard>();
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
}

/*POSSIBLE VALUES OF "subObject":
   1. fillClouds:
      it means that all the clouds have been refilled and 'studentsAddedToTheClouds' contains all te students
      added on each cloud
      [ex. 2 players => 2 clouds with 3 students each => in the attribute there will be 6 objects Creature, the first 3 belong to the cloud0 the last 3 to the cloud1]

   2. deck:
      it means that the deck chosen by the player is legit and 'notAvailableDecks' contains the decks that are no more available (useful for the next players)

   3. assistant:
      it means that the assistant chosen by the player is legit and 'assistantAlreadyUsedInThisRound' contains the assistants already chosen in this round's planning phase

   4. */
