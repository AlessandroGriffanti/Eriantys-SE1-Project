package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import it.polimi.ingsw.model.*;

import static org.junit.jupiter.api.Assertions.*;

class EntranceTest {

    @Test
    void addOneDragonToTheEntrance(){
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.addStudent(Creature.DRAGON);
        assertEquals(Creature.DRAGON, entrance.getStudentsInTheEntrance().get(0));

    }
    @Test
    void AddOneTypeOfEachStudent(){
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        for (Creature c : Creature.values()){
            entrance.addStudent(c);
        }

        assertEquals(Creature.DRAGON, entrance.getStudentsInTheEntrance().get(0));
        assertEquals(Creature.FAIRY, entrance.getStudentsInTheEntrance().get(1));
        assertEquals(Creature.UNICORN, entrance.getStudentsInTheEntrance().get(2));
        assertEquals(Creature.GNOME, entrance.getStudentsInTheEntrance().get(3));
        assertEquals(Creature.FROG, entrance.getStudentsInTheEntrance().get(4));
    }
    @Test
    void removeADRAGONStudent() {
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.getStudentsInTheEntrance().add(Creature.DRAGON);
        entrance.removeStudent(Creature.DRAGON);
        assertTrue(entrance.getStudentsInTheEntrance().size() == 0);
    }

    @Test
    void removeAFewStudent(){
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        for(Creature c : Creature.values()){
            entrance.getStudentsInTheEntrance().add(c);
        }
        entrance.removeStudent(Creature.FAIRY);
        entrance.removeStudent(Creature.DRAGON);
        assertTrue(entrance.getStudentsInTheEntrance().size() == 3);
        assertFalse(entrance.getStudentsInTheEntrance().contains(Creature.DRAGON));
    }

    @Test
    void moveADragonStudent() {
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.getStudentsInTheEntrance().add(Creature.DRAGON);
        entrance.moveStudent(entrance.getStudentsInTheEntrance().indexOf(Creature.DRAGON));
        assertEquals(1, entrance.getDoorTotheDiningRoom().getOccupiedSeats().get(Creature.DRAGON).intValue());
        assertEquals(0, entrance.getStudentsInTheEntrance().size());
    }

    @Test
    void moveStudentsToIsland() {
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));

    }
}