package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTest {

    @Test
    void addingOneTypeofEachStudent() {
        DiningRoom diningroom = new DiningRoom();
        for (Creature c : Creature.values()){
            diningroom.addStudent(c);
            assertEquals(1, diningroom.getOccupiedSeats().get(c).intValue());
        }
    }
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
    @Test
    void removeANonExistentStudent() {
        DiningRoom diningRoom = new DiningRoom();
        assertThrows(Exception.class,
                ()-> {
                    diningRoom.removeStudent(Creature.DRAGON);
                });
    }



    @Test
    void setCoinObserver() {
    }

    @Test
    void setEntrance() {
    }
}