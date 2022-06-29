package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class represents a miniature of the model.
 */
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
     * This integer is the ordervalue (1-10) of the last assistant card chosen by the player.
     */
    private int lastAssistantChosen;

    /**
     * This attribute gathers the students on the different clouds in the game.
     */
    private ArrayList <Creature> studentsOnClouds;

    /**
     * This attribute keeps saved some useful data of some characters for the match.
     */
    private CharactersDataView charactersDataView;

    /**
     * This constructor creates a new instance of the modelView.
     */
    public ModelView(int playerID){
        assistantCardsValuesPlayer = new HashMap<>();
        characterCardsInTheGame = new ArrayList<>();
        studentsOnClouds = new ArrayList<>();
        charactersDataView = new CharactersDataView();

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
        coinPlayer = new HashMap<>();


        for(int i = 0; i<= 2;i++){
            coinPlayer.put(i,1);
        }


    }

    public HashMap<Integer, Integer> getAssistantCardsValuesPlayer() {
        return assistantCardsValuesPlayer;
    }


    public ArrayList<String> getCharacterCardsInTheGame() {
        return characterCardsInTheGame;
    }

    public void setCharacterCardsInTheGame(ArrayList<String> characterCardsInTheGame) {
        this.characterCardsInTheGame = characterCardsInTheGame;
    }
    public int getCoinGame() {
        return coinGame;
    }

    public void setCoinGame(int coinGame) {
        this.coinGame = coinGame;
    }


    public int getNumberOfPlayersGame() {
        return numberOfPlayersGame;
    }

    public void setNumberOfPlayersGame(int numberOfPlayersGame) {
        this.numberOfPlayersGame = numberOfPlayersGame;
    }


    public HashMap<Integer, SchoolBoardView> getSchoolBoardPlayers() {
        return schoolBoardPlayers;
    }
    public  void setExpertModeGame(boolean expertModeGame) {
        this.expertModeGame = expertModeGame;
    }

    public boolean isExpertModeGame() {
        return expertModeGame;
    }

    public  ArrayList<ArchipelagoView> getIslandGame() {
        return islandGame;
    }

    public void setIslandGame(ArrayList<ArchipelagoView> islandGame) {
        this.islandGame = islandGame;
    }

    public  HashMap<Integer, Integer> getCoinPlayer() {
        return coinPlayer;
    }

    public void setCoinPlayer(HashMap<Integer, Integer> coinPlayer) {
        this.coinPlayer = coinPlayer;
    }


    public int getLastAssistantChosen() {
        return lastAssistantChosen;
    }

    public void setLastAssistantChosen(int lastAssistantChosen) {
        this.lastAssistantChosen = lastAssistantChosen;
    }

    public ArrayList<Creature> getStudentsOnClouds() {
        return studentsOnClouds;
    }

    /**
     * This method returns the student on the selected cloud.
     * @param cloudIDSelected is the ID of the cloud selected.
     * @return the student on the selected cloud.
     */
    public ArrayList<Creature> getStudentsOnSelectedCloud(int cloudIDSelected) {
        ArrayList<Creature> studentsOnTheSelectedCloud = new ArrayList<>();
        if(numberOfPlayersGame == 2) {
            if(cloudIDSelected == 1) {
                for (int i = 0; i < 3; i++) {
                    studentsOnTheSelectedCloud.add(this.studentsOnClouds.get(i));
                }
            }else if(cloudIDSelected == 2) {
                for (int i = 3; i < 6; i++) {
                    studentsOnTheSelectedCloud.add(this.studentsOnClouds.get(i));
                }
            }
        }else if(numberOfPlayersGame == 3) {
            if (cloudIDSelected == 1) {
                for (int i = 0; i < 4; i++) {
                    studentsOnTheSelectedCloud.add(this.studentsOnClouds.get(i));
                }
            } else if (cloudIDSelected == 2) {
                for (int i = 4; i < 8; i++) {
                    studentsOnTheSelectedCloud.add(this.studentsOnClouds.get(i));
                }
            } else if (cloudIDSelected == 3) {
                for (int i = 8; i < 12; i++) {
                    studentsOnTheSelectedCloud.add(this.studentsOnClouds.get(i));
                }
            }
        }
        return studentsOnTheSelectedCloud;
    }

    public void setStudentsOnClouds(ArrayList<Creature> studentsOnClouds) {
        this.studentsOnClouds = studentsOnClouds;
    }

    public CharactersDataView getCharactersDataView() {
        return charactersDataView;
    }

    public void setCharactersDataView(CharactersDataView charactersDataView) {
        this.charactersDataView = charactersDataView;
    }
}
