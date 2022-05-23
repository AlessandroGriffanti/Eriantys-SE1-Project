package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.Message;

/**
 * This class represent the message sent by the client for the first part of action phase, that is the
 * movement of students from the entrance to the dining room or the islands.
 * A message is sent for every student moved.
 */
public class MovedStudentsFromEntrance extends Message {

    /**
     * This attribute is the ID used to identify the student, that has just been moved,
     * inside the entrance array
     */
    private int student_ID;

    /**
     * This attribute is the ID of the island where the student has been moved
     * or the value -1 meaning that the student has been put into the dining room
     */
    private int location;


    public MovedStudentsFromEntrance(){
        this.object = "action_1";
    }

    public void setStudent_ID(int student_ID) {
        this.student_ID = student_ID;
    }

    public int getStudent_ID() {
        return student_ID;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }

}
