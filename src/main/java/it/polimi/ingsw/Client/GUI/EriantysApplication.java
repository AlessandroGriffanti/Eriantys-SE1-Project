package com.gui;

import com.gui.messages.fromClient.PingMessage;
import com.gui.model.Match;
import com.gui.model.Tower;
import com.gui.model.Wizard;
import com.gui.network.ServerHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class EriantysApplication extends Application implements Initializable {

    private Stage primaryStage;
    @FXML
    TextField serverAddress_tf = new TextField();
    @FXML
    TextField serverPort_tf = new TextField();
    @FXML
    Button connectionButton = new Button();

    /**
     * This  attribute is the controller of the game part, after the login and
     * creation / joining of the match
     */
    private GameController gameController;
    private static EriantysApplication currentApplication;
    /**
     * This attribute is the reference to the serverHandler, it's used to
     * receive and send messages from and to the server
     */
    private ServerHandler serverHandler;
    /**
     * This attribute is the reference to the match model
     */
    private Match match;
    /**
     * This attribute is the reference to the game controller, which
     * takes care of all the stuff related with the actual game phase
     */
    private GameController gameControl;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        serverAddress_tf.setText("127.0.0.1");
        serverPort_tf.setText("4444");
    }

    // When using IntelliJ, don't run the application from here, use the main
    // method in GUIMain
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        currentApplication = this;
        match = new Match();

        this.primaryStage = stage;
        primaryStage.setOnCloseRequest((event) -> {
            if (serverHandler.isConnected())
                serverHandler.closeConnection();
        });
        this.serverHandler = new ServerHandler();
        serverHandler.setConnectionClosedObserver(() -> {
            Platform.runLater(() -> {
                if (primaryStage.isShowing()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "The connection was closed.", ButtonType.OK);
                    alert.showAndWait();
                    switchToConnectionScene();
                }
            });
        });

        switchToConnectionScene();
        primaryStage.show();
    }

    /**
     * This method layout the connection scene where the player can specify the
     * server address and server port
     */
    private void switchToConnectionScene(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/connection-scene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Connect");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(GUIMain.class.getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.show();
    }

    /**
     * When the CONNECT button is clicked in connection-scene: this method connects the
     * app to the server reading the IP address and server port from the text fields in connection-scene
     * @param event the event of clicking on the CONNECT button in connection scene
     */
    @FXML
    public void connect(MouseEvent event){
        // start thread that sends ping messages to the server
        new Thread(() -> {
            while(true){
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                    currentApplication.getServerHandler().sendMessage(new PingMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        // set the stage
        this.primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();

        serverHandler = EriantysApplication.currentApplication.serverHandler;
        // set connectionCompleteObserver
        serverHandler.setConnectionCompleteObserver((ok) -> {
            Platform.runLater(() -> {
                if (!ok) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Connection not successful...", ButtonType.OK);
                    alert.showAndWait();
                    switchToConnectionScene();
                    connectionButton.setDisable(false);
                } else {
                    System.out.println("Connection complete.");
                    serverHandler.setConnectionCompleteObserver(null);
                }
            });
        });

        // set messageArrivedObserver: when a message arrives the message-receiver is called
        serverHandler.setMessageArrivedObserver((message) -> {
            Platform.runLater(() -> {
                Receiver receiver = new Receiver();
                receiver.accept(message);
            });
        });

        String ip = serverAddress_tf.getText();
        int port;

        try {
            port = Integer.parseInt(serverPort_tf.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The port number is not valid!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if((!ip.equals(""))){
            serverHandler.attemptConnection(ip, port);
        }

        // change scene
        switchToLoginScene();
    }

    /**
     * This method layout the login-scene where the player can choose his nickname and weather to
     * create a new match or to enter an existing one
     */
    public void switchToLoginScene(){
        // layout the login scene
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-scene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(GUIMain.class.getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * This method layout the new-match-scene where the player can select the specifics of
     * the new match that will be created
     * @param wasThePlayerChoice true if the player chose to create a new match
     *                           false if the player chose to enter a lobby, but no lobby is available
     */
    public void switchToNewMatchScene(boolean wasThePlayerChoice, int player_ID){

        // set the player ID
        this.match.setThisPlayer_ID(player_ID);

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/new-match-scene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("New Match");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(GUIMain.class.getResourceAsStream("/img/cranio-creation-logo.png")))));

        if(!wasThePlayerChoice){
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "There are no lobby available, please create a new match!",
                    ButtonType.OK);
            alert.showAndWait();
        }

        primaryStage.show();
    }

    /**
     * This method layout the enter-lobby-scene where the player can choose which lobby to join
     */
    public void switchToEnterLobbyScene(ArrayList<String> lobbiesData){
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/enter-lobby-scene.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        EnterLobbyController enterLobbyController = loader.getController();
        enterLobbyController.displayLobbiesData(lobbiesData);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Enter lobby");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(getClass().getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.show();
    }

    /**
     * This method layout the match-waiting-scene, the match is waiting for other players
     */
    public void switchToWaitingScene(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/match-waiting-scene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Waiting");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(getClass().getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.show();
    }

    /**
     * This method displays the scene where the player can choose his tower-color
     */
    public void switchToTowerChoice(int currentPlayer, ArrayList<Tower> notAvailableTowers){
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/choose-tower-scene.fxml"));
        try {
            root = loader.load();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        ChooseTowerController controller = loader.getController();
        controller.setUpScene(currentPlayer, notAvailableTowers);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Towers");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(getClass().getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.show();
    }

    public void switchToDeckChoice(int currentPlayer_ID, ArrayList<Wizard> notAvailableDecks){
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/choose-deck-scene.fxml"));
        try {
            root = loader.load();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        ChooseDeckController controller = loader.getController();
        controller.setUpScene(currentPlayer_ID, notAvailableDecks);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Wizards");
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(getClass().getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.show();
    }

    /**
     * This method layout the main game scene and give the control to the
     * GameController
     */
    public void switchToGame(){
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-scene.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.gameController = loader.getController();

        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image((Objects.requireNonNull(getClass().getResourceAsStream("/img/cranio-creation-logo.png")))));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static EriantysApplication getCurrentApplication(){return currentApplication;}

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ServerHandler getServerHandler(){return serverHandler;}

    public Match getMatch() {
        return match;
    }

    public GameController getGameController() {
        return gameController;
    }
}