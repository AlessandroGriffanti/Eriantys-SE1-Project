package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * This class represents the message sent when a player chooses the cloud he wants to take
 * The students from.
 */
public class ChosenCloudMessage extends Message {
    private int cloud_ID;

    /**
     * this attribute is the ID of the cloud chosen by the player, whose students will be added to the entrance.
     */
    public ChosenCloudMessage(){
        this.object = "action_3";
    }

    /**
     * This constructor can be used by the client to create the message to send
     * to the server
     * @param chosenCloudID is the ID of the cloud chosen by the player.
     */
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
