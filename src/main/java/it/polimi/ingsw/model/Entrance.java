package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Entrance {
    private ArrayList<Creature> studentsInTheEntrance;
    private DiningRoom doorTotheDiningRoom;
    private Realm realminentrance;
    public Entrance(DiningRoom d, Realm r){
        studentsInTheEntrance = new ArrayList<>();
        this.doorTotheDiningRoom = d;
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

    /** moves a student to the diningroom  */
    public void moveStudent(int index){
        doorTotheDiningRoom.addStudent(studentsInTheEntrance.get(index));

    }

    /** moves a student to an island */
    public void moveStudentsToIsland(int index, int islandID){
        realminentrance.addStudentsToIsland(studentsInTheEntrance.get(index));
    }


}