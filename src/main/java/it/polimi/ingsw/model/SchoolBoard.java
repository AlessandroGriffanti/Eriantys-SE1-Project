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
        this.towerArea = new TowerArea(numPlayers);
        this.diningRoom = new DiningRoom();
        this.entrance = new Entrance(diningRoom, r);
        this.professorTable = new ProfessorTable();
        this.bagForInitialSetUp = r.getBag();
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

    /** We use the 2 following methods to add and remove towers from and to the islands, in particular they are used
        in the Archipelago class in the setMasterOfArchipelago method, where the player with the highest influence
        over the archipelago (or island) changes and we need to substitute multiple towers */

    public void takeTowers(int numberOfTowers){
        towerArea.removeTower(numberOfTowers);
    }

    public void putTowers(int numberOfTowers){
        towerArea.addTower(numberOfTowers);
    }


    public void setCoinManager(CoinManagerObserver c){
        this.diningRoom.setCoinObserver(c);
    }

    public TowerArea getTowerArea(){
        return towerArea;
    }

    public ProfessorTable getProfessorTable() {
        return professorTable;
    }


    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    public Entrance getEntrance() {
        return entrance;
    }

}
