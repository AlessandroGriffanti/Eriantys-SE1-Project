package it.polimi.ingsw;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.ProfessorTable;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorTableTest {

    @Test
    void addProfessor() {
        ProfessorTable professorTable = new ProfessorTable();
        for(Creature c : Creature.values()){
            professorTable.addProfessor(c);
            assertTrue(professorTable.getOccupiedSeats().get(c));
        }
    }

    @Test
    void removeProfessor() {
        ProfessorTable professorTable = new ProfessorTable();
        for(Creature c : Creature.values()) {
            professorTable.getOccupiedSeats().replace(c, true);
        }
        for(Creature c : Creature.values()){
            professorTable.removeProfessor(c);
            assertFalse(professorTable.getOccupiedSeats().get(c));
        }
    }

    @Test
    void isOccupied() {
        ProfessorTable professorTable = new ProfessorTable();
        for (Creature c : Creature.values()) {
            professorTable.getOccupiedSeats().replace(c, true);
            assertTrue(professorTable.isOccupied(c));
        }
        for (Creature c : Creature.values()){
            professorTable.getOccupiedSeats().replace(c,false);
            assertFalse(professorTable.isOccupied(c));
    }



    }
}