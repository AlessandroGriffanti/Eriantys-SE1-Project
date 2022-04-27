package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SchoolBoard {
    private TowerArea towerArea;
    private DiningRoom diningRoom;
    private Entrance entrance;
    private ProfessorTable professorTable;
    private Bag bagForInitialSetUp;

    public SchoolBoard(int numPlayers, Realm r){
//TODO: we need to initialize also the bagForInitialSetUp, SchoolBoard must receive the reference to the bag
        towerArea = new TowerArea(numPlayers);
        diningRoom = new DiningRoom();
        entrance = new Entrance(diningRoom, r);
        professorTable = new ProfessorTable();
    }

    /**
     * Sets up the 7 initial students in the entrance
     */
    public void initialSetUpStudentsInTheEntrance(){
        ArrayList<Creature> studentsSetUpEntrance = new ArrayList<Creature>();
        studentsSetUpEntrance = bagForInitialSetUp.drawStudents(7);

        ArrayList<Creature> s = entrance.getStudentsInTheEntrance();
        for(Creature c: studentsSetUpEntrance){
            s.add(c);
        }
    }

    /**
     * Finds which are the professors controlled by the Player
     * @return the professors under the control of the player to whom the board(SchoolBoard) belongs
     */
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
