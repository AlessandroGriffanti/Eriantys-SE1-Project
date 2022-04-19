package it.polimi.ingsw.model;

import java.util.ArrayList;

public class SchoolBoard {
    private TowerArea towerArea;
    private DiningRoom diningRoom;
    private Entrance entrance;
    private ProfessorTable professorTable;

    public SchoolBoard(int numplayers, Realm r, Tower t){
        towerArea = new TowerArea(numplayers, r, t);
        diningRoom = new DiningRoom();
        entrance = new Entrance(r);
        professorTable = new ProfessorTable();
    }

    /** returns an arraylist of the controlled professors */
    public ArrayList<Creature> getControlledProfessors() {
        ArrayList<Creature> controlledprofessorslist = new ArrayList<>();
        for (Creature c : Creature.values()) {
            if(professorTable.isOccupied(c)) {
                controlledprofessorslist.add(c);
            }
        }
        return controlledprofessorslist;
    }

    public void setCoinManager(CoinManagerObserver c){
        this.diningRoom.setCoinObserver(c);
    }

    public void removetower(){
        towerArea.removeTower();
    }

}
