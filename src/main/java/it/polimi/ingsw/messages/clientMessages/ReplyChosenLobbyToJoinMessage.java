package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * This class represents the message sent by the client to the server when
 * the player has chosen the lobby to join
 */
public class ReplyChosenLobbyToJoinMessage extends Message {
    /**
     * This attribute is the ID of the lobby chosen by the player
     */
    int lobbyIDChosen;

    public ReplyChosenLobbyToJoinMessage(){
        this.object = "chosen lobby";
    }

    public int getLobbyIDChosen() {
        return lobbyIDChosen;
    }

    public ReplyChosenLobbyToJoinMessage(int lobbyIDchosenByPlayer){
        this.object = "chosen lobby";
        this.lobbyIDChosen = lobbyIDchosenByPlayer;
    }

}
