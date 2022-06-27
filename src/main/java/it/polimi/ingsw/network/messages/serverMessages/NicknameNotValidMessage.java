package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent by the server to the client to notify that the chosen nickname is not available.
 * The chosen nickname has already been chosen by another client.
 */
public class NicknameNotValidMessage extends Message {

    public NicknameNotValidMessage(){
        this.object = "NicknameNotValid";
    }
}
