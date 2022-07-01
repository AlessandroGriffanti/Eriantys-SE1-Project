package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

/**
 * This class represents the message sent by the server to the client to notify that the chosen nickname is not available.
 * The chosen nickname has already been chosen by another client.
 */
public class NicknameNotValidMessage extends Message {

    public NicknameNotValidMessage(){
        this.object = "NicknameNotValid";
    }
}
