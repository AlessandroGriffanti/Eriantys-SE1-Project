package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int playerID;
    private Socket clientSocket = null;
    private Gson gsonObj = new Gson();
    private BufferedReader inputBufferClient = null;
    private PrintWriter outputPrintClient = null;


    public static void main(String[] args) throws IOException {
        try{
            Client client = new Client();
            client.startClient();

        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    /**
     * This startClient method is called in the main method.
     * It starts the socket of client execution, and prepare it to be ready for a "conversation" with the server.
     * The first message is the login one: login().
     * @throws IOException
     */
    public void startClient() throws IOException{
        clientSocket = new Socket("localhost", 4444);

        inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputPrintClient = new PrintWriter(clientSocket.getOutputStream());

        // 1- per prima cosa il client avviato fa un login sul server
        loginFromClient();



        while(true){            // TODO: controllo connessione
            System.out.println("Still connected");
            String msgFromServer = new String();
            msgFromServer = inputBufferClient.readLine();
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
     * This method is used by client (player) to access the game:
     * First, player's nickname is asked,the the player is asked if he wants to create a new match (y/n) and his ansswer
     * his saved in the 'newMatchStr' variable. If he inserts 'y', it will later bepl created a new match,
     * otherwise the player will be asked which lobby, so which match, he wants to join.
     */
    public void loginFromClient() {
        Scanner loginScanner = new Scanner(System.in);

        System.out.println("Insert nickname: ");
        String nickNamePlayer = loginScanner.nextLine();
        System.out.println("ok");

        System.out.println("do you wat to create a new match? (y/n) ");
        String newMatchStr = loginScanner.nextLine();
        boolean newMatchBool;

        while ( !( (newMatchStr.equals("y")) || (newMatchStr.equals("n")))){
            System.out.println("please insert y if you want to create a new match: (y/n) ");
            newMatchStr = loginScanner.nextLine();
        }
        System.out.println("ok");

        if(newMatchStr.equals("y")){
            newMatchBool = true;
        }else {
            newMatchBool = false;
        }

        LoginMessage msgLogin = new LoginMessage(nickNamePlayer, newMatchBool);
        outputPrintClient.println(gsonObj.toJson(msgLogin));
        outputPrintClient.flush();

        System.out.println("sent");
    }


    /**
     * This method is used by client class to analyse the received message.
     * @param receivedMessageInJson is the string received in json format, which will be deserialized.
     * case "LoginSuccess": it means the player has logged in and h
     */
    public void analysisOfReceivedMessageServer(String receivedMessageInJson){
        System.out.println("Message analysis in progress...");
        System.out.println("messaggio ricevuto in json: " + receivedMessageInJson);

        Message receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, Message.class);

        System.out.println("Message translated");
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
        System.out.println(messageObject);
        System.out.println("Object Found.");

        //switch per l'analisi dell'oggetto del messaggio
        switch (messageObject) {

            /* DEFAULT TO USE:
                LoginMessage msgLogin = new LoginMessage();
                msgLogin = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
                System.out.println(msgLogin.getNicknameOfPlayer());
                loginInServer(msgLogin.getNicknameOfPlayer());
                break;
                */

            case "LoginSuccess":
                System.out.println("login success");
                LoginSuccessMessage msgLoginSuccess = gsonObj.fromJson(receivedMessageInJson, LoginSuccessMessage.class);
                boolean newMatchNeeded = msgLoginSuccess.getNewMatchNeeded();
                playerID = msgLoginSuccess.getPlayerID();
                if (newMatchNeeded == true) {
                    creatingNewSpecsFromClient();
                }else{
                    sendAckFromClient();
                }
                break;

            case "join match":
                AskMatchToJoinMessage askMatchToJoinMessage = gsonObj.fromJson(receivedMessageInJson, AskMatchToJoinMessage.class);
                System.out.println("Chose a Lobby: ");
                for (int i = 0; i < askMatchToJoinMessage.getLobbiesTmp().size(); i++) {
                    System.out.print(i + "  ");
                    System.out.println(askMatchToJoinMessage.getLobbiesTmp().get(i));
                }
                Scanner scannerLobbyChosen = new Scanner(System.in);
                int lobbyIDchosenByPlayer = scannerLobbyChosen.nextInt();
                while(askMatchToJoinMessage.getLobbiesTmp().get(lobbyIDchosenByPlayer) == true){
                    System.out.println("You can't join this lobby, select another one");
                    lobbyIDchosenByPlayer = scannerLobbyChosen.nextInt();
                }
                ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = new ReplyChosenLobbyToJoinMessage(lobbyIDchosenByPlayer);
                outputPrintClient.println(gsonObj.toJson(replyChosenLobbyToJoinMessage));
                outputPrintClient.flush();
                break;

            case "NicknameNotValid":
                System.out.println("Insert new nickname: ");
                loginFromClient();
                break;
            case "start":
                MatchStartMessage matchStartMessage = new MatchStartMessage();
                matchStartMessage = gsonObj.fromJson(receivedMessageInJson, MatchStartMessage.class);
                if (matchStartMessage.getFirstPlayer() == playerID) {
                    sendBagClickedByFirstClient();
                    break;
                } else {
                    sendAckFromClient();
                    break;
                }
            case "ack":                                                                                     //abbiamo raggruppato alcuni messaggi del server in ack e/o nack, dunque il server generico ci manda un ack e nel subObject specifica di cosa si tratta
                AckMessage ackMessageMapped = gsonObj.fromJson(receivedMessageInJson, AckMessage.class);    //se vediamo che l'oggetto del messaggio è un ack, rimappiamo il messaggio in uno della classe AckMessage
                switch(ackMessageMapped.getSubObject()) {
                    case "waiting":
                        sendAckFromClient();
                        break;
                }
                break;
            case "no lobby available" :
                System.out.println("No lobby available, creating a new one...");
                creatingNewSpecsFromClient();
                break;

            default:
                System.err.println("Error with the object of the message.");
        }
    }

    /** this method is used to send the BagClick message */
    public void sendBagClickedByFirstClient(){
        BagClickMessage bagClickMessage = new BagClickMessage();
        outputPrintClient.println(gsonObj.toJson(bagClickMessage));
        outputPrintClient.flush();
    }
    /**
     * This method is used to send an ack message to the server.
     * It create an AckMessage object which will be serialized in json in order to be sent.
     */
    private void sendAckFromClient(){
        AckMessage ackMessage = new AckMessage();
        outputPrintClient.println(gsonObj.toJson(ackMessage));
        outputPrintClient.flush();
        System.out.println("sent ack from client");
    }

    /**
     * This method is used by the client after receiving a loginSuccess message from the server;
     * he has to declare the number of players of the lobby and if he wants to play in expert mode or not.
     * */
    public void creatingNewSpecsFromClient(){
        Scanner inputForSpecs = new Scanner(System.in);
        MatchSpecsMessage newMatchSpecsMessage;
        System.out.println("Please insert the number of the player you want in the lobby: ");
        int numberOfPlayerInTheLobby = inputForSpecs.nextInt();
        System.out.println(numberOfPlayerInTheLobby);
        while( (numberOfPlayerInTheLobby <= 1) || (numberOfPlayerInTheLobby >= 4) ){
            System.out.println("Please, insert a valid number of players");
            numberOfPlayerInTheLobby = inputForSpecs.nextInt();
        }

        System.out.println("Do you want to play in expert mode? (y/n)");
        String expertMode = inputForSpecs.nextLine();

        while(!(expertMode.equals("y") || expertMode.equals("n"))){
            System.out.println("Please, insert y or n");
            expertMode = inputForSpecs.nextLine();
        }

        if(expertMode.equals("y")) {
            newMatchSpecsMessage = new MatchSpecsMessage(numberOfPlayerInTheLobby, true);
        }else{
            newMatchSpecsMessage = new MatchSpecsMessage(numberOfPlayerInTheLobby, false);
        }
        outputPrintClient.println(gsonObj.toJson(newMatchSpecsMessage));
        outputPrintClient.flush();

    }


}
