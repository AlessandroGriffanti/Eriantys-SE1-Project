package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Action_2;
import it.polimi.ingsw.controller.ChooseAssistantCard;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.characterCards.Herbalist;
import it.polimi.ingsw.controller.characterCards.Messenger;
import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharactersTest {

    /**
     * This method checks if the herbalist character is used correctly:
     * - character used by player to put a no-entry-tile on an island
     * - when mother nature arrives to that island the no-entry-tile goes back to the character
     */
    @Test
    public void HerbalistTest(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        String json = "";

        // mario chooses the deck
        Player mario = match.getPlayers().get(0);
        mario.chooseDeck(Wizard.CLOUDWITCH);

        // CHOOSE ASSISTANT CARD
        controller.setState(new ChooseAssistantCard());

        // mario chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"9\" }";
        controller.manageMsg(json);

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
        controller.getMatch().getRealmOfTheMatch().setDestinationOfMotherNature(10);
        assert controller.getMatch().getPositionOfMotherNature() == 10;

        // mario moves mother nature to island 2
        json = "{ \"object\": \"action_2\", \"sender_ID\": \"0\", \"destinationIsland_ID\": \"1\" }";
        controller.manageMsg(json);

        assert controller.getMatch().getPositionOfMotherNature() == 1;
        assertTrue(island.getNoEntryTiles() == 0);
        assertTrue(herbalist.getTilesOnTheCard() == 4);
    }

    /**
     * This method controls that the messenger character card can be used correctly
     */
    @Test
    public void messengerTest_twoMoreSteps(){
        Controller controller = createLobby();
        Match match = controller.getMatch();

        String json = "";

        // mario chooses the deck
        Player mario = match.getPlayers().get(0);
        mario.chooseDeck(Wizard.CLOUDWITCH);

        // CHOOSE ASSISTANT CARD
        controller.setState(new ChooseAssistantCard());

        // mario chooses his assistant card
        json = "{ \"object\": \"assistant_chosen\", \"sender_ID\": \"0\", \"assistantChosen\": \"3\" }";
        controller.manageMsg(json);

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

        assertTrue(controller.getMatch().getPositionOfMotherNature() == 9);
        assertFalse(controller.getCharactersManager().isMessengerActive());
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
