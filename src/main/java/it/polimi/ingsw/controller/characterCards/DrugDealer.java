package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.model.Creature;

public class DrugDealer extends Character {
    public DrugDealer() {
        this.price = 3;
    }

    @Override
    public void effect() {
    }

    /**
     * effect method gets three students of a choosen type from the Dining room of every players, and add them back to the bag.
     * If there aren't enought students of the choosen type, will be returned as many students as there are.
     * @param chosenCreature type of student chosen
     */
    public void effect(Creature chosenCreature) throws Exception {
        int numberOfStudentsOfTheChosenCreature; //number of students of the chosen creature type of the "current" (the one selected by the index) player
        int i;
        int indexOfPlayer;

        for(indexOfPlayer = 0; indexOfPlayer < getMatch().getNumberOfPlayers(); indexOfPlayer++) {
            numberOfStudentsOfTheChosenCreature = getMatch().getPlayers().get(indexOfPlayer).getSchoolBoard().getDiningRoom().getAllOccupiedSeats().get(chosenCreature).intValue();
            for (i = 0; i < numberOfStudentsOfTheChosenCreature && i < 3; i++) {
                getMatch().getPlayers().get(indexOfPlayer).getSchoolBoard().getDiningRoom().removeStudent(chosenCreature);
                getMatch().getBagOfTheMatch().addOneStudentToBag(chosenCreature);
            }
        }
    }
}
