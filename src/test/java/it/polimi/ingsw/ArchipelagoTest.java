package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArchipelagoTest {

    /**
     * We test if a single student is correctly added to the population of the archipelago;
     * at the same time the getStudentsOfType method is tested
     */
    @Test
    public void addStudent_oneGenericStudent_addedInStudentsPopulation() {
        Archipelago a = new Archipelago(1);
        a.addStudent(Creature.DRAGON);

        Assertions.assertEquals(1, a.getTotalNumberOfStudents());
        Assertions.assertEquals(1, a.getStudentsOfType(Creature.DRAGON));
    }

    /**
     * We test if more than one student are correctly added to the population
     */
    @Test
    public void addStudents_genericStudents_addedInStudentsPopulation() {
        Archipelago a = new Archipelago(2);
        a.addStudents(Creature.DRAGON, 5);

        Assertions.assertEquals(5, a.getStudentsOfType(Creature.DRAGON));
    }

    /**
     * We test if the setting of the master happens without errors
     */
    @Test
    public void setMasterOfArchipelago_changeMaster(){
        Match match = new Match(0, 2, true);
        Realm r = match.getRealmOfTheMatch();
        Player p1 = new Player(match, 0, "Jack", 2, r);
        Player p2 = new Player(match, 1, "Sonia", 2, r);

        Assertions.assertEquals(8, p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
        Assertions.assertEquals(8, p2.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

        r.moveMotherNatureWithSteps(2);
        int currentArchipelago = r.getPositionOfMotherNature();

        r.setMasterOfCurrentArchipelago(p1);
        Assertions.assertTrue(p1.equals(r.getArchipelagos().get(currentArchipelago).getMasterOfArchipelago()));
        //one tower has been taken from Jack (p1)
        Assertions.assertEquals(7, p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

        r.setMasterOfCurrentArchipelago(p2);
        //Jack (p1) takes back his tower
        Assertions.assertEquals(8, p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
        //One tower is taken from Sonia (p2)
        Assertions.assertEquals(7, p2.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

    }

    /**
     * If the player is set twice as master of the archipelago then his towers
     * must not be taken again
     */
    @Test
    public void setMasterOfArchipelago_masterNotChanged(){
        Match match = new Match(0, 2, true);
        Realm r = match.getRealmOfTheMatch();
        Player p1 = new Player(match, 0, "Jack", 2, r);
        Player p2 = new Player(match, 1, "Sonia", 2, r);

        r.moveMotherNatureWithSteps(2);
        int currentArchipelago = r.getPositionOfMotherNature();

        r.setMasterOfCurrentArchipelago(p1);
        r.setMasterOfCurrentArchipelago(p1);

        Assertions.assertEquals(7, p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
    }
}
