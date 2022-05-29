package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.clientMessages.CharacterRequestMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
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
     * false otherwise
     */
    private boolean cookActive = false;
    /**
     * This attribute is true if the messenger character card has been used by a player
     * and false otherwise
     */
    private boolean messengerActive = false;
    /**
     * This attribute tells us if the centaur character has been played,
     * in this case the computation of the influence won't take into account the towers
     * on the island
     */
    boolean centaurActive = false;
    /**
     * This attribute is true if the ambassador character has been used false otherwise
     */
    boolean ambassadorActive = false;
    /**
     * This attribute is ID of the player that chose to use the knight character card
     */
    private int knightUser = -1;


    public CharactersManager(Controller controller) {
        this.controller = controller;
        this.cards = new HashMap<String, Character>();
    }

    /**
     * This method chooses randomly, between all the available characters, only 3 of them
     * @return list of names associated with characters chosen in the exact order of choice
     */
    public Set<String> chooseCharacter(){

        Random random = new Random();
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
                        cards.put("messenger", new Messenger(controller));
                        break;
                    case 5:
                        cards.put("herbalist", new Herbalist(controller));
                        break;
                    case 6:
                        cards.put("centaur", new Centaur(controller));
                        break;
                    case 7:
                        cards.put("jester", new Jester(controller));
                        break;
                    case 8:
                        cards.put("knight", new Knight(controller));
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
    public void useCard(CharacterDataMessage request){

        String cardChosen = request.getCharacter();
        Player player = controller.getMatch().getPlayerByID(request.getSender_ID());
        int price = cards.get(cardChosen).getPrice();

        // withdraw coins from player
        player.spendCoins(price);

        // put the same amount of coins in the public reserve
        controller.getMatch().depositInCoinReserve(price);

        // call the effect of the card
        cards.get(cardChosen).effect(request);
    }

    public void checkCard(CharacterRequestMessage request){
        int player_ID = request.getSender_ID();
        Player player = controller.getMatch().getPlayerByID(player_ID);
        String cardChosen = request.getCharacter();

        // get the current price of the card
        int price = cards.get(cardChosen).getPrice();

        // the card chosen must be one of the three cards chosen at the beginning of the match
        assert cards.containsKey(cardChosen) : "The player chose a card that was not chosen as one of the three cards of this match!!";

        // control if the number of coins is sufficient, if so collect them
        int playerCoins = player.getCoinsOwned();
        if(price > playerCoins){
            NackMessage nack = new NackMessage("character_price");

            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }

        // control if the card can be used
        if(cards.get(cardChosen).checkCharacterAvailability()){
            AckMessage ack = new AckMessage();
            ack.setSubObject(cardChosen);

            controller.sendMessageToPlayer(request.getSender_ID(), ack);
        }else{
            NackMessage nack = new NackMessage(cardChosen);

            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }
    }

    /**
     * This method return one no-entry-tile to the herbalist character card
     */
    public void returnTileToHerbalist(){
        Herbalist herbalist =  (Herbalist) cards.get("herbalist");

        herbalist.addOneTile();
    }

    /**
     * This method resets the attributes used for character usage to their default values
     * (so that the characters won't be used by mistake the next round)
     */
    public void resetCharacterAttributes(){
        cookActive = false;
        messengerActive = false;
        centaurActive = false;
        ambassadorActive = false;
        knightUser = -1;

        // todo: complete with the other attributes
    }


    public HashMap<String, Character> getCards() {
        return cards;
    }

    // SETTER AND GETTER FOR cookActive
    public void setCookActive(boolean cookActive) {
        this.cookActive = cookActive;
    }

    public boolean isCookActive() {
        return cookActive;
    }

    // SETTER AND GETTER FOR messengerActive
    public void setMessengerActive(boolean messengerActive) {
        this.messengerActive = messengerActive;
    }

    public boolean isMessengerActive() {
        return messengerActive;
    }

    // SETTER AND GETTER FOR centaurActive
    public void setCentaurActive(boolean centaurActive) {
        this.centaurActive = centaurActive;
    }

    public boolean isCentaurActive() {
        return centaurActive;
    }

    // SETTER AND GETTER FOR ambassadorActive
    public void setAmbassadorActive(boolean ambassadorActive) {
        this.ambassadorActive = ambassadorActive;
    }

    public boolean isAmbassadorActive() {
        return ambassadorActive;
    }

    // SETTER AND GETTER FOR knightActive
    public void setKnightUser (int knightUser) {
        this.knightUser = knightUser;
    }

    public int getKnightUser() {
        return knightUser;
    }
}
