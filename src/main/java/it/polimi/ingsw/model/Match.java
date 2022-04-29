package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Match {
    private ArrayList<Player> players;
    private Bag bagOfTheMatch;
    private Realm realmOfTheMatch;

    public int getNumberOfPlayers() {
        int numberOfPlayers;
        numberOfPlayers = getPlayers().size();
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
