package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.VirtualView;
import java.util.ArrayList;


public class Match {
    /**
     * This attribute is the ID that identifies the match between all matches
     */
    private int ID;
    /**
     * This attribute is the number of players required for the match to begin
     */
    private int numberOfPlayers;
    /**
     * This attribute is the realm of the match
     */
    private Realm realmOfTheMatch;
    /**
     * This attribute is the bag containing all the students not yet used
     */
    private Bag bagOfTheMatch;
    /**
     * This attribute is the list of player joining the match
     */
    private ArrayList<Player> players;
    /**
     * This attribute is the ID of the current player
     */
    private int currentPlayer;
    /**
     * This attribute is the coin reserve/vault containing all the coins that no player owns
     */
    private int coinReserve;
    /**
     * This attribute is the virtual view behaving as an observer of the Model.
     * It will be called after changes inside the Model
     */
    private VirtualView virtualView;
    /**
     * This attribute memorizes the professors that are not already controlled by any of the players
     */
    private ArrayList<Creature> notControlledProfessors;
    /**
     * This attribute tells us if the match is expert mode or not
     */
    private boolean expertMode;


    public Match(int ID, int numberOfPlayers, VirtualView vv, boolean expertMode) {
        this.ID = ID;
        this.numberOfPlayers = numberOfPlayers;
        this.bagOfTheMatch = new Bag();
        this.realmOfTheMatch = new Realm(numberOfPlayers, bagOfTheMatch);
        this.players = new ArrayList<Player>();
        this.coinReserve = 20; //expert mode
        this.virtualView = vv;

        this.notControlledProfessors = new ArrayList<Creature>();
        for(Creature c: Creature.values()){
            notControlledProfessors.add(c);
        }

        this.expertMode = expertMode;
    }


    /**
     *This method add one player to the match
     * @param nickname name of the player
     */
    public void addPlayer(String nickname){
        Player p;

        p = new Player(players.size(), nickname, numberOfPlayers, realmOfTheMatch);
        this.coinReserve = this.coinReserve - 1;
    }

    public int getNumberOfPlayers() {
        //this.numberOfPlayers = getPlayers().size();
        return numberOfPlayers;
    }

    /**
     * This method sets the current Player to the one identified by the ID passed as argument
     * @param ID_currentPlayer ID of the 'new' current player
     */
    public void setCurrentPlayer(int ID_currentPlayer) {
        this.currentPlayer = ID_currentPlayer;
    }


    /**
     * This method searches the player by his nickName in the list of players
     * @param nickName the nickName of the player
     * @return Player - it's the searched player. If null, there is no player with that nickName
     */
    public Player getPlayerByNickname(String nickName){
        for(Player player : players){
            if(player.getNickName().equalsIgnoreCase(nickName)){
                return player;
            }
        }
        return null;
    }

    /**
     * This method searches the player by his ID in the list of players
     * @param ID the ID of the player
     * @return Player. If null, there is no player with that nickName
     */
    public Player getPlayerByID(int ID){
        for(Player player : players){
            if(player.getPlayerID() == ID){
                return player;
            }
        }
        return null;
    }

    /**
     *
     */
    public void moveStudentsFromBagToCloudsEveryRound(){
        getRealmOfTheMatch().moveStudentsToClouds();
    }

    public void moveStudentsFromCloudToEntrance(int cloud_ID){
        //calls the Realm to take the students on the cloud
        ArrayList<Creature> takenStudents = this.realmOfTheMatch.takeStudentsFromCloud(cloud_ID);

        //adds the students to the current player's entrance
        players.get(currentPlayer).getPlayerSchoolBoard().getEntrance().addMultipleStudents(takenStudents);
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
