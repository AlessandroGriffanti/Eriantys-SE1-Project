package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTest {

    /** checking if the add method correctly adds one type of each student */
    @Test
    void addingOneTypeofEachStudent() {
        DiningRoom diningroom = new DiningRoom();
        for (Creature c : Creature.values()){
            diningroom.addStudent(c);
            assertEquals(1, diningroom.getOccupiedSeats().get(c).intValue());
        }
    }

    /** checking if adding a third Dragon student correctly grants a coin the player through the CoinManagerObserver */
    @Test
    void adding3DragonTogetACoin() {
        DiningRoom diningroom = new DiningRoom();
        Player p = new Player(1, "player", 3, new Realm(3, new Bag()));
        CoinManagerObserver c = new CoinManagerObserver(p);
        diningroom.setCoinObserver(c);
        diningroom.getOccupiedSeats().replace(Creature.DRAGON,2);
        diningroom.addStudent(Creature.DRAGON);
        assertEquals(1, p.getCoinsOwned());
    }


    /** checking if the removeStudent method correctly removes the students */
    @Test
    void removeOneTypeOfEachStudent() {
        DiningRoom diningRoom = new DiningRoom();
        for(Creature c : Creature.values()) {
            diningRoom.getOccupiedSeats().replace(c, 1);
            try{
                diningRoom.removeStudent(c);
                assertEquals(0, diningRoom.getOccupiedSeats().get(c).intValue());
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /** checks if it is correctly removed a dragon type student */
    @Test
    void removeOneDragon (){
        DiningRoom diningRoom = new DiningRoom();
        diningRoom.getOccupiedSeats().replace(Creature.DRAGON, 1);
        try {
            diningRoom.removeStudent(Creature.DRAGON);
            assertEquals(0, diningRoom.getOccupiedSeats().get(Creature.DRAGON).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** checking if the removeStudent method correctly throws an exception when the number of the student we'd like to remove is 0 */
    @Test
    void removeANonExistentStudent() {
        DiningRoom diningRoom = new DiningRoom();
        assertThrows(Exception.class,
                ()-> {
                    diningRoom.removeStudent(Creature.DRAGON);
                });
    }

    @Test
    void setEntrance() {
    }
}