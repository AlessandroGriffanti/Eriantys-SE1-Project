package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Entrance {
    private ArrayList<Creature> studentsInTheEntrance;
    private DiningRoom doorTotheDiningRoom;

    public Entrance(){                  //nel costruttore devo istanziare l'arraylist e crare doortothediningroom... o?
        studentsInTheEntrance = new ArrayList<>();
        doorTotheDiningRoom = new DiningRoom();
    }

    /** adds a creature to the entrance */
    public void addStudent(Creature s){
        studentsInTheEntrance.add(s);
    }

    /** moves the student */
    public void moveStudent(int index){ //esattamente che fa?
        //studentsInTheEntrance.get(index);
        doorTotheDiningRoom.addStudent(studentsInTheEntrance.get(index)); //questo?
    }

    public void setDiningRoom(DiningRoom d){ // non ben chiaro
        this.doorTotheDiningRoom = d;
    }

}
