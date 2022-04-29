package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolBoardTest {

    /** checks if correctly returns the controlled dragon professor */
    @Test
    void getControlledProfessors() {
        SchoolBoard schoolBoard = new SchoolBoard(3, new Realm(3, new Bag()));
        schoolBoard.getProfessorTable().addProfessor(Creature.DRAGON);
        assertTrue(schoolBoard.getControlledProfessors().contains(Creature.DRAGON));
    }

    /** checks if the initial setup of the entrance correctly works */
    @Test
    void initialSetUpStudentsInTheEntrance(){
        Bag bag = new Bag();
        Realm r = new Realm(2, bag);
        SchoolBoard schoolBoard = new SchoolBoard(2, r);

        schoolBoard.initialSetUpStudentsInTheEntrance();

        assertTrue(schoolBoard.getEntrance().getStudentsInTheEntrance().size() == 7);
    }
    @Test
    void setCoinManager() {
    }


    /** this test checks if the players' tower Areas are correctly updated: first of all p1 is set as the master of the
     * archipelago 0 (which is just and island now) and his towers are correctly lowered by one (from 8 to 7). Then p1 becomes
     * the master of the archipelago 1 too and the two archipelagos are united and p1's tower area correctly contains 6 towers.
     * then p2 becomes the master over this archipelago made of two island and 2 of his towers are correctly set there and
     * removed from his tower area.
     */
    @Test
    void takeTowers(){
        Bag bag = new Bag();
        Realm realm = new Realm(2, bag);
        Player p1 = new Player(1, "Jack", 2, realm);
        Player p2 = new Player(2, "Lisa", 2, realm);

        p1.setTowerColor(Tower.BLACK);
        p2.setTowerColor(Tower.GREY);

        realm.getArchipelagos().get(0).setMasterOfArchipelago(p1);
        assertEquals(7, p1.getPlayerSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

        realm.getArchipelagos().get(1).setMasterOfArchipelago(p1);

        p1.getRealm().unifyArchipelago(0,1);
        assertEquals(6, p1.getPlayerSchoolBoard().getTowerArea().getCurrentNumberOfTowers());

        realm.getArchipelagos().get(0).setMasterOfArchipelago(p2);
        assertEquals(6, p2.getPlayerSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
        assertEquals(8,p1.getPlayerSchoolBoard().getTowerArea().getCurrentNumberOfTowers());
    }


}