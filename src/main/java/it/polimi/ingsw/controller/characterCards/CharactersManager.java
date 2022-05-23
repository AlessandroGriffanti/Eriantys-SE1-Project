package it.polimi.ingsw.controller.characterCards;

import java.util.ArrayList;
import java.util.Random;

public class CharactersManager {
    /**
     * This attribute is the list of chosen characters for the match (references to the Character(s))
     */
    private ArrayList<Character> characterCards;
    /**
     * This attribute is the list of name associated with each Character chosen
     */
    private ArrayList<String> charactersName;
    /**
     * This attribute is the list of boolean that tell if a particular Character has been already used
     */
    private ArrayList<Boolean> charactersUsed;

    public CharactersManager() {
        this.characterCards = new ArrayList<Character>();
        this.charactersName = new ArrayList<String>();

        this.charactersUsed = new ArrayList<Boolean>();
        charactersUsed.add(false);
        charactersUsed.add(false);
        charactersUsed.add(false);

    }

    /**
     * This method chooses randomly, between all the available characters, only 3 of them
     * @return list of names associated with characters chosen in the exact order of choice
     */
    public ArrayList<String> chooseCharacter(){

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
                        characterCards.add(new Monk());
                        charactersName.add("monk");
                        break;
                    case 2:
                        characterCards.add(new Cook());
                        charactersName.add("cook");
                        break;
                    case 3:
                        characterCards.add(new Ambassador());
                        charactersName.add("ambassador");
                        break;
                    case 4:
                        characterCards.add(new Messenger());
                        charactersName.add("messenger");
                        break;
                    case 5:
                        characterCards.add(new Herbalist());
                        charactersName.add("herbalist");
                        break;
                    case 6:
                        characterCards.add(new Centaur());
                        charactersName.add("centaur");
                        break;
                    case 7:
                        characterCards.add(new Jester());
                        charactersName.add("jester");
                        break;
                    case 8:
                        characterCards.add(new Knight());
                        charactersName.add("knight");
                        break;
                    case 9:
                        characterCards.add(new MushroomMerchant());
                        charactersName.add("mushroomMerchant");
                        break;
                    case 10:
                        characterCards.add(new Bard());
                        charactersName.add("bard");
                        break;
                    case 11:
                        characterCards.add(new Princess());
                        charactersName.add("princess");
                        break;
                    case 12:
                        characterCards.add(new Trafficker());
                        charactersName.add("trafficker");
                        break;
                }
            }
        }

        return charactersName;
    }


   /* public void useCharacter(int index){
        int priceCharacterSelected;
        int coinsOwned;

        this.charactersUsed.set(index, true);

         //coin usage
         priceCharacterSelected = this.characterCards.get(index).getPrice();

         if(charactersUsed.get(index)) {
             priceCharacterSelected = priceCharacterSelected + 1;
         }

         try {
             if (p.getCoinsOwned() >= priceCharacterSelected) {
                 coinsOwned = p.getCoinsOwned() - priceCharacterSelected;
                 p.setCoinsOwned(coinsOwned);
             }
         } catch (Exception e) {
             //System.out.println("Not enough money!");
         }

         characterCards.get(index).effect();

    }*/

}
