package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class ReplyChosenLobbyToJoinMessage extends Message {
    int lobbyIDchosen;

    public ReplyChosenLobbyToJoinMessage(){
        this.object = "chosen lobby";
    }

    public int getLobbyIDchosen() {
        return lobbyIDchosen;
    }

    public ReplyChosenLobbyToJoinMessage(int lobbyIDchosenByPlayer){
        this.object = "chosen lobby";
        this.lobbyIDchosen = lobbyIDchosenByPlayer;
    }

}
