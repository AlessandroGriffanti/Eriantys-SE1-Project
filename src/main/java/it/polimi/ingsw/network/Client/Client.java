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

        //per prima cosa il client avviato fa un login sul server
        loginFromClient();



        String msgFromServer = inputBufferClient.readLine();
        System.out.println("Still connected");
        System.out.println(msgFromServer);


        inputBufferClient.close();
        outputPrintClient.close();
        clientSocket.close();
    }


    /**
     * This method is used by client (player) to access the game:
     * player's nickname is asked and then sent to the server in json format
     */
    public void loginFromClient() {
        Scanner loginScanner = new Scanner(System.in);
        System.out.println("Insert nickname: ");
        //String nickNamePlayer = loginScanner.nextLine();
        LoginMessage msgLogin = new LoginMessage(loginScanner.nextLine());
        System.out.println("ok");
        outputPrintClient.println(gsonObj.toJson(msgLogin));
        outputPrintClient.flush();
        System.out.println("sent");
    }

    //  per il case switch dei messaggi ricevuti questa sotto è una buona base

    /**
     * This method is used by client class to analyse the received message.
     * @param receivedMessageInJson is the string received in json format, which will be deserialized.
     */
    public void analysisOfReceivedMessageServer(String receivedMessageInJson){
        System.out.println("Message analysis in progress...");
        System.out.println(receivedMessageInJson);
        Message receivedMessageFromJson = new Message();
        receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, Message.class);
        System.out.println("Message translated");
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
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
                LoginSuccessMessage msgLoginSuccess = new LoginSuccessMessage();
                msgLoginSuccess = gsonObj.fromJson(receivedMessageInJson, LoginSuccessMessage.class);
                boolean newMatchNeeded = msgLoginSuccess.getNewMatchNeeded();
                playerID = msgLoginSuccess.getPlayerID();
                if(newMatchNeeded == false) {
                    creatingNewSpecsFromClient();
                }else{
                    sendAckFromClient();
                }
                break;

            case "NicknameNotValid":
                System.out.println("Insert new nickname: ");
                loginFromClient();
                break;

            case "":

                break;

            default:
                System.err.println("Error with the object of the message.");
        }
    }

    /**
     * This method is used to send an ack message to the server.
     * It create an AckMessage object which will be serialized in json in order to be sent.
     */
    private void sendAckFromClient(){
        AckMessage ackMessage = new AckMessage();
        outputPrintClient.println(gsonObj.toJson(ackMessage));
        outputPrintClient.flush();
        System.out.println("sent");
    }

    /**
     * This method is used by the client after receiving a loginSuccess message from the server;
     * he has to declare the number of players of the lobby and
     * if he wants to play in expert mode or not.
     * */
    public void creatingNewSpecsFromClient(){
        Scanner inputForSpecs = new Scanner(System.in);
        MatchSpecsMessage newMatchSpecs;

        System.out.println("Please insert the number of the player of the lobby: ");
        int numberOfPlayerInTheLobby = inputForSpecs.nextInt();
        while(numberOfPlayerInTheLobby < 0 && numberOfPlayerInTheLobby > 4){
            System.out.println("Please, insert a valid number of players");
            numberOfPlayerInTheLobby = inputForSpecs.nextInt();
        }

        System.out.println("Do you want to play in expert mode? Insert yes or no");
        String expertMode = inputForSpecs.nextLine();
        while(!(expertMode.equals("yes") || expertMode.equals("no"))){
            System.out.println("Please, insert yes or no");
            expertMode = inputForSpecs.nextLine();
        }
        if(expertMode.equals("yes")) {
            newMatchSpecs = new MatchSpecsMessage(numberOfPlayerInTheLobby, true);
        }else{
            newMatchSpecs = new MatchSpecsMessage(numberOfPlayerInTheLobby, false);
        }
        outputPrintClient.println(gsonObj.toJson(newMatchSpecs));
        outputPrintClient.flush();

    }


}
