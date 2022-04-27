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

    /** effect method for this character */
    public void effect(int indexOfPlayer, int positionEntranceOne, int positionEntranceTwo, Creature creatureChosenOne, Creature creatureChosenTwo) throws Exception {
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().moveStudent(positionEntranceOne);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().moveStudent(positionEntranceTwo);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().removeStudent(creatureChosenOne);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().addStudent(creatureChosenOne);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().removeStudent(creatureChosenTwo);
        getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getEntrance().addStudent(creatureChosenTwo);
    }
}
