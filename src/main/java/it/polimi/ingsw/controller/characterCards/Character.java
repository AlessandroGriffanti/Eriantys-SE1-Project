package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;

/**
 * This class represent one character card
 */
public abstract class Character{

    /**
     * This attribute is the price of the card
     */
    int price;
    /**
     * This attribute is the reference to the controller of the match
     */
    Controller controller;
    /**
     * This attribute says if the card has already been used one time , hence the price increased
     */
    private boolean priceIncreased = false;


    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    /**
     * This method increases the price of the card by one, only if it's the first use of this card
     */
    public void increasePrice(){
        if(!priceIncreased){
            price++;
            priceIncreased = true;
        }
    }

    public abstract void effect(ChosenCharacterMessage request);

}
