package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;


import java.util.ArrayList;

/**
 * This class represents a miniature of the Entrance of the player.
 */
public class EntranceView {

    private ArrayList<Creature> studentsInTheEntrancePlayer;
    private DiningRoomView doorToTheDiningRoomPlayer; //forse non server

    /**
     * This constructor creates a new instance of the EntranceView.
     * @param diningRoomView is the diningRoomView instanced in the SchoolBoardView constructor.
     */
    public EntranceView(DiningRoomView diningRoomView){
        this.doorToTheDiningRoomPlayer = diningRoomView;
        studentsInTheEntrancePlayer = new ArrayList<Creature>();
    }

    public synchronized ArrayList<Creature> getStudentsInTheEntrancePlayer() {
        return studentsInTheEntrancePlayer;
    }

    public synchronized void setStudentsInTheEntrancePlayer(ArrayList<Creature> studentsInTheEntrancePlayer) {
        this.studentsInTheEntrancePlayer = studentsInTheEntrancePlayer;
    }

    public synchronized DiningRoomView getDoorToTheDiningRoomPlayer() {
        return doorToTheDiningRoomPlayer;
    }

    public synchronized void setDoorToTheDiningRoomPlayer(DiningRoomView doorToTheDiningRoomPlayer) {
        this.doorToTheDiningRoomPlayer = doorToTheDiningRoomPlayer;
    }
}
