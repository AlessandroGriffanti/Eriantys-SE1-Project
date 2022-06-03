package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Action_2;
import it.polimi.ingsw.controller.ChooseAssistantCard;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.characterCards.Herbalist;
import it.polimi.ingsw.controller.characterCards.Jester;
import it.polimi.ingsw.controller.characterCards.Messenger;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import org.junit.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class performs some tests on the character cards
 *
 * PLEASE PAY ATTENTION:
 * before running this tests we need to comment the lines inside the methode
 * messageToSerialize of the class ClientHandler; this is because otherwise we receive
 * an error of outputStream not initialized.
 */
public class CharactersTest {

    /**
     * This method checks if the herbalist character is used correctly:
     * - character used by player to put a no-entry-tile on an island
     * - when mother nature arrives to that island the no-entry-tile goes back to the character
     */
    @Test
    public void HerbalistTest(){
        Controller controller = createMatch();
        String json = "";

        // ACTION_2
        controller.setState(new Action_2());

        // we create a new herbalist as it has been chosen by the game control
        controller.getCharactersManager().getCards().put("herbalist", new Herbalist(controller));
        Herbalist herbalist = (Herbalist) controller.getCharactersManager().getCards().get("herbalist");

        // mario uses herbalist
        json = "{" +
                "\"object\": \"character_request\"," +
                "\"sender_ID\": \"0\", " +
                "\"character\": \"herbalist\" }";
        controller.manageMsg(json);

        json = "{" +
                "\"object\": \"character_data\"," +
                "\"sender_ID\": \"0\", " +
                "\"character\": \"herbalist\"," +
                "\"island_ID\": \"1\" }";
        controller.manageMsg(json);

        Archipelago island = controller.getMatch().getRealmOfTheMatch().getArchipelagos().get(1);
        assertTrue(island.getNoEntryTiles() == 1);

        assertTrue(herbalist.getPrice() == 2 + 1);
        assertTrue(herbalist.getTilesOnTheCard() == 3);


        // set mother nature on island 10
        controller.getMatch().getRealmOfTheMatch().setDestinationOfMotherNature(11);
        assert controller.getMatch().getPositionOfMotherNature() == 11;

        // mario moves mother nature to island 2
        json = "{ \"object\": \"action_2\", \"sender_ID\": \"0\", \"destinationIsland_ID\": \"1\" }";
        controller.manageMsg(json);

        assert controller.getMatch().getPositionOfMotherNature() == 1;
        assertTrue(island.getNoEntryTiles() == 0);
        assertTrue(herbalist.getTilesOnTheCard() == 4);
        assertEquals(3, controller.getCharactersManager().getCards().get("herbalist").getPrice());
    }

    /**
     * This method controls that the messenger character card can be used correctly
     */
    @Test
    public void messengerTest_twoMoreSteps(){
        Controller controller = createMatch();
        String json = "";

        // ACTION_2
        controller.setState(new Action_2());
        // set mother nature position
        controller.getMatch().getRealmOfTheMatch().setDestinationOfMotherNature(5);

        // ADD CHARACTER
        controller.getCharactersManager().getCards().put("messenger", new Messenger(controller));
        // USE CHARACTER MESSENGER
        json = "{ \"object\": \"character_request\", \"sender_ID\": \"0\", \"character\": \"messenger\" }";
        controller.manageMsg(json);

        json = "{ \"object\": \"character_data\", \"sender_ID\": \"0\", \"character\": \"messenger\" }";
        controller.manageMsg(json);

        // MOVE MOTHER NATURE TWO STEPS FARTHER THAN THE ASSISTANT'S VALUE
        json = "{ \"object\": \"action_2\", \"sender_ID\": \"0\", \"destinationIsland_ID\": \"9\" }";
        controller.manageMsg(json);

        assertEquals(9, controller.getMatch().getPositionOfMotherNature());
        assertEquals(2, controller.getCharactersManager().getCards().get("messenger").getPrice());
        assertFalse(controller.getCharactersManager().isMessengerActive());
    }

    /**
     * This method controls if the jester character can be used correctly
     * N.B. It's possible to visually see if the action was performed correctly
     * checking the print on std output
     */
    @Test
    public void jesterTest(){
        Controller controller = createMatch();
        String json = "";

        Player mario = controller.getMatch().getPlayerByID(0);
        //System.out.println("Initial entrance of Mario: " + mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance());

        Creature playerStudent_1 = mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(1);
        Creature playerStudent_2 = mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(2);
        Creature playerStudent_3 = mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(3);

        // ADD CHARACTER
        controller.getCharactersManager().getCards().put("jester", new Jester(controller));
        Jester jester = (Jester)controller.getCharactersManager().getCards().get("jester");
        //System.out.println("Initial students on the card: " + jester.getStudentsOnCard());

        Creature jesterStudent_5 = jester.getStudentsOnCard().get(5);
        Creature jesterStudent_4 = jester.getStudentsOnCard().get(4);
        Creature jesterStudent_3 = jester.getStudentsOnCard().get(3);

        // ask to use character jester
        json = "{ \"object\": \"character_request\", \"sender_ID\": \"0\", \"character\": \"jester\" }";
        controller.manageMsg(json);

        // use character jester
        CharacterDataMessage dataMessage = new CharacterDataMessage(0, "jester");
        ArrayList<Integer> students = new ArrayList<Integer>(List.of(1, 2, 3));
        dataMessage.setStudentsFromPlayerEntrance(students);
        students = new ArrayList<Integer>(List.of(5, 4, 3));
        dataMessage.setElementsFromCard(students);

        Gson gson = new Gson();
        json = gson.toJson(dataMessage);

        controller.manageMsg(json);

        assertEquals(2, controller.getCharactersManager().getCards().get("jester").getPrice());
        assertEquals(playerStudent_1, jester.getStudentsOnCard().get(5));
        assertEquals(jesterStudent_5, mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(1));

        assertEquals(playerStudent_2, jester.getStudentsOnCard().get(4));
        assertEquals(jesterStudent_4, mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(2));

        assertEquals(playerStudent_3, jester.getStudentsOnCard().get(3));
        assertEquals(jesterStudent_3, mario.getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(3));

    }

    @Test
    public void ambassadorTest(){
        Controller controller = createMatch();
        String json = "";


    }

    /**
     * This method creates a simple match with:
     * - two players [mario & luigi]
     * - mario chooses deck: CLOUDWITCH; tower-color: black; assistant: [3, 2]
     * - luigi chooses deck: FORESTWIZARD; tower_color: white; assistant [6, 3]
     * @return reference to the controller of the match
     */
    private Controller createMatch(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        String json = "";

        // mario chooses tower-color
        json = "{ \"object\": \"tower_color\", \"sender_ID\": \"0\", \"color\": \"BLACK\" }";
        controller.manageMsg(json);
        // mario chooses tower-color
        json = "{ \"object\": \"tower_color\", \"sender_ID\": \"1\", \"color\": \"WHITE\" }";
        controller.manageMsg(json);

        // mario chooses deck
        json = "{ \"object\": \"deck\", \"sender_ID\": \"0\", \"deck\": \"CLOUDWITCH\" }";
        controller.manageMsg(json);
        // mario chooses deck
        json = "{ \"object\": \"deck\", \"sender_ID\": \"1\", \"deck\": \"FORESTWIZARD\" }";
        controller.manageMsg(json);

        // refill the clouds
        json = "{ \"object\": \"draw\" }";
        controller.manageMsg(json);

        // CHOOSE ASSISTANT CARD
        controller.setState(new ChooseAssistantCard());
        // mario chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);
        // luigi chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"1\", \"assistantChosen\": \"6\" }";
        controller.manageMsg(json);

        return controller;
    }

    /**
     * This method
     * @return
     */
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
