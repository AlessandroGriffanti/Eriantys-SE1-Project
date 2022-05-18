package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.Server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

/** this class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * it should deserialize the json received and pass the information to the controller.
 * @server is a reference to the main server
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private String nicknamePlayer;

    /**
     * Gson object "gsonObj" to deserialize the json message received
     */
    private final Gson gsonObj = new Gson();

    //input and output stream
    private InputStream inputStream = null;
    private PrintWriter outputHandler = null;
    private BufferedReader inputHandler = null;


    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    public void run() {

        try {
            System.out.println("running");
            inputStream = clientSocket.getInputStream(); //A CHE SERVE??
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

            System.out.println("Nickname ricevuto: " + receivedMessageFromJson.getNicknameOfPlayer());

            if (checkNickname(receivedMessageFromJson.getNicknameOfPlayer())){  //if checkNickname returns true, which means the nickname of the player isn't already used
                server.getPlayersNicknames().add(receivedMessageFromJson.getNicknameOfPlayer());    //aggiungiamo il nickname del giocatore all'arraylist nel server
                nicknamePlayer = receivedMessageFromJson.getNicknameOfPlayer();                     //settiamo la variabile nicknamePlayer del clientHandler con il valore del nickname proveniente dal client

                System.out.println("checked the nickname, it is ok");
                System.out.println("Player " + server.getPlayersNicknames().indexOf(receivedMessageFromJson.getNicknameOfPlayer()) + ": " + receivedMessageFromJson.getNicknameOfPlayer());
                System.out.println("player ok");

                checkNewMatchRequest(receivedMessageFromJson.isCreateNewMatch(), nicknamePlayer);   //controlliamo se vuole creare un nuovo match. se sì mandiamo un loginSuccess
                                                                                                    //se no, mandiamo un nolobbyavailable (se non ce ne sono) o un askmatchtojoin

                String messageReceivedInJson = inputHandler.readLine();                             //qui riceve il nuovo mex dal client
                Message messageReceivedFromJson = gsonObj.fromJson(messageReceivedInJson, Message.class);

                if(messageReceivedFromJson.getObjectOfMessage().equals("creation")){                //se riceve il messaggio di MatchSpecs
                    System.out.println("I received a new match specs message");
                    lobbyCreation(nicknamePlayer);                                                  // --> allora crea la lobby

                    System.out.println("Lobby created");
                   // wait(5*1000);   //ATTENZIONE

                    System.out.println("num giocatori: " + server.getLobbies().get(String.valueOf(server.getLobbies().keySet().size()-1)).getPlayersAddedCounter());    //stampo il numero di giocatori della lobby per controllo
                    server.getLobbies().get(String.valueOf(server.getLobbies().keySet().size() - 1)).manageMsg(messageReceivedInJson);            //passa numero di giocatori massimo


                }else if(messageReceivedFromJson.getObjectOfMessage().equals("chosen lobby")) {          //se il server riceve questo significa che il player ha scelto a che lobby unirsi
                    System.out.println("I received a new chosen lobby message");
                    ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = gsonObj.fromJson(messageReceivedInJson, ReplyChosenLobbyToJoinMessage.class);

                    server.getLobbies().get(String.valueOf(replyChosenLobbyToJoinMessage.getLobbyIDchosen())).addPlayerHandler(this, nicknamePlayer);   //aggiungiamo il player alla lobby (cioè il controller dell'hashmap) corrispondente.
                    System.out.println("nuovo numero di giocatori nella lobby: " + server.getLobbies().get(String.valueOf(replyChosenLobbyToJoinMessage.getLobbyIDchosen())).getPlayersAddedCounter());
                   // server.getLobbies().get(String.valueOf(server.getLobbies().keySet().size())).manageMsg(messageReceivedInJson);      // questo serve?

                }else {
                    System.out.println("Error: not right specs message");
                }

                //SIAMO ARRIVATI QUI

            }else{                                                //se il check del nome dà false, finiamo qui dove viene inviato un nicknamenotvalid e nel client verrà rifatto il login
                System.out.println("nickname already used");
                sendingNicknameNotValid();
                System.out.println("sent nack ok");
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
     * @param requestValue is false, we first check if the number of total lobbies is 0, which means there is no lobby to join.
     * If so, we send a NoLobbyAvailableMessage, otherwise we call the method askMatchToJoin.
     * @param nicknameOfPlayer is the nickname of the player who wants to create, or not, the new match
     */
    public void checkNewMatchRequest(boolean requestValue, String nicknameOfPlayer){
        if(requestValue == true){
            sendingLoginSuccess(server.getPlayersNicknames().indexOf(nicknameOfPlayer), true);
        }else{
            if(server.getLobbies().keySet().size() == 0){
                NoLobbyAvailableMessage noLobbyAvailableMessage = new NoLobbyAvailableMessage();

                outputHandler.println(gsonObj.toJson(noLobbyAvailableMessage));
                outputHandler.flush();

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
        outputHandler.println(gsonObj.toJson(loginSuccessMessage));
        outputHandler.flush();
        System.out.println("sent LoginSuccessMessage");
    }

    /** this method checks the status of each lobby (which is the controller associated in the hashmap lobbies).
     * If the status is true, it means the match is waiting for other players to join, so it is available and we add the boolean 'true'.
     * If the status is false, it means the match has already started, so it's not available and we add the boolean 'false'.
     * Then, we create and send a AskMatchToJoinMessage
     */
    public void askMatchToJoin() {
        ArrayList<Boolean> listAvailableLobbies = new ArrayList<>();
        for(String lobby : server.getLobbies().keySet()) {
            if(server.getLobbies().get(lobby).getPlayingStatus() == true) {
                listAvailableLobbies.add(true);
            }else {
                listAvailableLobbies.add(false);
            }
        }
        AskMatchToJoinMessage askMatchToJoinMessage = new AskMatchToJoinMessage(listAvailableLobbies);

        outputHandler.println(gsonObj.toJson(askMatchToJoinMessage));
        outputHandler.flush();

        System.out.println("sent AskMatchToJoinMessage");
    }

    /** this method creates a new lobby if the player wants, so if he has declared in the previous messages (e.g. LoginMessage)
     * that he wants to create a new one.
     * @param nicknameOfNewPlayer is the nickname of the player who wants to create the new lobby.
     * @numberOfTotalLobbies is the number of lobbies we have reached, so it is the size of the keyset of the hashmap lobbies, which is in the main server
     * @addPlayerHandler is the method through which we add the player to its game in the controller
     */
    public synchronized void lobbyCreation(String nicknameOfNewPlayer) {
        int numberOfTotalLobbies = server.getLobbies().keySet().size();                                                            //+1 ???
        server.getLobbies().put((String.valueOf(numberOfTotalLobbies)), new Controller(numberOfTotalLobbies));
        server.getLobbies().get(String.valueOf(numberOfTotalLobbies)).addPlayerHandler(this, nicknameOfNewPlayer);
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
        outputHandler.println(gsonObj.toJson(msgToSerialize));
        outputHandler.flush();
        System.out.println("sent ok");
    }

}