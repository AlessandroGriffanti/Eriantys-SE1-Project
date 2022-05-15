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
*       1. */