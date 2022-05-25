package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.model.schoolboard.Entrance;
import it.polimi.ingsw.model.schoolboard.ProfessorTable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class represent a single match of Eriantys
 */
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
     * This attribute memorizes the professors that are not already controlled by any of the players
     */
    private ArrayList<Creature> notControlledProfessors;
    /**
     * This attribute tells us if the match is expert mode or not
     */
    private boolean expertMode;


    public Match(int ID, int numberOfPlayers, boolean expertMode) {
        this.ID = ID;
        this.numberOfPlayers = numberOfPlayers;
        this.bagOfTheMatch = new Bag();
        this.realmOfTheMatch = new Realm(numberOfPlayers, bagOfTheMatch);
        this.players = new ArrayList<Player>();
        this.coinReserve = 20;

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
     * This method finds the type of a particular student in the entrance of the specified player
     * @param player_ID ID of the player
     * @param student_ID ID of the student we are interested in
     * @return type of student [Creature]
     */
    public Creature getStudentInEntranceOfPlayerByID(int player_ID, int student_ID){
        return players.get(player_ID).getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(student_ID);
    }

    /**
     *This method fills the clouds with new students taken from the bag of the match
     */
    public ArrayList<Creature> moveStudentsFromBagToCloudsEveryRound(){
        return getRealmOfTheMatch().moveStudentsToClouds();
    }

    public ArrayList<Creature> moveStudentsFromCloudToEntrance(int cloud_ID){
        Entrance entrance = players.get(currentPlayer).getSchoolBoard().getEntrance();

        //calls the Realm to take the students on the cloud
        ArrayList<Creature> takenStudents = this.realmOfTheMatch.takeStudentsFromCloud(cloud_ID);

        //adds the students to the current player's entrance
        ArrayList<Creature> newEntrance = entrance.addMultipleStudents(takenStudents);
        return newEntrance;
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
    public void moveStudentFromEntranceToIsland(int student_ID, int island_ID){
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
     * This method searches for towers on a particular island belonging to the player
     * specified
     * @param player_ID ID of the player whose towers we are looking for
     * @param island_ID ID of the island on which we are looking for the towers
     * @return number of towers found
     */
    public int numberOfTowersOnTheIsland(int player_ID, int island_ID){
        int numberOfTowers = 0;
        Archipelago island = realmOfTheMatch.getArchipelagos().get(island_ID);

        if(island.getMasterOfArchipelago() != null && players.get(player_ID).equals(island.getMasterOfArchipelago())){
            assert players.get(player_ID).getTowerColor().equals(island.getTowerColor()) :
                    "The player is the island's master but the color of the towers does not correspond.";
            numberOfTowers = island.getNumberOfIslands();
        }

        return numberOfTowers;
    }

    /**
     * This method finds out if the current island (where mother nature stands) can be unified with the previous one,
     * the next one or both of them
     * @return 0: nor the previous nor the next island can be unified with the current one
     *         1: the current island must be unified with the previous one
     *         2: the current island must be unified with next island
     *         3: the current island must be unified with both previous and next islands
     */
    public int islandUnifyControl(){
        int returnValue = 0;
        int positionOfMotherNature = getPositionOfMotherNature();
        Tower colorOnMotherNaturePosition = realmOfTheMatch.getArchipelagos().get(positionOfMotherNature).getTowerColor();

        if(colorOnMotherNaturePosition != null){
            int nextIsland_ID = realmOfTheMatch.nextIsland(positionOfMotherNature);
            Tower colorOnNextIsland = realmOfTheMatch.getArchipelagos().get(nextIsland_ID).getTowerColor();

            int previousIsland_ID = realmOfTheMatch.previousIsland(positionOfMotherNature);
            Tower colorOnPreviousIsland = realmOfTheMatch.getArchipelagos().get(previousIsland_ID).getTowerColor();

            if(colorOnMotherNaturePosition.equals(colorOnPreviousIsland)){
                returnValue = 1;
            }

            if(colorOnMotherNaturePosition.equals(colorOnNextIsland)){
                if(returnValue == 1){
                    returnValue = 3;
                }else{
                    returnValue = 2;
                }
            }
        }

        return returnValue;
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

    /**
     * This method set a new master on the current island where mother nature is
     * @param playerMaster_ID ID of the new master
     * @return true if the match must end because the player has no more towers left
     *         false if the match can continue
     */
    public boolean newMasterOnIsland(int playerMaster_ID){
        // who is the new master ?
        Player newMaster = players.get(playerMaster_ID);
        // take the current island
        Archipelago currentIsland = realmOfTheMatch.getArchipelagos().get(getPositionOfMotherNature());
        // set new master
        boolean endOfMatch = currentIsland.setMasterOfArchipelago(newMaster);

        return endOfMatch;
    }

    public int getPositionOfMotherNature(){
        return realmOfTheMatch.getPositionOfMotherNature();
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

    /**
     * This method put a certain amount of coins in the coin reserve
     * @param quantity number of coins to deposit in the  coin reserve
     * @return number of coins actually in the coin reserve
     */
    public int depositInCoinReserve(int quantity){
        coinReserve += quantity;

        return coinReserve;
    }

    public int getCoinReserve() {
        return coinReserve;
    }
}
