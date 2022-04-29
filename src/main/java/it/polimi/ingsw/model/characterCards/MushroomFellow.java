package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Creature;


public class MushroomFellow extends Character {

    public MushroomFellow(){
        this.price = 3;
    }
    public void effect(){};


    /** same idea as the one presented in the centaur character card but concerning the students. The idea is that we set
     * the value of a certain creature c in the enum (the one equal to the creatureChosen) to a constant called 'TRANSPARENT' and, in the
     * controller, we do not count the influence over and Island of the TRANSPARENT students.
     */
    public void effect(Creature creatureChosen) {
        for(Creature c : Creature.values()){
            if(c.equals(creatureChosen)){
                creatureChosen.setCreature(c);
            }

        }

    }
}
