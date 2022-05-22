package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.messages.Message;

public class ChosenCloudMessage extends Message {
    private int cloud_ID;

    public ChosenCloudMessage(){
        this.object = "action_3";
    }

    public void setCloud_ID(int cloud_ID) {
        this.cloud_ID = cloud_ID;
    }

    public int getCloud_ID() {
        return cloud_ID;
    }
}
