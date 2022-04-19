package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    private String nickName;
    private int playerID;
    private SchoolBoard playerSchoolBoard;
    private int coinsOwned;

    public Player(int id, String nickName){
        this.nickName = nickName;
        this.playerID = id;
    }

    /** adds a coin to the player */
    public void earnCoin(){
        this.coinsOwned ++;
    }

    /** player's controlled professors */
    public ArrayList<Creature> getMyProfessor(){
        return playerSchoolBoard.getControlledProfessors();
    }

    public void setCoinManager(CoinManagerObserver c){
        playerSchoolBoard.setCoinManager(c);
    }

    public void spendCoins(int coinSpent){}


}
