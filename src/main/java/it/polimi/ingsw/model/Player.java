package it.polimi.ingsw.model;

import java.lang.reflect.Array;
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
        realm.addTowerToIsland(playerTowerColor);
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
    public AssistantsDeck choosedeck (Wizard wizard){
        this.playerAssistantsDeck = new AssistantsDeck(wizard);
        return this.playerAssistantsDeck;
    }


}
