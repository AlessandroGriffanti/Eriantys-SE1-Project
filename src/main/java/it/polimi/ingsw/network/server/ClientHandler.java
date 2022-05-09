package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** this class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * it should deserialize the json received and pass the information to the controller
 */
public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Server server;

    /** input and output stream */
    private PrintWriter outputHandler;
    private BufferedReader inputHandler;

    /** Gson object "gsonObj" to deserialize the json message received */
    private final Gson gsonObj = new Gson();

    public ClientHandler(Socket socket){
        this.clientSocket = socket;
    }
    @Override
    public void run(){
        try {
            System.out.println("running");

            outputHandler = new PrintWriter(clientSocket.getOutputStream());
            inputHandler = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            /*
            String outputFromServer = inputHandler.readLine();
            System.out.println("nickreceived: " + outputFromServer); //ricevuto dal client

            outputHandler.println(outputFromServer + " ok\n");
            outputHandler.flush();
            System.out.println("sent ok");
            */

            analysisOfReceivedMessageServer(inputHandler.readLine());
            System.out.println("end");

            outputHandler.close();
            inputHandler.close();
            clientSocket.close();
            System.out.println("Client " + clientSocket.getInetAddress() + "disconnected from server.");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method in the server analyse all the received message from the client and calls the right method in consequence.
     * @param receivedMessageInJson is the message received in json format through the socket reader.
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
            case "login":
                System.out.println("I received a login message");
                LoginMessage msgLogin = new LoginMessage();
                msgLogin = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
                System.out.println(msgLogin.getNicknameOfPlayer());
                loginInServer(msgLogin.getNicknameOfPlayer());
                break;

            default:
                System.out.println("Error with the object of the message.");
        }
    }

    /**
     * This method receives the player's nickname chosen for the game:
     * player's nickname is verified by checking the Players Nicknames arraylist.
     * If accepted, it is added to the arraylist
     * @param nicknamePassed is the string nickname chosen by client
     */
    public void loginInServer(String nicknamePassed) {
        int i;
        if (server.getPlayersNicknames().size() == 0) {
            server.getPlayersNicknames().add(nicknamePassed);
            System.out.println("nickname ok");

            outputHandler.println("ok");
            outputHandler.flush();
            System.out.println("sent ok");
        } else {
            for (i = 0; i < server.getPlayersNicknames().size(); i++) {
                if (server.getPlayersNicknames().get(i) != nicknamePassed) {
                    server.getPlayersNicknames().add(nicknamePassed);
                    System.out.println("nickname ok");

                    outputHandler.println("ok");
                    outputHandler.flush();
                    System.out.println("sent ok");

                } else {
                    System.out.println("nickname already used");

                    outputHandler.println("nickname already used, choose a new one");
                    outputHandler.flush();
                    System.out.println("sent ok");
                }
            }
        }
    }

}