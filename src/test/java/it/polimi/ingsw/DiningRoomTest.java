package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTest {

    /** checking if the add method correctly adds one type of each student */
    @Test
    void addingOneTypeofEachStudent() {
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        DiningRoom diningroom = new DiningRoom(p);
        for (Creature c : Creature.values()){
            diningroom.addStudent(c);
            assertEquals(1, diningroom.getAllOccupiedSeats().get(c).intValue());
        }
    }

    /** checking if adding a third Dragon student correctly grants a coin the player through the CoinManagerObserver
     * The AssertEquals checks if Player's coins are equal to 2 because the number of coin, in player's constructor, is initialized
     * to 1
     */
    @Test
    void adding3DragonTogetACoin() {
        Player p = new Player(1, "player", 3, new Realm(3, new Bag()));
        DiningRoom diningroom = new DiningRoom(p);
        //CoinManagerObserver c = new CoinManagerObserver(p);
        //diningroom.setCoinObserver(c);
        diningroom.getAllOccupiedSeats().replace(Creature.DRAGON,2);
        diningroom.addStudent(Creature.DRAGON);
        assertEquals(2, p.getCoinsOwned());
    }


    /** checking if the removeStudent method correctly removes the students */
    @Test
    void removeOneTypeOfEachStudent() {
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        DiningRoom diningRoom = new DiningRoom(p);
        for(Creature c : Creature.values()) {
            diningRoom.getAllOccupiedSeats().replace(c, 1);
            try{
                diningRoom.removeStudent(c);
                assertEquals(0, diningRoom.getAllOccupiedSeats().get(c).intValue());
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /** checks if it is correctly removed a dragon type student */
    @Test
    void removeOneDragon (){
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        DiningRoom diningRoom = new DiningRoom(p);
        diningRoom.getAllOccupiedSeats().replace(Creature.DRAGON, 1);
        try {
            diningRoom.removeStudent(Creature.DRAGON);
            assertEquals(0, diningRoom.getAllOccupiedSeats().get(Creature.DRAGON).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** checking if the removeStudent method correctly throws an exception when the number of the student we'd like to remove is 0 */
    @Test
    void removeANonExistentStudent() {
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        DiningRoom diningRoom = new DiningRoom(p);
        assertThrows(Exception.class,
                ()-> {
                    diningRoom.removeStudent(Creature.DRAGON);
                });
    }

    @Test
    void setEntrance() {
    }
}