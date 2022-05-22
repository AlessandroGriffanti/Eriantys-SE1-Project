package it.polimi.ingsw.model.schoolboard;

/** tower area class */

public class TowerArea {

    private int currentNumberOfTowers;
    private int capacity;

    public TowerArea(int numPlayers){
        if(numPlayers == 2 || numPlayers == 4){
            this.currentNumberOfTowers = 8;
            this.capacity = 8;
        }else if(numPlayers == 3){
            this.currentNumberOfTowers = 6;
            this.capacity = 6;
        }
    }

    /**
     * This method adds a tower to the area
     * @param numberOfTowers
     */
    public void addTower(int numberOfTowers){
        currentNumberOfTowers += numberOfTowers;

    }

    /**
     * This method removes a tower from the area
     * @param numberOfTowers
     */
    public void removeTower(int numberOfTowers){
        currentNumberOfTowers -= numberOfTowers;
    }

    public int getCurrentNumberOfTowers() {
        return currentNumberOfTowers;
    }

    public int getCapacity() {
        return capacity;
    }
}
