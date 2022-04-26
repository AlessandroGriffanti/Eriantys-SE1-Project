package it.polimi.ingsw.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Player {
    private String nickName;
    private int playerID;
    private SchoolBoard playerSchoolBoard;
    private AssistantsDeck playerAssistantsDeck;
    private int coinsOwned;
    private Tower playerTowerColor;
    private Realm playerRealm;

    public Player(int id, String nickName, int numplayers, Realm r){
        this.nickName = nickName;
        this.playerID = id;
        this.playerRealm = r;

    }
    /** removes the tower from the tower area */
    public void movetower(){
        playerSchoolBoard.removetower();
    }

    /** moves a tower to an island */
    public void moveToIsland() {
        playerRealm.addTowerToIsland(playerTowerColor);
    }

    /** adds a coin to the player */
    public void earnCoin(){
        this.coinsOwned ++;
    }


    public void setTowerColor(Tower t){
        this.playerTowerColor = t;
    }

    /** player's controlled professors */
    public ArrayList<Creature> getMyProfessor(){
        return playerSchoolBoard.getControlledProfessors();
    }

    /*public void setCoinManager(CoinManagerObserver c){
        playerSchoolBoard.setCoinManager(c);
    }*/

    public int getCoinsOwned() {
        return coinsOwned;
    }

    public void setCoinsOwned(int coinsOwned) {
        this.coinsOwned = coinsOwned;
    }

    /** creates the deck */
    public AssistantsDeck choosedeck (Wizard wizard) throws FileNotFoundException {
        this.playerAssistantsDeck = new AssistantsDeck(wizard);
        return this.playerAssistantsDeck;
    }

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

    public Realm getPlayerRealm() {
        return playerRealm;
    }

    //ridefinire equals per controllare se due player hanno stesso nickname, gli passo come parametro un giocatore, mi torna
    //un booleano


}
