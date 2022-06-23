package it.polimi.ingsw.model.schoolboard;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

/**
 * This class represents the board used by each player, divided into four
 * sections:
 * - tower area
 * - dining room
 * - entrance
 * - professors' table
 */
public class SchoolBoard {
    /**
     * This attribute is the reference to the tower area of the board
     */
    private TowerArea towerArea;
    /**
     * This attribute is the reference to the diningRoom of the board
     */
    private DiningRoom diningRoom;
    /**
     * This attribute is the reference to the entrance of the board
     */
    private Entrance entrance;
    /**
     * This attribute is the reference to the professors' table of the board
     */
    private ProfessorTable professorTable;

    /**
     * This constructor creates all the four section of the board
     * @param match reference to the Match
     * @param numPlayers number of players in the match
     * @param r reference to the realm
     * @param p reference to the player to whom this board belongs
     */
    public SchoolBoard(Match match, int numPlayers, Realm r, Player p){
        this.towerArea = new TowerArea(numPlayers);
        this.diningRoom = new DiningRoom(match, p);
        this.professorTable = new ProfessorTable();
        this.entrance = new Entrance(diningRoom, r, numPlayers);
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

    /**
     * This method controls if there are enough towers and remove theme from the tower-area;
     * if there are not enough towers then the match must end, so the return value will be false
     * @param numberOfTowers number of towers to take from the tower area
     * @return true if there were enough towers to be taken from the tower-area
     *         false if taking the towers requested the players remains with 0 towers
     */
    public boolean takeTowers(int numberOfTowers){
        boolean endOfMatch = false;

        // if there was not enough towers
        if(numberOfTowers > towerArea.getCurrentNumberOfTowers()){
            endOfMatch = true;
            // remove all the towers left
            towerArea.takeTowers(towerArea.getCurrentNumberOfTowers());
        }
        // if there are enough towers left
        else{
            towerArea.takeTowers(numberOfTowers);
        }

        return endOfMatch;
    }

    /**
     * This method put towers into the tower-area
     * @param numberOfTowers number of towers to add
     */
    public void putTowers(int numberOfTowers){
        towerArea.addTowers(numberOfTowers);

        // there should never be more towers than the area can keep
        assert towerArea.getCurrentNumberOfTowers() <= towerArea.getCapacity();
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
