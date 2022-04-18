package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Bag {
    ArrayList<Creature> studentsIslandSetUp;
    ArrayList<Creature> remainingStudents;
    boolean bagCreated;

    public Bag() {
    }

    public ArrayList<Creature> getRemainingStudents() {
        return remainingStudents;
    }

    public int getNumberOfRemainingStudents(ArrayList<Creature> remainingStudents) {
        int numberRemainingStudents;
        numberRemainingStudents = this.remainingStudents.size();
    return numberRemainingStudents;
    }

    public boolean isBagCreated() {
        return bagCreated;
    }

}