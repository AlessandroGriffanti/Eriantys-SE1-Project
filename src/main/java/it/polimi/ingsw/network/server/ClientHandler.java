package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.messages.LoginSuccessMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.NicknameNotValidMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** this class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * it should deserialize the json received and pass the information to the controller
 */
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private String nicknamePlayer;

    /**
     * input and output stream
     */
    private PrintWriter outputHandler;
    private BufferedReader inputHandler;

    /**
     * Gson object "gsonObj" to deserialize the json message received
     */
    private final Gson gsonObj = new Gson();

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            System.err.println("running");

            outputHandler = new PrintWriter(clientSocket.getOutputStream());
            inputHandler = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            /*
            String outputFromServer = inputHandler.readLine();
            System.out.println("nickreceived: " + outputFromServer); //ricevuto dal client

            outputHandler.println(outputFromServer + " ok\n");
            outputHandler.flush();
            System.out.println("sent ok");
            */

            loginInServer(inputHandler.readLine());

            //aggiungere qui il controllo sull'hashmap, quella con il controller, (e sull'attributo booleano del controller per vedere se creare un nuovo controller o meno.

            while(clientSocket.isConnected()){
                server.getLobbies().get(server.getLobbyIDByPlayerName(nicknamePlayer)).manageMsg(inputHandler.readLine());          //messaggio passato in json al controller
                System.out.println("pippo while");
            }

            System.out.println("end");

            outputHandler.close();
            inputHandler.close();
            clientSocket.close();
            System.out.println("Client " + clientSocket.getInetAddress() + "disconnected from server.");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * this method checks if a new lobby must be created:
     * it loops over the lobbies in the collection of lobbies (server.getLobbies().keySet()) and it controls if the corresponding
     * controller's boolean attribute "playing" is true or false. If it is false, it means the lobby is not full, so we pass
     * the clientHandler just created, with the nickname of the player, to the controller of the corresponding lobby
     * through the addPlayerHandler method. If it is true, then we add a new lobby to lobbies and we create the new corresponding
     * controller to which we pass the lobby ID
     */

    public synchronized boolean checkNewLobbyCreation(String nicknameOfNewPlayer) {
        if (server.getLobbies().keySet().isEmpty()) {
            int newNumberOfTotalLobbies = 1;
            server.getLobbies().put((String.valueOf(newNumberOfTotalLobbies)), new Controller(newNumberOfTotalLobbies));
            server.getLobbies().get(newNumberOfTotalLobbies).addPlayerHandler(this, nicknameOfNewPlayer);
            return true;
        } else {
            for (String lobbyID : server.getLobbies().keySet()) {
                if ((server.getLobbies().get(lobbyID).getPlayingStatus()) == false) {
                    server.getLobbies().get(lobbyID).addPlayerHandler(this, nicknameOfNewPlayer);
                    return false;
                } else {
                    int newNumberOfTotalLobbies = server.getLobbies().size() + 1;
                    server.getLobbies().put((String.valueOf(newNumberOfTotalLobbies)), new Controller(newNumberOfTotalLobbies));
                    server.getLobbies().get(newNumberOfTotalLobbies).addPlayerHandler(this, nicknameOfNewPlayer);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * This method in the server analyse all the received message from the client and calls the right method in consequence.
     *
     * @param receivedMessageInJson is the message received in json format through the socket reader.
     */
    public void loginInServer(String receivedMessageInJson) {
        System.out.println(receivedMessageInJson);
        LoginMessage receivedMessageFromJson = new LoginMessage();
        receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
        System.out.println("Message translated");
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
        System.out.println("Object Found.");
        if (messageObject.equals("login")) {
            System.out.println("I received a login message");
            // System.out.println(receivedMessageFromJson.getNicknameOfPlayer());
            int j = this.server.getPlayersNicknames().size();
            if (j == 0) {
                //PRIMO GIOCATORE COLLEGATO
                server.getPlayersNicknames().add(receivedMessageFromJson.getNicknameOfPlayer());
                nicknamePlayer = receivedMessageFromJson.getNicknameOfPlayer();
                System.out.println("primo nickname ricevuto: " + receivedMessageFromJson.getNicknameOfPlayer());
                System.out.println("nickname ok");

                boolean checkNew = checkNewLobbyCreation(receivedMessageFromJson.getNicknameOfPlayer());
                sendingLoginSuccess(server.getPlayersNicknames().indexOf(receivedMessageFromJson.getNicknameOfPlayer()), checkNew);

                System.out.println("sent ok");

            } else {
                int flag = 0;
                for (int k = 0; k < j; k++) {
                    if (server.getPlayersNicknames().get(k).equals(receivedMessageFromJson.getNicknameOfPlayer())) {
                        flag = 1;
                        System.out.println("equals");
                        break;
                    }
                }
                System.out.println("flag");
                if (flag != 1) {
                    server.getPlayersNicknames().add(receivedMessageFromJson.getNicknameOfPlayer());
                    nicknamePlayer = receivedMessageFromJson.getNicknameOfPlayer();
                    System.out.println("altro nickname ricevuto: " + receivedMessageFromJson.getNicknameOfPlayer());
                    System.out.println("nickname ok");

                    boolean checkNew = checkNewLobbyCreation(receivedMessageFromJson.getNicknameOfPlayer());
                    sendingLoginSuccess(server.getPlayersNicknames().indexOf(receivedMessageFromJson.getNicknameOfPlayer()), checkNew);

                } else {
                    System.out.println("nickname already used");
                    sendingNicknameNotValid();

                }
                System.out.println("sent ok");
            }
        }
    }


    public void sendingLoginSuccess(int playerID, boolean newMatchNeeded){
        LoginSuccessMessage loginSuccessMessage = new LoginSuccessMessage(playerID, newMatchNeeded);
        outputHandler.println(gsonObj.toJson(loginSuccessMessage));
        outputHandler.flush();
        System.out.println("sent ok");
    }

    public void sendingNicknameNotValid(){
        NicknameNotValidMessage nicknameNotValidMessage = new NicknameNotValidMessage();
        outputHandler.println(gsonObj.toJson(nicknameNotValidMessage));
        outputHandler.flush();
        System.out.println("sent ok");
    }

    /**
     * This method receives a Message type class, serializes it and sends it to the client.
     * @param msgToSerialize is the message passed by the controller and sent to the client.
     */
    public void messageToSerialize(Message msgToSerialize){
        outputHandler.println(gsonObj.toJson(msgToSerialize));
        outputHandler.flush();
        System.out.println("sent ok");
    }

}