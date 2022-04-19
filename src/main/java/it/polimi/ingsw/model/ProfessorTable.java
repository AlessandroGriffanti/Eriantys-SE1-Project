package it.polimi.ingsw.model;

import java.util.HashMap;

public class ProfessorTable {
    private HashMap<Creature, Boolean> occupiedSeats;


    public ProfessorTable(){
        occupiedSeats = new HashMap<>();
        occupiedSeats.put(Creature.DRAGON, false);
        occupiedSeats.put(Creature.UNICORN, false);
        occupiedSeats.put(Creature.FROG, false);
        occupiedSeats.put(Creature.FAIRY, false);
        occupiedSeats.put(Creature.GNOME, false);
    }

    /** adds professor of p type */
    public void addProfessor(Creature p) {
        for (Creature c : Creature.values())
            if (c.equals(p)) {
                occupiedSeats.replace(p, true);
            }
    }
    /** removes professor of p type */
    public void removeProfessor(Creature p){
        for(Creature c : Creature.values())
            if(c.equals(p)){
                occupiedSeats.replace(p, false);
            }
    }

    /** checks if professor of p type has been taken */
    public boolean isOccupied(Creature p){
        return occupiedSeats.get(p);
    }

}
