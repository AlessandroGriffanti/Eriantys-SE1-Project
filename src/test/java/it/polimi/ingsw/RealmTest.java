package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RealmTest {

    @Test
    /**
     * We test if the constructor of Realm build correctly the islands ring
     * - Control1: there are totally 12 archipelagos right after the construction of Realm
     * - Control2: on the island of the initial position of mother nature there are no students
     * - Control3: the sixth island after the initial position of mother nature there are no students
     *             in this third case of taste we are also testing the correct functioning of the method
     *             drawSetUpStudents() defined in the Bag class, because it is called in the constructor
     *             of Realm
     */
    public void RealmConstruction(){
        Bag bag = new Bag();
        Realm realm = new Realm(2, bag);

        int motherNaturePosition = realm.getPositionOfMotherNature();

        //Control1
        assertTrue(realm.getArchipelagos().size() == 12);
        System.out.println("RealmConstruction Control1 passed");

        //Control2
        Archipelago initialIslandOfMotherNature = realm.getArchipelagos().get(motherNaturePosition);
        assertTrue(initialIslandOfMotherNature.getTotalNumberOfStudents() == 0);
        System.out.println("RealmConstruction Control2 passed");

        //Control3
        Archipelago sixthIsland = realm.getArchipelagos().get((motherNaturePosition + 6)%12);
        assertTrue(sixthIsland.getTotalNumberOfStudents() == 0);
        System.out.println("RealmConstruction Control3 passed");

    }

    /**
     * We test if the union of two archipelagos is correct
     * - first assert: the second archipelago (parameter a2 of unifyArchipelago) is null
     */
    @Test
    public void unifyArchipelago(){
        Bag bag = new Bag();
        Realm realm = new Realm(2, bag);

        realm.unifyArchipelago(1, 2);
        assertEquals(null, realm.getArchipelagos().get(2));
    }

    @Test
    /**
     * We test the correct functioning of the moveMotherNature method:
     * - case1: 3 steps, every archipelago is made of one single island-tile
     * - case2: 3 steps, there are some archipelagos which are made of multiple island-tile
     * - case3: 0 steps
     */
    public void moveMotherNatureTest(){
        Bag bag = new Bag();
        Realm realm = new Realm(2, bag);
        int previousPositionOfMotherNature;
        System.out.println("Mother nature start archipelago: " + realm.getPositionOfMotherNature());

        //case1
        previousPositionOfMotherNature = realm.getPositionOfMotherNature();

        realm.moveMotherNature(3);
        assertTrue(realm.getPositionOfMotherNature() == (previousPositionOfMotherNature+3)%12);
        System.out.println("moveMotherNatureTest Case1 passed");

        //case2
        previousPositionOfMotherNature = realm.getPositionOfMotherNature();

        realm.unifyArchipelago((previousPositionOfMotherNature+2)%12, (previousPositionOfMotherNature+3)%12);
        realm.moveMotherNature(3);
        assertTrue(realm.getPositionOfMotherNature() == (previousPositionOfMotherNature+4)%12);
        System.out.println("moveMotherNatureTest Case2 passed");

        //case3
        previousPositionOfMotherNature = realm.getPositionOfMotherNature();

        realm.moveMotherNature(0);
        assertTrue(realm.getPositionOfMotherNature() == previousPositionOfMotherNature);
        System.out.println("moveMotherNatureTest Case3 passed");

    }
}
