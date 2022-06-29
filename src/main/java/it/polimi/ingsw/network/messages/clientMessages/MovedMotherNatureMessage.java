package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent by the client when the player moves mother nature during action2.
 */
public class MovedMotherNatureMessage extends Message {

    /**
     * This attribute is the ID of the island where the player chose to move mother nature
     */
    private int destinationIsland_ID;

    /**
     * This constructor can be used by the  controller to deserialize the message received
     */
    public MovedMotherNatureMessage(){
        this.object = "action_2";
    }

    /**
     * This constructor can be used by the client to create the message to send
     * to the server
     * @param sender_ID ID of the player that send the message
     */
    public MovedMotherNatureMessage(int sender_ID){
        this.object = "action_2";
        this.sender_ID = sender_ID;
    }

    public void setDestinationIsland_ID(int destinationIsland_ID) {
        this.destinationIsland_ID = destinationIsland_ID;
    }

    public int getDestinationIsland_ID() {
        return destinationIsland_ID;
    }
}
