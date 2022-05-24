package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

import java.util.ArrayList;


public class Monk  extends Character {
    /**
     * This attribute is the list of students placed on the card
     */
    private ArrayList<Creature> students;
    /**
     * This attribute is the reference to the bag used in the match
     */
    private Bag bag;
    /**
     * This attribute is the reference to the controller of the match
     */
    Controller controller;

    public Monk(Controller controller) {
        this.controller = controller;
        this.price = 1;
        this.bag = controller.getMatch().getBagOfTheMatch();

        this.students = controller.getMatch().getBagOfTheMatch().drawStudents(4);
    }

    /**
     * This method takes one of the students on the monk-card and puts it on an island chosen by the player;
     * finally it creates the AckCharacter message to send back to all the players
     * @param request message containing island_ID and student_ID chosen by the player
     *
     */
    @Override
    public void effect(ChosenCharacterMessage request) {
        // increase price by one if this is the first time that the card is used
        increasePrice();

        int student_ID = request.getStudent_ID();
        int island_ID = request.getIsland_ID();

        // the island must not be null
        assert controller.getMatch().getRealmOfTheMatch().getArchipelagos().get(island_ID) != null : "The island chosen by the player turned out to be NULL !!";

        // take the students from the card
        Creature chosenStudent = students.get(student_ID);
        students.set(student_ID, null);

        // put the student on the island
        this.controller.getMatch().getRealmOfTheMatch().addStudentToIsland(chosenStudent, island_ID);

        // draw new student from the bag and put it on the card
        Creature caughtStudentFromBag = bag.drawOneStudent();
        addStudent(caughtStudentFromBag);

        // create and send the response
        AckCharactersMessage ack = new AckCharactersMessage();
        ack.setCard("monk");
        ack.setCoinReserve(controller.getMatch().getCoinReserve());
        ack.setStudent(chosenStudent);
        ack.setStudentsOnCard(this.students);
    }

    /**
     * This method puts the student specified into the array of students in the position where it finds a null pointer
     * (we know that there is only one position of the array containing a null pointer)
     * @param student the type of student to add to the array
     */
    private void addStudent(Creature student){

        for(int i = 0; i < 4; i++){
            if(students.get(i) == null){
                students.set(i, student);
            }
        }
    }
}
