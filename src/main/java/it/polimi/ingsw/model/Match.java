package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.CharactersManager;

import java.util.ArrayList;

public class Match {
    private int ID;
    private int numberOfPlayers;
    private Realm realmOfTheMatch;
    private Bag bagOfTheMatch;
    private CharactersManager charactersManager; //TODO: dobbiamo capire che fare con i caracters
    private ArrayList<Player> players;
    private Player currentPlayer;
    private int coinReserve;


    public Match(int ID, int numberOfPlayers) {
        this.ID = ID;
        this.numberOfPlayers = numberOfPlayers;
        this.bagOfTheMatch = new Bag();
        this.realmOfTheMatch = new Realm(numberOfPlayers, bagOfTheMatch);
        this.players = new ArrayList<Player>();
        this.coinReserve = 20;                                                  //expert mode
    }

    /**
     *
     * @param nickName
     */
    public void addPlayer(String nickName){
        Player p;

        p = new Player(players.size(), nickName, numberOfPlayers, realmOfTheMatch);
        this.coinReserve = this.coinReserve - 1;


    }

    /**
     *
     */
    public void moveStudentsFromBagToCloudsEveryRound(){
        getRealmOfTheMatch().moveStudentsToClouds();
    }


    public int getNumberOfPlayers() {
        //this.numberOfPlayers = getPlayers().size();
        return numberOfPlayers;
    }

    public Bag getBagOfTheMatch() {
        return bagOfTheMatch;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Realm getRealmOfTheMatch() {
        return realmOfTheMatch;
    }

}
