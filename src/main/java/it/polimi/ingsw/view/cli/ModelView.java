package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.SchoolBoard;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelView {
    private int CoinGame;
    private int numberOfPlayersGame = 0;

    private boolean expertModeGame;
    private HashMap<Integer, Assistant> deckPlayer; //integer rappresenta l'order-value, Assistant contiene lo stesso order value e mothernature value, magari possiamo toglierla visto cjhe ha già scelto il mago
    private ArrayList <Archipelago> islandGame = new ArrayList<>();
    private int motherNaturePositionGame;

    /**
     * This HashMap gathers the SchoolBoard (a miniature) of all players.
     * The key is the playerID; the value is the SchoolBoardView indeed.
     */
    private HashMap<Integer, SchoolBoardView> schoolBoardPlayers;

    private ArrayList <Integer> assistantCardsValuesPlayer;


    public ModelView(){
        assistantCardsValuesPlayer = new ArrayList<>();
        for(int i = 1; i<= 10; i++){
            assistantCardsValuesPlayer.add(i);
        }

        schoolBoardPlayers = new HashMap<>();


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


}
