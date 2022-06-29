package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.SupportFunctions;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

import java.util.HashMap;

/**
 * This class represents the character card called "trafficker".
 * It allows players to remove from their own dining room and from those of the
 * other players 3 students from a specific table; if a player does not
 * have 3 students of the kind chosen by the user of the card then will be
 * taken just the students of the type that he owns (could be 0).
 */
public class Trafficker extends Character {

    /**
     * This attribute is the reference to the bag used in the match
     */
    Bag bag;

    public Trafficker(Controller controller) {
        this.controller = controller;
        this.price = 3;

        this.bag = controller.getMatch().getBagOfTheMatch();
    }

    /**
     * This method controls if the character can be used (in this case the card can always be used
     * because there are no restrictions)
     * @return always true
     */
    @Override
    public boolean checkCharacterAvailability() {
        return true;
    }

    /**
     * This method removes 3 students or fewer, from the dining room of
     * each player; if there are less than 3 students of the chosen type
     * then will be removed just those.
     * @param request message sent by the client containing the type of students to remove
     */
    @Override
    public void effect(CharacterDataMessage request) {
        increasePrice();

        Creature creatureChosen = request.getCreature();
        Player player;
        DiningRoom diningRoom;
        int numberStudentsRemoved;

        // find who are the professors' masters before using the card
        int previousProfessorOwner_ID = SupportFunctions.whoControlsTheProfessor(controller.getMatch(), creatureChosen);

        // EFFECT
        /* remove from all players' dining room three students or fewer
        * (if there are not 3 students in the player's dining room)*/
        for(int i = 0; i < controller.getNumberOfPlayers(); i++){
            player = controller.getMatch().getPlayerByID(i);

            // remove from dining room
            diningRoom = player.getSchoolBoard().getDiningRoom();
            numberStudentsRemoved = diningRoom.removeStudents(3, creatureChosen);

            // put into the bag the removed students
            bag.addStudentsOfType(numberStudentsRemoved, creatureChosen);
        }

        // UPDATE PROFESSORS' CONTROL
        SupportFunctions.updateProfessorControl(controller, previousProfessorOwner_ID, creatureChosen);

        // create and send ack message
        int coinsReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "trafficker", coinsReserve);
        ack.setCreature(creatureChosen);

        for(int k = 0; k < controller.getNumberOfPlayers(); k++){
            player = controller.getMatch().getPlayerByID(k);
            ack.setPlayerProfessors(k, player.getMyProfessors());
            ack.addPlayerDiningRoom(k, player.getSchoolBoard().getDiningRoom().getOccupiedSeats());
        }

        int coinsOfPlayer = controller.getMatch().getPlayerByID(request.getSender_ID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }

}
