package it.polimi.ingsw.network.messages;

public class ReplyChosenLobbyToJoinMessage extends Message{
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
