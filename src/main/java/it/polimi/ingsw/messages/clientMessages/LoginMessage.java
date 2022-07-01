package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/** This is the message used in first part of the login process.
 * @nicknameOfPlayer represents the nickname of the player
 * @createNewMatch represents the will of the player, so if he wants to create a new match(true), otherwise false.
 */
public class LoginMessage extends Message {
    /**
     * It represents the nickname of the player.
     */
    String nicknameOfPlayer;

    /**
     * It tells if the player wants to create a new match or not.
     */
    boolean createNewMatch;


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
