package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTest {

    /**
     * This method checks if the add method correctly adds one type of each student
     */
    @Test
    void addingOneTypeofEachStudent() {
        DiningRoom diningroom = createDiningRoom();

        for (Creature c : Creature.values()){
            diningroom.addStudent(c);
            Assertions.assertEquals(1, diningroom.getOccupiedSeats().get(c).intValue());
        }
    }

    /** checking if adding a third Dragon student correctly grants a coin the player through the CoinManagerObserver
     * The AssertEquals checks if Player's coins are equal to 2 because the number of coin, in player's constructor, is initialized
     * to 1
     */
    @Test
    void adding3DragonToGetACoin() {
        Player p = new Player(1, "player", 3, new Realm(3, new Bag()));
        DiningRoom diningroom = new DiningRoom(p);

        diningroom.getOccupiedSeats().replace(Creature.DRAGON,2);
        diningroom.addStudent(Creature.DRAGON);
        Assertions.assertEquals(2, p.getCoinsOwned());
    }


    /**
     * This method checks if the removeStudents method correctly removes the students
     */
    @Test
    void removeStudentsTest() {
        DiningRoom diningRoom = createDiningRoom();

        for(Creature c : Creature.values()) {
            diningRoom.getOccupiedSeats().replace(c, 1);

            diningRoom.removeStudents(1, c);
            Assertions.assertEquals(0, diningRoom.getOccupiedSeats().get(c).intValue());
        }

        // try to remove more student than there are at the table
        diningRoom.addStudent(Creature.FROG);
        Assertions.assertEquals(1, diningRoom.removeStudents(2, Creature.FROG));
        Assertions.assertEquals(0, diningRoom.getOccupiedSeatsAtTable(Creature.FROG));

    }

    /**
     * This method controls if the method to get the total number of students in the
     * dining room works well.
     */
    @Test
    public void getTotalNumberOfStudentsTest(){
        DiningRoom diningRoom = createDiningRoom();

        diningRoom.addStudent(Creature.UNICORN);
        diningRoom.addStudent(Creature.FROG);
        diningRoom.addStudent(Creature.FAIRY);
        diningRoom.addStudent(Creature.DRAGON);
        diningRoom.addStudent(Creature.GNOME);
        diningRoom.addStudent(Creature.GNOME);

        Assertions.assertEquals(6, diningRoom.getTotalNumberOfStudents());
    }

    /**
     * This method creates a dining room object to use in the tests methods
     * @return reference to the dining room created
     */
    private DiningRoom createDiningRoom(){
        Bag bag = new Bag();
        Realm realm = new Realm(2, bag);
        Player player = new Player(0, "mario", 2, realm);

        return new DiningRoom(player);
    }
}