package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CloudTile {
    private int ID;
    private int capacity;
    private ArrayList<Creature> students;
    private Bag bag;

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

    public ArrayList<Creature> takeStudents(){
        ArrayList<Creature> takenStudents = new ArrayList<Creature>();

        takenStudents.addAll(this.students);
        this.students.clear();

    //put new students on the cloud
        putStudents(bag.drawStudents(capacity));

        return takenStudents;
    }

    private void putStudents(ArrayList<Creature> newStudents) {
        this.students = newStudents;
    }
}
