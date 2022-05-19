package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Wizard;

import java.util.ArrayList;

public class NackMessage extends Message{
    /**
     * This attribute is the second object of the message, and it tells which data are stored in this message
     */
    String subObject = "";

    public NackMessage(){
        this.object = "nack";
    }

    public void setSubObject(String subObject) {
        this.subObject = subObject;
    }

    public String getSubObject() {
        return subObject;
    }
}

/*POSSIBLE VALUES OF "subObject":
*   1. assistant:
*      it means that the assistant card chosen is not legit because another player has already chosen it and the current
*      player has other cards that can choose
*
*   2. invalid_mother_nature_movement:
*      it means that the movement of mother nature is not legit because it does not respect the maximum distance
*      feasible for mother nature which is written on the last used assistant card*/
