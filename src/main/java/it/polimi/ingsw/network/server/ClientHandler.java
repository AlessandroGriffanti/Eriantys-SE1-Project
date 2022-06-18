package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * It deserializes the json message received and passes it to the controller.
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private String nicknamePlayer;
    private int playerID;
    private int lobbyID;
    private int numberPlayerLobby;

    private boolean matchStarted = false;
    private boolean lobbyAccessed = false;

    /**
     * Gson object "gsonObj" to deserialize the json message received
     */
    private final Gson gsonObj = new Gson();

    //input and output stream
    private PrintWriter outputHandler = null;
    private BufferedReader inputHandler = null;

    /**
     * Constructor of the ClientHandler
     * @param socket is the client socket returned from the serverSocket.accept() method.
     * @param server is a reference to the server.
     */
    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
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

            /*while(clientSocket.isConnected()){
                server.getLobbies().get(server.getLobbyIDByPlayerName(nicknamePlayer)).manageMsg(inputHandler.readLine());          //messaggio passato in json al controller
                System.out.println("pippo while");
            }*/
            System.out.println("pippo while");
            while( !( server.getLobbies().get(String.valueOf(lobbyID)).isMatchEnded() ) ){                                      //
                //System.out.println("is match ended: " + server.getLobbies().get(String.valueOf(lobbyID)).isMatchEnded());
                String msg = inputHandler.readLine();                                                                                   //leggiamo il mex in input dal client
                System.out.println("messaggio ricevuto dal client: " + msg);
                if(server.getLobbies().get(String.valueOf(lobbyID)).isMatchEnded()){
                    server.getLobbiesEnd().set(lobbyID, true);
                }else {
                    server.getLobbies().get(String.valueOf(lobbyID)).manageMsg(msg);                                //mandiamo al controller il messaggio ricevuto dal client
                }
                //Stringget(0).manageMsg(inputHandler.readLine()); // get0 perchè c'è solo questa lobby
            }

            TimeUnit.MILLISECONDS.sleep(5000);

            System.out.println("end");

            outputHandler.close();
            inputHandler.close();
            clientSocket.close();
            System.out.println("Client " + clientSocket.getInetAddress() + "disconnected from server.");


        } catch (IOException e) {
            System.out.println(e.getMessage());

            if(matchStarted) {
                System.out.println("OUT1");
                server.getLobbies().get(String.valueOf(lobbyID)).onePlayerDisconnected(playerID);
                server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, false);
            }else if(lobbyAccessed){
                System.out.println("OUT2");
                server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, false);
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
    public synchronized void loginInServer(String receivedMessageInJson) throws IOException, InterruptedException {
        System.out.println(receivedMessageInJson);
        LoginMessage receivedMessageFromJson; //= new LoginMessage();
        receivedMessageFromJson = gsonObj.fromJson(receivedMessageInJson, LoginMessage.class);
        System.out.println("Message translated");
        String messageObject = receivedMessageFromJson.getObjectOfMessage();
        System.out.println("Object Found.");

        if (messageObject.equals("login")) {
            System.out.println("I received a login message");

            System.out.println("Nickname ricevuto: " + receivedMessageFromJson.getNicknameOfPlayer());


            if (checkNickname(receivedMessageFromJson.getNicknameOfPlayer())){  //if checkNickname returns true, which means the nickname of the player isn't already used
                server.getPlayersNicknames().add(receivedMessageFromJson.getNicknameOfPlayer());    //aggiungiamo il nickname del giocatore all'arraylist nel server
                nicknamePlayer = receivedMessageFromJson.getNicknameOfPlayer();                     //settiamo la variabile nicknamePlayer del clientHandler con il valore del nickname proveniente dal client

                //playerID = server.getPlayersNicknames().size() - 1;
                //System.out.println("Player id " + playerID);

                System.out.println("checked the nickname, it is ok");
                System.out.println("Player: " + receivedMessageFromJson.getNicknameOfPlayer());
                System.out.println("player ok");

                checkNewMatchRequest(receivedMessageFromJson.isCreateNewMatch(), nicknamePlayer);   //controlliamo se vuole creare un nuovo match. se sì mandiamo un loginSuccess
                                                                                                //se no, mandiamo un nolobbyavailable (se non ce ne sono) o un askmatchtojoin

                //nuova rilettura
                String messageReceivedInJson = inputHandler.readLine();                             //qui riceve il nuovo mex dal client
                Message messageReceivedFromJson = gsonObj.fromJson(messageReceivedInJson, Message.class);

                if(messageReceivedFromJson.getObjectOfMessage().equals("creation")){                //se riceve il messaggio di MatchSpecs
                    System.out.println("I received a new match specs message");
                    MatchSpecsMessage matchSpecsMessage = gsonObj.fromJson(messageReceivedInJson, MatchSpecsMessage.class);
                    numberPlayerLobby = matchSpecsMessage.getNumOfPlayers();
                    lobbyCreation(nicknamePlayer, numberPlayerLobby);                                                  // --> allora crea la lobby

                    System.out.println("Lobby created");
                    System.out.println("lobby id: " + lobbyID);
                    System.out.println("player id: "+ playerID);

                    System.out.println("num giocatori: " + server.getLobbies().get(String.valueOf(server.getLobbies().keySet().size() - 1)).getPlayersAddedCounter());    //stampo il numero di giocatori della lobby per controllo
                    server.getLobbies().get(String.valueOf(lobbyID)).manageMsg(messageReceivedInJson);            //passa numero di giocatori massimo


                }else if(messageReceivedFromJson.getObjectOfMessage().equals("chosen lobby")) {          //se il server riceve questo significa che il player ha scelto a che lobby unirsi
                    System.out.println("I received a new chosen lobby message");
                    ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = gsonObj.fromJson(messageReceivedInJson, ReplyChosenLobbyToJoinMessage.class);

                    lobbyID = replyChosenLobbyToJoinMessage.getLobbyIDchosen();
                    System.out.println("lobbyID: " + lobbyID);

                    playerID = server.getLobbies().get(String.valueOf(lobbyID)).getPlayersAddedCounter();
                    server.getLobbiesPlayersConnection().get(lobbyID).set(playerID, true);
                    numberPlayerLobby = server.getLobbies().get(String.valueOf(lobbyID)).getNumberOfPlayers();
                    lobbyAccessed = true;
                    System.out.println("player id: " + playerID + " lobby id: " + lobbyID);

                    IDSetAfterLobbyChoiceMessage idSetAfterLobbyChoice = new IDSetAfterLobbyChoiceMessage(playerID);
                    sendMessageFromServer(idSetAfterLobbyChoice);

                    //matchStarted = true;
                    server.getLobbies().get(String.valueOf(lobbyID)).addPlayerHandler(this, nicknamePlayer);   //aggiungiamo il player alla lobby (cioè il controller dell'hashmap) corrispondente.
                    //System.out.println("nuovo numero di giocatori nella lobby: " + server.getLobbies().get(String.valueOf(lobbyID)).getPlayersAddedCounter());

                }else {
                    System.out.println("Error: not right specs message");
                }

            }else{                                                //se il check del nome dà false, finiamo qui dove viene inviato un nicknamenotvalid e nel client verrà rifatto il login
                System.out.println("nickname already used");
                sendingNicknameNotValid();
                System.out.println("sent nack ok");

                loginInServer(inputHandler.readLine());
            }
        }else {
            System.out.println("Error: not a Login message");
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
        int totalNumberOfPlayers = this.server.getPlayersNicknames().size(); //IN QUESTO CASO FORSE BISOGNA CREARE SUBITO LA LOBBY
        if (totalNumberOfPlayers != 0) {
            int equal = 0;
            for(int k = 0; k < totalNumberOfPlayers; k++){
                if (server.getPlayersNicknames().get(k).equals(nicknameChosenPlayer)){
                    equal = 1;                                              //se ne trovo uno uguale setto equal a 1
                    System.out.println("equals");
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
        if(requestValue == true){
            playerID = 0;
            sendingLoginSuccess(playerID, true);  //server.getPlayersNicknames().indexOf(nicknameOfPlayer
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
                System.out.println("sent NoLobbyAvailableMessage ok");
            }else{
                askMatchToJoin();
            }
        }
    }


    /**
     * this method sends a LoginSuccess message after checking no other player has the same nickname and after adding
     * the new player's nickname to the arraylist in the main server.
     * @param playerID is the unique ID associated to the player and it is the index in the playersNicknames arraylist
     * @param newMatchNeeded is true if the player wants to create a new game, otherwise is false.
     */
    public void sendingLoginSuccess(int playerID, boolean newMatchNeeded){
        LoginSuccessMessage loginSuccessMessage = new LoginSuccessMessage(playerID, newMatchNeeded);
        sendMessageFromServer(loginSuccessMessage);
        System.out.println("sent LoginSuccessMessage");
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
     * @numberOfTotalLobbies is the number of lobbies we have reached, so it is the size of the keyset of the hashmap lobbies, which is in the main server
     * @addPlayerHandler is the method through which we add the player to its game in the controller
     */
    public synchronized void lobbyCreation(String nicknameOfNewPlayer, int numberOfPlayer) {
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
        System.out.println("sent nickNameNotValid ok");

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
        //System.out.println("sent ok");
    }

    public void sendMessageFromServer(Message msgToSend){
        outputHandler.println(gsonObj.toJson(msgToSend));
        outputHandler.flush();
    }

}