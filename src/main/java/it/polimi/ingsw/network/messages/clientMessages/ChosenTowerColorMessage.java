package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.network.messages.Message;

public class ChosenTowerColorMessage extends Message {
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
