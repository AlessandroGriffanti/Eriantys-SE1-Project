package it.polimi.ingsw;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.AssistantsDeck;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssistantsDeckTest {

    /**
     * We test if the construction of AssistantDeck is done correctly; this depends on the
     * correct reading of the fil assistantList.txt
     */
    @Test
    public void AssistantDeckConstructor() throws IOException{

        AssistantsDeck deck = new AssistantsDeck(Wizard.DESERTWIZARD);

        assertTrue(deck.getDeck().get(1).getMotherNatureMovementValue() == 1);
        assertTrue(deck.getDeck().get(2).getMotherNatureMovementValue() == 1);
        assertTrue(deck.getDeck().get(3).getMotherNatureMovementValue() == 2);
        assertTrue(deck.getDeck().get(4).getMotherNatureMovementValue() == 2);
        assertTrue(deck.getDeck().get(5).getMotherNatureMovementValue() == 3);
        assertTrue(deck.getDeck().get(6).getMotherNatureMovementValue() == 3);
        assertTrue(deck.getDeck().get(7).getMotherNatureMovementValue() == 4);
        assertTrue(deck.getDeck().get(8).getMotherNatureMovementValue() == 4);
        assertTrue(deck.getDeck().get(9).getMotherNatureMovementValue() == 5);
        assertTrue(deck.getDeck().get(10).getMotherNatureMovementValue() == 5);

        //The next lines print the set of keys and the values to which they correspond
        System.out.println("keys: " + deck.getDeck().keySet().toString());

        ArrayList<Integer> values = new ArrayList<Integer>();
        for(int i: deck.getDeck().keySet()){
            values.add(deck.getDeck().get(i).getMotherNatureMovementValue());
        }
        System.out.println("Values: " + values);
    }
    @Test
    void useCard_useTheSameCardTwice_returnNullPointer() throws IOException{
        AssistantsDeck deck = new AssistantsDeck(Wizard.CLOUDWITCH);

        deck.useCard(2);

        assertEquals(null, deck.useCard(2));
        assertEquals(null, deck.useCard(2));

        assertTrue(deck.getNumberOfRemainingCards() == 9);

        //The lastUsedCard must be the one with motherNatureMovementValue = 1
        assertTrue(deck.getLastUsedCard().getMotherNatureMovementValue() == 1);
    }

    @Test
    void useCard_useAllCards() throws IOException {
        AssistantsDeck deck = new AssistantsDeck(Wizard.CLOUDWITCH);

        deck.useCard(2);
        deck.useCard(3);
        deck.useCard(4);
        deck.useCard(5);
        deck.useCard(6);
        deck.useCard(7);
        deck.useCard(8);
        deck.useCard(9);
        deck.useCard(1);
        deck.useCard(10);

        assertTrue(deck.getNumberOfRemainingCards() == 0);
        assertTrue(deck.getLastUsedCard().getMotherNatureMovementValue() == 5);

        assertTrue(deck.getDeck().isEmpty());
    }
}