package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;

import java.util.logging.Handler;

public class Controller {
    ControllerState state = null;
    Match match = null;
    final int numberOfPlayers;
    final int ID;
    private MappingClassForJson msg_in = null;
    private ArrayList<PlayerHandler> playerhandlers;

    /**
     * Thi method creates a new Match (Model) and set the initial state of this controller to "WaitingForPlayers"
     * N.B. This method is called only when a new Client (Player) access the Server, and there is no Match waiting for Players.
     * @param ID identifier of the controller and the Match created
     * @param numPlayers number of players that are necessary to start the Match
     */
    public Controller(int ID, int numPlayers){
        this.numberOfPlayers = numPlayers;
        this.ID = ID;
        this.match = new Match(this.ID, this.numberOfPlayers, vv);
        this.state = new WaitingForPlayers();
    }

    public Match getMatch(){
        return this.match;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public int getMatchID(){
        return this.ID;
    }

    public MappingClassForJson getMsg(){
        return msg_in;
    }

    public void addPlayerHandler(PlayerHandler playerHandler){
        this.playerhandlers.add(playerHandler);
    }
    /**
     * This method creates the Match model and initializes the state of this controller with the first state
     * of the FSM (Finite State Machine)
     */
    public void startMatch(){
        //Creates the virtualView so that it can notify the client (client side)
        VirtualView vv = new VirtualView(this.match);
        //Notify all the handlers
        for(PlayerHandler p: playerhandlers){
            p.matchStart();
        }
        nextState();
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

    //We could return an object that will be serialized by the ClientHandler
    public void manageMsg(MappingClassForJson msg){
        msg_in = msg;
        state.stateExecution(this);
    }
}
