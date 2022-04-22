package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
    private ArrayList<Creature> studentsIslandSetUp;
    private ArrayList<Creature> remainingStudents;
    protected boolean bagCreated = false;

    public Bag() {
        int i;
        boolean flag_x, flag_y;
        flag_x = false;
        flag_y = false;

        this.studentsIslandSetUp = new ArrayList<Creature>(10);
        for(i = 0; i < 2; i++) {
            studentsIslandSetUp.add(Creature.DRAGON);
            studentsIslandSetUp.add(Creature.FAIRY);
            studentsIslandSetUp.add(Creature.UNICORN);
            studentsIslandSetUp.add(Creature.GNOME);
            studentsIslandSetUp.add(Creature.FROG);
            if(i == 1){
                flag_x = true;
            }
        }
        //System.out.println(studentsIslandSetUp);

        this.remainingStudents = new ArrayList<>(120);
        for(i = 0; i < 24; i++) {
            remainingStudents.add(Creature.DRAGON);
            remainingStudents.add(Creature.FAIRY);
            remainingStudents.add(Creature.UNICORN);
            remainingStudents.add(Creature.GNOME);
            remainingStudents.add(Creature.FROG);
            if(i == 23){
                flag_y = true;
            }
        }
        //System.out.println(remainingStudents);

        if (flag_y){
            if (flag_x) {
                this.bagCreated = true;
            }
        }

    }

    public ArrayList<Creature> getStudentsIslandSetUp() {
        return studentsIslandSetUp;
    }

    public ArrayList<Creature> getRemainingStudents() {
        return remainingStudents;
    }

    public int getNumberOfRemainingStudents() {
        int numberRemainingStudents;
        numberRemainingStudents = getRemainingStudents().size();
    return numberRemainingStudents;
    }

    public boolean isBagCreated() {
        return bagCreated;
    }

    public ArrayList<Creature> drawStudents(int numberToDraw){
        ArrayList<Creature> caughtStudents;
        Random randomGenerator;
        int randomIndex;
        int i;

        caughtStudents = new ArrayList<Creature>(numberToDraw);
        randomGenerator = new Random();

        for(i = 0; i < numberToDraw; i++) {
            randomIndex = randomGenerator.nextInt(this.remainingStudents.size());
            caughtStudents.add(this.remainingStudents.get(randomIndex));
            this.remainingStudents.remove(randomIndex);
        }

        return caughtStudents;
    }



}