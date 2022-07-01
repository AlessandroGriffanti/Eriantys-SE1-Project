package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.SchoolBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolBoardTest {

    /** checks if correctly returns the controlled dragon professor */
    @Test
    void getControlledProfessors() {
        Match match = new Match(0, 2, true);
        Player james = new Player(match, 0, "james",2, match.getRealmOfTheMatch());
        SchoolBoard schoolBoard = new SchoolBoard(match, 3, match.getRealmOfTheMatch(), james);
        schoolBoard.getProfessorTable().addProfessor(Creature.DRAGON);
        assertTrue(schoolBoard.getControlledProfessors().contains(Creature.DRAGON));
    }

    /** checks if the initial setup of the entrance correctly works */
    @Test
    void initialSetUpStudentsInTheEntrance(){
        Match match = new Match(0, 2, true);
        Realm r = match.getRealmOfTheMatch();
        Player james = new Player(match, 0, "james",2, match.getRealmOfTheMatch());
        SchoolBoard schoolBoard = new SchoolBoard(match, 2, r, james);

        assertEquals(7, schoolBoard.getEntrance().getStudentsInTheEntrance().size());
    }

    /** this test checks if the players' tower Areas are correctly updated: first of all p1 is set as the master of the
     * archipelago 0 (which is just and island now) and his towers are correctly lowered by one (from 8 to 7). Then p1 becomes
     * the master of the archipelago 1 too and the two archipelagos are united and p1's tower area correctly contains 6 towers.
     * then p2 becomes the master over this archipelago made of two island and 2 of his towers are correctly set there and
     * removed from his tower area.
     */
    @Test
    void takeTowers(){
        Match match = new Match(0, 2, true);
        Realm realm = match.getRealmOfTheMatch();
        Player p1 = new Player(match, 1, "Jack", 2, realm);
        Player p2 = new Player(match, 2, "Lisa", 2, realm);

        p1.setTowerColor(Tower.BLACK);
        p2.setTowerColor(Tower.GREY);

        realm.getArchipelagos().get(0).setMasterOfArchipelago(p1);
        assertEquals(7, p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

        realm.getArchipelagos().get(1).setMasterOfArchipelago(p1);


        p1.getRealm().unifyArchipelago(0,1);
        assertEquals(6, p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

        realm.getArchipelagos().get(0).setMasterOfArchipelago(p2);
        assertEquals(6, p2.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
        assertEquals(8,p1.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
    }


}