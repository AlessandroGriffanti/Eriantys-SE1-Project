package it.polimi.ingsw.model;

/** tower area class */
public class TowerArea {
    private int currentnumberoftowers;
    private Realm realmintowerarea;

    public TowerArea(int numplayers, Realm r, Tower t){
        if(numplayers == 2)
            this.currentnumberoftowers = 8;
        if(numplayers == 3)
            this.currentnumberoftowers = 6;
        if(numplayers == 4)
            this.currentnumberoftowers = 8;
        this.realmintowerarea = r;
    }

    /** adds a tower to the area */
    public void addTower(Tower t, int islandID){
        currentnumberoftowers ++;
        realmintowerarea.addTowerToIsland(t, islandID);
    }

    /** removes a tower from the area */
    public void removeTower(){
        currentnumberoftowers --;
    }

}
