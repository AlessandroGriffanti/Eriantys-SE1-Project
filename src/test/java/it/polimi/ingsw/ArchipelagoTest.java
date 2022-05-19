package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArchipelagoTest {

    /**
     * We test if a single student is correctly added to the population of the archipelago;
     * at the same time the getStudentsOfType method is tested
     */
    @Test
    public void addStudent_oneGenericStudent_addedInStudentsPopulation() {
        Archipelago a = new Archipelago(1);
        a.addStudent(Creature.DRAGON);

        assertTrue(a.getStudentsOfType(Creature.DRAGON) == 1);
    }

    /**
     * We test if more than one student are correctly added to the population
     */
    @Test
    public void addStudents_genericStudents_addedInStudentsPopulation() {
        Archipelago a = new Archipelago(2);
        a.addStudents(Creature.DRAGON, 5);

        assertTrue(a.getStudentsOfType(Creature.DRAGON) == 5);
    }

    /**
     * We test if the setting of the master happens without errors
     */
    @Test
    public void setMasterOfArchipelago_changeMaster(){
        Realm r = new Realm(2, new Bag());
        Player p1 = new Player(1, "Jack", 2, r);
        Player p2 = new Player(2, "Sonia", 2, r);

        assertTrue(p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 8);
        assertTrue(p2.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 8);

        System.out.println("Initial position of mother nature is:" + r.getPositionOfMotherNature());
        r.moveMotherNatureWithSteps(2);
        int currentArchipelago = r.getPositionOfMotherNature();

        r.setMasterOfCurrentArchipelago(p1);
        assertTrue(p1.equals(r.getArchipelagos().get(currentArchipelago).getMasterOfArchipelago()));
        //one tower has been taken from Jack (p1)
        assertTrue(p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 7);

        r.setMasterOfCurrentArchipelago(p2);
        //Jack (p1) takes back his tower
        assertTrue(p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 8);
        //One tower is taken from Sonia (p2)
        assertTrue(p2.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 7);

    }
}
