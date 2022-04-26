package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Creature;

public class DrugDealer extends Character {
    public DrugDealer() {
        this.price = 3;
    }

    @Override
    public void effect() {
    }

    public void effect(int indexOfPlayer, Creature chosenCreature) throws Exception {
        int numberOfStudentsOfTheChosenCreature;
        int i;

        numberOfStudentsOfTheChosenCreature = getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().getOccupiedSeats().get(chosenCreature).intValue();
        for(i = 0; i < numberOfStudentsOfTheChosenCreature; i++) {
            getMatch().getPlayers().get(indexOfPlayer).getPlayerSchoolBoard().getDiningRoom().removeStudent(chosenCreature);
            getMatch().getBagOfTheMatch().AddStudentToBag(chosenCreature);
        }
    }
}
