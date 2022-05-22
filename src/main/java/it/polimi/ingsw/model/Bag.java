package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

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

    public boolean isBagCreated() {
        return bagCreated;
    }

    public ArrayList<Creature> getStudentsIslandSetUp() {
        ArrayList<Creature> returnArray = new ArrayList<Creature>();
        for(Creature c: studentsIslandSetUp){
            returnArray.add(c);
        }
        return returnArray;
    }

    public ArrayList<Creature> getRemainingStudents() {
        ArrayList<Creature> returnArray = new ArrayList<Creature>();
        for(Creature c: remainingStudents){
            returnArray.add(c);
        }
        return returnArray;
    }

    public int getNumberOfRemainingStudents() {
        int numberRemainingStudents;
        numberRemainingStudents = getRemainingStudents().size();
        return numberRemainingStudents;
    }

    /**
     * This method adds only one student in the "remainingStudents" ArrayList, whose size is increased by 1.
     * @param studentToPutInTheBag the kind of student that must be added to the bag
     */
    public void addOneStudentToBag (Creature studentToPutInTheBag){
        this.remainingStudents.add(studentToPutInTheBag);
    }

    /**
     * This method adds an Array of students to the bag (to the array "remainingStudents")
     * @param studentsToAdd the array of students' type that must be added
     */
    public void addStudentsToBag(ArrayList<Creature> studentsToAdd){
        for(Creature c: studentsToAdd){
            remainingStudents.add(c);
        }
    }

    /**
     * This method draw all ten students from the "studentsIslandSetUp" ArrayList and put them, sequentially, in a new
     * ArrayList that will be returned (is used by Realm to put the first student on each archipelago except the one
     * where mother nature is at the beginning of the match and the one opposite to mother nature position).
     * The "studentsIslandSetUp" ArrayList at the end of the method execution will be empty.
     * @return The 10 students belonging to "studentsIslandSetUp" in the order they have been drawn.
     */
    public ArrayList<Creature> drawSetUpStudents() {
        ArrayList<Creature> caughtStudentsForSetup;
        Random randomGenerator;
        int randomIndex;
        int i;

        caughtStudentsForSetup = new ArrayList<Creature>();
        randomGenerator = new Random();

        for (i = 0; i < 10; i++) {
            randomIndex = randomGenerator.nextInt(this.studentsIslandSetUp.size());
            caughtStudentsForSetup.add(this.studentsIslandSetUp.get(randomIndex));
            this.studentsIslandSetUp.remove(randomIndex);
        }

        return caughtStudentsForSetup;
    }

    /**
     * This method draws a certain number of students, specified by the parameter, from the bag, and remove it from
     * the "remainingStudents" arrayList.
     * The bag's size (size of "remainingStudents") is decreased by the 'numberOfStudentsToDraw'
     * @param numberOfStudentsToDraw number of students that must be drawn (size of the ArrayList returned)
     * @return ArrayList containing 'numberOfStudentsToDraw' students in the order they have been drawn from the bag.
     */
    public ArrayList<Creature> drawStudents(int numberOfStudentsToDraw){
        ArrayList<Creature> caughtStudents = null;
        Random randomGenerator;
        int randomIndex;
        int i;

        caughtStudents = new ArrayList<Creature>();
        randomGenerator = new Random();

        for(i = 0; i < numberOfStudentsToDraw; i++) {

            if(!(this.getNumberOfRemainingStudents()==0)){
                randomIndex = randomGenerator.nextInt(this.remainingStudents.size());
                caughtStudents.add(this.remainingStudents.get(randomIndex));
                this.remainingStudents.remove(randomIndex);
            }else{
                break;
            }

        }

        return caughtStudents;

    }

    /**
     * This method draws only one student from the bag ("remainingStudents") and remove it from the ArrayList
     * "remainingStudents"
     * @return The kind of student that has been drawn
     */
    public Creature drawOneStudent(){
        Creature caughtStudent;
        Random randomGenerator;
        int randomIndex;
        int i;

        randomGenerator = new Random();

        randomIndex = randomGenerator.nextInt(this.remainingStudents.size());
        caughtStudent = this.remainingStudents.get(randomIndex);
        this.remainingStudents.remove(randomIndex);

        return caughtStudent;
    }
}