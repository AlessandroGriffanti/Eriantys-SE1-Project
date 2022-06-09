package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChosenCloudMessage extends Message {
    private int cloud_ID;

    public ChosenCloudMessage(){
        this.object = "action_3";
    }

    public ChosenCloudMessage(int chosenCloudID){
        this.object = "action_3";
        this.cloud_ID = chosenCloudID;

    }


    public void setCloud_ID(int cloud_ID) {
        this.cloud_ID = cloud_ID;
    }

    public int getCloud_ID() {
        return cloud_ID;
    }
}
