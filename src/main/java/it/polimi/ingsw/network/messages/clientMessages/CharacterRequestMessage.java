package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent when the player wants to use a character card.
 */
public class CharacterRequestMessage extends Message {

    /**
     * This attribute is the name of the character chosen by the player
     */
    private String character;

    public CharacterRequestMessage(){}

    public CharacterRequestMessage(int sender_ID, String characterName){
        this.object = "character_request";
        this.sender_ID = sender_ID;

        this.character = characterName;
    }

    public String getCharacter() {
        return character;
    }
}
