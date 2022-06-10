package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

/**
 * This class represents the message sent by the server to the client providing the available lobbies to join.
 */

public class AskMatchToJoinMessage extends Message {
    /**
     * This attribute is the list of lobbies' ID that are available for joining
     */
    ArrayList<Boolean> availableLobbiesTmp = new ArrayList<>();
    /**
     * This attribute is the list of players playing in every lobby (waiting or not)
     */
    ArrayList<Integer> lobbiesNumberOfPlayers = new ArrayList<>();
    /**
     * This attribute tells if the lobby was created as expert mode or not
     */
    ArrayList<Boolean> lobbiesExpertMode = new ArrayList<>();

    public AskMatchToJoinMessage(){
        this.object = "join match";
    }

    public AskMatchToJoinMessage (ArrayList<Boolean> listAvailableLobbies, ArrayList<Integer> lobbiesNumberOfPlayers, ArrayList<Boolean> lobbiesExpertMode){
        this.object = "join match";
        this.availableLobbiesTmp = listAvailableLobbies;
        this.lobbiesNumberOfPlayers = lobbiesNumberOfPlayers;
        this.lobbiesExpertMode = lobbiesExpertMode;
    }

    public ArrayList<Boolean> getLobbiesTmp() {
        return availableLobbiesTmp;
    }

    public ArrayList<Boolean> getLobbiesExpertMode() {
        return lobbiesExpertMode;
    }

    public ArrayList<Integer> getLobbiesNumberOfPlayers() {
        return lobbiesNumberOfPlayers;
    }
}
