package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.view.cli.CLI;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkHandler {
    private CLI cli;
    private String nickNamePlayer;
    private int playerID;
    private Socket clientSocket = null;
    private Gson gsonObj = new Gson();
    private BufferedReader inputBufferClient = null;
    private PrintWriter outputPrintClient = null;
    private String ip;
    private int port;
    Tower towerColor;


    public NetworkHandler(String ipReceived, int portReceived, CLI cliReceived) {
        this.ip = ipReceived;
        this.port = portReceived;
        this.cli = cliReceived;
    }


    /**
     * This startClient method is called in the main method.
     * It starts the socket of client execution, and prepare it to be ready for a "conversation" with the server.
     * The first message is the login one: login().
     * @throws IOException
     */
    public void startClient() throws IOException{
        clientSocket = new Socket(ip, port);

        inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputPrintClient = new PrintWriter(clientSocket.getOutputStream());

        // 1- per prima cosa il client avviato fa un login sul server
        loginFromClient();

        while(true){            // TODO: controllo connessione
            System.out.println("Still connected");
            String msgFromServer = inputBufferClient.readLine();
            System.out.println("messaggio dal server: " + msgFromServer);
            analysisOfReceivedMessageServer(msgFromServer);
        }

        /*
        inputBufferClient.close();
        outputPrintClient.close();
        clientSocket.close();

         */
    }


    /**
     * This method serialize in json and send the passed message.
     * @param msgToSend is the message to be sent.
     */
    public void sendMessage(Message msgToSend){
        outputPrintClient.println(gsonObj.toJson(msgToSend));
        outputPrintClient.flush();
    }


    /**
     * This method is used by client (player) to access the game:
     * First, player's nickname is asked,the the player is asked if he wants to create a new match (y/n) and his ansswer
     * his saved in the 'newMatchStr' variable. If he inserts 'y', it will later bepl created a new match,
     * otherwise the player will be asked which lobby, so which match, he wants to join.
     */
    public void loginFromClient() {
        nickNamePlayer = cli.loginNickname();

        boolean newMatchBool = cli.newMatchBoolean();

        LoginMessage msgLogin = new LoginMessage(nickNamePlayer, newMatchBool);
        sendMessage(msgLogin);
    }


    /**
     * This method is used by client class to analyse the received message.
     * @param receivedMessageInJson is the string received in json format, which will be deserialized.
     * case "LoginSuccess": it means the player has logged in and h
     */
    public synchronized void analysisOfReceivedMessageServer(String receivedMessageInJson){
        //System.out.println("Message analysis in progress...");
        //System.out.println("messaggio ricevuto in json: " + receivedMessageInJson);

        Message receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, Message.class);

        //System.out.println("Message translated");
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
        //System.out.println(messageObject);
        //System.out.println("Object Found.");

        ////switch per l'analisi dell'oggetto del messaggio
        switch (messageObject) {

            /* DEFAULT TO USE:
                LoginMessage msgLogin = new LoginMessage();
                msgLogin = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
                System.out.println(msgLogin.getNicknameOfPlayer());
                loginInServer(msgLogin.getNicknameOfPlayer());
                break;
                */

            case "LoginSuccess":
                cli.loginSuccess();
                LoginSuccessMessage msgLoginSuccess = gsonObj.fromJson(receivedMessageInJson, LoginSuccessMessage.class);
                boolean newMatchNeeded = msgLoginSuccess.getNewMatchNeeded();
                playerID = msgLoginSuccess.getPlayerID();
                //System.out.println("player id " + playerID);
                if (newMatchNeeded == true) {
                    creatingNewSpecsFromClient();
                }else{
                    sendAckFromClient();
                }
                break;

            case "join match":
                AskMatchToJoinMessage askMatchToJoinMessage = gsonObj.fromJson(receivedMessageInJson, AskMatchToJoinMessage.class);
                //System.out.println("player id" + playerID);

                int lobbyIDchosenByPlayer = cli.lobbyToChoose( askMatchToJoinMessage.getLobbiesTmp() );

                ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = new ReplyChosenLobbyToJoinMessage(lobbyIDchosenByPlayer);
                sendMessage(replyChosenLobbyToJoinMessage);

                break;

            case "playerID_set":
                IDSetAfterLobbyChoiceMessage idSetAfterLobbyChoiceMessage = gsonObj.fromJson(receivedMessageInJson, IDSetAfterLobbyChoiceMessage.class);
                playerID = idSetAfterLobbyChoiceMessage.getPlayerID();
                System.out.println("player id: " + playerID);
                break;

            case "NicknameNotValid":
                cli.nicknameNotAvailable();
                loginFromClient();
                break;

            case "start":
                cli.startAlert();
                MatchStartMessage matchStartMessage = new MatchStartMessage();
                matchStartMessage = gsonObj.fromJson(receivedMessageInJson, MatchStartMessage.class);
                System.out.println("player id: " + playerID);
                System.out.println("first id: " + matchStartMessage.getFirstPlayer());
                if (matchStartMessage.getFirstPlayer() == playerID) {
                    cli.isYourTurn();

                    towerColor = cli.towerChoice();

                    ChosenTowerColorMessage chosenTowerColorMessage = new ChosenTowerColorMessage();
                    chosenTowerColorMessage.setColor(towerColor);
                    chosenTowerColorMessage.setSender_ID(playerID);
                    sendMessage(chosenTowerColorMessage);
                    System.out.println("sent ok");

                    break;
                } else {
                    cli.turnWaiting();
                    break;
                }

            case "ack":                                                                                     //abbiamo raggruppato alcuni messaggi del server in ack e/o nack, dunque il server generico ci manda un ack e nel subObject specifica di cosa si tratta
                AckMessage ackMessageMapped = gsonObj.fromJson(receivedMessageInJson, AckMessage.class);    //se vediamo che l'oggetto del messaggio è un ack, rimappiamo il messaggio in uno della classe AckMessage
                switch(ackMessageMapped.getSubObject()) {
                    case "waiting":
                        cli.ackWaiting();
                        break;

                    case "tower_color":
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            ArrayList<Tower> notAvailableTowerColors = ackMessageMapped.getNotAvailableTowerColors();

                            boolean blackAvailability = true;
                            boolean greyAvailability = true;
                            boolean whiteAvailability = true;

                            for(int i = 0; i < notAvailableTowerColors.size(); i++){
                                if(String.valueOf(notAvailableTowerColors.get(i)) == "BLACK"){
                                    blackAvailability = false;
                                    //System.out.println(blackAvailability);
                                }
                                if(String.valueOf(notAvailableTowerColors.get(i)) == "GREY"){
                                    greyAvailability = false;
                                    //System.out.println(greyAvailability);
                                }
                                if(String.valueOf(notAvailableTowerColors.get(i)) == "WHITE"){
                                    whiteAvailability = false;
                                    //System.out.println(whiteAvailability);
                                }
                            }

                            towerColor = Tower.valueOf(cli.towerChoiceNext(blackAvailability, greyAvailability, whiteAvailability) );

                            ChosenTowerColorMessage chosenTowerColorMessage = new ChosenTowerColorMessage();
                            chosenTowerColorMessage.setColor(towerColor);
                            chosenTowerColorMessage.setSender_ID(playerID);
                            sendMessage(chosenTowerColorMessage);
                            System.out.println("sent ok");

                            break;
                        } else {
                            //sendAckFromClient();
                            cli.turnWaiting();
                            break;
                        }
                }
                break;

            case "no lobby available" :
                NoLobbyAvailableMessage noLobbyAvailableMessage = gsonObj.fromJson(receivedMessageInJson, NoLobbyAvailableMessage.class);
                playerID = noLobbyAvailableMessage.getPlayerID();
                cli.lobbyNotAvailable();
                creatingNewSpecsFromClient();
                break;

            default:
                cli.errorObject();
        }
    }

    /**
     * This method is used to send an ack message to the server.
     * It create an AckMessage object which will be serialized in json in order to be sent.
     */
    private void sendAckFromClient(){
        AckMessage ackMessage = new AckMessage();
        sendMessage(ackMessage);
    }


    /**
     * This method is used by the client after receiving a loginSuccess message from the server;
     * he has to declare the number of players of the lobby and if he wants to play in expert mode or not.
     * */
    public void creatingNewSpecsFromClient(){
        MatchSpecsMessage newMatchSpecsMessage;

        int numberOfPlayerInTheLobby = cli.numberOfPlayer();
        boolean expertMode = cli.expertModeSelection();

        newMatchSpecsMessage = new MatchSpecsMessage( numberOfPlayerInTheLobby, expertMode );

        sendMessage(newMatchSpecsMessage);
    }


    /** this method is used to send the BagClick message */
    public void sendBagClickedByFirstClient(){
        BagClickMessage bagClickMessage = new BagClickMessage();
        outputPrintClient.println(gsonObj.toJson(bagClickMessage));
        outputPrintClient.flush();
        System.out.println("sendBagClickedbyFirst sent");
    }



}