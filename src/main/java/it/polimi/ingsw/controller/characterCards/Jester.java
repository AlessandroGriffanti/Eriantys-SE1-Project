package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.schoolboard.Entrance;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

import java.util.ArrayList;

/**
 * This class represents the character card called "jester".
 * It allows the player to switch at maximum 3 students between the character's students and
 * the students in the player's entrance.
 */
public class Jester extends Character {

    /**
     * This attribute is the list of students on the card
     */
    private ArrayList<Creature> studentsOnCard;


    public Jester(Controller controller){
        this.controller = controller;
        this.price = 1;

        Bag bag = controller.getMatch().getBagOfTheMatch();
        this.studentsOnCard = bag.drawStudents(6);
    }

    /**
     * This method controls if there are six students on the card or not
     * @return since there must always be six students on the card the return value should always be true
     */
    @Override
    public boolean checkCharacterAvailability() {
        assert studentsOnCard.size() <= 6;
        assert studentsOnCard.size() == 6 : "ERROR: on the jester character card there are less than 6 students!";

        return true;
    }

    /**
     * This method takes one student from the card and switch it with one student in the player's entrance
     * @param request message sent by the client containing data about the students chosen
     */
    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        // control the number of students chosen
        assert request.getElementsFromCard().size() > 0 && request.getElementsFromCard().size() < 4;
        assert request.getStudentsFromPlayerEntrance().size() > 0 && request.getStudentsFromPlayerEntrance().size() < 4;

        // take data from the message
        ArrayList<Integer> studentsFromTheCard_ID = request.getElementsFromCard();
        ArrayList<Integer> studentsFromEntrance_ID = request.getStudentsFromPlayerEntrance();

        // take the entrance of the player
        Player player = controller.getMatch().getPlayerByID(request.getSender_ID());
        Entrance entrance = player.getSchoolBoard().getEntrance();

        // type of students taken from the entrance
        int i = 0;
        int entranceIndex;
        Creature studentFromEntrance;

        for(int j: studentsFromTheCard_ID){
            // take the students from the entrance
            entranceIndex = studentsFromEntrance_ID.get(i);
            studentFromEntrance =  entrance.getStudentsInTheEntrance().get(entranceIndex);
            entrance.removeStudent(entranceIndex);
            // move the students from the card to the entrance
            entrance.addStudent(takeStudentFromCard(j));
            // put the students taken from the entrance on the card
            addStudentToCard(studentFromEntrance);

            i++;
        }


        int coinsInReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "jester", coinsInReserve);
        ack.setStudentsOnCard(this.studentsOnCard);
        ack.setEntranceOfPlayer(entrance.getStudentsInTheEntrance());

        int coinsOfPlayer = controller.getMatch().getPlayerByID(request.getSender_ID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }

    /**
     * This methode takes one student from the card and replaces it with a null pointer
     * @param student_ID ID of the student to take
     * @return type of student taken
     */
    private Creature takeStudentFromCard(int student_ID){
        // take student
        Creature student = studentsOnCard.get(student_ID);

        // remove student
        studentsOnCard.set(student_ID, null);

        return student;
    }

    /**
     * This method adds one student to the card putting it in the first position containing a null pointer
     * @param student type of student to add
     */
    private void addStudentToCard(Creature student){
        for(int j = 0; j < studentsOnCard.size(); j++){
            if(studentsOnCard.get(j) == null){
                studentsOnCard.set(j, student);
                break;
            }
        }
    }

    public ArrayList<Creature> getStudentsOnCard() {
        return studentsOnCard;
    }
}
