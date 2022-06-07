package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.controller.characterCards.CharactersManager;
import it.polimi.ingsw.controller.characterCards.Herbalist;
import it.polimi.ingsw.controller.characterCards.Messenger;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.schoolboard.DiningRoom;
import it.polimi.ingsw.model.schoolboard.Entrance;
import it.polimi.ingsw.model.schoolboard.ProfessorTable;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import org.junit.Test;

import java.net.Socket;
import java.util.ArrayList;
import static org.junit.Assert.*;

/*Note: when running this tests it is necessary to comment the content of
*       messageToSerialize method of ClientHandler class*/
public class ControllerTest {

    /**
     * This method controls if the method inside the controller class can correctly find the next
     * player of a planning phase
     */
    @Test
    public void nextPlayer_planningPhase(){
        Controller controller= createController();

        assertTrue(controller.nextPlayer(0) == 1);
        assertTrue(controller.nextPlayer(1) == 2);
        assertTrue(controller.nextPlayer(2) == 0);

    }

    /**
     * This method controls if the method inside the controller class can correctly find the next
     * player of an action phase
     */
    @Test
    public void nextPlayer_actionPhase(){
        Controller controller = createController();

        // set the order of action phase
        ArrayList<Integer> order = new ArrayList<>();
        order.add(2);
        order.add(0);
        order.add(1);

        controller.setActionPhase(true);
        controller.setActionPhaseOrder(order);

        assertTrue(controller.nextPlayer(2) == 0);
        assertTrue(controller.nextPlayer(0) == 1);
        assertTrue(controller.nextPlayer(1) == 2);

    }

    private Controller createController(){
        Controller controller = new Controller(0);

        ClientHandler c1 = new ClientHandler(new Socket(), new Server(4444));
        ClientHandler c2 = new ClientHandler(new Socket(), new Server(4444));
        ClientHandler c3 = new ClientHandler(new Socket(), new Server(4444));

        controller.addPlayerHandler(c1, "mario");
        controller.addPlayerHandler(c2, "luigi");
        controller.addPlayerHandler(c3, "toad");

        MatchSpecsMessage msg = new MatchSpecsMessage();
        msg.setExpertMode(true);
        msg.setNumOfPlayers(3);
        msg.setSender_ID(0);

        Gson gson = new Gson();
        String json = gson.toJson(msg);
        controller.manageMsg(json);

        return controller;
    }


    // TESTS ON 'MATCH_CREATING' STATE
    @Test
    public void matchCreating_rightOrder(){
        Gson gson = new Gson();

        ClientHandler c1 = new ClientHandler(new Socket(), new Server(4444));
        ClientHandler c2 = new ClientHandler(new Socket(), new Server(4444));

        Controller controller = new Controller(0);

        controller.addPlayerHandler(c1, "mario");

        Message requestClass = clientCreateSpecs();
        String request = gson.toJson(requestClass);
        controller.manageMsg(request);

        assertTrue(controller.getMatch() == null);

        controller.addPlayerHandler(c2, "luigi");

        assertTrue(controller.getMatch().getNumberOfPlayers() == 2);
        assertTrue(controller.getMatch().isExpertMode() == true);

        assertEquals("mario", controller.getMatch().getPlayerByID(0).getNickName());
        assertEquals("luigi", controller.getMatch().getPlayerByID(1).getNickName());

    }

    @Test
    public void matchCreating_addTwoPlayerAndThenReceiveSpecs(){
        Gson gson = new Gson();

        ClientHandler c1 = new ClientHandler(new Socket(), new Server(4444));
        ClientHandler c2 = new ClientHandler(new Socket(), new Server(4444));

        Controller controller = new Controller(0);

        controller.addPlayerHandler(c1, "mario");
        controller.addPlayerHandler(c2, "luigi");

        Message requestClass = clientCreateSpecs();
        String request = gson.toJson(requestClass);
        controller.manageMsg(request);

        assertTrue(controller.getMatch().getNumberOfPlayers() == 2);
        assertTrue(controller.getMatch().isExpertMode() == true);

        assertEquals("mario", controller.getMatch().getPlayerByID(0).getNickName());
        assertEquals("luigi", controller.getMatch().getPlayerByID(1).getNickName());
    }

    // TESTS ON ACTION_1 STATE

    /**
     * This method controls if one student is correctly moved from the entrance to the dining room
     */
    @Test
    public void action_1_studentToDiningRoom(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        Entrance luigi_entrance = match.getPlayers().get(1).getSchoolBoard().getEntrance();
        DiningRoom luigi_diningRoom = match.getPlayers().get(1).getSchoolBoard().getDiningRoom();
        ProfessorTable luigi_professorTable = match.getPlayers().get(1).getSchoolBoard().getProfessorTable();

        // let's jump to action_1 state
        controller.setState(new Action_1());

        //luigi moves a student into the dining room
        Creature creature = luigi_entrance.getStudentsInTheEntrance().get(2);
        String json = "{ \"object\": \"action_1\", \"sender_ID\": \"1\", \"student_ID\": \"2\", \"location\": \"-1\" }";

        controller.manageMsg(json);

        // the student with index 2 is no more in the entrance
        assertTrue(luigi_entrance.getStudentsInTheEntrance().get(2) == null);

        // the table of the student's type has only one seat occupied
        assertTrue(luigi_diningRoom.getOccupiedSeatsAtTable(creature) == 1);

        // luigi must have acquired the control over the professor
        assertTrue(luigi_professorTable.isOccupied(creature));
    }

    /**
     * This method controls if the student is correctly moved from the entrance to an island
     */
    @Test
    public void action_1_studentToIsland(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        Entrance mario_entrance = match.getPlayers().get(0).getSchoolBoard().getEntrance();

        // let's jump to action_1 state
        controller.setState(new Action_1());

        Creature creature = match.getStudentInEntranceOfPlayerByID(0, 5);
        System.out.println("Initial position of mother nature: " + match.getPositionOfMotherNature());
        int previousPopulationOfType = match.getRealmOfTheMatch().getArchipelagos().get(4).getStudentsOfType(creature);

        // mario moves a student on an island
        String json = "{ \"object\": \"action_1\", \"sender_ID\": \"0\", \"student_ID\": \"5\", \"location\": \"4\" }";
        controller.manageMsg(json);

        // the number of student of the same type as the student moved increased by 1
        assertTrue(match.getRealmOfTheMatch().getArchipelagos().get(4).getStudentsOfType(creature) == previousPopulationOfType+1);

        // the student with index 5 is no more in the entrance
        assertTrue(mario_entrance.getStudentsInTheEntrance().get(5) == null);
    }

    // TESTS ON CHOOSE_ASSISTANT_CARD STATE

    /**
     * This method controls if a nack message is sent when the player chooses an invalid assistant card
     */
    @Test
    public void chooseAssistantCardState_invalidChoice(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        Player mario = match.getPlayers().get(0);
        mario.chooseDeck(Wizard.CLOUDWITCH);

        Player luigi = match.getPlayers().get(1);
        luigi.chooseDeck(Wizard.DESERTWIZARD);

        // let's jump to ChooseAssistantCard state
        controller.setState(new ChooseAssistantCard());

        // mario chooses his assistant card
        String json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);

        // luigi chooses the same card as mario, so it will receive a nack message
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"1\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);
    }

    /**
     * This method controls if the modifications made to the model are consistent with the request from the player
     */
    @Test
    public void chooseAssistantCardState_validChoices(){
        Controller controller = createLobby();
        Match match = controller.getMatch();
        String json = "";

        Player mario = match.getPlayers().get(0);
        mario.chooseDeck(Wizard.CLOUDWITCH);

        Player luigi = match.getPlayers().get(1);
        luigi.chooseDeck(Wizard.DESERTWIZARD);

        // let's jump to ChooseAssistantCard state
        controller.setState(new ChooseAssistantCard());

        // mario chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"6\" }";
        controller.manageMsg(json);

        // luigi chooses his card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"1\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);

        assertTrue(mario.getAssistantsDeck().getWizard().equals(Wizard.CLOUDWITCH));
        assertTrue(mario.getAssistantsDeck().getLastUsedCard().getPlayOrderValue() == 6);
        assertTrue(mario.getAssistantsDeck().getLastUsedCard().getMotherNatureMovementValue() == 3);

        assertTrue(luigi.getAssistantsDeck().getWizard().equals(Wizard.DESERTWIZARD));
        assertTrue(luigi.getAssistantsDeck().getLastUsedCard().getPlayOrderValue() == 3);
        assertTrue(luigi.getAssistantsDeck().getLastUsedCard().getMotherNatureMovementValue() == 2);

        // action phase order should be [ 1, 0 ]
        assertTrue(controller.getActionPhaseOrder().get(0) == 1);
        assertTrue(controller.getActionPhaseOrder().get(1) == 0);

    }

    /**
     * This method controls if action_2 state can find an invalid movement;
     * we expect a message to be print on console saying : "Nack sent for invalid mother nature movement"
     */
    @Test
    public void action_2_movementNotValid(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        String json = "";

        Player mario = match.getPlayers().get(0);
        mario.chooseDeck(Wizard.CLOUDWITCH);

        Player luigi = match.getPlayers().get(1);
        luigi.chooseDeck(Wizard.DESERTWIZARD);

        // let's jump to ChooseAssistantCard state
        controller.setState(new ChooseAssistantCard());
        // mario chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"6\" }";
        controller.manageMsg(json);
        // luigi chooses his card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"1\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);

        // let's jump to action_2 state
        controller.setState(new Action_2());
        // put mother nature on island_3
        match.getRealmOfTheMatch().setDestinationOfMotherNature(2);

        // mario sends a message for action_2
        json = "{ \"object\": \"action_2\", \"sender_ID\": \"0\", \"destinationIsland_ID\": \"6\" }";
        controller.manageMsg(json);
    }

    @Test
    public void action_2_correctMovement(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        String json = "";

        Player mario = match.getPlayers().get(0);
        mario.chooseDeck(Wizard.CLOUDWITCH);

        Player luigi = match.getPlayers().get(1);
        luigi.chooseDeck(Wizard.DESERTWIZARD);

        // let's jump to ChooseAssistantCard state
        controller.setState(new ChooseAssistantCard());
        // mario chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"6\" }";
        controller.manageMsg(json);
        // luigi chooses his card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"1\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);

        // let's jump to action_2 state
        controller.setState(new Action_2());
        // put mother nature on island_3
        match.getRealmOfTheMatch().setDestinationOfMotherNature(3);


        // mario sends a message for action_2
        json = "{ \"object\": \"action_2\", \"sender_ID\": \"0\", \"destinationIsland_ID\": \"6\" }";
        controller.manageMsg(json);

        Archipelago island = match.getRealmOfTheMatch().getArchipelagos().get(6);
        assertTrue(match.getRealmOfTheMatch().getPositionOfMotherNature() == 6);

        assertTrue(island.getTowerColor() == null);
        assertTrue(island.getMasterOfArchipelago() == null);
    }


    private Controller createLobby(){
        Gson gson = new Gson();
        Controller controller = new Controller(0);

        ClientHandler c1 = new ClientHandler(new Socket(), new Server(4444));
        ClientHandler c2 = new ClientHandler(new Socket(), new Server(4444));

        controller.addPlayerHandler(c1, "mario");
        controller.addPlayerHandler(c2, "luigi");

        Message requestClass = clientCreateSpecs();
        String request = gson.toJson(requestClass);
        controller.manageMsg(request);

        return controller;
    }

    private MatchSpecsMessage clientCreateSpecs(){
        MatchSpecsMessage msg = new MatchSpecsMessage();
        msg.setExpertMode(true);
        msg.setNumOfPlayers(2);
        msg.setSender_ID(0);

        return msg;
    }
}
