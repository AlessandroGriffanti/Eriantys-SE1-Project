package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.model.schoolboard.Entrance;
import it.polimi.ingsw.model.schoolboard.ProfessorTable;

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
        Player p = new Player(players.size(), nickname, numberOfPlayers, realmOfTheMatch);
        players.add(p);
        this.coinReserve = this.coinReserve - 1;
    }

    public int getNumberOfPlayers() {
        //this.numberOfPlayers = getPlayers().size();
        return numberOfPlayers;
    }

    public int getPositionOfMotherNature(){
        return realmOfTheMatch.getPositionOfMotherNature();
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
            if(player.getID() == ID){
                return player;
            }
        }
        return null;
    }

    /**
     *
     */
    public ArrayList<Creature> moveStudentsFromBagToCloudsEveryRound(){
        return getRealmOfTheMatch().moveStudentsToClouds();
    }

    public void moveStudentsFromCloudToEntrance(int cloud_ID){
        //calls the Realm to take the students on the cloud
        ArrayList<Creature> takenStudents = this.realmOfTheMatch.takeStudentsFromCloud(cloud_ID);

        //adds the students to the current player's entrance
        players.get(currentPlayer).getSchoolBoard().getEntrance().addMultipleStudents(takenStudents);
    }

    /**
     * This method moves one student from the entrance to the dining room of the current player
     * @param student_ID the ID used to identify the student inside th entrance
     */
    public void moveStudentFromEntranceToDiningRoom(int student_ID){
        Entrance entrance = players.get(currentPlayer).getSchoolBoard().getEntrance();
        entrance.moveStudentToDiningRoom(student_ID);
    }

    /**
     * This method moves one student from the entrance of the current player to an island
     * @param student_ID the ID used to identify the student inside th entrance
     * @param island_ID the ID of the island where to move the student
     */
    public  void moveStudentFromEntranceToIsland(int student_ID, int island_ID){
        players.get(currentPlayer).getSchoolBoard().getEntrance().moveStudentToIsland(student_ID, island_ID);
    }
    /**
     * This method notifies the current Player to set its own assistant deck
     * @param wizard assistants' deck chosen by the player
     */
    public void playerChoosesDeck(Wizard wizard){
        players.get(currentPlayer).chooseDeck(wizard);
    }

    /**
     * This method sets the tower color of the current player
     * @param color color chosen by the player
     */
    public void playerChoosesTowerColor(Tower color){
        players.get(currentPlayer).setTowerColor(color);
    }

    /**
     * This method calls the Player instance to use the assistant card chosen by the player
     */
    public void useCard(int orderValueOfTheCard){
        this.players.get(currentPlayer).useAssistantCard(orderValueOfTheCard);
    }

    /**
     * This method asks the player identified by the playerID how many assistant cards are left in its deck
     * @param playerID ID of the player
     * @return number of remaining cards
     */
    public int numberOfRemainingCardsOfPlayer(int playerID){
        return players.get(playerID).numberOfRemainingAssistantCards();
    }

    /**
     * This method takes the professor from the player specified, effectively taking away his control
     * over the professor.
     * @param player_ID the player who will lose the professor
     * @param professor the kind of professor that will be taken away from the player
     */
    public void looseControlOnProfessor(int player_ID, Creature professor){
        ProfessorTable table = players.get(player_ID).getSchoolBoard().getProfessorTable();
        table.removeProfessor(professor);
    }

    /**
     * This method gives the control on the specified professor to the current player and
     * remove it from the notControlledProfessors list if it is still there
     * @param professor the kind of professor the current player will take control on
     */
    public void acquireControlOnProfessor(Creature professor){
        if(notControlledProfessors.contains(professor)){
            notControlledProfessors.remove(professor);
        }
        players.get(currentPlayer).getSchoolBoard().getProfessorTable().addProfessor(professor);
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

    public boolean isExpertMode(){
        return expertMode;
    }

    public ArrayList<Creature> getNotControlledProfessors() {
        return notControlledProfessors;
    }
}
