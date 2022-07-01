package com.gui;

import com.gui.messages.Message;
import com.gui.messages.fromServer.*;
import com.google.gson.Gson;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Receiver implements Consumer<String> {

    /**
     * This attribute is the object used to deserialize the json formatted message
     * received from the server
     */
    private final Gson gson = new Gson();
    /**
     *
     */
    private final EriantysApplication app = EriantysApplication.getCurrentApplication();


    /**
     * This method controls which type of message has been received and based on it
     * performs some action
     * @param json the json message received
     */
    @Override
    public void accept(String json) {

        // control what is the object of the message received
        switch (findOutObject(json)){
            case "NicknameNotValid":
                /*received only when the player chose a non-available nickname*/
                Alert alert = new Alert(Alert.AlertType.ERROR, "The nickname is already used, try with another one!", ButtonType.OK);
                alert.showAndWait();

                app.switchToLoginScene();
                break;
            case "MatchCreation":
                /* received only when the player chose to create a new match*/
                LoginSuccessMessage m1 = gson.fromJson(json, LoginSuccessMessage.class);
                app.switchToNewMatchScene(true, m1.getPlayerID());
                break;
            case "no lobby available":
                /* received only when the player wanted to enter a lobby, but
                 * there are no lobby available*/
                NoLobbyAvailableMessage m2 = gson.fromJson(json, NoLobbyAvailableMessage.class);
                app.switchToNewMatchScene(false, m2.getPlayerID());
                break;
            case "join match":
                /* received only when the player wanted to enter a lobby and there are
                * some lobby available*/
                AskMatchToJoinMessage m3 = gson.fromJson(json, AskMatchToJoinMessage.class);

                // prepare data for all lobbies
                ArrayList<String> lobbiesData = new ArrayList<>();
                for(int i = 0; i < m3.getLobbiesTmp().size(); i++){
                    // show the lobby only if it is available
                    if(m3.getLobbiesTmp().get(i)){
                        lobbiesData.add("Lobby_ID: " + i + ";    " +
                                "Number of players: " + m3.getLobbiesNumberOfPlayers().get(i) + ";    " +
                                "Expert mode: " + (m3.getLobbiesExpertMode().get(i) ? "Yes" : "No")  + ";    ");

                    }
                }
                // switch to enter-lobby-scene
                app.switchToEnterLobbyScene(lobbiesData);
                break;
            case "playerID_set":
                /* received only after the player chose the lobby to join
                * between those available*/
                IDSetAfterLobbyChoiceMessage m4 = gson.fromJson(json, IDSetAfterLobbyChoiceMessage.class);
                app.getMatch().setThisPlayer_ID(m4.getPlayerID());
                app.switchToWaitingScene();
                break;
            case "start":
                MatchStartMessage m5 = gson.fromJson(json, MatchStartMessage.class);
                app.getMatch().setUpTableGame(m5);
                app.switchToTowerChoice(m5.getFirstPlayer(), null);
                break;
            case "ack":
                manageAckMessage(gson.fromJson(json, AckMessage.class));
                break;
            case "nack":
                manageNackMessage(gson.fromJson(json, NackMessage.class));
        }

    }

    /**
     * This method controls what type of ack message is the one received and performs
     * or call the right methods, based on the type
     * @param message ack message received
     */
    private void manageAckMessage(AckMessage message){
        String subObject = message.getSubObject();
        int numOfPlayer = EriantysApplication.getCurrentApplication().getMatch().getNumberOfPlayers();

        switch (subObject){
            case "waiting":
                /*received when the player entered a match that is still waiting for
                * other players to join*/
                app.switchToWaitingScene();
                break;
            case "tower_color":
                /* if all the players chose their towers' color we can go to the deck choice, otherwise
                * we display the choose-tower-color-scene*/
                if(message.getNotAvailableTowerColors().size() == numOfPlayer){
                    app.switchToDeckChoice(message.getNextPlayer(), message.getNotAvailableDecks());
                }else{
                    app.switchToTowerChoice(message.getNextPlayer(), message.getNotAvailableTowerColors());
                }
                break;
            case "deck":
                /*If all the players chose their deck then we can move on to the game*/
                if(message.getNotAvailableDecks().size() == numOfPlayer){
                    app.switchToGame();
                }else{
                    app.switchToDeckChoice(message.getNextPlayer(), message.getNotAvailableDecks());
                }
                break;
        }
    }

    private void manageNackMessage(NackMessage message){
        String subObject = message.getSubObject();

        switch (subObject){

        }
    }

    /**
     * This method extract from the message the object
     * @param json message received
     * @return the object of the message
     */
    private String findOutObject(String json){
        Message message = gson.fromJson(json, Message.class);
        System.out.println(message.getObjectOfMessage()); // todo: remove
        return message.getObjectOfMessage();
    }
}
