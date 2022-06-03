package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.SupportFunctions;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class Princess extends Character {

    /** this attribute handles the students on the card */
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
     * @param request message containing the ID of the student chosen by the player from
     *                the character
     */
    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        // find who are the professors' masters before using the card
        HashMap<Creature, Integer> previousProfessorsMaster = new HashMap<Creature, Integer>();
        for(Creature c: Creature.values()){
            previousProfessorsMaster.put(c, SupportFunctions.whoControlsTheProfessor(controller.getMatch(), c));
        }

        // take the student from the character card
        Creature studentTaken = takeStudent(request.getStudent_ID());
        // put the student in the player's dining room
        Player player = controller.getMatch().getPlayerByID(request.getSender_ID());
        player.getSchoolBoard().getDiningRoom().addStudent(studentTaken);

        // draw one student from the bag and add it to the character card
        addStudent();

        // CONTROL OVER PROFESSORS
        // find who are the professors' masters after the card has been used
        HashMap<Creature, Integer> currentProfessorsMaster = new HashMap<Creature, Integer>();
        for(Creature c: Creature.values()){
            currentProfessorsMaster.put(c, SupportFunctions.whoControlsTheProfessor(controller.getMatch(), c));
        }

        //if the master has changed then we move the professor from a school-board to another
        int previousMaster_ID;
        int currenMaster_ID;
        for(Creature c: Creature.values()){
            previousMaster_ID = previousProfessorsMaster.get(c);
            currenMaster_ID = currentProfessorsMaster.get(c);

            if(previousMaster_ID != currenMaster_ID){
                // remove professor from the previous controller
                controller.getMatch().getPlayerByID(previousMaster_ID).getSchoolBoard().getProfessorTable().removeProfessor(c);
                // give professor to the current controller
                controller.getMatch().getPlayerByID(currenMaster_ID).getSchoolBoard().getProfessorTable().addProfessor(c);
            }
        }

        // create and send the ack message
        int coinReserve = controller.getMatch().getCoinReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "princess", coinReserve);

        ack.setStudentsOnCard(studentsOnPrincess);
        ack.setPlayerDiningRoom(player.getSchoolBoard().getDiningRoom().getOccupiedSeats());

        for(int k = 0; k < controller.getNumberOfPlayers(); k++){
            player = controller.getMatch().getPlayerByID(k);
            ack.setPlayerProfessors(k, player.getMyProfessors());
        }

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
