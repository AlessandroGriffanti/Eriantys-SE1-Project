package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent from the client providing the tower color chosen by the player.
 */
public class ChosenTowerColorMessage extends Message {
    /**
     * This attribute represents the tower color chosen by the player.
     */
    Tower color;

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
