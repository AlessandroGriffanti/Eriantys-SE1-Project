package it.polimi.ingsw.model.schoolboard;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Realm;

import java.util.ArrayList;

public class Entrance {
    /**
     * This attribute is the list of students on the entrance
     */
    private ArrayList<Creature> studentsInTheEntrance;
    /**
     * This attribute is the reference to the dining room, where the students could be moved
     */
    private DiningRoom doorToTheDiningRoom;
    /**
     * This attribute is the reference to the Realm of the match (where there are the clouds and the islands)
     */
    private Realm realmInEntrance;

    public Entrance(DiningRoom d, Realm r, int numOfPlayers){
        this.doorToTheDiningRoom = d;
        this.realmInEntrance = r;
        studentsInTheEntrance = new ArrayList<Creature>();

        ArrayList<Creature> initialStudents = initialSetUpStudentsInTheEntrance(numOfPlayers);

        for(Creature c: initialStudents){
            studentsInTheEntrance.add(c);
        }
    }

    /**
     * This method sets up the 7 or 9 initial students in the entrance
     * @return array of the students drawn from the bag
     */
    public ArrayList<Creature> initialSetUpStudentsInTheEntrance(int numOfPlayers){

        ArrayList<Creature> studentsSetUpEntrance = new ArrayList<Creature>();

        if(numOfPlayers == 3){
            studentsSetUpEntrance = realmInEntrance.getBag().drawStudents(9);
        }else if(numOfPlayers == 2 || numOfPlayers == 4){
            studentsSetUpEntrance = realmInEntrance.getBag().drawStudents(7);
        }

        return studentsSetUpEntrance;
    }


    /**
     * This method adds a creature (student) to the entrance in the spot where there is a null pointer
     * @param s the kind of student
     */
    public void addStudent(Creature s){
        for(int j = 0; j < studentsInTheEntrance.size(); j++){
            if(studentsInTheEntrance.get(j) == null){
                studentsInTheEntrance.set(j, s);

            }
        }
    }

    /**
     * This method adds multiple students to the entrance in the spots where there is a null pointer
     * @param students array of students that will be added
     */
    public ArrayList<Creature> addMultipleStudents(ArrayList<Creature> students){
        int indexOfParameter = 0;
        for(int j = 0; j < studentsInTheEntrance.size(); j++){
            if(studentsInTheEntrance.get(j) == null){
                studentsInTheEntrance.set(j, students.get(indexOfParameter));

                indexOfParameter++;

                if(indexOfParameter > students.size()){
                    break;
                }
            }
        }

        return this.studentsInTheEntrance;
    }

    /**
     * This method removes a student from the entrance and set its position in the array to null
     * @param index the index of the student to remove
     */
    public void removeStudent(int index){
        studentsInTheEntrance.set(index, null);
    }

    /**
     * This method moves a student to the diningRoom and removes it from the entrance (set the position to null)
     * @param index index of the student
     */
    public void moveStudentToDiningRoom(int index){
        doorToTheDiningRoom.addStudent(studentsInTheEntrance.get(index));
        studentsInTheEntrance.set(index, null);
    }

    /**
     * This method moves a student to an island and removes it from the entrance (set the positions to null)
     * @param student_ID index of the student
     * @param islandID index of the island
     */
    public void moveStudentToIsland(int student_ID, int islandID){
        realmInEntrance.addStudentToIsland(studentsInTheEntrance.get(student_ID), islandID);
        studentsInTheEntrance.set(student_ID, null);
    }

    /** useful for tests */
    public ArrayList<Creature> getStudentsInTheEntrance() {
        return studentsInTheEntrance;
    }

    /** useful for tests */
    public DiningRoom getDoorToTheDiningRoom() {
        return doorToTheDiningRoom;
    }

    public Realm getRealmInEntrance() {
        return realmInEntrance;
    }
}
