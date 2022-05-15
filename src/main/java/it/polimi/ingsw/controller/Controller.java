package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.messages.MatchStartMessage;
import it.polimi.ingsw.network.messages.MatchWaitingMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.Random;

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
     * This attribute is the number of players needed to start the match
     */
    private int numberOfPlayers;
    /**
     * This attribute tells if the match must ber played in expert mode or not
     */
    private boolean expertMode;
    /**
     * This attribute is the identifier of the match
     */
    private final int ID;
    /**
     * This attribute is the last message received from the client
     */
    private String msg_in = null;
    /**
     * This attribute is an array of references to all the ClientHandler of the players playing this match
     */
    private ArrayList<ClientHandler> clientHandlers = null;
    /**
     * This attribute is the list of names chosen by the player
     */
    private ArrayList<String> playersNickname = null;
    /**
     * This attribute is the counter of players added to match
     */
    private int playersAddedCounter;
    /**
     * This attribute represents the playing order of the action phase;
     * it was determined inside the 'ChooseAssistantCard' state
     */
    private ArrayList<Integer> actionPhaseOrder;


    private int actualNumberOfPlayers;
    /**
     * This attribute tells if there is one or more players disconnected from the game
     * We control its value in order to return the number of players that are actually playing the match
     *      true -> actualNumberOfPlayers
     *      false -> numberOfPlayers
     */
    private boolean isSomePlayerDisconnected;

    /**
     * Controller constructor
     * @param ID the ID of the match
     */
    public Controller(int ID){
        this.ID = ID;
        this.playing = false;
        this.clientHandlers = new ArrayList<ClientHandler>();
        this.playersNickname = new ArrayList<String>();
        this.actionPhaseOrder = new ArrayList<Integer>();
        this.state = new MatchCreating();
        this.playersAddedCounter = 0;
        isSomePlayerDisconnected = false;
    }

    /**
     * This method adds a player to the match and controls if there are enough players to start playing.
     * Furthermore, it sends the notify message "MatchWaiting" to the player that is just been added
     * @param playerHandler reference to the ClientHandler of the added player
     * @param nickname nickname chosen by the player (client) and approved by the ClientHandler
     */
    public void addPlayerHandler(ClientHandler playerHandler, String nickname){
        this.clientHandlers.add(playerHandler);
        this.playersNickname.add(nickname);

        playersAddedCounter++;

        /*while for the first player, that is the one who chose the match settings, the notify message "MatchWaiting" is
        sent from the MatchCreation state execution, we must notify the other players too; here we send the message to the
        added player*/
        if(playersAddedCounter > 1){
            sendMessageToPlayer(playersAddedCounter, new MatchWaitingMessage());
        }

        if(playersAddedCounter == numberOfPlayers){
            playing = true;
            startMatch();
        }
    }
    /**
     * This method lets the match start, creates the view and chooses the first player of the match randomly; finally it
     * sends the message to the ClientHandlers
     */
    public void startMatch(){
        //Creates the virtualView so that it can notify the client (client side)
        VirtualView vv = new VirtualView(this.match);
        this.match = new Match(this.ID, this.numberOfPlayers, vv, expertMode);
        //adds all the players
        for(String s: playersNickname){
            this.match.addPlayer(s);
        }

        //chooses the first player of the match
        Random random = new Random();
        int firstPlayer_ID = random.nextInt(numberOfPlayers);

        //set the current player inside the model
        match.setCurrentPlayer(firstPlayer_ID);

        //prepares MatchStart message for all the clients
        MatchStartMessage msg = new MatchStartMessage(firstPlayer_ID);
        sendMessageAsBroadcast(msg);

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
        state.stateExecution(this);
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

    public Match getMatch(){
        return this.match;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        this.actualNumberOfPlayers = numberOfPlayers;
    }

    /**
     * This method decrease of 1 the number of actual playing players , this is because it was disconnected
     * @param playerID ID of the player who disconnected
     */
    public void onePlayerDisconnected(int playerID){
        this.numberOfPlayers--;
    }

    /**
     * This method increases of 1 the number of players because one player reconnected to match
     * @param playerID ID of the player who reconnected
     */
    public void onePlayerReconnected(int playerID){
        this.numberOfPlayers++;
    }

    /**
     * This method computes the ID of the next player to make his move
     * @param currentPlayer ID of the current player
     * @return ID of the next player
     */
    public int nextPlayer(int currentPlayer){
        return ((currentPlayer + 1) % numberOfPlayers);
    }

    public int getMatchID(){
        return this.ID;
    }

    public String getMsg(){
        return msg_in;
    }

    public boolean getPlayingStatus(){
        return playing;
    }

    public void setExpertMode(boolean mode){
        this.expertMode = mode;
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
}
