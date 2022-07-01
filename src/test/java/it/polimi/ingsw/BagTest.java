package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;

import it.polimi.ingsw.model.Creature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class BagTest {

    /**
     * We test if we get a correct array containing the students used for the setup of the islands
     */
    @Test
    public void drawSetUpStudents(){
        Bag bag = new Bag();
        ArrayList<Creature> setUpStudentsReceived = bag.drawSetUpStudents();

        assertEquals(10, setUpStudentsReceived.size());

        assertEquals(2, getNumberOfStudentsType(setUpStudentsReceived, Creature.DRAGON));
        assertEquals(2, getNumberOfStudentsType(setUpStudentsReceived, Creature.FAIRY));
        assertEquals(2, getNumberOfStudentsType(setUpStudentsReceived, Creature.FROG));
        assertEquals(2, getNumberOfStudentsType(setUpStudentsReceived, Creature.UNICORN));
        assertEquals(2, getNumberOfStudentsType(setUpStudentsReceived, Creature.GNOME));
    }

    /**
     *We test the correct functioning of the "drawStudent" methode in these cases:
     * - draw no students from the bag
     * - draw a generic number of students
     */
    @Test
    public void drawStudents_drawNoStudent_NumberNotChanged(){

        Bag bag = new Bag();

        assertEquals(10, bag.getStudentsIslandSetUp().size());

        //draw 0 students
        bag.drawStudents(0);
        assertEquals(120, bag.getRemainingStudents().size());
    }

    /**
     * We draw a generic number of students between 0 and 120
     */
    @Test
    public void drawStudents_drawGenericNumberOfStudents(){
        Bag bag = new Bag();

        Random random = new Random();
        int numberOfStudents = random.nextInt(121);

        ArrayList<Creature> drawnStudents = bag.drawStudents(numberOfStudents);
        assertEquals(bag.getRemainingStudents().size(), 120 - numberOfStudents);
    }

    /**
     * This method control if the bag returns all its student left if we want to draw more
     * students than it has
     */
    @Test
    public void drawStudents_notEnoughStudents(){
        Bag bag = new Bag();
        // reduce the number of students in the bag to 5 students
        int studentsLeft = bag.getNumberOfRemainingStudents();
        bag.drawStudents(studentsLeft - 5);
        assert bag.getNumberOfRemainingStudents() == 5;

        // we want to draw 10 students
        int tooManyStudents = 10;

        ArrayList<Creature> studentsDrawn = bag.drawStudents(tooManyStudents);
        /* we expect the number of students drawn to be less than the number of students required
           and in particular equals to the number of students left in the bag*/
        assertEquals(5, studentsDrawn.size());
    }


    /**
     * We try to draw only one random type of student from the bag using the method
     * "drawOneStudent"
     */
    @Test
    public void drawOneStudent_drawOneGenericTypeOfStudent(){
        Bag bag = new Bag();
        int initialNumberOfDragons = 24;
        int initialNumberOfFairies = 24;
        int initialNumberOfGnomes = 24;
        int initialNumberOfUnicorns = 24;
        int initialNumberOfFrogs = 24;

        Creature drawn = bag.drawOneStudent();

        assert(bag.getNumberOfRemainingStudents() == 120-1);
        ArrayList<Creature> actualBag = bag.getRemainingStudents();
        switch (drawn){
            case DRAGON:
                assert(getNumberOfStudentsType(actualBag, drawn) == initialNumberOfDragons-1);
            case FAIRY:
                assert(getNumberOfStudentsType(actualBag, drawn) == initialNumberOfFairies-1);
            case GNOME:
                assert(getNumberOfStudentsType(actualBag, drawn) == initialNumberOfGnomes-1);
            case UNICORN:
                assert(getNumberOfStudentsType(actualBag, drawn) == initialNumberOfUnicorns-1);
            case FROG:
                assert(getNumberOfStudentsType(actualBag, drawn) == initialNumberOfFrogs-1);
        }
    }

    /**
     * This method controls if the adding of students is done correctly through the two methods
     * defined in the Bag class, that are 'addStudentsOfType' and 'addStudents'
     */
    @Test
    public void addStudentsTest(){
        ArrayList<Creature> studentsToAdd = new ArrayList<Creature>();
        studentsToAdd.add(Creature.FROG);
        studentsToAdd.add(Creature.UNICORN);
        studentsToAdd.add(Creature.UNICORN);
        studentsToAdd.add(Creature.GNOME);

        Bag bag = new Bag();
        bag.drawStudents(120);
        assertEquals(0, bag.getNumberOfRemainingStudents());

        // control method addStudentsOfType
        bag.addStudentsOfType(3, Creature.FAIRY);

        assertEquals(3, bag.getNumberOfRemainingStudents());
        assertEquals(3, getNumberOfStudentsType(bag.getRemainingStudents(), Creature.FAIRY));

        // control method addStudents
        bag.addStudents(studentsToAdd);
        assertEquals(7, bag.getNumberOfRemainingStudents());
        assertEquals(1, getNumberOfStudentsType(bag.getRemainingStudents(), Creature.FROG));
        assertEquals(1, getNumberOfStudentsType(bag.getRemainingStudents(), Creature.GNOME));
        assertEquals(2, getNumberOfStudentsType(bag.getRemainingStudents(), Creature.UNICORN));
        assertEquals(3, getNumberOfStudentsType(bag.getRemainingStudents(), Creature.FAIRY));

    }

    /**
     * This method compute the number of students of a particular type inside an array
     * @param array the array from where we count the number of students
     * @param requiredType type of students of whose quantity inside the bag will be computed
     * @return number of students of type "requiredType" found inside the bag
     */
    private int getNumberOfStudentsType(ArrayList<Creature> array, Creature requiredType){
        int returnValue = 0;

        for(Creature c: array){
            if(c.equals(requiredType)){
                returnValue++;
            }
        }
        return returnValue;
    }

}
