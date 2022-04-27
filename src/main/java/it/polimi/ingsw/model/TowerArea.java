package it.polimi.ingsw.model;

/** tower area class */

public class TowerArea {

    private int currentNumberOfTowers;

    public TowerArea(int numPlayers){
        if(numPlayers == 2)
            this.currentNumberOfTowers = 8;
        if(numPlayers == 3)
            this.currentNumberOfTowers = 6;
        if(numPlayers == 4)
            this.currentNumberOfTowers = 8;
        ;
    }

    /** adds a tower to the area */
    public void addTower(int numberOfTowers){
        currentNumberOfTowers += numberOfTowers;

    }

    /** removes a tower from the area */
    public void removeTower(int numberOfTowers){
        currentNumberOfTowers -= numberOfTowers;
    }
    public int getCurrentNumberOfTowers() {
        return currentNumberOfTowers;
    }

}
