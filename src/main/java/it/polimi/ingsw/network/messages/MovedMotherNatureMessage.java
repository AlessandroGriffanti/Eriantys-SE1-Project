package it.polimi.ingsw.network.messages;

public class MovedMotherNatureMessage extends Message{

    /**
     * This attribute is the ID of the island where the player chose to move mother nature
     */
    private int destinationIsland_ID;

    public MovedMotherNatureMessage(){
        this.object = "action_2";
    }

    public void setDestinationIsland_ID(int destinationIsland_ID) {
        this.destinationIsland_ID = destinationIsland_ID;
    }

    public int getDestinationIsland_ID() {
        return destinationIsland_ID;
    }
}
