package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;


import java.util.ArrayList;

/**
 * This class represents a miniature of the Entrance of the player.
 */
public class EntranceView {
    /**
     * This attributes represents the list of students in the entrance.
     */
    private ArrayList<Creature> studentsInTheEntrancePlayer;

    private DiningRoomView doorToTheDiningRoomPlayer;

    /**
     * This constructor creates a new instance of the EntranceView.
     * @param diningRoomView is the diningRoomView instanced in the SchoolBoardView constructor.
     */
    public EntranceView(DiningRoomView diningRoomView){
        this.doorToTheDiningRoomPlayer = diningRoomView;
        studentsInTheEntrancePlayer = new ArrayList<Creature>();
    }

    public ArrayList<Creature> getStudentsInTheEntrancePlayer() {
        return studentsInTheEntrancePlayer;
    }

    public void setStudentsInTheEntrancePlayer(ArrayList<Creature> studentsInTheEntrancePlayer) {
        this.studentsInTheEntrancePlayer = studentsInTheEntrancePlayer;
    }

    public DiningRoomView getDoorToTheDiningRoomPlayer() {
        return doorToTheDiningRoomPlayer;
    }

    public void setDoorToTheDiningRoomPlayer(DiningRoomView doorToTheDiningRoomPlayer) {
        this.doorToTheDiningRoomPlayer = doorToTheDiningRoomPlayer;
    }
}
