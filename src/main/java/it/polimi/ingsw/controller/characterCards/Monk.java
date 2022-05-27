package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;

/**
 * This class represents the character card called 'monk' (first one in the rules' file).
 * It allows players to take one of its students and put it on an island.
 */
public class Monk  extends Character {
    /**
     * This attribute is the list of students placed on the card
     */
    private ArrayList<Creature> students;
    /**
     * This attribute is the reference to the bag used in the match
     */
    private Bag bag;


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
     * @return true if the effect was activated, false otherwise
     */
    @Override
    public boolean effect(CharacterDataMessage request) {
        // increase price by one if this is the first time that the card is used
        increasePrice();

        // this variable tells if the effect of the character was activated or not
        boolean effectActivated;

        if(students.size() == 0){
            effectActivated = false;

            NackMessage nack = new NackMessage();
            nack.setSubObject("monk");

            controller.sendMessageToPlayer(request.getSender_ID(), nack);
        }else{
            effectActivated = true;

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
            int coinsInReserve = controller.getMatch().getCoinReserve();
            AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "monk", coinsInReserve);
            ack.setStudent(chosenStudent);
            ack.setStudentsOnCard(this.students);

            controller.sendMessageAsBroadcast(ack);
        }

        return effectActivated;
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
