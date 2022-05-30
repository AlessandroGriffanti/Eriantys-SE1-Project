package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelView {
    private int CoinGame;
    private int numberOfPlayersGame;
    private boolean expertModeGame;
    //private HashMap <Integer, Creature> entranceGame; //Integer è l'id del player,
    private ArrayList <Creature> entrancePlayer;
    private int towerPlayer;
    private HashMap<Creature, Integer> diningRoomPlayer;
    private HashMap<Integer, Assistant> deckPlayer; //integer rappresenta l'order-value, Assistant contiene lo stesso order value e mothernature value, magari possiamo toglierla visto cjhe ha già scelto il mago
    private ArrayList <Archipelago> islandGame = new ArrayList<>();
    private int motherNaturePositionGame;

    private ArrayList <Integer> assistantCardsValuesPlayer;


    public ModelView(){ //qui dobbiamo avere il primo messaggio di match start per inizializzare le cose come argomento del costruttore
        assistantCardsValuesPlayer = new ArrayList<>();
        for(int i = 1; i<= 10; i++){
            assistantCardsValuesPlayer.add(i);
        }


    }


    public ArrayList<Integer> getAssistantCardsValuesPlayer() {
        return assistantCardsValuesPlayer;
    }

    public void setAssistantCardsValuesPlayer(ArrayList<Integer> assistantCardValuePlayer) {
        this.assistantCardsValuesPlayer = assistantCardValuePlayer;
    }
}
