package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.SchoolBoard;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelView {
    private int coinGame;
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

    private ArrayList<Archipelago> islandsGame = new ArrayList<>();

    private ArrayList <Integer> assistantCardsValuesPlayer;


    private ArrayList<String> characterCardsInTheGame;
    /**
     * This constructor creates a new instance of the modelView.
     */
    public ModelView(){
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
    public int getCoinGame() {
        return coinGame;
    }

    public void setCoinGame(int coinGame) {
        this.coinGame = coinGame;
    }


    public synchronized int getNumberOfPlayersGame() {
        return numberOfPlayersGame;
    }

    public synchronized void setNumberOfPlayersGame(int numberOfPlayersGame) {
        this.numberOfPlayersGame = numberOfPlayersGame;
    }
    public ArrayList<Integer> getAssistantCardsValuesPlayer() {
        return assistantCardsValuesPlayer;
    }

    public void setAssistantCardsValuesPlayer(ArrayList<Integer> assistantCardValuePlayer) {
        this.assistantCardsValuesPlayer = assistantCardValuePlayer;
    }

    public HashMap<Integer, SchoolBoardView> getSchoolBoardPlayers() {
        return schoolBoardPlayers;
    }
    public synchronized void setExpertModeGame(boolean expertModeGame) {
        this.expertModeGame = expertModeGame;
    }

    public synchronized boolean isExpertModeGame() {
        return expertModeGame;
    }

    public ArrayList<ArchipelagoView> getIslandGame() {
        return islandGame;
    }

    public void setIslandGame(ArrayList<ArchipelagoView> islandGame) {
        this.islandGame = islandGame;
    }


}
