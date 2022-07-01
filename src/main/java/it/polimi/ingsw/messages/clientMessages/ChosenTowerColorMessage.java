package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.messages.Message;

/**
 * This class represents the message sent from the client providing the tower color chosen by the player.
 */
public class ChosenTowerColorMessage extends Message {
    /**
     * This attribute represents the tower color chosen by the player.
     */
    Tower color;

    public ChosenTowerColorMessage(int sender_ID, Tower color){
        this.object = "tower_color";
        this.color = color;
        this.sender_ID = sender_ID;
    }

    public ChosenTowerColorMessage(){
        this.object = "tower_color";
    }

    public void setColor(Tower color) {
        this.color = color;
    }

    public Tower getColor() {
        return color;
    }
}
