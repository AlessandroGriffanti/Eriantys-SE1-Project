package com.gui;

import com.gui.messages.fromClient.ChosenDeckMessage;
import com.gui.model.Wizard;
import com.gui.network.ServerHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class ChooseDeckController {
    @FXML
    private Button forestWizard_bt;
    @FXML
    private Button desertWizard_bt;
    @FXML
    private Button cloudsWitch_bt;
    @FXML
    private Button lightningWizard_bt;
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
    public void forestWizardClicked(){
        if(yourTurn){
            ChosenDeckMessage message = new ChosenDeckMessage(Wizard.FORESTWIZARD);
            // set deck in model
            EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setWizard(Wizard.FORESTWIZARD);
            serverHandler.sendMessage(message);
        }else{
            notYourTurnAlert();
        }
    }

    @FXML
    public void desertWizardClicked(){
        if(yourTurn){
            ChosenDeckMessage message = new ChosenDeckMessage(Wizard.DESERTWIZARD);
            // set deck in model
            EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setWizard(Wizard.DESERTWIZARD);
            serverHandler.sendMessage(message);
        }else{
            notYourTurnAlert();
        }
    }

    @FXML
    public void cloudsWitchClicked(){
        if(yourTurn){
            ChosenDeckMessage message = new ChosenDeckMessage(Wizard.CLOUDSWITCH);
            // set deck in model
            EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setWizard(Wizard.CLOUDSWITCH);
            serverHandler.sendMessage(message);
        }else{
            notYourTurnAlert();
        }
    }

    @FXML
    public void LightningWizardClicked(){
        if(yourTurn){
            ChosenDeckMessage message = new ChosenDeckMessage(Wizard.LIGHTNINGWIZARD);
            // set deck in model
            EriantysApplication.getCurrentApplication().getMatch().getThisPlayer().setWizard(Wizard.LIGHTNINGWIZARD);
            serverHandler.sendMessage(message);
        }else{
            notYourTurnAlert();
        }
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

    public void setUpScene(int currentPlayer, ArrayList<Wizard> notAvailableDecks){
        this.serverHandler = EriantysApplication.getCurrentApplication().getServerHandler();
        this.player_ID = EriantysApplication.getCurrentApplication().getMatch().getThisPlayer_ID();

        if(currentPlayer == player_ID){
            this.yourTurn = true;

            if(notAvailableDecks != null){

                for(Wizard wizard: notAvailableDecks){
                    if(wizard == Wizard.FORESTWIZARD){
                        forestWizard_bt.setDisable(true);
                    }else if(wizard == Wizard.DESERTWIZARD){
                        desertWizard_bt.setDisable(true);
                    }else if(wizard == Wizard.CLOUDSWITCH){
                        cloudsWitch_bt.setDisable(true);
                    }else if(wizard == Wizard.LIGHTNINGWIZARD){
                        lightningWizard_bt.setDisable(true);
                    }
                }

            }

        }else{
            this.prohibitionBar.setVisible(true);
            this.yourTurn = false;

            forestWizard_bt.setOpacity(0.5);
            desertWizard_bt.setOpacity(0.5);
            cloudsWitch_bt.setOpacity(0.5);
            lightningWizard_bt.setOpacity(0.5);
        }
    }
}
