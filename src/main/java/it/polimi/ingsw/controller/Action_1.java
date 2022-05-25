package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.messages.clientMessages.ChosenCharacterMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckMessage;
import it.polimi.ingsw.network.messages.clientMessages.MovedStudentsFromEntrance;

public class Action_1 implements ControllerState{

    private int studentsMoved = 0;


    @Override
    public void nextState(Controller controller) {
        controller.setState(new Action_2());
    }

    @Override
    public void stateExecution(Controller controller) {
        Match match = controller.getMatch();

        String json = controller.getMsg();
        MovedStudentsFromEntrance request = gson.fromJson(json, MovedStudentsFromEntrance.class);

        if(request.getObjectOfMessage().equals("character")){

            ChosenCharacterMessage characterRequest = gson.fromJson(json, ChosenCharacterMessage.class);
            controller.getCharactersManager().useCard(characterRequest);

        }else if(request.getObjectOfMessage().equals("action_1")){

            match.setCurrentPlayer(request.getSender_ID());

            // take and remove the chosen student from the entrance
            int location = request.getLocation();
            Creature creature = match.getStudentInEntranceOfPlayerByID(request.getSender_ID(), request.getStudent_ID());


            AckMessage response = new AckMessage();
            response.setRecipient(request.getSender_ID());
            response.setTypeOfStudentMoved(creature);
            response.setStudentMoved_ID(request.getStudent_ID());

            // student moved in the dining room
            if(location == -1){
                response.setSubObject("action_1_dining_room");

                // find who is controlling the professor of the same type chosen by the player
                int previousOwnerOfProfessor = SupportFunctions.whoControlsTheProfessor(match, creature);

                // move the student from entrance to dining room
                match.moveStudentFromEntranceToDiningRoom(request.getStudent_ID());

                // if the player controlling the professor is another player then...
                if(previousOwnerOfProfessor != request.getSender_ID() && previousOwnerOfProfessor != -1){
                    int previousNumberOfStudents = match.getPlayerByID(previousOwnerOfProfessor).getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(creature);
                    int currentPlayerStudents = match.getPlayerByID(request.getSender_ID()).getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(creature);

                    if(controller.getCharactersManager().isCookUsed()){

                        /*the current player takes control over the professor even if he has the same number of students
                          as the previousOwnerOfProfessor*/
                        if((currentPlayerStudents >= previousNumberOfStudents)){
                            match.looseControlOnProfessor(previousOwnerOfProfessor, creature);
                            match.acquireControlOnProfessor(creature);

                            response.setProfessorTaken(true);
                            response.setPreviousOwnerOfProfessor(previousOwnerOfProfessor);
                        }

                        // reset the value of cookUsed (it lasts only for the current players' round)
                        controller.getCharactersManager().setCookUsed(false);
                    }else{

                        //the current player takes control over the professor only if it has more students than the old player
                        if((currentPlayerStudents > previousNumberOfStudents)){
                            match.looseControlOnProfessor(previousOwnerOfProfessor, creature);
                            match.acquireControlOnProfessor(creature);

                            response.setProfessorTaken(true);
                            response.setPreviousOwnerOfProfessor(previousOwnerOfProfessor);
                        }
                    }
                }else if(previousOwnerOfProfessor == -1){
                    match.acquireControlOnProfessor(creature);
                }
            }
            // student moved on an island
            else{
                response.setSubObject("action_1_island");
                response.setDestinationIsland_ID(request.getLocation());

                match.moveStudentFromEntranceToIsland(request.getStudent_ID(), request.getLocation());
            }
            // send the ack message in broadcast
            controller.sendMessageAsBroadcast(response);
            studentsMoved++;

            if(controller.getNumberOfPlayers() == 2){
                // if 3 students have been moved we can change state
                if(studentsMoved == 3){
                    controller.nextState();
                }
            }else if(controller.getNumberOfPlayers() == 3){
                // if 4 students have been moved we can change state
                if(studentsMoved == 4){
                    controller.nextState();
                }
            }
        }else{
            System.out.println("ACTION_1: \nexpected message with object [action_1] or [character]" +
                                          "\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }
    }
}
