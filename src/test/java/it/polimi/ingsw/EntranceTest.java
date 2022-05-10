package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import it.polimi.ingsw.model.schoolboard.Entrance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class EntranceTest {

    /**
     * This method checks the removal of one student is done correctly
     */
    @Test
    void removeOneStudent_positionBecomesNull(){
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        Entrance entrance = new Entrance(new DiningRoom(p), new Realm(3, new Bag()), 2);
        entrance.removeStudent(2);
        assertEquals(null, entrance.getStudentsInTheEntrance().get(2));

    }

    /**
     *
     */
    @Test
    void addStudentAfterRemoval_studentAddedInTheSpotWithNul(){
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        Entrance entrance = new Entrance(new DiningRoom(p), new Realm(3, new Bag()), 2);

        entrance.removeStudent(2);

        entrance.addStudent(Creature.FAIRY);
        assertEquals(Creature.FAIRY, entrance.getStudentsInTheEntrance().get(2));
    }


    /** check if a few students are correctly removed */
    @Test
    void addMultipleStudentsAfterRemoval_studentAddedInTheSpotWithNul(){
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        Entrance entrance = new Entrance(new DiningRoom(p), new Realm(3, new Bag()), 2);

        ArrayList<Creature> studentsToAdd = new ArrayList<Creature>();
        studentsToAdd.add(Creature.FROG);
        studentsToAdd.add(Creature.GNOME);
        studentsToAdd.add(Creature.UNICORN);

        entrance.removeStudent(0);
        entrance.removeStudent(4);
        entrance.removeStudent(5);

        //The size of the array must not change because we set the removed students position to null
        assertTrue(entrance.getStudentsInTheEntrance().size() == 7);

        entrance.addMultipleStudents(studentsToAdd);

        assertEquals(Creature.FROG, entrance.getStudentsInTheEntrance().get(0));
        assertEquals(Creature.GNOME, entrance.getStudentsInTheEntrance().get(4));
        assertEquals(Creature.UNICORN, entrance.getStudentsInTheEntrance().get(5));

    }

    /** Checks if it correctly moves a Dragon type student to the dining room from the entrance */
    @Test
    void moveStudent_onlyOneStudentInTheDiningRoomTable() {
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        Entrance entrance = new Entrance(new DiningRoom(p), new Realm(3, new Bag()), 2);

        Creature creatureMoved = entrance.getStudentsInTheEntrance().get(2);
        entrance.moveStudent(2);
        assertEquals(1, entrance.getDoorToTheDiningRoom().getOccupiedSeats().get(creatureMoved).intValue());
    }

    /** checks if it correctly moves 2 students from the entrance to an island: a dragon type one on the island where mother nature currently stands, a fairy one
     * in the following island so that the total number of students on the island where mother nature currently stands is 1,
     * the number of students on the following island is 2 (one added and one from the set up of the islands), while
     * the total number of students on the opposite island (mother nature position + 6) is 0
     */
    @Test
    void moveStudentsToIsland() {
        Player p = new Player(0, "james",2, new Realm(2, new Bag()));
        Entrance entrance = new Entrance(new DiningRoom(p), new Realm(3, new Bag()), 2);
        entrance.addStudent(Creature.DRAGON);
        entrance.addStudent(Creature.FAIRY);

        int motherNaturePosition = entrance.getRealmInEntrance().getPositionOfMotherNature();
        entrance.moveStudentsToIsland(entrance.getStudentsInTheEntrance().indexOf(Creature.DRAGON), motherNaturePosition);
        assertEquals(1, entrance.getRealmInEntrance().getArchipelagos().get(motherNaturePosition).getTotalNumberOfStudents());
        assertEquals(0, entrance.getRealmInEntrance().getArchipelagos().get((motherNaturePosition+6)%12).getTotalNumberOfStudents());

        if(motherNaturePosition == 11) {
            entrance.moveStudentsToIsland(entrance.getStudentsInTheEntrance().indexOf(Creature.FAIRY), 1);
            assertEquals(2, entrance.getRealmInEntrance().getArchipelagos().get(1).getTotalNumberOfStudents());

        }else {
            entrance.moveStudentsToIsland(entrance.getStudentsInTheEntrance().indexOf(Creature.FAIRY), motherNaturePosition + 1);
            assertEquals(2, entrance.getRealmInEntrance().getArchipelagos().get(motherNaturePosition + 1).getTotalNumberOfStudents());
        }



    }
}