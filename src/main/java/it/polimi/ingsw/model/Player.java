package it.polimi.ingsw.model;

import it.polimi.ingsw.model.schoolboard.SchoolBoard;

import java.util.ArrayList;

public class Player {
    private String nickName;
    private int ID;
    private SchoolBoard schoolBoard;
    private AssistantsDeck assistantsDeck;
    private int coinsOwned;
    private Tower towerColor;
    private Realm realm;

    public Player(Match match, int id, String nickName, int numPlayers, Realm r){
        this.nickName = nickName;
        this.ID = id;
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


    public void setTowerColor(Tower t){
        this.towerColor = t;
    }

    /**
     * This method decreases the amount of coins owned by the player by the value specified
     * @param price number of coins taken
     */
    public void spendCoins(int price){
        coinsOwned -= price;
    }

    public int getCoinsOwned() {
        return coinsOwned;
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

    // through this method we move the tower from and to island
    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public String getNickName() {
        return nickName;
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

    public boolean equals(Player p){
        return this.nickName.equals(p.nickName);
    }
}
