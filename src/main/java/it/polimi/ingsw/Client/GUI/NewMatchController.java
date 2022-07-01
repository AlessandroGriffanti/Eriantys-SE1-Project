package com.gui;

import com.gui.messages.fromClient.MatchSpecsMessage;
import com.gui.model.Match;
import com.gui.network.ServerHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewMatchController implements Initializable {

    @FXML
    private ImageView backgroundImage;
    @FXML
    private CheckBox expertMode_cb;
    @FXML
    private ChoiceBox<Integer> numPlayers_cb;
    @FXML
    private Button createMatchButton;

    /**
     * This method sends the MatchSpecs message to the server
     */
    @FXML
    public void createMatchClicked(){
        EriantysApplication app = EriantysApplication.getCurrentApplication();
        int numOfPlayers = numPlayers_cb.getValue();
        boolean expertMode = expertMode_cb.isSelected();

        // send message with match specs
        ServerHandler sh = app.getServerHandler();
        sh.sendMessage(new MatchSpecsMessage(numOfPlayers, expertMode));

        // set values in match model
        Match match = app.getMatch();
        match.setNumberOfPlayers(numOfPlayers);
        match.setExpertMode(expertMode);
    }

    /*Add the items desired to the ChoiceBox used for the number of players' selection*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Integer> possibleNumOfPlayers = new ArrayList<>();
        possibleNumOfPlayers.add(2);
        possibleNumOfPlayers.add(3);

        numPlayers_cb.getItems().addAll(possibleNumOfPlayers);
    }

}
