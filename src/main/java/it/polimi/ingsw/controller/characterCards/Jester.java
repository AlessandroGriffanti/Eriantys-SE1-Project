package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;

import java.util.ArrayList;

public class Jester extends Character {

    /** this attribute handles the students on the card */
    private ArrayList<Creature> studentsOnJoker;


    public Jester(){
        this.price = 1;
        this.studentsOnJoker = new ArrayList<>();
    }



    public ArrayList<Creature> getStudentsOnJoker() {
        return studentsOnJoker;
    }

    @Override
    public boolean effect(CharacterDataMessage request) {
return true;
    }
}
