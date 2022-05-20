package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.Test;

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

        //Control2
        Archipelago initialIslandOfMotherNature = realm.getArchipelagos().get(motherNaturePosition);
        assertTrue(initialIslandOfMotherNature.getTotalNumberOfStudents() == 0);

        //Control3
        Archipelago sixthIsland = realm.getArchipelagos().get((motherNaturePosition + 6)%12);
        assertTrue(sixthIsland.getTotalNumberOfStudents() == 0);

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

        //case1
        previousPositionOfMotherNature = realm.getPositionOfMotherNature();

        realm.moveMotherNatureWithSteps(3);
        assertTrue(realm.getPositionOfMotherNature() == (previousPositionOfMotherNature+3)%12);

        //case2
        previousPositionOfMotherNature = realm.getPositionOfMotherNature();

        realm.unifyArchipelago((previousPositionOfMotherNature+2)%12, (previousPositionOfMotherNature+3)%12);
        realm.moveMotherNatureWithSteps(3);
        assertTrue(realm.getPositionOfMotherNature() == (previousPositionOfMotherNature+4)%12);

        //case3
        previousPositionOfMotherNature = realm.getPositionOfMotherNature();

        realm.moveMotherNatureWithSteps(0);
        assertTrue(realm.getPositionOfMotherNature() == previousPositionOfMotherNature);

    }

    @Test
    public void previousIslandTest(){
        Realm realm = new Realm(2, new Bag());

        // move mother nature on island 2
        realm.setDestinationOfMotherNature(2);

        // island_0 - 1 step = island_11
        assertTrue(realm.previousIsland(0) == 11);

        // island_3 - 1 step = island_2
        assertTrue(realm.previousIsland(3) == 2);

        /* unify islands 10 and 11
        *  island_0 - 1 step = island_10*/
        realm.unifyArchipelago(10, 11);

        assertTrue(realm.previousIsland(0) == 10);

        /*unify islands 2 and 3
        * island_4 - 1 step = island_2*/
        realm.unifyArchipelago(2, 3);
        assertTrue(realm.previousIsland(4) == 2);

        // more than one island at null in one single step backward
        realm.unifyArchipelago(10, 9);
        assertTrue(realm.previousIsland(0) == 10);
        assertTrue(realm.previousIsland(10) == 8);
    }

    @Test
    public void nextIslandTest(){
        Realm realm = new Realm(2, new Bag());

        // move mother nature on island 2
        realm.setDestinationOfMotherNature(2);

        // island_5 + 1 step = island_6
        assertTrue(realm.nextIsland(5) == 6);

        /*  unify islands 5 and 6
         *  island_5 + 1 step = island_7*/
        realm.unifyArchipelago(5, 6);
        assertTrue(realm.nextIsland(5) == 7);

        // island_11 + 1 step = island_0
        assertTrue(realm.nextIsland(11) == 0);

        /* unify islands 10, 11 and 9, 10 -> 10 and 11 to null
        *  island_9 + 1 step = island_0 */
        realm.unifyArchipelago(10, 11);
        assert realm.getArchipelagos().get(11) == null;
        realm.unifyArchipelago(9, 10);
        assert realm.getArchipelagos().get(10) == null;

        assertTrue(realm.nextIsland(9) == 0);
    }
}
