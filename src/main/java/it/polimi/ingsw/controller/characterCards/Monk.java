package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Character;
import it.polimi.ingsw.model.Creature;

import java.util.ArrayList;


public class Monk  extends Character {
    private ArrayList<Creature> studentsOnMonk;

    public Monk() {
        this.price = 1;                                      //price of the card
        studentsOnMonk = new ArrayList<>(4);     //arraylist of the 4 students which will be drawn from the bag
    }


    /**
     * setUpEffect method prepare the card to be used in the game.
     * With this method, 4 students are drawn from the bag, and place on the monk card
     * (the 4 students are place in the arraylist of the students who are on the monk card)
     */
    public void setUpEffect() {
        this.studentsOnMonk = getMatch().getBagOfTheMatch().drawStudents(4);
    }


    /**
     * effect method takes one student from the monk card and place it on an island.
     * Then 1 student will be drawn from the bag and placed on the monk card
     * (it will be placed in the arraylist of the students who are on the monk card).
     * @param islandToPutID id of the island on which the student is placed
     * @param chosenStudentIndex int number of the chosen student from studentOnMonk arraylist
    */
    public void effect(int islandToPutID, int chosenStudentIndex) {
        Creature chosenStudent;
        Creature caughtStudentFromBag;

        chosenStudent = studentsOnMonk.get(chosenStudentIndex);
        getMatch().getRealmOfTheMatch().addStudentToIsland(chosenStudent, islandToPutID);

        caughtStudentFromBag = getMatch().getBagOfTheMatch().drawOneStudent();
        studentsOnMonk.add(caughtStudentFromBag);
    }


    @Override
    public void effect() {
    }
}
