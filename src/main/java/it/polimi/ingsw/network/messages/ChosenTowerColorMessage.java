package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Tower;

public class ChosenTowerColorMessage extends Message{
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
