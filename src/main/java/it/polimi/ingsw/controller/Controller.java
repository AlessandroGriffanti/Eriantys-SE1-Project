package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.characterCards.CharactersManager;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.serverMessages.MatchStartMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Controller {
    /**
     * This attribute tells if the match has already started (no more player allowed in) -> [1]
     * or if it's waiting for other players to join -> [0]
     */
    private boolean playing;
    /**
     * This attribute is the current state of the match
     */
    private ControllerState state = null;
    /**
     * This attribute is the reference to the Model of the match
     */
    private Match match = null;
    /**
     * This attribute is the reference to the characters' manager of the match
     */
    private CharactersManager charactersManager = null;
    /**
     * This attribute is the number of players needed to start the match
     */
    private int numberOfPlayers = 0;
    /**
     * This attribute tells if the match must ber played in expert mode or not
     */
    private boolean expertMode;
    /**
     * This attribute is the identifier of the match
     */
    private final int match_ID;
    /**
     * This attribute is the last message received from the client
     */
    private String msg_in = null;
    /**
     * This attribute is an array of references to all the ClientHandler of the players playing this match
     */
    private ArrayList<ClientHandler> clientHandlers;
    /**
     * This attribute is the list of names chosen by the player
     */
    private ArrayList<String> playersNickname;
    /**
     * This attribute is the counter of players added to match
     */
    private int playersAddedCounter;
    /**
     * This attribute represents the playing order of the action phase;
     * it was determined inside the 'ChooseAssistantCard' state
     */
    private ArrayList<Integer> actionPhaseOrder;
    /**
     * This attribute says if we are playing the action phase (TRUE) or the planning phase (FALSE);
     * it's used in the 'nextPlayer()' method
     */
    private boolean actionPhase;
    /**
     * This attribute is the ID of the current player playing its action phase
     */
    private int actionPhaseCurrentPlayer = -1;
    /**
     * This attribute is the list of disconnected players where the indexes correspond to the IDs of the players
     */
    private ArrayList<Boolean> playersDisconnected;
    /**
     * This attribute is set to true if the match is ended and false otherwise
     */
    private boolean matchEnded;

    /**
     * Controller constructor
     * @param ID the ID of the match
     */
    public Controller(int ID){
        this.match_ID = ID;
        this.playing = false;
        this.clientHandlers = new ArrayList<ClientHandler>();
        this.playersNickname = new ArrayList<String>();
        this.actionPhaseOrder = new ArrayList<Integer>();
        this.state = new MatchCreating();
        this.playersAddedCounter = 0;
        this.actionPhase = false;

        this.playersDisconnected = new ArrayList<Boolean>();

        matchEnded = false;
    }

    /**
     * This method adds a player to the match and controls if there are enough players to start playing.
     * Furthermore, it sends the notify message "MatchWaiting" to the player that is just been added
     * @param playerHandler reference to the ClientHandler of the added player
     * @param nickname nickname chosen by the player (client) and approved by the ClientHandler
     * @return ID of the just added player
     */
    public int addPlayerHandler(ClientHandler playerHandler, String nickname){
        this.clientHandlers.add(playerHandler);
        this.playersNickname.add(nickname);

        playersAddedCounter++;

        /*while for the first player, that is the one who chose the match settings, the notification message "MatchWaiting" is
        sent from the MatchCreation state execution, we must notify the other players too; here we send the message to the
        added player*/
        if(playersAddedCounter > 1){
            AckMessage ack = new AckMessage();
            ack.setSubObject("waiting");
            sendMessageToPlayer(playersAddedCounter-1, ack);
        }

        if(playersAddedCounter == numberOfPlayers){
            startMatch();
        }

        return clientHandlers.size() - 1;
    }
    /**
     * This method lets the match start, chooses the first player of the match randomly, and finally it
     * sends the message to all the players through the ClientHandlers
     */
    public void startMatch(){

        this.match = new Match(this.match_ID, this.numberOfPlayers, expertMode);
        //adds all the players
        for(String s: playersNickname){
            this.match.addPlayer(s);
        }

        //chooses the first player of the match
        Random random = new Random();
        int firstPlayer_ID = random.nextInt(numberOfPlayers);

        //set the current player inside the model
        match.setCurrentPlayer(firstPlayer_ID);

        // get initial position of mother nature
        int motherNatureInitialPosition = match.getPositionOfMotherNature();

        // create the startOfMatch message
        MatchStartMessage startMessage = new MatchStartMessage(firstPlayer_ID, motherNatureInitialPosition);
        // add characters if the match will be played in expert mode
        if(expertMode){
            this.charactersManager = new CharactersManager(this);
            Set<String> characters = charactersManager.chooseCharacter();
            startMessage.setCharacters(characters);
        }
        // add the students in the entrance of each player
        for(int i = 0; i < clientHandlers.size(); i++){
            ArrayList<Creature> students = match.getPlayerByID(i).getSchoolBoard().getEntrance().getStudentsInTheEntrance();
            startMessage.setStudentsInEntrance(i, students);
        }

        sendMessageAsBroadcast(startMessage);
        playing = true;
        nextState();
    }

    /**
     * This methode sends a message to one particular player
     * @param player_ID ID of the addressee
     * @param msg msg that must be sent to the player
     */
    public void sendMessageToPlayer(int player_ID, Message msg){
        ClientHandler addressee = clientHandlers.get(player_ID);
        addressee.messageToSerialize(msg);
    }

    /**
     * This method sends a message to all players of the match
     * @param msg the message that must be sent to the players
     */
    public void sendMessageAsBroadcast(Message msg){
        for(ClientHandler p: clientHandlers){
            p.messageToSerialize(msg);
        }
    }

    /**
     * This method hands the message received by the client over to the state
     * @param msg message sent by the client
     */
    public void manageMsg(String msg){
        msg_in = msg;

        // the state will control the type of message and will execute the actions required
        state.controlMessageAndExecute(this);
    }

    /**
     * This method calls the current state asking for the next one
     */
    public void nextState(){
        state.nextState(this);
    }

    /**
     * This method sets the new state of the controller; it called exclusively by the current state of the controller.
     * controller.nextState() --> state.nextState() --> controller.setState()
     * @param cs the new state of the controller
     */
    public void setState(ControllerState cs){
        this.state = cs;
    }

    /**
     * This method decrease of 1 the number of actual playing players , this is because it was disconnected.
     * Then it controls if there are more than 1 players connected, if it's not so the match can end, so
     * the methode notifies the clientHandlers end calls the end of the match.
     * @param playerID ID of the player who disconnected
     */
    public void onePlayerDisconnected(int playerID){
        playersDisconnected.set(playerID, true);

        int numberOfPlayerDisconnected = 0;

        for(boolean i: playersDisconnected){
            if(i){
                numberOfPlayerDisconnected++;
            }
        }

        if(numberOfPlayerDisconnected == numberOfPlayers){
            //TODO: notify the clientHandler that the match can end
        }else if(numberOfPlayerDisconnected == numberOfPlayers - 1){
            int lastPlayer_ID = -1;

            for(int i = 0; i < numberOfPlayers; i++){
                if(!playersDisconnected.get(i)){
                    assert lastPlayer_ID == -1 : "There should be only one player still connected";
                    lastPlayer_ID = i;
                }
            }
            SupportFunctions.endMatch(this, "disconnection", lastPlayer_ID);
        }
    }

    /**
     * This method increases of 1 the number of players because one player reconnected to match
     * @param playerID ID of the player who reconnected
     */
    public void onePlayerReconnected(int playerID){
        playersDisconnected.set(playerID, false);
    }

    /**
     * This method computes the ID of the next player to make his move
     * @param currentPlayer ID of the current player
     * @return ID of the next player
     */
    public int nextPlayer(int currentPlayer){
        int nextPlayerID;

        if(actionPhase){
            int indexOfCurrentPlayer = actionPhaseOrder.indexOf(currentPlayer);
            int i = 1;

            while(playersDisconnected.get(actionPhaseOrder.get((indexOfCurrentPlayer + i) % numberOfPlayers))){
                i++;
            }
            nextPlayerID = actionPhaseOrder.get((indexOfCurrentPlayer + 1) % numberOfPlayers);

        }else{
            nextPlayerID = ((currentPlayer + 1) % numberOfPlayers);
        }

        return nextPlayerID;
    }

    public int getMatchID(){
        return this.match_ID;
    }

    public String getMsg(){
        return msg_in;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean getPlayingStatus(){
        return playing;
    }

    public void setExpertMode(boolean mode){
        this.expertMode = mode;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void setActionPhaseOrder(ArrayList<Integer> actionPhaseOrder) {
        this.actionPhaseOrder = actionPhaseOrder;
    }

    public ArrayList<Integer> getActionPhaseOrder() {
        return actionPhaseOrder;
    }

    public void setActionPhase(boolean actionPhase) {
        this.actionPhase = actionPhase;
    }

    public boolean isActionPhase() {
        return actionPhase;
    }

    public int getPlayersAddedCounter() {
        return playersAddedCounter;
    }

    public Match getMatch(){
        return this.match;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        // set to false the values of disconnections' array
        for(int i = 0; i < numberOfPlayers; i++){
            playersDisconnected.add(false);
        }
    }

    public void setActionPhaseCurrentPlayer(int actionPhaseCurrentPlayer) {
        this.actionPhaseCurrentPlayer = actionPhaseCurrentPlayer;
    }

    public int getActionPhaseCurrentPlayer() {
        return actionPhaseCurrentPlayer;
    }

    public ArrayList<String> getPlayersNickname() {
        return playersNickname;
    }

    public ArrayList<Boolean> getPlayersDisconnected() {
        return playersDisconnected;
    }

    public void setMatchEnded(boolean matchEnded) {
        this.matchEnded = matchEnded;
    }

    public boolean isMatchEnded() {
        return matchEnded;
    }

    public CharactersManager getCharactersManager() {
        return charactersManager;
    }
}
