package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
    /**
     * This attribute is the list of students from where we'll draw for
     * the setup of the islands
     */
    private ArrayList<Creature> studentsIslandSetUp;
    /**
     * This attribute is the list of students in the bag used during the match after
     * the setup of the islands
     */
    private ArrayList<Creature> remainingStudents;

    /**
     * This constructor builds the two attributes: add two students of each type in the
     * studentsIslandSetUp array and the remaining students are added to the remainingStudents array
     */
    public Bag() {
        int i;

        this.studentsIslandSetUp = new ArrayList<Creature>(10);
        for(i = 0; i < 2; i++) {
            studentsIslandSetUp.add(Creature.DRAGON);
            studentsIslandSetUp.add(Creature.FAIRY);
            studentsIslandSetUp.add(Creature.UNICORN);
            studentsIslandSetUp.add(Creature.GNOME);
            studentsIslandSetUp.add(Creature.FROG);
        }

        this.remainingStudents = new ArrayList<>(120);
        for(i = 0; i < 24; i++) {
            remainingStudents.add(Creature.DRAGON);
            remainingStudents.add(Creature.FAIRY);
            remainingStudents.add(Creature.UNICORN);
            remainingStudents.add(Creature.GNOME);
            remainingStudents.add(Creature.FROG);
        }
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

    /**
     * This method computes the total number of students remaining in the bag
     * @return number of students in the bag
     */
    public int getNumberOfRemainingStudents() {
        int numberRemainingStudents;
        numberRemainingStudents = getRemainingStudents().size();
        return numberRemainingStudents;
    }

    /**
     * This method adds a certain quantity of students of the same kind in the bag
     * @param qt number of students to add
     * @param typeOfStudents type of students to add
     */
    public void addStudentsOfType(int qt, Creature typeOfStudents){
        for(int i = 0; i < qt; i++){
            remainingStudents.add(typeOfStudents);
        }
    }

    /**
     * This method adds an Array of students to the bag (to the array "remainingStudents")
     * @param studentsToAdd the array of students' type that must be added
     */
    public void addStudents(ArrayList<Creature> studentsToAdd){
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