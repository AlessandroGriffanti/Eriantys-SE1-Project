package it.polimi.ingsw.model;

import java.net.URL;
import java.nio.Buffer;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

public class AssistantsDeck {
    /**
     * This attribute is the deck of the player, composed by ten different cards
     * (Assistant); each card correspond to a unique key which is its order-value,
     * visible on the top left of the card.
     */
    private final HashMap<Integer, Assistant> deck;
    /**
     * This attribute is the last card that the player used; this is the same
     * card at the top of the already-used-card deck on the table game
     */
    private Assistant lastUsedCard = null;
    /**
     * This attribute is the factor that unites all the cards in the same deck, that is
     * the back figure of each card.
     */
    private Wizard wizard;
    /**
     * This attribute is the number of cards that can still be played by the player
     */
    private int numberOfRemainingCards;


    public AssistantsDeck (Wizard wizard) {
        this.wizard = wizard;
        this.deck = new HashMap<Integer, Assistant>();
        this.numberOfRemainingCards = 10;
        int i, x;


        try{
            FileReader fileText = new FileReader("resources/assistantList.txt");
            Scanner scanner = new Scanner(fileText);

            for(i = 0; i < 10; i++) {

                x = scanner.nextInt();
                this.deck.put( x, new Assistant(x, scanner.nextInt() ) );

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public HashMap<Integer, Assistant> getDeck(){
        return deck;
    }

    public int getNumberOfRemainingCards() {
        return numberOfRemainingCards;
    }

    public Assistant getLastUsedCard() {
        return lastUsedCard;
    }

    /**
     * This method take the used card, remove it from the deck, decreasing the number
     * of cards still available, and lastly set it as the lastUsedCard
     * @param orderValue the key used to access the deck
     * @return the card that has been used
     */
    public Assistant useCard(int orderValue) {
        Assistant card;

        card = this.deck.get(orderValue);

        //If the card has already been used we return null
        if(card == null){
            return null;
        }

        this.deck.remove(orderValue);
        this.lastUsedCard = card;
        this.numberOfRemainingCards--;

        return card;
    }

    public Wizard getWizard() {
        return wizard;
    }
}