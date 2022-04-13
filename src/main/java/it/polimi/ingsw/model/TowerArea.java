package it.polimi.ingsw.model;

/** tower area class */
public class TowerArea {
    private int currentnumberoftowers;

    /** adds a tower to the area */
    public void addTower(){
        currentnumberoftowers ++; //non mi ricordo se faccia questo o cosa
    }
    /** removes a tower from the area */
    public void removeTower(){
        currentnumberoftowers --;
    }

}
