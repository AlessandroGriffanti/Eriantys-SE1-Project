package it.polimi.ingsw.model.schoolboard;

import it.polimi.ingsw.model.CoinManagerObserver;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;

public class DiningRoom {
    /**
     * This attribute contains information about the seats in the dining room, for each type of
     * student the number of student on that particular table is associated
     */
    private HashMap<Creature, Integer> occupiedSeats;
    /**
     * This attribute is the reference to the entrance of the school board
     */
    private Entrance entrance;
    /**
     * This attribute is the reference to the coin observer whose task is to
     * give a coin to the player every time a student is positioned on a
     * seat containing a coin picture
     */
    private CoinManagerObserver coinObserver;

    public DiningRoom(Match match, Player p){
        occupiedSeats = new HashMap<>();
        occupiedSeats.put(Creature.DRAGON,0);
        occupiedSeats.put(Creature.UNICORN,0);
        occupiedSeats.put(Creature.FROG,0);
        occupiedSeats.put(Creature.FAIRY,0);
        occupiedSeats.put(Creature.GNOME,0);

        coinObserver = new CoinManagerObserver(match, p);
    }

    /**
     * This method adds one student to the dining room putting it at the table of its type
     * @param s the type of student to add
     */
    public void addStudent(Creature s){
        occupiedSeats.replace(s, occupiedSeats.get(s) + 1);
        if ((occupiedSeats.get(s)) % 3 == 0) {
            coinObserver.depositCoin();
        }
    }

    /**
     * This method removes a certain quantity of students from one table of the
     * dining room
     * @param qt number of students to remove
     * @param s type of students to remove
     * @return number of students removed
     */
    public int removeStudents(int qt, Creature s){
        int numberStudentsRemoved;

        if(occupiedSeats.get(s) >= 3){
            numberStudentsRemoved = 3;
        }else {
            numberStudentsRemoved = occupiedSeats.get(s);
        }

        occupiedSeats.replace(s, occupiedSeats.get(s) - qt);
        /* if there weren't enough students we set the number of students on
        that table to 0 */
        if(occupiedSeats.get(s) < 0){
            occupiedSeats.replace(s, 0);
        }

        return numberStudentsRemoved;
    }

    /**
     * This method computes the number of students seated at a specified table
     * @param table the table we are interested int
     * @return number of students seating at the table
     */
    public int getOccupiedSeatsAtTable(Creature table){
        return occupiedSeats.get(table);
    }

    /**
     * This method computes the total number of students inside the dining room
     * @return total number of students
     */
    public int getTotalNumberOfStudents(){
        int tot = 0;

        for(Creature c: Creature.values()){
            tot += occupiedSeats.get(c);
        }
        return  tot;
    }

    public void setEntrance(Entrance d){
        this.entrance = d;
    }

    public HashMap<Creature, Integer> getOccupiedSeats() {
        return occupiedSeats;
    }

    public Entrance getEntrance() {
        return entrance;
    }
}
