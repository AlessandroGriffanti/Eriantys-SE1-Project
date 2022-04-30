package it.polimi.ingsw.model;

import it.polimi.ingsw.model.schoolboard.SchoolBoard;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Player {
    private String nickName;
    private int playerID;
    private SchoolBoard playerSchoolBoard;
    private AssistantsDeck playerAssistantsDeck;
    private int coinsOwned;
    private Tower playerTowerColor;
    private Realm realm;

    public Player(int id, String nickName, int numPlayers, Realm r){
        this.nickName = nickName;
        this.playerID = id;
        this.realm = r;

        this.coinsOwned = 1;
        this.playerSchoolBoard = new SchoolBoard(numPlayers, r, this);
    }


    /** adds a coin to the player */
    public void earnCoin(){
        this.coinsOwned ++;
    }


    public void setTowerColor(Tower t){
        this.playerTowerColor = t;
    }

    /** player's controlled professors */
    public ArrayList<Creature> getMyProfessors(){
        return playerSchoolBoard.getControlledProfessors();
    }

    /*public void setCoinManager(CoinManagerObserver c){
        playerSchoolBoard.setCoinManager(c);
    }*/

    public int getCoinsOwned() {
        return coinsOwned;
    }

    /** used in CharactersManager in the useCharacter method */
    public void setCoinsOwned(int coinsOwned) {
        this.coinsOwned = coinsOwned;
    }

    /** creates the deck */
    public AssistantsDeck chooseDeck (Wizard wizard) throws FileNotFoundException {
        this.playerAssistantsDeck = new AssistantsDeck(wizard);
        return this.playerAssistantsDeck;
    }

    /** through this method we move the tower from and to island */
    public SchoolBoard getPlayerSchoolBoard() {
        return playerSchoolBoard;
    }

    public String getNickName() {
        return nickName;
    }

    public int getPlayerID() {
        return playerID;
    }

    public AssistantsDeck getPlayerAssistantsDeck() {
        return playerAssistantsDeck;
    }

    public Tower getPlayerTowerColor() {
        return playerTowerColor;
    }

    public Realm getRealm() {
        return realm;
    }

    public boolean equals(Player p){
        return this.nickName.equals(p.nickName);
    }
}
