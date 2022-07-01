package com.gui;

import com.gui.messages.fromClient.ChosenTowerColorMessage;
import com.gui.model.Tower;
import com.gui.network.ServerHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class ChooseTowerController {
    @FXML
    private Button whiteTower_bt;
    @FXML
    private Button greyTower_bt;
    @FXML
    private Button blackTower_bt;
    /**
     * This attribute is true if it's the turn of the player, false otherwise
     */
    private boolean yourTurn;
    @FXML
    Line prohibitionBar;
    /**
     * This attribute is the reference to the server handler, used to send messages
     * to the server
     */
    private ServerHandler serverHandler;
    /**
     * This attribute is the ID of the player
     */
    private int player_ID;

    @FXML
    public void whiteTowerClicked(){
        if(!yourTurn){
            notYourTurnAlert();
            return;
        }
        ChosenTowerColorMessage message = new ChosenTowerColorMessage(player_ID, Tower.WHITE);
        // set tower color in model
        //EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setTowerColor(Tower.WHITE);
        serverHandler.sendMessage(message);
    }

    @FXML
    public void greyTowerClicked(){
        if(!yourTurn){
            notYourTurnAlert();
            return;
        }
        ChosenTowerColorMessage message = new ChosenTowerColorMessage(player_ID, Tower.GREY);
        // set tower color in model
        //EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setTowerColor(Tower.GREY);
        serverHandler.sendMessage(message);
    }

    @FXML
    public void blackTowerClicked(){
        if(!yourTurn){
            notYourTurnAlert();
            return;
        }
        ChosenTowerColorMessage message = new ChosenTowerColorMessage(player_ID, Tower.BLACK);
        // set tower color in model
        //EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setTowerColor(Tower.BLACK);
        serverHandler.sendMessage(message);
    }

    /**
     * This method launches a thread to display an INFORMATION message;
     * called when the player tries to click the buttons when it's not his turn.
     */
    private void notYourTurnAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "This is not your turn, please wait for your turn!",
                ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * This method display a different scene based on the fact that the player is the current player
     * or not
     * @param currentPlayerID ID of the current player
     * @param towerNotAvailable list of already chosen towers' colors
     */
    public void setUpScene(int currentPlayerID, ArrayList<Tower> towerNotAvailable){
        this.player_ID = EriantysApplication.getCurrentApplication().getMatch().getThisPlayer_ID();
        this.serverHandler = EriantysApplication.getCurrentApplication().getServerHandler();

        if(currentPlayerID == player_ID){
            this.yourTurn = true;
            Button button_tmp = null;

            if(towerNotAvailable != null){
                for(Tower t: Tower.values()){
                    if(t == Tower.WHITE){
                        button_tmp = whiteTower_bt;
                    }else if(t == Tower.GREY){
                        button_tmp = greyTower_bt;
                    }else if(t == Tower.BLACK){
                        button_tmp = blackTower_bt;
                    }

                    if(towerNotAvailable.contains(t)){
                        assert button_tmp != null;
                        button_tmp.setDisable(true);
                    }
                }
            }
        }else {
            this.prohibitionBar.setVisible(true);
            this.yourTurn = false;

            whiteTower_bt.setOpacity(0.5);
            greyTower_bt.setOpacity(0.5);
            blackTower_bt.setOpacity(0.5);
        }

    }

}
