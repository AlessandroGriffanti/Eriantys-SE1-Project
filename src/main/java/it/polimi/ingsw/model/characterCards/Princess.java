package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Creature;

import java.util.ArrayList;

public class Princess extends Character {

    /** this attribute handles the students on the card */
    private ArrayList<Creature> studentsOnPrincess;

    public Princess(){
        this.price = 2;
        studentsOnPrincess = new ArrayList<>();
    }

    /** same idea as the one explained in the joker character card */
    public void setUpEffect(){
        studentsOnPrincess.addAll(getMatch().getBagOfTheMatch().drawStudents(4));
    }
    public void effect(){};

    /** the idea of this method is nearly the same as the one in the joker */
    public void effect(int indexOfPlayer, int indexOfTheStudentOnPrincess, int indexOfTheStudentInTheDiningRoom){
        Creature creatureToRemoveFromPrincess = getStudentsOnPrincess().get(indexOfTheStudentOnPrincess);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().addStudent(creatureToRemoveFromPrincess);

        ArrayList<Creature> newStudentToAddToPrincess = getMatch().getBagOfTheMatch().drawStudents(1);
        studentsOnPrincess.addAll(newStudentToAddToPrincess);
    }

    public ArrayList<Creature> getStudentsOnPrincess() {
        return studentsOnPrincess;
    }

}
