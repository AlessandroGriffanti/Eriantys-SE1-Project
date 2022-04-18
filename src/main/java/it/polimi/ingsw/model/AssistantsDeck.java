package it.polimi.ingsw.model;

import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

public class AssistantsDeck {
    private HashMap<Integer, Assistant> deck;
    private Assistant lastUsedCard;                 //last one from waste stack
    private Wizard wizard;
    private int remainingCardsNumber;

    /*
    public showCard(int orderValue) {
    }
    */


    public Assistant useCard(int orderValue) {
        Assistant x;

        x = this.deck.get(orderValue);
        this.deck.remove(orderValue);
        this.lastUsedCard = x;
        this.remainingCardsNumber = getRemainingCardsNumber() - 1;

        return x;
    }


    public int getRemainingCardsNumber() {
        return remainingCardsNumber;
    }


    public Assistant getLastUsedCard() {
        return lastUsedCard;
    }


    public AssistantsDeck (Wizard wizard) throws FileNotFoundException {
        this.wizard = wizard;
        this.deck = new HashMap<Integer, Assistant>();
        this.remainingCardsNumber = 10;
        int i, x;

        File fileText = new File("assistantlist.txt");
        Scanner s = new Scanner(fileText);

        for(i = 0; i < 10; i++) {
            x = s.nextInt();
            this.deck.put( x, new Assistant(x, s.nextInt() ) );
        }

        s.close();
    }

}