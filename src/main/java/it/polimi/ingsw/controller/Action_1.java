package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.messages.AckMessage;
import it.polimi.ingsw.network.messages.MovedStudentsFromEntrance;

public class Action_1 implements ControllerState{

    private int studentsMoved = 0;
    private boolean characterUseRequest = false;


    @Override
    public void nextState(Controller controller) {
        if (characterUseRequest){
            /* todo: state of character's management
                   could be better having a parallel state into this one (the same for the nest states)*/
        }else{
            controller.setState(new Action_2());
        }
    }

    @Override
    public void stateExecution(Controller controller) {
        Match match = controller.getMatch();

        String json = controller.getMsg();
        MovedStudentsFromEntrance request = gson.fromJson(json, MovedStudentsFromEntrance.class);

        if(!(request.getObjectOfMessage().equals("action_1"))){
            System.out.println("ACTION_1: \nexpected message with object [action_1]\nreceived message with object["+ request.getObjectOfMessage() + "]");
        }else{

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

                int previousOwnerOfProfessor;

                // who is controlling the professor of the same type as the student chosen ?
                previousOwnerOfProfessor = whoControlsTheProfessor(match, creature);

                // if the player controlling the professor is another player then...
                if(previousOwnerOfProfessor != request.getSender_ID() && previousOwnerOfProfessor != -1){

                    int previousNumberOfStudents = match.getPlayerByID(previousOwnerOfProfessor).getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(creature);
                    int currentPlayerStudents = match.getPlayerByID(request.getSender_ID()).getSchoolBoard().getDiningRoom().getOccupiedSeatsAtTable(creature);

                    //the current player takes control over the professor only if it has more students than the old player
                    if(currentPlayerStudents +1 > previousNumberOfStudents){
                        match.looseControlOnProfessor(previousOwnerOfProfessor, creature);
                        match.acquireControlOnProfessor(creature);

                        response.setProfessorTaken(true);
                        response.setPreviousOwnerOfProfessor(previousOwnerOfProfessor);
                    }
                }else if(previousOwnerOfProfessor == -1){
                    match.acquireControlOnProfessor(creature);
                }

                // move the student from entrance to dining room
                match.moveStudentFromEntranceToDiningRoom(request.getStudent_ID());
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
        }
    }


    /**
     * This method finds which player is currently controlling the professor given as argument
     * @param match reference to the match (model)
     * @param creature the kind of professor we are interested in
     * @return ID of the player controlling the professor or -1 if no player controls it
     */
    private int whoControlsTheProfessor(Match match, Creature creature){
        int player_ID = -1;

        for(int i = 0; i < match.getPlayers().size(); i++){

            if(match.getPlayers().get(i).getSchoolBoard().getProfessorTable().isOccupied(creature)){
                assert player_ID == -1 : "ACTION_1 controller state:\ntwo players simultaneously " +
                                         "controlling the" + creature + "creature";
                player_ID = i;
            }
        }

        // if nobody controls the creature it should be in the notControlledProfessors list
        if(player_ID == -1){
            assert match.getNotControlledProfessors().contains(creature) :
                "The creature is not yet controlled but it is not inside the notoControlledProfessors attribute" +
                "inside the class match";
        }

        return player_ID;
    }

}
