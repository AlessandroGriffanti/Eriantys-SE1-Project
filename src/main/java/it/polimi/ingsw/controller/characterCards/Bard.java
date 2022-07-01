package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.SupportFunctions;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import it.polimi.ingsw.model.schoolboard.Entrance;
import it.polimi.ingsw.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.messages.serverMessages.AckCharactersMessage;

import java.util.ArrayList;

/**
 * This class represents the character called "bard" (the third to last on the rules file).
 * It allows the players to choose 1 or 2 students from the player's entrance and switch them with,
 * respectively, 1 or 2 students from the player's dining room;
 * if the players gets more students in the dining room than other players he takes control over the
 * corresponding professor.
 */
public class Bard extends Character {
    public Bard(Controller controller) {
        this.controller = controller;
        this.price = 1;
    }

    /**
     * This method controls if there is at least one student in the dining room to use for the
     * switch.
     * @return true if there is at least one student in the dining room, false otherwise
     */
    @Override
    public boolean checkCharacterAvailability() {
        /*String json = controller.getMsg();
        Gson gson = new Gson();
        Message request = gson.fromJson(json, Message.class);
        int player_ID = request.getSender_ID();

        Player user = controller.getMatch().getPlayerByID(player_ID);
        Entrance entrance = user.getSchoolBoard().getEntrance();
        DiningRoom diningRoom = entrance.getDoorToTheDiningRoom();

        // check if there is at least one student in the dining room
        return diningRoom.getTotalNumberOfStudents() > 0;*/
        return true;
    }

    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        Player player = controller.getMatch().getPlayerByID(request.getSender_ID());
        Entrance entrance = player.getSchoolBoard().getEntrance();
        DiningRoom diningRoom = player.getSchoolBoard().getDiningRoom();

        // get the students chosen by the player
        ArrayList<Integer> studentsFromEntrance_ID = request.getStudentsFromPlayerEntrance();
        ArrayList<Creature> studentsFromDiningRoom = request.getStudentsFromPlayerDiningRoom();

        if(studentsFromDiningRoom == null){
            sendAckMessage(player);
            return;
        }

        int previousOwnerEntranceProfessor_ID;
        int previousOwnerDiningRoomProfessor_ID;
        Creature entranceCreature;
        Creature diningRoomCreature;

        int entrance_ID;
        // SWITCH STUDENTS
        for(int i = 0; i < studentsFromEntrance_ID.size(); i++){
            entrance_ID = studentsFromEntrance_ID.get(i);
            entranceCreature = entrance.getStudentsInTheEntrance().get(entrance_ID);
            diningRoomCreature = studentsFromDiningRoom.get(i);

            /* find who is controlling the professor corresponding to the student moved
            from the entrance to the dining room*/
            previousOwnerEntranceProfessor_ID = SupportFunctions.whoControlsTheProfessor(controller.getMatch(), entranceCreature);
            /* find who is controlling the professor corresponding to the student moved
            from the dining room to the entrance*/
            previousOwnerDiningRoomProfessor_ID = SupportFunctions.whoControlsTheProfessor(controller.getMatch(), diningRoomCreature);

            diningRoom.removeStudents(1, diningRoomCreature);
            diningRoom.addStudent(entranceCreature);

            entrance.removeStudent(entrance_ID);
            entrance.addStudentWithIndex(entrance_ID, diningRoomCreature);

            // UPDATE PROFESSORS' CONTROL
            SupportFunctions.updateProfessorControl(controller, previousOwnerEntranceProfessor_ID, entranceCreature);
            SupportFunctions.updateProfessorControl(controller, previousOwnerDiningRoomProfessor_ID, diningRoomCreature);

            sendAckMessage(player);
        }
    }

    /**
     * This method sends the AckCharacterMessage with character set to "bard"
     * @param player reference to the player that used the character card
     */
    private void sendAckMessage(Player player){
        // create and send the ack message
        int coinsReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(player.getID(), "bard", coinsReserve);

        ack.setEntranceOfPlayer(player.getSchoolBoard().getEntrance().getStudentsInTheEntrance());
        ack.setPlayerDiningRoom(player.getSchoolBoard().getDiningRoom().getOccupiedSeats());

        for(int k = 0; k < controller.getNumberOfPlayers(); k++){
            player = controller.getMatch().getPlayerByID(k);
            ack.setPlayerProfessors(k, player.getMyProfessors());
        }

        int coinsOfPlayer = controller.getMatch().getPlayerByID(player.getID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }
}
