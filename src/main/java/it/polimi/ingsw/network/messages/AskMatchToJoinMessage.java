package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.Controller;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the message sent by the server to the client providing the available lobbies to join.
 * @listAvailableLobbiesTmp is the arraylist list of the available lobby we send to the client.
 */

public class AskMatchToJoinMessage extends Message{
    ArrayList<Boolean> listAvailableLobbiesTmp = new ArrayList<>();
    //private int playerID;

    public AskMatchToJoinMessage(){
        this.object = "join match";
    }

    public AskMatchToJoinMessage (ArrayList<Boolean> listAvailableLobbies){
        this.object = "join match";
        this.listAvailableLobbiesTmp = listAvailableLobbies;
       // this.playerID = playerID;
    }

    public ArrayList<Boolean> getLobbiesTmp() {
        return listAvailableLobbiesTmp;
    }

    /*public int getPlayerID() {
        return playerID;
    }*/

}
