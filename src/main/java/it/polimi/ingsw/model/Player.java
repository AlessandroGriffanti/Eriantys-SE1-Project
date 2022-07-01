package it.polimi.ingsw.model;

import it.polimi.ingsw.model.schoolboard.SchoolBoard;

import java.util.ArrayList;

public class Player {
    /**
     * This attribute is the name chosen by the player, unique inside the server
     */
    private String nickname;
    /**
     * This attribute is the ID that identifies the player inside the match
     */
    private int ID;
    /**
     * This attribute is the reference to the schoolBoard of the player
     */
    private SchoolBoard schoolBoard;
    /**
     * This attribute is the reference to the deck ok assistant of the player
     */
    private AssistantsDeck assistantsDeck;
    /**
     * This attribute is the amount of coins owned by the player
     */
    private int coinsOwned;
    /**
     * This attribute is the color of the towers of the player, chosen by the player
     * at the beginning of the match
     */
    private Tower towerColor;
    /**
     * This attribute is the reference to the Realm of the match
     */
    private Realm realm;


    public Player(Match match, int ID, String nickname, int numPlayers, Realm r){
        this.nickname = nickname;
        this.ID = ID;
        this.realm = r;

        this.coinsOwned = 1;
        this.schoolBoard = new SchoolBoard(match, numPlayers, r, this);
    }


    /**
     * This method adds a coin to the player's treasure
     */
    public void earnCoin(){
        this.coinsOwned ++;
    }

    /**
     * This method decreases the amount of coins owned by the player by the value specified
     * @param price number of coins taken
     */
    public void spendCoins(int price){
        coinsOwned -= price;
    }

    /**
     * This method finds the professors controlled by the player
     * @return list of professors controlled
     */
    public ArrayList<Creature> getMyProfessors(){
        return schoolBoard.getControlledProfessors();
    }

    /**
     * This method sets the assistants' deck of the player
     * @param wizard deck chosen
     */
    public void chooseDeck (Wizard wizard) {
        this.assistantsDeck = new AssistantsDeck(wizard);
    }

    /**
     * This method calls the deck in order to use the chosen assistant card
     * @param orderValue value that identifies the card chosen
     */
    public void useAssistantCard(int orderValue){
        this.assistantsDeck.useCard(orderValue);
    }

    /**
     * This method asks the assistants deck for n=the number of remaining cards
     * @return number of remaining card (cards that has not been used yet)
     */
    public int numberOfRemainingAssistantCards(){
        return this.assistantsDeck.getNumberOfRemainingCards();
    }

    /**
     * This method checks if two players are the same one, it can be so only if they
     * have the same nickname
     * @param p reference to the player to compare
     * @return true if the two player are the same, false otherwise
     */
    public boolean equals(Player p){
        return this.nickname.equals(p.nickname);
    }

    public int getCoinsOwned() {
        return coinsOwned;
    }

    public void setTowerColor(Tower t){
        this.towerColor = t;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public String getNickname() {
        return nickname;
    }

    public int getID() {
        return ID;
    }

    public AssistantsDeck getAssistantsDeck() {
        return assistantsDeck;
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public Realm getRealm() {
        return realm;
    }
}
