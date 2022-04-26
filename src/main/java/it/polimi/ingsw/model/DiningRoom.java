package it.polimi.ingsw.model;

import java.util.HashMap;

public class DiningRoom {
    private HashMap<Creature, Integer> occupiedSeats;
    private Entrance entrance;
    private CoinManagerObserver coinObserver;

    public DiningRoom(){
        occupiedSeats = new HashMap<>();
        occupiedSeats.put(Creature.DRAGON,0);
        occupiedSeats.put(Creature.UNICORN,0);
        occupiedSeats.put(Creature.FROG,0);
        occupiedSeats.put(Creature.FAIRY,0);
        occupiedSeats.put(Creature.GNOME,0);
    }

    /** adds a student to the diningroom */
    public void addStudent(Creature s){
        occupiedSeats.replace(s, occupiedSeats.get(s).intValue() + 1);
        if ((occupiedSeats.get(s)) % 3 == 0) {
            coinObserver.depositCoin();
        }
    }

    /** removes a student from the diningRoom */
    /* public void removeStudent(Creature s){
        try {
            if ((occupiedSeats.get(s) >= 1)) {
                occupiedSeats.replace(s, occupiedSeats.get(s).intValue() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /** removes a student from the diningRoom */
    public void removeStudent(Creature s) throws Exception {
        if ((occupiedSeats.get(s) >= 1)) {
            occupiedSeats.replace(s, occupiedSeats.get(s).intValue() - 1);
        } else {
            throw new Exception();
        }
    }

    /** set the reference to the coinmanagerobserver */
    public void setCoinObserver(CoinManagerObserver c){
        this.coinObserver = c;
    }

    public void setEntrance(Entrance d){
        this.entrance = d;
    }

    /** returns the hashmap in the diningroom attribute: useful for tests */
    public HashMap<Creature, Integer> getOccupiedSeats() {
        return occupiedSeats;
    }
}
