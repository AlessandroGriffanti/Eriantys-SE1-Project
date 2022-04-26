package it.polimi.ingsw;

import it.polimi.ingsw.model.Assistant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantTest {

    @Test
    void getPlayOrderValue() {
        Assistant x = new Assistant(2,3);
        assertEquals(2, x.getPlayOrderValue() );
    }

    @Test
    void getMotherNatureMovementValue() {
        Assistant x = new Assistant(2,3);
        assertEquals(3, x.getMotherNatureMovementValue() );
    }
}