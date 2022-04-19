package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Entrance {
    private ArrayList<Creature> studentsInTheEntrance;
    private DiningRoom doorTotheDiningRoom;
    //private Realm realm;
    public Entrance(){
        studentsInTheEntrance = new ArrayList<>();
        doorTotheDiningRoom = new DiningRoom();
    }

    /** adds a creature to the entrance */
    public void addStudent(Creature s){
        studentsInTheEntrance.add(s);
    }

    /** removes a student from the entrance */
    public void removeStudent(Creature s){
        studentsInTheEntrance.remove(s);
    }

    /** moves the student */
    public void moveStudent(int index){
        doorTotheDiningRoom.addStudent(studentsInTheEntrance.get(index)); //questo?
        // realm.
    }

    public void setDiningRoom(DiningRoom d){
        this.doorTotheDiningRoom = d;
    }

}
