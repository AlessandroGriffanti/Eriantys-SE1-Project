package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.SchoolBoard;

import java.util.ArrayList;
import java.util.HashMap;

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
    private int numberOfPlayersGame;
    private boolean expertModeGame;
    private HashMap<Integer, Assistant> deckPlayer; //integer rappresenta l'order-value, Assistant contiene lo stesso order value e mothernature value, magari possiamo toglierla visto cjhe ha già scelto il mago

    /**
     * This arraylist gathers all the archipelagos in the game.
     */

    private ArrayList <ArchipelagoView> islandGame = new ArrayList<>();
    /**
     * This HashMap gathers the SchoolBoard (a miniature) of all players.
     * The key is the playerID; the value is the SchoolBoardView indeed.
     */
    private HashMap<Integer, SchoolBoardView> schoolBoardPlayers;

    //private ArrayList<Archipelago> islandsGame = new ArrayList<>();

    private ArrayList <Integer> assistantCardsValuesPlayer;


    private ArrayList<String> characterCardsInTheGame;
    /**
     * This constructor creates a new instance of the modelView.
     */
    public ModelView(){
        coinPlayer = new HashMap<>();
        assistantCardsValuesPlayer = new ArrayList<>();
        characterCardsInTheGame = new ArrayList<>();
        for(int i = 1; i<= 10; i++){                        //creo le carte assistente
            assistantCardsValuesPlayer.add(i);
        }

        for(int i = 0; i<12; i++){                         //creo le isole e le aggiungo all'arraylist
            islandGame.add(new ArchipelagoView(i));
        }

        schoolBoardPlayers = new HashMap<>();


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
    public synchronized ArrayList<Integer> getAssistantCardsValuesPlayer() {
        return assistantCardsValuesPlayer;
    }

    public synchronized void setAssistantCardsValuesPlayer(ArrayList<Integer> assistantCardValuePlayer) {
        this.assistantCardsValuesPlayer = assistantCardValuePlayer;
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
