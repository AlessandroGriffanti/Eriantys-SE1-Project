package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ModelView {
    /**
     * This attribute tracks the number of total coin the game, the reserve.
     */
    private int coinGame;

    /**
     * This attribute tracks the number of coins for each player.
     * Key: player ID, value: number of coins.
     */
    private HashMap<Integer, Integer> coinPlayer;
    /**
     * This attribute represents the total number of player in the game.
     */
    private int numberOfPlayersGame;
    /**
     * This attributes tells if the game is played in expert mode or not.
     */
    private boolean expertModeGame;

    //private HashMap<Integer, Assistant> deckPlayer; //integer rappresenta l'order-value, Assistant contiene lo stesso order value e mothernature value, magari possiamo toglierla visto cjhe ha già scelto il mago

    /**
     * This arraylist gathers all the archipelagos in the game.
     */
    private ArrayList <ArchipelagoView> islandGame = new ArrayList<>();
    /**
     * This HashMap gathers the SchoolBoard (a miniature) of all players.
     * The key is the playerID; the value is the SchoolBoardView indeed.
     */
    private HashMap<Integer, SchoolBoardView> schoolBoardPlayers;

    /**
     * This hashmap gathers all the assistant cards of the player.
     * The key is the order value, the value is the mother nature value.
     */
    private HashMap <Integer, Integer> assistantCardsValuesPlayer;

    /**
     * This attribute gathers all the 3 character cards available in the game, if played in expert mode.
     */
    private ArrayList<String> characterCardsInTheGame;

    /**
     * This constructor creates a new instance of the modelView.
     */
    public ModelView(int playerID){
        coinPlayer = new HashMap<>();
        assistantCardsValuesPlayer = new HashMap<>();
        characterCardsInTheGame = new ArrayList<>();

        for(int i = 1; i<= 10; i++){                        //creo le carte assistente con il relativo valore di madre natura
            if(i == 1 || i == 2) {
                assistantCardsValuesPlayer.put(i, 1);
            }else if( i == 3 || i == 4){
                assistantCardsValuesPlayer.put(i, 2);
            }else if(i == 5 || i == 6){
                assistantCardsValuesPlayer.put(i, 3);
            }else if(i == 7 || i == 8) {
                assistantCardsValuesPlayer.put(i, 4);
            }else if(i == 9 || i == 10) {
                assistantCardsValuesPlayer.put(i, 5);
            }

        }

        for(int i = 0; i<12; i++){                         //creo le isole e le aggiungo all'arraylist
            islandGame.add(new ArchipelagoView(i));
        }

        schoolBoardPlayers = new HashMap<>();


    }

    public synchronized HashMap<Integer, Integer> getAssistantCardsValuesPlayer() {
        return assistantCardsValuesPlayer;
    }


    public synchronized ArrayList<String> getCharacterCardsInTheGame() {
        return characterCardsInTheGame;
    }

    public synchronized void setCharacterCardsInTheGame(ArrayList<String> characterCardsInTheGame) {
        this.characterCardsInTheGame = characterCardsInTheGame;
    }
    public synchronized  int getCoinGame() {
        return coinGame;
    }

    public synchronized void setCoinGame(int coinGame) {
        this.coinGame = coinGame;
    }


    public synchronized int getNumberOfPlayersGame() {
        return numberOfPlayersGame;
    }

    public synchronized void setNumberOfPlayersGame(int numberOfPlayersGame) {
        this.numberOfPlayersGame = numberOfPlayersGame;
    }


    public synchronized HashMap<Integer, SchoolBoardView> getSchoolBoardPlayers() {
        return schoolBoardPlayers;
    }
    public synchronized void setExpertModeGame(boolean expertModeGame) {
        this.expertModeGame = expertModeGame;
    }

    public synchronized boolean isExpertModeGame() {
        return expertModeGame;
    }

    public synchronized ArrayList<ArchipelagoView> getIslandGame() {
        return islandGame;
    }

    public synchronized void setIslandGame(ArrayList<ArchipelagoView> islandGame) {
        this.islandGame = islandGame;
    }

    public synchronized HashMap<Integer, Integer> getCoinPlayer() {
        return coinPlayer;
    }

    public synchronized void setCoinPlayer(HashMap<Integer, Integer> coinPlayer) {
        this.coinPlayer = coinPlayer;
    }

}
