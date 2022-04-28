package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class EntranceTest {

    /** check if it correctly added a dragon type student to the entrance */
    @Test
    void addOneDragonToTheEntrance(){
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.addStudent(Creature.DRAGON);
        assertEquals(Creature.DRAGON, entrance.getStudentsInTheEntrance().get(0));

    }

    /** check if each type of student is correctly added to the entrance */
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

    /** check if it is correctly removed a dragon type student */
    @Test
    void removeADRAGONStudent() {
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.getStudentsInTheEntrance().add(Creature.DRAGON);
        entrance.removeStudent(Creature.DRAGON);
        assertTrue(entrance.getStudentsInTheEntrance().size() == 0);
    }

    /** check if a few students are correctly removed */
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

    /** Checks if it correctly moves a Dragon type student to the dining room from the entrance */
    @Test
    void moveADragonStudent() {
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.addStudent(Creature.DRAGON);
        entrance.moveStudent(entrance.getStudentsInTheEntrance().indexOf(Creature.DRAGON));
        assertEquals(1, entrance.getDoorToTheDiningRoom().getOccupiedSeats().get(Creature.DRAGON).intValue());
        assertEquals(0, entrance.getStudentsInTheEntrance().size());
    }

    /** checks if it correctly moves 2 students from the entrance to an island: a dragon type one on the island where mother nature currently stands, a fairy one
     * in the following island so that the total number of students on the island where mother nature currently stands is 1,
     * the number of students on the following island is 2 (one added and one from the set up of the islands), while
     * the total number of students on the opposite island (mother nature position + 6) is 0
     */
    @Test
    void moveStudentsToIsland() {
        Entrance entrance = new Entrance(new DiningRoom(), new Realm(3, new Bag()));
        entrance.addStudent(Creature.DRAGON);
        entrance.addStudent(Creature.FAIRY);

        int motherNaturePosition = entrance.getRealminentrance().getPositionOfMotherNature();
        entrance.moveStudentsToIsland(entrance.getStudentsInTheEntrance().indexOf(Creature.DRAGON), motherNaturePosition);
        assertEquals(1, entrance.getRealminentrance().getArchipelagos().get(motherNaturePosition).getTotalNumberOfStudents());
        assertEquals(0, entrance.getRealminentrance().getArchipelagos().get((motherNaturePosition+6)%12).getTotalNumberOfStudents());
        assertTrue(entrance.getStudentsInTheEntrance().size() == 1);
        if(motherNaturePosition == 11) {
            entrance.moveStudentsToIsland(entrance.getStudentsInTheEntrance().indexOf(Creature.FAIRY), 1);
            assertEquals(2, entrance.getRealminentrance().getArchipelagos().get(1).getTotalNumberOfStudents());
            assertTrue(entrance.getStudentsInTheEntrance().size() ==0);

        }else {
            entrance.moveStudentsToIsland(entrance.getStudentsInTheEntrance().indexOf(Creature.FAIRY), motherNaturePosition + 1);
            assertEquals(2, entrance.getRealminentrance().getArchipelagos().get(motherNaturePosition + 1).getTotalNumberOfStudents());
            assertTrue(entrance.getStudentsInTheEntrance().size() ==0);
        }



    }
}