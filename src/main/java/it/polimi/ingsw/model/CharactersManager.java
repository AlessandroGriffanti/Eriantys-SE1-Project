package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Random;

public class CharactersManager {
    private HashMap<Character, Boolean> characters;
    private ArrayList characterList;


    public CharactersManager() {
        this.characters= new HashMap<Character, Boolean>();
        Random randomGeneratorCharacter;
        int randomIndexCharacter, i;
        int old1, old2;
        Character x = null;
        Character character0 = null;
        Character character1 = null;
        Character character2 = null;

        randomGeneratorCharacter = new Random();
        old1 = 14;
        old2 = 14;
        for(i = 0; i < 3; i++) {
            do{
                randomIndexCharacter = randomGeneratorCharacter.nextInt(13);
            }while (randomIndexCharacter == old1 || randomIndexCharacter == old2);

            switch (randomIndexCharacter) {
                case 1:
                    x = new Monk();
                    break;
                case 2:
                    x = new Cook();
                    break;
                case 3:
                    x = new PizzaFlag();
                    break;
                case 4:
                    x = new PiedPiper();
                    break;
                case 5:
                    x = new FruitSeller();
                    break;
                case 6:
                    x = new Centaur();
                    break;
                case 7:
                    x = new Joker();
                    break;
                case 8:
                    x = new Knight();
                    break;
                case 9:
                    x = new MushroomFellow();
                    break;
                case 10:
                    x = new Bard();
                    break;
                case 11:
                    x = new Princess();
                    break;
                case 12:
                    x = new DrugDealer();
                    break;
            }
            if (i == 0) {
                character0 = x;
            } else if (i == 1) {
                old1 = randomIndexCharacter;
                character1 = x;
            } else if (i == 2) {
                old2 = randomIndexCharacter;
                character2 = x;
            }
        }

        characters.put(character0, false);
        characters.put(character1, false);
        characters.put(character2, false);
    }

    public void useCharacter(int index){
        if(index == 0){
            this.characters.replace();
        }else if(index == 1){

        }else if(index == 2){

        }
    }

}
