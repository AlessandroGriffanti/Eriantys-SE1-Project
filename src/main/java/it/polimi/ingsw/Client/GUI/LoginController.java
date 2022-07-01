package com.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class LoginController  {

    @FXML
    protected ProgressBar progressBar = new ProgressBar();
    @FXML
    protected Button newMatch_bt = new Button();
    @FXML
    protected Button enterLobby_bt = new Button();
    @FXML
    private TextField nickname_tf;


    @FXML
    public void newMatchClicked(MouseEvent event){

        if(checkNickname()){
            sendLoginRequest(true, nickname_tf.getText());
        }
    }

    @FXML
    public void enterLobbyClicked(MouseEvent event){
        if(checkNickname()){
            sendLoginRequest(false, nickname_tf.getText());
        }
    }

    /**
     * This method controls if the nickname consists at least of one character
     * @return true if the nickname is valid
     */
    private boolean checkNickname(){
        String nickname = nickname_tf.getText();

        if (nickname.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Insert a valid username!", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void sendLoginRequest(boolean newMatch, String nickname){

        // progress bar loads and in the meanwhile the LoginMessage is sent
        SendLoginMessageTask loginMessageTask = new SendLoginMessageTask(newMatch, nickname);
        progressBar.progressProperty().bind(loginMessageTask.progressProperty());
        Thread th = new Thread(loginMessageTask);
        th.setDaemon(true);
        th.start();
    }
}