package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class CharactersManager {
    /**
     * This attribute contains the name and reference of each of the three cards chosen at the beginning of the match
     * key: card's name
     * value: reference to the Character object
     */
    private HashMap<String, Character> cards;
    /**
     * This attribute is the reference to the controller of the match
     */
    Controller controller;
    /**
     * This attribute is true if the cook characterCard has been used
     *                   false otherwise
     */
    private boolean cookUsed = false;
    /**
     * This attribute tells us if the character card showing a centaur has been played,
     * in this case the computation of the influence won't take into account the towers
     * on the island
     */
    boolean centaurUsed = false;


    public CharactersManager(Controller controller) {
        this.cards = new HashMap<String, Character>();
    }

    /**
     * This method chooses randomly, between all the available characters, only 3 of them
     * @return list of names associated with characters chosen in the exact order of choice
     */
    public Set<String> chooseCharacter(){

        Random random = new Random();;
        int randomNumber;

        ArrayList<Integer> alreadyDrawnNumbers = new ArrayList<Integer>();

        for(int i = 0; i < 3; i++) {
            randomNumber = random.nextInt(13);

            if(!alreadyDrawnNumbers.contains(randomNumber)){
                /* add the number just drawn to the array so that we can choose a different card the
                   next time we draw another number*/
                alreadyDrawnNumbers.add(randomNumber);

                // control to which one of the characters the number corresponds
                switch (randomNumber) {
                    case 1:
                        cards.put("monk", new Monk(controller));
                        break;
                    case 2:
                        cards.put("cook", new Cook(controller));
                        break;
                    case 3:
                        cards.put("ambassador", new Ambassador());
                        break;
                    case 4:
                        cards.put("messenger", new Messenger());
                        break;
                    case 5:
                        cards.put("herbalist", new Herbalist());
                        break;
                    case 6:
                        cards.put("centaur", new Centaur(controller));
                        break;
                    case 7:
                        cards.put("jester", new Jester());
                        break;
                    case 8:
                        cards.put("knight", new Knight());
                        break;
                    case 9:
                        cards.put("mushroomMerchant", new MushroomMerchant());
                        break;
                    case 10:
                        cards.put("bard", new Bard());
                        break;
                    case 11:
                        cards.put("princess", new Princess());
                        break;
                    case 12:
                        cards.put("trafficker", new Trafficker());
                        break;
                }
            }
        }

        return cards.keySet();
    }

    /**
     *This method controls if the player has enough coins to use the character, if so it withdraws the price from the player
     * and put the same amount of coins in the public reserve of coins and then calls the effect of the
     * chosen character
     * @param request message sent by the client asking to use the character
     */
    public void useCard(ChosenCharacterMessage request){

        int player_ID = request.getSender_ID();
        Player player = controller.getMatch().getPlayerByID(player_ID);
        String cardChosen = request.getCardName();

        // get the current price of the card
        int price = cards.get(cardChosen).getPrice();

        // the card chosen must be one of the three cards chosen at the beginning of the match
        assert cards.containsKey(cardChosen) : "The player chose a card that was not chosen as one of the three cards of this match!!";

        // control if the number of coins is sufficient, if so collect them
        int playerCoins = player.getCoinsOwned();
        if(price > playerCoins){
            NackMessage nack = new NackMessage();
            nack.setSubObject("character");
        }

        // withdraw coins from player
        player.spendCoins(price);

        // put the same amount of coins in the public reserve
        controller.getMatch().depositInCoinReserve(price);

        // call the effect of the card
        cards.get(cardChosen).effect(request);
    }

    // SETTER AND GETTER FOR cookUsed
    public void setCookUsed(boolean cookUsed) {
        this.cookUsed = cookUsed;
    }

    public boolean isCookUsed() {
        return cookUsed;
    }

    // SETTER AND GETTER FOR centaurUsed
    public void setCentaurUsed(boolean centaurUsed) {
        this.centaurUsed = centaurUsed;
    }

    public boolean isCentaurUsed() {
        return centaurUsed;
    }
}
