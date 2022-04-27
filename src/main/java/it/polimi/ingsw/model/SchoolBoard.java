package it.polimi.ingsw.model;

import java.util.ArrayList;

public class SchoolBoard {
    private TowerArea towerArea;
    private DiningRoom diningRoom;
    private Entrance entrance;
    private ProfessorTable professorTable;

    public SchoolBoard(int numPlayers, Realm r){
        towerArea = new TowerArea(numPlayers);
        diningRoom = new DiningRoom();
        entrance = new Entrance(diningRoom, r);
        professorTable = new ProfessorTable();
    }

    /** returns an arraylist of the controlled professors */
    public ArrayList<Creature> getControlledProfessors() {
        ArrayList<Creature> controlledProfessorsList = new ArrayList<>();
        for (Creature c : Creature.values()) {
            if(professorTable.isOccupied(c)) {
                controlledProfessorsList.add(c);
            }
        }
        return controlledProfessorsList;
    }

    public void setCoinManager(CoinManagerObserver c){
        this.diningRoom.setCoinObserver(c);
    }


    public TowerArea getTowerArea(){return towerArea;}

    public ProfessorTable getProfessorTable() {
        return professorTable;
    }

    public void takeTowers(int numberOfTowers){
        towerArea.removeTower(numberOfTowers);
    }

    public void putTowers(int numberOfTowers){
        towerArea.addTower(numberOfTowers);
        }

    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    public Entrance getEntrance() {
        return entrance;
    }
}
