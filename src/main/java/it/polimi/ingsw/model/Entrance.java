package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Entrance {
    private ArrayList<Creature> studentsInTheEntrance;
    private DiningRoom doorToTheDiningRoom;
    private Realm realminentrance;

    public Entrance(DiningRoom d, Realm r){
        studentsInTheEntrance = new ArrayList<>();
        this.doorToTheDiningRoom = d;
        this.realminentrance = r;
    }


    /** adds a creature to the entrance */
    public void addStudent(Creature s){
        studentsInTheEntrance.add(s);
    }

    /** removes a student from the entrance */
    public void removeStudent(Creature s){
        studentsInTheEntrance.remove(s);
    }

    /** moves a student to the diningroom and removes it from the entrance  */
    public void moveStudent(int index){
        doorToTheDiningRoom.addStudent(studentsInTheEntrance.get(index));
        Creature creatureRemoved = studentsInTheEntrance.get(index);
        studentsInTheEntrance.remove(creatureRemoved);

    }

    /** moves a student to an island and removes it from the entrance */
    public void moveStudentsToIsland(int index, int islandID){
        realminentrance.addStudentToIsland(studentsInTheEntrance.get(index), islandID);
        Creature creatureRemoved = studentsInTheEntrance.get(index);
        studentsInTheEntrance.remove(creatureRemoved);
    }

    /** useful for tests */
    public ArrayList<Creature> getStudentsInTheEntrance() {
        return studentsInTheEntrance;
    }

    /** useful for tests */
    public DiningRoom getDoorToTheDiningRoom() {
        return doorToTheDiningRoom;
    }

    public Realm getRealminentrance() {
        return realminentrance;
    }
}
