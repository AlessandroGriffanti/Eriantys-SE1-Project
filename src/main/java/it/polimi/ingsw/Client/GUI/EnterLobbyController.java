package com.gui;

import com.gui.messages.fromClient.ReplyChosenLobbyToJoinMessage;
import com.gui.model.Match;
import com.gui.network.ServerHandler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EnterLobbyController {
    /**
     * This attribute is the listView that contains all the available match
     * that the player can join
     */
    @FXML
    private ListView<String> lobbyListView;
    /**
     * This attribute is the button to enter the lobby selected in the listView
     */
    @FXML
    private Button enterLobby_bt;
    /**
     * This attribute is the height of a single element in the listview
     */
    private final int ITEM_HEIGHT = 32;


    /**
     * This method sets the lobbies data
     * @param lobbiesData list of data about the lobbies
     */
    public void displayLobbiesData(ArrayList<String> lobbiesData){
        lobbyListView.getItems().addAll(lobbiesData);
        lobbyListView.setPrefHeight(lobbiesData.size() * ITEM_HEIGHT);
    }

    /**
     * This method controls what lobby the player chose and send a message to inform
     * the server about the choice
     */
    @FXML
    public void enterLobbyClicked(){
        EriantysApplication app = EriantysApplication.getCurrentApplication();
        ServerHandler serverHandler = app.getServerHandler();

        // look what the player chose and send a ReplyChosenLobbyToJoinMessage to the server
        String chosenLobby = lobbyListView.getSelectionModel().getSelectedItem();

        // get the ID
        int chosenLobby_ID = Integer.parseInt(String.valueOf(chosenLobby.charAt(10)));
        int numOfPlayers = Integer.parseInt(String.valueOf(chosenLobby.charAt(35)));
        boolean expertMode = (chosenLobby.substring(54, 57).equals("Yes"));

        // set number of players and if the match is expert mode in the Match class
        Match match = app.getMatch();

        match.setNumberOfPlayers(numOfPlayers);
        match.setExpertMode(expertMode);

        // create the message
        ReplyChosenLobbyToJoinMessage message = new ReplyChosenLobbyToJoinMessage(chosenLobby_ID);
        serverHandler.sendMessage(message);
    }
}
