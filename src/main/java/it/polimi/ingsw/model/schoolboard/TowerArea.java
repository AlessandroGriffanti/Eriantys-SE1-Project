package it.polimi.ingsw.model.schoolboard;

/** tower area class */

public class TowerArea {

    /**
     * This attribute is the number of towers currently inside
     * the tower area
     */
    private int currentNumberOfTowers;
    /**
     * This attribute is the maximum number of towers that the tower area
     * can contain
     */
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
     * This method adds towers to the area
     * @param numberOfTowers number of towers to add to the area
     */
    public void addTowers(int numberOfTowers){
        currentNumberOfTowers += numberOfTowers;

    }

    /**
     * This method removes towers from the area
     * @param numberOfTowers number of towers to take from the area
     */
    public void takeTowers(int numberOfTowers){
        currentNumberOfTowers -= numberOfTowers;
    }

    public int getCurrentNumberOfTowers() {
        return currentNumberOfTowers;
    }

    public int getCapacity() {
        return capacity;
    }
}
