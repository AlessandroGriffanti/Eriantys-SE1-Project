package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * This class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * It deserializes the json message received and passes it to the controller.
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    /**
     * This attribute indicates the nickname chosen by the player.
     */
    private String nicknamePlayer;
    private int playerID;
    /**
     * This attribute is the lobbyID to which the player is connected.
     */
    private int lobbyID;

    /**
     * This attribute tells the number of the player of the new lobby which is being created.
     */
    private int numberPlayerLobby;

    /**
     * This attribute tells if the match has started or not.
     */
    private boolean matchStarted = false;
    /**
     * This attribute tells if the lobby has been accessed or not.
     */
    private boolean lobbyAccessed = false;

    /**
     * Gson object "gsonObj" to deserialize the json message received
     */
    private final Gson gsonObj = new Gson();

    //input and output stream
    private PrintWriter outputHandler = null;
    private BufferedReader inputHandler = null;

    private static int TIMEOUT = 10000;
    /**
     * Constructor of the ClientHandler
     * @param socket is the client socket returned from the serverSocket.accept() method.
     * @param server is a reference to the server.
     */
    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
        try{
            clientSocket.setSoTimeout(TIMEOUT);
        }catch (SocketException e){
            e.printStackTrace();
        }
    }

    /**
     * Main method of the ClientHandler. Since it is a thread, it executes the run method and, through it,
     * it starts listening for messages from the client (NetworkHandler).
     */
    public void run() {

        try {
            System.out.println("running");
            inputHandler = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputHandler = new PrintWriter(clientSocket.getOutputStream());

            loginInServer(inputHandler.readLine());

            System.out.println("pippo while");
            while( !( server.getLobbies().get(String.valueOf(lobbyID)).isMatchEnded() ) ) {
                //System.out.println("is match ended: " + server.getLobbies().get(String.valueOf(lobbyID)).isMatchEnded());
                try {
                    String msg = inputHandler.readLine();                                                                                   //leggiamo il mex in input dal client
                    System.out.println("messaggio ricevuto dal client: " + msg);
                    if(msg != null) {
                        if (!msg.equals("{\"object\":\"ping\",\"sender_ID\":0}")) {
                            System.out.println("messaggio ricevuto dal client: " + msg);
                            if (server.getLobbies().get(String.valueOf(lobbyID)).isMatchEnded()) {
                                server.getLobbiesEnd().set(lobbyID, true);
                            } else {
                                server.getLobbies().get(String.valueOf(lobbyID)).manageMsg(msg);                                //mandiamo al controller il messaggio ricevuto dal client
                            }
                        }
                    }
                }catch (SocketTimeoutException e){
                    clientSocket.close();
                    System.out.println("Chiusura socket");
                    if(matchStarted) {
                        System.out.println("OUT1");
                        server.getLobbies().get(String.valueOf(lobbyID)).onePlayerDisconnected(playerID);
                        server.getLobbiesEnd().set(lobbyID, true);
                        server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, false);
                    }else if(lobbyAccessed){
                        System.out.println("OUT2");
                        server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, false);     //TODO forse playerConnection non serve più
                        server.getLobbiesEnd().set(lobbyID, true);
                        server.getLobbies().get(String.valueOf(lobbyID)).onePlayerDisconnected(playerID);
                    }else{
                        System.out.println("OUT3");
                    }
                }
            }

            socketClosing();


        } catch (IOException e) {
            System.out.println(e.getMessage());

            if(matchStarted) {
                System.out.println("OUT1");
                server.getLobbies().get(String.valueOf(lobbyID)).onePlayerDisconnected(playerID);
                server.getLobbiesEnd().set(lobbyID, true);
                server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, false);
            }else if(lobbyAccessed){
                System.out.println("OUT2");
                server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, false);     //TODO forse playerConnection non serve più
                server.getLobbiesEnd().set(lobbyID, true);
                server.getLobbies().get(String.valueOf(lobbyID)).onePlayerDisconnected(playerID);
            }else{
                System.out.println("OUT3");
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }



    /**
     * This method in the server analyse all the received message from the client and calls the right method in consequence.
     *
     * @param receivedMessageInJson is the message received in json format through the socket reader.
     */
    public void loginInServer(String receivedMessageInJson) throws IOException, InterruptedException {
        System.out.println(receivedMessageInJson);
        LoginMessage receivedMessageFromJson;
        receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
        if(receivedMessageFromJson.getObjectOfMessage().equals("ping")){
            loginInServer(inputHandler.readLine());
        }else if (messageObject.equals("login")) {
            if (checkNickname(receivedMessageFromJson.getNicknameOfPlayer())){  //if checkNickname returns true, which means the nickname of the player isn't already used
                server.getPlayersNicknames().add(receivedMessageFromJson.getNicknameOfPlayer());    //aggiungiamo il nickname del giocatore all'arraylist nel server
                nicknamePlayer = receivedMessageFromJson.getNicknameOfPlayer();                     //settiamo la variabile nicknamePlayer del clientHandler con il valore del nickname proveniente dal client

                checkNewMatchRequest(receivedMessageFromJson.isCreateNewMatch(), nicknamePlayer);   //controlliamo se vuole creare un nuovo match. se sì mandiamo un ackMatchCreationMessage

                //nuova rilettura
                String messageReceivedInJson = inputHandler.readLine();                             //qui riceve il nuovo mex dal client
                boolean pluto = false;
                while(!pluto) {
                    if(messageReceivedInJson.equals("{\"object\":\"ping\",\"sender_ID\":0}")){
                        messageReceivedInJson = inputHandler.readLine();
                    }else{
                        receivingSpecsInfo(messageReceivedInJson);
                        pluto = true;
                    }
                }

            }else{                                                //se il check del nome dà false, finiamo qui dove viene inviato un nicknamenotvalid e nel client verrà rifatto il login
                sendingNicknameNotValid();

                loginInServer(inputHandler.readLine());
            }
        }else {
            System.out.println("Error: not a Login message");
        }
    }

    public void receivingSpecsInfo(String messageReceivedInJson){
        Message messageReceivedFromJson = gsonObj.fromJson(messageReceivedInJson, Message.class);


        if (messageReceivedFromJson.getObjectOfMessage().equals("creation")) {                //se riceve il messaggio di MatchSpecs
            MatchSpecsMessage matchSpecsMessage = gsonObj.fromJson(messageReceivedInJson, MatchSpecsMessage.class);
            numberPlayerLobby = matchSpecsMessage.getNumOfPlayers();
            lobbyCreation(nicknamePlayer, numberPlayerLobby);                                                  // --> allora crea la lobby
            server.getLobbies().get(String.valueOf(lobbyID)).manageMsg(messageReceivedInJson);            //passa numero di giocatori massimo


        } else if (messageReceivedFromJson.getObjectOfMessage().equals("chosen lobby")) {          //se il server riceve questo significa che il player ha scelto a che lobby unirsi
            ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = gsonObj.fromJson(messageReceivedInJson, ReplyChosenLobbyToJoinMessage.class);

            int templobbyID = replyChosenLobbyToJoinMessage.getLobbyIDChosen();
            //CHECK IF FULL WHILE TRYING TO JOIN
            int maxNumOfPlayerInTheLobbyChosen = server.getLobbies().get(String.valueOf(templobbyID)).getNumberOfPlayers();
            int numOfPlayerInTheLobbyChosen = server.getLobbies().get(String.valueOf(templobbyID)).getPlayersAddedCounter();

            if (maxNumOfPlayerInTheLobbyChosen != numOfPlayerInTheLobbyChosen) {                                  //se non è ancora stato raggiunto il numero massimo di giocatori, va bene
                lobbyID = templobbyID;
                playerID = server.getLobbies().get(String.valueOf(lobbyID)).getPlayersAddedCounter();
                server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, true);
                numberPlayerLobby = server.getLobbies().get(String.valueOf(lobbyID)).getNumberOfPlayers();
                lobbyAccessed = true;

                IDSetAfterLobbyChoiceMessage idSetAfterLobbyChoice = new IDSetAfterLobbyChoiceMessage(playerID);
                sendMessageFromServer(idSetAfterLobbyChoice);

                server.getLobbies().get(String.valueOf(lobbyID)).addPlayerHandler(this, nicknamePlayer);

            } else {                                                                                             //altrimenti nack
                NackMessage nackMessageForLobby = new NackMessage("lobby_not_available");
                sendMessageFromServer(nackMessageForLobby);
                server.getPlayersNicknames().remove(this.nicknamePlayer);

                try{
                    socketClosing();
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("SIAMO QUI ERRORE");
                }
            }
        }
    }
    /** this method checks if the nickname of the player who wants to login is already
     * used or not in the server, where we store in an arraylist (playersNicknames) all the nicknames
     * @param nicknameChosenPlayer is the nickname to check
     * @return true: the first one means that the size of the arraylist is not 0, but the nickname is not used
     * @return false: we check, the nickname is already used, 'equal' is set to 1 and we return false
     * @return true: the second one means the arraylist is empty, so the player is the first one, which means
     * his nickname can't be already used, so we return true.
     * */
    public boolean checkNickname(String nicknameChosenPlayer){
        int totalNumberOfPlayers = this.server.getPlayersNicknames().size();
        if (totalNumberOfPlayers != 0) {
            int equal = 0;
            for(int k = 0; k < totalNumberOfPlayers; k++){
                if (server.getPlayersNicknames().get(k).equals(nicknameChosenPlayer)){
                    equal = 1;
                    break;
                }
            }
            if(equal != 1){                         //quindi il nickname non è stato usato -> ritorno true
                return true;
            }else{                                  //ritorno false se  è già usato
                return false;
            }
        }else{                                       //se la size dell'arraylist dei player è 0, non può essere usato
            return true;                                //dunque ritorno true
        }
    }

    /** this methood checks if the player wants to create a new match or not.
     * @param requestValue is the boolean 'createNewMatch' in the LoginMessage, true if he wants to create a new match, false otherwise. If
     *                     requestValue is false, we first check if the number of total lobbies is 0, which means there is no lobby to join;
     *                     if so, we send a NoLobbyAvailableMessage, otherwise we call the method askMatchToJoin.
     * @param nicknameOfPlayer is the nickname of the player who wants to create, or not, the new match
     */
    public void checkNewMatchRequest(boolean requestValue, String nicknameOfPlayer){
        if(requestValue){
            playerID = 0;
            sendingAckMatchCreation(playerID, true);  //server.getPlayersNicknames().indexOf(nicknameOfPlayer
        }else{
            boolean availableLobby = false;

            for(int i = 0; i < server.getLobbies().size(); i++){
                if(!server.getLobbiesEnd().get(i)) {
                    if (!server.getLobbies().get(String.valueOf(i)).getPlayingStatus()) {
                        availableLobby = true;
                    }
                }
            }

            if(server.getLobbies().keySet().size() == 0 || !availableLobby){
                playerID = 0;
                NoLobbyAvailableMessage noLobbyAvailableMessage = new NoLobbyAvailableMessage(playerID); //server.getPlayersNicknames().indexOf(nicknameOfPlayer)
                sendMessageFromServer(noLobbyAvailableMessage);
            }else{
                askMatchToJoin();
            }
        }
    }


    /**
     * this method sends an ackMatchCreation after checking no other player has the same nickname and after adding
     * the new player's nickname to the arraylist in the main server.
     * @param playerID is the unique ID associated to the player and it is the index in the playersNicknames arraylist.
     * @param newMatchNeeded is true if the player wants to create a new game, otherwise is false.
     */
    public void sendingAckMatchCreation(int playerID, boolean newMatchNeeded){
        AckMatchCreationMessage ackMatchCreationMessage = new AckMatchCreationMessage(playerID, newMatchNeeded);
        sendMessageFromServer(ackMatchCreationMessage);
    }


    /** This method checks the status of each lobby (which is the controller associated in the hashmap lobbies).
     * If the status is false, it means the match is waiting for other players to join, so it is available, and we add the boolean 'true' and we increase the counter
     * of lobbies that are waiting. If the status is true, it means the match has already started, so it's not available, and we add the boolean 'false'.
     * After that, if the counter is 0, it means all the lobbies are full, so a new one is required, and we
     * send a NoLobbyAvailable Message, otherwise we send a AskMatchToJoinMessage. In the end, we set the player ID
     */
    public void askMatchToJoin() {
        ArrayList<Boolean> availableLobbies = new ArrayList<>();
        ArrayList<Integer> lobbiesNumberOfPlayers = new ArrayList<>();
        ArrayList<Boolean> lobbiesExpertMode = new ArrayList<>();
        ArrayList<Boolean> lobbiesEnd = server.getLobbiesEnd();

        int numberOfLobbiesInWaiting = 0;
        for(String lobby : server.getLobbies().keySet()) {
            if(server.getLobbies().get(lobby).getPlayingStatus()) {
                availableLobbies.add(Integer.parseInt(lobby), false);
            }else {
                if (server.getLobbiesEnd().get(Integer.parseInt(lobby))) {
                    availableLobbies.add(Integer.parseInt(lobby), false);
                }else{
                    availableLobbies.add(Integer.parseInt(lobby), true);
                }
                numberOfLobbiesInWaiting ++;
            }

            // add number of players of the lobby
            lobbiesNumberOfPlayers.add(server.getLobbies().get(lobby).getNumberOfPlayers());
            // add expertMode of the lobby
            lobbiesExpertMode.add(server.getLobbies().get(lobby).isExpertMode());
        }
        if(numberOfLobbiesInWaiting == 0){
            playerID = 0;
            NoLobbyAvailableMessage noLobbyAvailableMessage = new NoLobbyAvailableMessage(playerID); //server.getPlayersNicknames().indexOf(nicknameOfPlayer)
            sendMessageFromServer(noLobbyAvailableMessage);
        }else {
            AskMatchToJoinMessage askMatchToJoinMessage = new AskMatchToJoinMessage(availableLobbies, lobbiesNumberOfPlayers, lobbiesExpertMode, lobbiesEnd); //server.getPlayersNicknames().indexOf(nicknameOfPlayer)
            sendMessageFromServer(askMatchToJoinMessage);
            System.out.println("sent AskMatchToJoinMessage");
        }
    }


    /** this method creates a new lobby if the player wants, so if he has declared in the previous messages (e.g. LoginMessage)
     * that he wants to create a new one.
     * @param nicknameOfNewPlayer is the nickname of the player who wants to create the new lobby.
     * @numberOfTotalLobbies is the number of lobbies we have reached, so it is the size of the keyset of the hashmap lobbies, which is in the main server.
     * @addPlayerHandler is the method through which we add the player to its game in the controller.
     */
    public void lobbyCreation(String nicknameOfNewPlayer, int numberOfPlayer) {
        int numberOfTotalLobbies = server.getLobbies().keySet().size();
        lobbyID = numberOfTotalLobbies;

        ArrayList<Boolean> tempPlayers = new ArrayList<>();          //creiamo arraylist temporaneo
        tempPlayers.add(true);                          //aggiunta del primo (indice zero, per forza collegato -> true)
        for(int i = 1; i < numberOfPlayer; i++){
            tempPlayers.add(false);
        }
        server.getLobbiesPlayersConnection().add(tempPlayers);
        lobbyAccessed = true;
        server.getLobbies().put((String.valueOf(lobbyID)), new Controller(lobbyID));
        //server.getLobbiesStarted().add(false);
        matchStarted = false;
        server.getLobbiesEnd().add(false);

        //matchStarted = true;
        server.getLobbies().get(String.valueOf(lobbyID)).addPlayerHandler(this, nicknameOfNewPlayer);
    }


    /** this method sends a NicknameNotValid Message if the login fails, which means there is another
     * player with the same nickname.
     */
    public void sendingNicknameNotValid(){
        NicknameNotValidMessage nicknameNotValidMessage = new NicknameNotValidMessage();
        outputHandler.println(gsonObj.toJson(nicknameNotValidMessage));
        outputHandler.flush();

    }

    /**
     * This method receives a Message type class, serializes it and sends it to the client.
     * @param msgToSerialize is the message passed by the controller and sent to the client.
     */
    public void messageToSerialize(Message msgToSerialize){
        if(msgToSerialize.getObjectOfMessage().equals("start")){
            matchStarted = true;
            server.checkPlayersConnectionOnStart(lobbyID, playerID, numberPlayerLobby);
        }

        outputHandler.println(gsonObj.toJson(msgToSerialize));
        outputHandler.flush();
    }

    public void sendMessageFromServer(Message msgToSend){
        outputHandler.println(gsonObj.toJson(msgToSend));
        outputHandler.flush();
    }

    public void socketClosing() throws IOException {
        outputHandler.close();
        inputHandler.close();
        clientSocket.close();
        System.out.println("Client " + clientSocket.getInetAddress() + "disconnected from server.");
    }

}