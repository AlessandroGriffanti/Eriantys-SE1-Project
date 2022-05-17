package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/** this class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * it should deserialize the json received and pass the information to the controller
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private String nicknamePlayer;

    //input and output stream
    InputStream inputStream = null;
    PrintWriter outputHandler = null;
    BufferedReader inputHandler = null;

    /**
     * Gson object "gsonObj" to deserialize the json message received
     */
    private final Gson gsonObj = new Gson();

    public ClientHandler(Socket socket, Server server) {
    //public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.server = server;
    }

    public void run() {


        try {
            System.out.println("running");
            inputStream = clientSocket.getInputStream();
            inputHandler = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputHandler = new PrintWriter(clientSocket.getOutputStream());

            loginInServer(inputHandler.readLine());

            //aggiungere qui il controllo sull'hashmap, quella con il controller, (e sull'attributo booleano del controller per vedere se creare un nuovo controller o meno.

            /*
            while(clientSocket.isConnected()){
                server.getLobbies().get(server.getLobbyIDByPlayerName(nicknamePlayer)).manageMsg(inputHandler.readLine());          //messaggio passato in json al controller
                System.out.println("pippo while");
            }

             */

            System.out.println("end");

            outputHandler.close();
            inputHandler.close();
            clientSocket.close();
            System.out.println("Client " + clientSocket.getInetAddress() + "disconnected from server.");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        //    public synchronized boolean checkNewLobbyCreation(String nicknameOfNewPlayer)



    /**
     * This method in the server analyse all the received message from the client and calls the right method in consequence.
     *
     * @param receivedMessageInJson is the message received in json format through the socket reader.
     */
    public void loginInServer(String receivedMessageInJson) throws IOException, InterruptedException {
        System.out.println(receivedMessageInJson);
        LoginMessage receivedMessageFromJson; //= new LoginMessage();
        receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
        System.out.println("Message translated");
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
        System.out.println("Object Found.");

        if (messageObject.equals("login")) {
            System.out.println("I received a login message");

            System.out.println("Nickname ricevuto: " + receivedMessageFromJson.getNicknameOfPlayer() );

            if ( checkNickname( receivedMessageFromJson.getNicknameOfPlayer() ) ) {
                server.getPlayersNicknames().add(receivedMessageFromJson.getNicknameOfPlayer());
                nicknamePlayer = receivedMessageFromJson.getNicknameOfPlayer();

                checkNewMatchRequest(receivedMessageFromJson.isCreateNewMatch(), nicknamePlayer);

                System.out.println("nickname ok");

                System.out.println("Player " + server.getPlayersNicknames().indexOf(receivedMessageFromJson.getNicknameOfPlayer()) + ": " + receivedMessageFromJson.getNicknameOfPlayer());
                System.out.println("player ok");


                String messageReceivedInJson = inputHandler.readLine();
                Message messageReceivedFromJson = gsonObj.fromJson(messageReceivedInJson, Message.class);

                if(messageReceivedFromJson.getObjectOfMessage().equals("creation")){
                    System.out.println("I received a new match specs message");
                    lobbyCreation(nicknamePlayer);

                    wait(5*1000);   //ATTENZIONE

                    server.getLobbies().get(server.getLobbies().keySet().size()).addPlayerHandler(this, nicknamePlayer);
                    server.getLobbies().get(server.getLobbies().keySet().size()).manageMsg(messageReceivedInJson);            //passa numero di giocatori massimo

                }else if (messageReceivedFromJson.getObjectOfMessage().equals("chosen lobby")) {
                    System.out.println("I received a new chosen lobby message");
                    ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = gsonObj.fromJson(messageReceivedInJson, ReplyChosenLobbyToJoinMessage.class);

                    server.getLobbies().get(replyChosenLobbyToJoinMessage.getLobbyIDchosen()).addPlayerHandler(this, nicknamePlayer);
                    server.getLobbies().get(server.getLobbies().keySet().size()).manageMsg(messageReceivedInJson);

                }else {
                    System.out.println("Error: not right specs message");
                }

                //SIAMO ARRIVATI QUI

            } else {
                System.out.println("nickname already used");
                sendingNicknameNotValid();
                System.out.println("sent nack ok");
            }
        }else {
            System.out.println("Error: not a Login message");
        }
    }


    public boolean checkNickname(String nicknameChosenPlayer){
        int totalNumberOfPlayers = this.server.getPlayersNicknames().size();

        if (totalNumberOfPlayers != 0) {
            int equal = 0;

            for (int k = 0; k < totalNumberOfPlayers; k++) {
                if (server.getPlayersNicknames().get(k).equals(nicknameChosenPlayer) ) {
                    equal = 1;
                    System.out.println("equals");
                    break;
                }
            }

            if (equal != 1) {
                return true;
            }else {
                return false;
            }

        } else{
            return true;
        }
    }


    public void checkNewMatchRequest(boolean requestValue, String nicknameOfPlayer){
        if(requestValue == true){
            sendingLoginSuccess( server.getPlayersNicknames().indexOf(nicknameOfPlayer), true);
        }else {
            if (server.getLobbies().keySet().size() == 0) {
                NoLobbyAvailableMessage noLobbyAvailableMessage = new NoLobbyAvailableMessage();

                outputHandler.println(gsonObj.toJson(noLobbyAvailableMessage));
                outputHandler.flush();

                System.out.println("sent ok");
            } else {
                askMatchToJoin();
            }
        }
    }

    public void sendingLoginSuccess(int playerID, boolean newMatchNeeded){
        LoginSuccessMessage loginSuccessMessage = new LoginSuccessMessage(playerID, newMatchNeeded);

        outputHandler.println(gsonObj.toJson(loginSuccessMessage));
        outputHandler.flush();

        System.out.println("sent ok");
    }


    public void askMatchToJoin() {
        ArrayList<Boolean> listAvailableLobbies = new ArrayList<>();

        for(String lobby : server.getLobbies().keySet()) {
            if(server.getLobbies().get(lobby).getPlayingStatus()) {
                listAvailableLobbies.add(true);
            }else {
                listAvailableLobbies.add(false);
            }
        }

        AskMatchToJoinMessage askMatchToJoinMessage = new AskMatchToJoinMessage (listAvailableLobbies);

        outputHandler.println(gsonObj.toJson(askMatchToJoinMessage));
        outputHandler.flush();

        System.out.println("sent ok");
    }


    public synchronized void lobbyCreation(String nicknameOfNewPlayer) {
        int numberOfTotalLobbies = server.getLobbies().keySet().size() ;                                                            //+1 ???
        server.getLobbies().put( (String.valueOf(numberOfTotalLobbies) ), new Controller(numberOfTotalLobbies));
        server.getLobbies().get(numberOfTotalLobbies).addPlayerHandler(this, nicknameOfNewPlayer);

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