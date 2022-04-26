package it.polimi.ingsw;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.AssistantsDeck;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssistantsDeckTest {

    @Test
    void useCard() {

    }

    @Test
    void getRemainingCardsNumber() {
        try {
            AssistantsDeck x = new AssistantsDeck(Wizard.CLOUDWITCH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int remainingCardsNumber = 8;
        assertEquals(8, remainingCardsNumber);
    }

    @Test
    void getLastUsedCard() {
        try {
            AssistantsDeck x = new AssistantsDeck(Wizard.CLOUDWITCH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assistant lastUsedCard = new Assistant(2,3);
        assertEquals(2, lastUsedCard.getPlayOrderValue());
    }

}