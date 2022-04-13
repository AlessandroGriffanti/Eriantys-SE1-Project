package it.polimi.ingsw.model;

import java.util.ArrayList;

public class SchoolBoard {
    private TowerArea towerArea;
    private DiningRoom diningRoom;
    private Entrance entrance;
    private ProfessorTable professorTable;

    public SchoolBoard(){  //qui devo solo istanziare le cose?
        towerArea = new TowerArea();
        diningRoom = new DiningRoom();
        entrance = new Entrance();
        professorTable = new ProfessorTable();
    }

    /** returns an arraylist of the controlled professors */
    public ArrayList<Creature> getControlledProfessors() {
        ArrayList<Creature> a = new ArrayList<>();          //devo ritornare così l'arraylist o come?
        for (Creature c : Creature.values()) {
            if(professorTable.isOccupied(c)) {
                a.add(c);
            }
        }
        return a;
    }

}
