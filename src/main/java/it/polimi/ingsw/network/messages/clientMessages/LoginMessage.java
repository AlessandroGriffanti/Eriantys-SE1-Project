package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/** This is the message used in first part of the login process.
 * @nicknameOfPlayer represents the nickname of the player
 * @createNewMatch represents the will of the player, so if he wants to create a new match(true), otherwise false.
 */
public class LoginMessage extends Message {
    String nicknameOfPlayer;
    boolean createNewMatch;


    /** Added this constructor otherwise the client handler can't create a new Login message without a parameter */
    public LoginMessage(){
        this.object = "login";
    }

    public LoginMessage(String nicknameOfPlayer, boolean createNewMatch) {
        this.object = "login";
        this.nicknameOfPlayer = nicknameOfPlayer;
        this.createNewMatch = createNewMatch;
    }

    public void setNicknameOfPlayer(String nicknameOfPlayer) {
        this.nicknameOfPlayer = nicknameOfPlayer;
    }

    public String getNicknameOfPlayer() {
        return nicknameOfPlayer;
    }

    public boolean isCreateNewMatch() {
        return createNewMatch;
    }

}
