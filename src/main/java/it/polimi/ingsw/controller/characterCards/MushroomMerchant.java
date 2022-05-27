package it.polimi.ingsw.controller.characterCards;


import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;

public class MushroomMerchant extends Character {

    public MushroomMerchant(){
        this.price = 3;
    }
    public boolean effect(ChosenCharacterMessage request){return true;};


    /** same idea as the one presented in the centaur character card but concerning the students. The idea is that we set
     * the value of a certain creature c in the enum (the one equal to the creatureChosen) to a constant called 'TRANSPARENT' and, in the
     * controller, we do not count the influence over and Island of the TRANSPARENT students.
     */

    /*
    public void effect(Creature creatureChosen) {
        for(Creature c : Creature.values()){
            if(c.equals(creatureChosen)){
                creatureChosen.setCreature(c);
            }

        }

    } */
}
