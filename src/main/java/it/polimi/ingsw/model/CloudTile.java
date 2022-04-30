package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 *This Class represents one single cloud-tile
 */
public class CloudTile {
    /**
     * This attribute is the identifier of the cloud, which is unique
     */
    private int ID;
    /**
     * This attribute is the number of students that can be on the cloud at the same time
     */
    private int capacity;
    /**
     * This attribute is the list of students currently on the cloud
     */
    private ArrayList<Creature> students;

    /**
     * This attribute is the reference to the bag used in the match, it can be called to draw the
     * needed students
     */
    private Bag bag;

    /**
     * Constructor
     * @param ID identifier of the cloud
     * @param numOfPlayers number of players playing the match
     * @param bag reference to the bag used in the match
     */
    public CloudTile(int ID, int numOfPlayers, Bag bag){
        this.ID = ID;

        if(numOfPlayers == 3){
            this.capacity = 4;
        }else{
            this.capacity = 3;
        }
        this.bag = bag;
        this.students = bag.drawStudents(capacity);
    }

    public int getID() {
        return ID;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Creature> getStudents() {
        ArrayList<Creature> returnArray= new ArrayList<Creature>();
        for(Creature c: students){
            returnArray.add(c);
        }
        return returnArray;
    }

    /**
     * Copies the "students" attribute into a new ArrayList that will be returned and put new students,
     * as many as the previous ones and drawn from the "bag", inside the array "students".
     * @return ArrayList of all students found on the cloud
     */
    public ArrayList<Creature> takeStudents(){

        ArrayList<Creature> takenStudents = new ArrayList<Creature>(this.students);
        this.students.clear();

        return takenStudents;
    }

    /**
     * Puts new students on the cloud. Draw a new set of  students from the bag
     */
    public void putStudents() {
        this.students = bag.drawStudents(capacity);
    }
}
