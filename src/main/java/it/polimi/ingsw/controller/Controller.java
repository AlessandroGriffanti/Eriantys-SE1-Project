package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.messages.MatchStart;
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
     * This attribute is the identifier of the match
     */
    private final int ID;
    /**
     * This attribute is the last message received from the client
     */
    private Message msg_in = null;
    /**
     * This attribute is an array of references to all the ClientHandler of the players playing this match
     */
    private ArrayList<ClientHandler> clientHandlers;
    /**
     * This attribute is the counter of players added to match
     */
    private int playersAddedCounter;

    /**
     * Controller constructor
     * @param ID the ID of the match
     */
    public Controller(int ID){
        this.ID = ID;
        this.playing = false;
        this.clientHandlers = new ArrayList<ClientHandler>();
        this.state = new MatchCreating();
        this.playersAddedCounter = 0;
    }

    /**
     * This method adds a player to the match and controls if there are enough players to start the playing
     * @param playerHandler reference to the ClientHandler of the added player
     * @param nickname nickname chosen by the player (client) and approved by the ClientHandler
     */
    public void addPlayerHandler(ClientHandler playerHandler, String nickname){
        this.clientHandlers.add(playerHandler);
        this.match.addPlayer(nickname);

        playersAddedCounter++;

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
        this.match = new Match(this.ID, this.numberOfPlayers, vv);

        Random random = new Random();
        int firstPlayer_ID = random.nextInt(numberOfPlayers);

        MatchStart msg = new MatchStart(firstPlayer_ID);
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
     * @param msg
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
    public void manageMsg(Message msg){
        msg_in = msg;
        //TODO: we could run a control on the message and if it is an ack or just a reading type message then we do not
        //  hand it over to the state executor
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
    }

    public int getMatchID(){
        return this.ID;
    }

    public Message getMsg(){
        return msg_in;
    }

    public boolean getPlayingStatus(){
        return playing;
    }
}
