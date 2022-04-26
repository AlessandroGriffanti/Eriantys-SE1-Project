package it.polimi.ingsw.model;

/** tower area class */

public class TowerArea {

    private int currentnumberoftowers;

    public TowerArea(int numplayers){
        if(numplayers == 2)
            this.currentnumberoftowers = 8;
        if(numplayers == 3)
            this.currentnumberoftowers = 6;
        if(numplayers == 4)
            this.currentnumberoftowers = 8;
        ;
    }

    /** adds a tower to the area */
    public void addTower(int numberoftowers){
        currentnumberoftowers += numberoftowers;

    }

    /** removes a tower from the area */
    public void removeTower(int numberoftowers){
        currentnumberoftowers -= numberoftowers;
    }
    public int getCurrentnumberoftowers() {
        return currentnumberoftowers;
    }

}
