package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolBoardTest {

    @Test
    void getControlledProfessors() {
        SchoolBoard schoolBoard = new SchoolBoard(3, new Realm(3, new Bag()), Tower.BLACK);
        schoolBoard.getProfessorTable().addProfessor(Creature.DRAGON);
        assertTrue(schoolBoard.getControlledProfessors().contains(Creature.DRAGON));
    }

    @Test
    void setCoinManager() {
    }

    @Test
    void removetower() {
    }
}