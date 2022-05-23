package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class NicknameNotValidMessage extends Message {

    public NicknameNotValidMessage(){
        this.object = "NicknameNotValid";
    }
}
