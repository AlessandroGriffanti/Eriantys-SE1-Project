package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.SupportFunctions;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

import java.util.ArrayList;

/**
 * This class represents the character card called "princess".
 * It allows players to choose one student from the character card and put it in their
 * dining room.
 */
public class Princess extends Character {

    /**
     * This attribute is the list of students on the character
     */
    private ArrayList<Creature> studentsOnPrincess;

    public Princess(Controller controller){
        this.controller = controller;
        this.price = 2;
        studentsOnPrincess = new ArrayList<>();

        Bag bag = controller.getMatch().getBagOfTheMatch();
        this.studentsOnPrincess = bag.drawStudents(4);
    }

    /**
     * This method controls if there is at least one student on the character card
     * @return true if there is at least one student on the character card, false otherwise
     */
    @Override
    public boolean checkCharacterAvailability() {
        return studentsOnPrincess.size() > 0;
    }

    /**
     * This method takes one student  from the character card and put it in the player's
     * dining room, the draws another student from the bag and put it on the character card
     * in order to replace the one previously taken
     * @param request message sent by the client containing the ID of the student chosen by the player from
     *                the character
     */
    @Override
    public void effect(CharacterDataMessage request) {
        // take the student from the character card
        Creature studentTaken = takeStudent(request.getStudent_ID());
        // put the student in the player's dining room
        Player player = controller.getMatch().getPlayerByID(request.getSender_ID());

        int studentsAtTheTable = player.getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(studentTaken);
        if(studentsAtTheTable == 10){
            NackMessage nack = new NackMessage("princess");
            controller.sendMessageToPlayer(request.getSender_ID(), nack);
            return;
        }

        increasePrice();

        int previousProfessorMaster = SupportFunctions.whoControlsTheProfessor(controller.getMatch(), studentTaken);

        player.getSchoolBoard().getDiningRoom().addStudent(studentTaken);

        // draw one student from the bag and add it to the character card
        addStudent();

        // UPDATE PROFESSORS' CONTROL
        SupportFunctions.updateProfessorControl(controller, previousProfessorMaster, studentTaken);

        // create and send the ack message
        int coinReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "princess", coinReserve);

        ack.setCreature(studentTaken);
        ack.setStudentsOnCard(studentsOnPrincess);
        ack.setPlayerDiningRoom(player.getSchoolBoard().getDiningRoom().getOccupiedSeats());

        for(int k = 0; k < controller.getNumberOfPlayers(); k++){
            player = controller.getMatch().getPlayerByID(k);
            ack.setPlayerProfessors(k, player.getMyProfessors());
        }

        int coinsOfPlayer = controller.getMatch().getPlayerByID(request.getSender_ID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }

    public ArrayList<Creature> getStudentsOnPrincess() {
        return studentsOnPrincess;
    }

    /**
     * This method takes one student from the character card and set the corresponding
     * position to null
     * @param student_ID the ID of the student on the character card
     * @return the type of student taken from the card
     */
    private Creature takeStudent(int student_ID){
        Creature student = studentsOnPrincess.get(student_ID);
        studentsOnPrincess.set(student_ID, null);

        return student;
    }

    /**
     * This method draw one student from the match's bag and put it in the
     * spot containing the null pointer on the character card.
     */
    private void addStudent(){

        for(int i = 0; i < studentsOnPrincess.size(); i++){
            if(studentsOnPrincess.get(i) == null){
                Creature studentDrawn = controller.getMatch().getBagOfTheMatch().drawOneStudent();
                studentsOnPrincess.set(i, studentDrawn);
            }
        }
    }

}
