package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;

import java.util.ArrayList;

public class Princess extends Character {

    /** this attribute handles the students on the card */
    private ArrayList<Creature> studentsOnPrincess;

    public Princess(){
        this.price = 2;
        studentsOnPrincess = new ArrayList<>();
    }

    public ArrayList<Creature> getStudentsOnPrincess() {
        return studentsOnPrincess;
    }

    @Override
    public boolean checkCharacterAvailability() {
        return false;
    }

    @Override
    public void effect(CharacterDataMessage request) {

    }
}
