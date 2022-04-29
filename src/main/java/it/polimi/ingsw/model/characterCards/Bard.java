package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Creature;

public class Bard extends Character {
    public Bard() {
        this.price = 1;
    }

    @Override
    public void effect() {
        System.out.println("Error: no variables received");
    }

    /**
     * effect method swaps to students of the entrance with two students of the entrance.
     * first is used the moveStudent method to move the chosen student to the dining room.
     * then is used the removeStudent method to remove the chosen student from the dining room.
     * the student removed from the dining room is then added to the entrance with the method addStudent.
     * @param indexOfPlayer int index of the player involved
     * @param positionEntranceOne int position of the first student to be taken from the arraylist of the students in the entrance
     * @param positionEntranceTwo int position of the second student to be taken from the arraylist of the students in the entrance
     * @param creatureChosenOne first student chosen from the hashmap of the students in the dining room
     * @param creatureChosenTwo second student chosen from the hashmap of the students in the dining room
     */
    public void effect(int indexOfPlayer, int positionEntranceOne, int positionEntranceTwo, Creature creatureChosenOne, Creature creatureChosenTwo) throws Exception {
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().moveStudent(positionEntranceOne);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().moveStudent(positionEntranceTwo);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().removeStudent(creatureChosenOne);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().addStudent(creatureChosenOne);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().removeStudent(creatureChosenTwo);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().addStudent(creatureChosenTwo);
    }
}
