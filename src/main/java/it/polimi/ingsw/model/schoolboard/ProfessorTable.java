package it.polimi.ingsw.model.schoolboard;

import it.polimi.ingsw.model.Creature;

import java.util.HashMap;

public class ProfessorTable {
    /**
     * This attribute contains information about the table of the professors, if a seat is
     * occupied or not
     */
    private HashMap<Creature, Boolean> occupiedSeats;


    public ProfessorTable(){
        occupiedSeats = new HashMap<>();
        occupiedSeats.put(Creature.DRAGON, false);
        occupiedSeats.put(Creature.UNICORN, false);
        occupiedSeats.put(Creature.FROG, false);
        occupiedSeats.put(Creature.FAIRY, false);
        occupiedSeats.put(Creature.GNOME, false);
    }

    /**
     * This method adds one professor at the table
     * @param p the kind of professor added
     */
    public void addProfessor(Creature p) {
        occupiedSeats.put(p, true);
    }

    /**
     * This method removes a professor from the table
     * @param p the kind of professor removed
     */
    public void removeProfessor(Creature p){
        occupiedSeats.put(p, false);
    }

    /**
     * This method checks if professor of p type has been taken
     * @param p the seats we are checking
     * @return true if the seat is occupied
     *         false if the seat is not occupied
     */
    public boolean isOccupied(Creature p){
        return occupiedSeats.get(p);
    }


    /**
     * This method allows us to get the entire hashmap, and it is useful for Tests
     * @return the reference to the hashmap with the number of occupied seats for each table
     */
    public HashMap<Creature, Boolean> getOccupiedSeats() {
        return occupiedSeats;
    }
}
