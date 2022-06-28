package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.clientMessages.CharacterRequestMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.MovedStudentsFromEntranceMessage;
import it.polimi.ingsw.network.messages.serverMessages.NackMessage;

public class Action_1 implements ControllerState{

    private int studentsMoved = 0;


    @Override
    public void nextState(Controller controller) {
        controller.setState(new Action_2());
    }

    /**
     * This method controls if the message must be handed over to the character manager or
     * to the stateExecution method
     * @param controller reference to the controller of the match
     */
    @Override
    public void controlMessageAndExecute(Controller controller) {
        String json = controller.getMsg();
        Message message = gson.fromJson(json, Message.class);
        String object = message.getObjectOfMessage();

        switch (object) {
            case "character_request":
                CharacterRequestMessage request = gson.fromJson(json, CharacterRequestMessage.class);
                controller.getCharactersManager().checkCard(request);
                break;
            case "character_data":
                CharacterDataMessage dataMessage = gson.fromJson(json, CharacterDataMessage.class);
                controller.getCharactersManager().useCard(dataMessage);
                break;
            case "action_1":
                stateExecution(controller);
                break;
            default:
                System.out.println("ACTION_1: \nexpected message with object [action_1] or [character_request] or [character_data]" +
                                   "\nreceived message with object["+ message.getObjectOfMessage() + "]");
                break;
        }
    }

    /**
     * This method moves the student chosen by the player in his entrance to the location specified in the message
     * (an island or the dining room)
     * @param controller reference to the controller of the match
     */
    @Override
    public void stateExecution(Controller controller) {
        Match match = controller.getMatch();

        String json = controller.getMsg();
        MovedStudentsFromEntranceMessage request = gson.fromJson(json, MovedStudentsFromEntranceMessage.class);
        match.setCurrentPlayer(request.getSender_ID());

        // take and remove the chosen student from the entrance
        int location = request.getLocation();
        Creature creatureChosen = match.getStudentInEntranceOfPlayerByID(request.getSender_ID(), request.getStudent_ID());

        AckMessage ack = new AckMessage();
        ack.setRecipient(request.getSender_ID());
        ack.setTypeOfStudentMoved(creatureChosen);
        ack.setStudentMoved_ID(request.getStudent_ID());

        // student moved in the dining room
        if(location == -1){
            DiningRoom diningRoom = match.getPlayerByID(request.getSender_ID()).getSchoolBoard().getDiningRoom();
            int studentsAtTheTable = diningRoom.getOccupiedSeatsAtTable(creatureChosen);

            if(studentsAtTheTable == 10){
                NackMessage nack = new NackMessage("table_full");
                controller.sendMessageToPlayer(request.getSender_ID(), nack);
                return;
            }

            ack.setSubObject("action_1_dining_room");

            // find who is controlling the professor of the same type chosen by the player
            int previousOwnerOfProfessor = SupportFunctions.whoControlsTheProfessor(match, creatureChosen);

            // move the student from entrance to dining room
            match.moveStudentFromEntranceToDiningRoom(request.getStudent_ID());

            // if the player controlling the professor is another player then...
            if(previousOwnerOfProfessor != request.getSender_ID() && previousOwnerOfProfessor != -1){
                int previousNumberOfStudents = match.getPlayerByID(previousOwnerOfProfessor).getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(creatureChosen);
                int currentPlayerStudents = match.getPlayerByID(request.getSender_ID()).getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(creatureChosen);

                if(controller.isExpertMode() && controller.getCharactersManager().isCookActive()){

                        /*the current player takes control over the professor even if he has the same number of students
                          as the previousOwnerOfProfessor*/
                    if((currentPlayerStudents >= previousNumberOfStudents)){
                        match.looseControlOnProfessor(previousOwnerOfProfessor, creatureChosen);
                        match.acquireControlOnProfessor(creatureChosen);

                        ack.setProfessorTaken(true);
                        ack.setPreviousOwnerOfProfessor(previousOwnerOfProfessor);
                    }

                }else{

                    //the current player takes control over the professor only if it has more students than the old player
                    if((currentPlayerStudents > previousNumberOfStudents)){
                        match.looseControlOnProfessor(previousOwnerOfProfessor, creatureChosen);
                        match.acquireControlOnProfessor(creatureChosen);

                        ack.setProfessorTaken(true);
                        ack.setPreviousOwnerOfProfessor(previousOwnerOfProfessor);
                    }
                }
            }else if(previousOwnerOfProfessor == -1){
                /* set professorTaken attribute to true but not the previousOwnerOfProfessor because
                   the professor was not controlled
                 */
                ack.setProfessorTaken(true);
                match.acquireControlOnProfessor(creatureChosen);
            }
        }
        // student moved on an island
        else{
            ack.setSubObject("action_1_island");
            ack.setDestinationIsland_ID(request.getLocation());

            if(match.getRealmOfTheMatch().getArchipelagos().get(request.getLocation()) != null){
                match.moveStudentFromEntranceToIsland(request.getStudent_ID(), request.getLocation());
            }
        }

        // send the ack message in broadcast
        ack.setNextPlayer(request.getSender_ID());
        controller.sendMessageAsBroadcast(ack);
        studentsMoved++;

        if(controller.getNumberOfPlayers() == 2){
            // if 3 students have been moved we can change state
            if(studentsMoved == 3){
                controller.nextState();
                // reset the value of cookUsed (it lasts only for the current players' action_1)
                controller.getCharactersManager().setCookActive(false);
            }
        }else if(controller.getNumberOfPlayers() == 3){
            // if 4 students have been moved we can change state
            if(studentsMoved == 4){
                controller.nextState();
                // reset the value of cookUsed (it lasts only for the current players' action_1)
                controller.getCharactersManager().setCookActive(false);
            }
        }
    }
}
