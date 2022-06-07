package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.Entrance;
import org.junit.Test;

import java.security.cert.Certificate;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*Note: some methods won't be tested in this class because used by the controller
* methods which are tested in ControllerTest class*/
public class MatchTest {

    /**
     * This method controls if players are correctly added to the match
     */
    @Test
    public void addPlayerTest_twoPlayersAdded(){
        Match match = new Match(0, 2, false);

        match.addPlayer("mario");
        match.addPlayer("luigi");

        Player mario = match.getPlayerByID(0);
        Player luigi = match.getPlayerByID(1);

        assertEquals("mario", mario.getNickName());
        assertEquals(0, mario.getID());
        assertEquals("luigi", luigi.getNickName());
        assertEquals(1, luigi.getID());

        assertEquals(1, mario.getCoinsOwned());
        assertEquals(1, luigi.getCoinsOwned());
    }

    /**
     * This method controls that we can correctly get a player reference using the nickname
     */
    @Test
    public void getPlayersByNickname(){
        Match match = new Match(0, 2, false);

        match.addPlayer("mario");
        match.addPlayer("luigi");
        Player mario = match.getPlayerByNickname("mario");
        Player luigi = match.getPlayerByNickname("luigi");

        assertEquals(0, mario.getID());
        assertEquals("mario", mario.getNickName());
        assertEquals(1, luigi.getID());
        assertEquals("luigi", luigi.getNickName());
    }

    /**
     * This method controls if the 'getInitialStudentsOnEachIsland' method works
     * correctly; we test it right after the island initialization (one student put
     * on each island except for where there is MotherNature and the opposite island)
     */
    @Test
    public void match_getInitialStudentsOnEachIsland(){
        Match match = new Match(0, 2, true);
        Realm realm = match.getRealmOfTheMatch();
        Bag bag = match.getBagOfTheMatch();

        int island_ID = realm.getPositionOfMotherNature();

        ArrayList<Creature> studentOnEachArchipelago = new ArrayList<>();
        do{
            Archipelago a = realm.getArchipelagos().get(island_ID);
            for(Creature c: Creature.values()){
                if(a.getStudentsPopulation().get(c) != 0){
                    studentOnEachArchipelago.add(c);
                }
            }
            island_ID = (island_ID + 1) % 12;
        }while (island_ID != realm.getPositionOfMotherNature());
        assertEquals(studentOnEachArchipelago, match.getInitialStudentsOnEachIsland());
    }

    /**
     * This method runs 3 tests (assertEquals) on islandUnifyControl method:
     * - unification between current and previous island
     * - unification between current and both previous and next island
     * - unification between current and next island
     */
    @Test
    public void islandUnifyControlTest_previousAndNextIsland(){
        Match match = new Match(0, 2, true);
        match.addPlayer("mario");
        match.addPlayer("luigi");

        Player mario = match.getPlayerByID(0);
        Player luigi = match.getPlayerByID(1);

        // set tower color of mario
        mario.setTowerColor(Tower.BLACK);
        // set tower color of luigi
        luigi.setTowerColor(Tower.WHITE);

        ArrayList<Archipelago> archipelagos = match.getRealmOfTheMatch().getArchipelagos();
        int MNPosition = match.getPositionOfMotherNature();

        int previousIsland_ID = match.getRealmOfTheMatch().previousIsland(MNPosition);
        int nextIsland_ID = match.getRealmOfTheMatch().nextIsland(MNPosition);

        // mario master of previous island
        archipelagos.get(previousIsland_ID).setMasterOfArchipelago(mario);
        // mario master of current island
        archipelagos.get(MNPosition).setMasterOfArchipelago(mario);
        assertEquals(1, match.islandUnifyControl());

        // mario master of next island
        archipelagos.get(nextIsland_ID).setMasterOfArchipelago(mario);
        // mario used two towers
        assertEquals(3, match.islandUnifyControl());

        // luigi becomes the new master of previous island
        archipelagos.get(previousIsland_ID).setMasterOfArchipelago(luigi);
        assertEquals(2, match.islandUnifyControl());

        assert luigi.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 7;
        assert mario.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers() == 6;
    }

}
