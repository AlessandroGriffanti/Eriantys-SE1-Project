package com.gui;

import com.gui.model.Match;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MatchWaitingController implements Initializable {
    /**
     * This attribute is the label that will contain the number of players
     * chosen by the player, inside the match-waiting-scene view
     */
    @FXML
    private Label numberOfPlayer_label;
    /**
     * This attribute is the label that will contain "yes" or "no"
     * based on the fact that the player chose expert mode or not
     */
    @FXML
    private Label expertMode_label;
    /**
     * This attribute is the label that will contain the ID of the player
     */
    @FXML
    private Label playerID_label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EriantysApplication app = EriantysApplication.getCurrentApplication();
        Match match = app.getMatch();

        numberOfPlayer_label.setText(String.valueOf(match.getNumberOfPlayers()));
        playerID_label.setText(String.valueOf(app.getMatch().getThisPlayer_ID()));
        if(match.isExpertMode()){
            expertMode_label.setText("Yes");
        }else{
            expertMode_label.setText("No");
        }
    }
}
