package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.ModelView;
import it.polimi.ingsw.view.cli.SchoolBoardView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The class NetworkHandler handles the client-side connection between the server and the client
 */
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
    private Tower towerColor;
    private Wizard wizard;
    private ModelView modelView;
    /**
     * We use this flag to understand if the player has already used the assistant card.
     */
    private boolean assistantChoiceFlag = false;


    /**
     * NetworkHandler constructor which creates a new instance of the NetworkHandler.
     * @param ipReceived is the server ip.
     * @param portReceived is the server port.
     * @param cliReceived is a reference to the cli.
     */
    public NetworkHandler(String ipReceived, int portReceived, CLI cliReceived) {
        this.ip = ipReceived;
        this.port = portReceived;
        this.cli = cliReceived;
    }


    /**
     * This method starts the communication between the client (NetworkHandler) and the server.
     * It initializes the socket, the input and output buffer and launches the login part through the loginFromClient method.
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
     * This method serializes the client message in json and sends it to the server.
     * @param msgToSend is the message to be sent.
     */
    public void sendMessage(Message msgToSend){
        outputPrintClient.println(gsonObj.toJson(msgToSend));
        outputPrintClient.flush();
    }

    /**
     * This method is used by the NetworkHandler, which represent the client-side of the game, to access the game:
     * nickNamePlayer represents the nickName chosen.
     * newMatchBool specifies whether the player wants to create a new match or not.
     */
    public void loginFromClient() {
        nickNamePlayer = cli.loginNickname();
        boolean newMatchBool = cli.newMatchBoolean();
        LoginMessage msgLogin = new LoginMessage(nickNamePlayer, newMatchBool);
        sendMessage(msgLogin);
    }


    /**
     * This method is used by the NetworkHandler class to analyse the received message from the server.
     * @param receivedMessageInJson is the string received in json format, which will be deserialized.
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
                modelView = new ModelView();
                MatchStartMessage matchStartMessage = new MatchStartMessage();
                //qui va settato il numero di giocatori totali della partita, verrà passato dal server (controller)
                matchStartMessage = gsonObj.fromJson(receivedMessageInJson, MatchStartMessage.class);

                System.out.println("NUMERO DI GIOCATORI TOTALI: " + matchStartMessage.getNumPlayer());
                System.out.println("PARTITA IN EXPERT MODE: " + matchStartMessage.isExpertMode());

                System.out.println("player id: " + playerID);
                System.out.println("first id: " + matchStartMessage.getFirstPlayer());

                updateStartModelView(matchStartMessage);                                            //primo update della cli, gli passo il messaggio ricevuto dal server così come parametro così

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
                        if ((ackMessageMapped.getNextPlayer() == playerID) && (towerColor == null)){
                            ArrayList<Tower> notAvailableTowerColors = ackMessageMapped.getNotAvailableTowerColors();
                            towerColor = cli.towerChoiceNext(notAvailableTowerColors);

                            ChosenTowerColorMessage chosenTowerColorMessage = new ChosenTowerColorMessage();
                            chosenTowerColorMessage.setColor(towerColor);
                            chosenTowerColorMessage.setSender_ID(playerID);
                            sendMessage(chosenTowerColorMessage);
                            System.out.println("sent ok");

                            break;
                        }else if((ackMessageMapped.getNextPlayer() == playerID) && (towerColor != null)) {
                            //sendAckFromClient();
                            //cli.turnWaiting();
                            wizard = cli.deckChoice();
                            ChosenDeckMessage chosenDeckMessage = new ChosenDeckMessage();
                            chosenDeckMessage.setDeck(wizard);
                            chosenDeckMessage.setSender_ID(playerID);
                            System.out.println("DECK SCELTO NON NELL'ACK: " + chosenDeckMessage.getDeck());
                            sendMessage(chosenDeckMessage);
                            break;
                        }else if((ackMessageMapped.getNextPlayer() != playerID) && (towerColor != null)){
                            cli.turnWaiting();
                            break;
                        }

                    case "deck":
                        if ((ackMessageMapped.getNextPlayer() == playerID) && (wizard == null)){
                            ArrayList<Wizard> notAvailableDecks = ackMessageMapped.getNotAvailableDecks();

                            wizard = cli.deckChoiceNext(notAvailableDecks);
                            ChosenDeckMessage chosenDeckMessage = new ChosenDeckMessage();
                            chosenDeckMessage.setDeck(wizard);
                            chosenDeckMessage.setSender_ID(playerID);
                            System.out.println("DECK SCELTO NELL'ACK: " + chosenDeckMessage.getDeck());
                            sendMessage(chosenDeckMessage);
                            break;
                        }else if(ackMessageMapped.getNextPlayer() != playerID && (wizard != null)){
                            cli.turnWaiting();
                            break;
                        }else if((ackMessageMapped.getNextPlayer() == playerID) && (wizard != null)) {
                            cli.bagClick();
                            sendBagClickedByFirstClient();
                            break;
                        }
                    case "refillClouds":
                        cli.showStudentsInEntrancePlayer(playerID, modelView);                         //A questo punto della partita ad esempio, cioè prima della scelta dell'assistente, possiamo mostrare al giocatore i suoi studenti nell'entrance.

                        if(ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag == false){
                            int assistantChosen = cli.assistantChoice(modelView.getAssistantCardsValuesPlayer());
                            modelView.getAssistantCardsValuesPlayer().remove(modelView.getAssistantCardsValuesPlayer().indexOf(assistantChosen));          //rimuovo la carta scelta dal giocatore dalla modelview
                            for(Integer i : modelView.getAssistantCardsValuesPlayer()){                                                                     //questo for è inserito solo per controllare che la carta venga effettivamente tolta nella modelview
                                System.out.print(i + " ");
                            }
                            assistantChoiceFlag = true;
                            sendChosenAssistantCardMessage(assistantChosen);
                            break;
                        }else if(ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == false){                 //se non tocca a te e non l'hai ancora scelta
                            cli.turnWaiting();
                            break;
                        }
                    case "assistant":                                                                          //significa che il giocatore in questione non è il primo giocatore a scegliere l'assistente
                        if(ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag == false){
                            ArrayList<Integer> assistantAlreadyUsedInThisRound = ackMessageMapped.getAssistantAlreadyUsedInThisRound();
                            int assistantChosen = cli.assistantChoiceNext(modelView.getAssistantCardsValuesPlayer(), assistantAlreadyUsedInThisRound );
                            modelView.getAssistantCardsValuesPlayer().remove(modelView.getAssistantCardsValuesPlayer().indexOf(assistantChosen)); //lo rimuovo dalla modelview
                            for(Integer i : modelView.getAssistantCardsValuesPlayer()){                                                             //questo for è inserito solo per controllare che la carta venga effettivamente tolta nella modelview
                                System.out.print(i + " ");
                            }
                            assistantChoiceFlag = true;
                            sendChosenAssistantCardMessage(assistantChosen);
                            break;
                        }else if(ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag){
                            cli.turnWaiting();
                            break;
                        }else if(ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag == true) { //tocca a te e hai già scelto, mandi il messaggio movedstudentsfromentrance
                            assistantChoiceFlag = false;                                                        //qui cambia la flag il primo giocatore
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
     * This method creates an Ack message and sends it to the server.
     */
    private void sendAckFromClient(){
        AckMessage ackMessage = new AckMessage();
        sendMessage(ackMessage);
    }


    /**
     * This method is used by the client after receiving a loginSuccess message from the server;
     * It creates a MatchSpecsMessage message with the number of players and the variant (expert mode) chosen by the player
     */
    public void creatingNewSpecsFromClient(){
        MatchSpecsMessage newMatchSpecsMessage;

        int numberOfPlayerInTheLobby = cli.numberOfPlayer();
        boolean expertMode = cli.expertModeSelection();

        newMatchSpecsMessage = new MatchSpecsMessage( numberOfPlayerInTheLobby, expertMode );

        sendMessage(newMatchSpecsMessage);
    }


    /**
     * This method creates a new  BagClick message and sends it to the server.
     */
    public void sendBagClickedByFirstClient(){
        BagClickMessage bagClickMessage = new BagClickMessage();
        bagClickMessage.setSender_ID(playerID);
        sendMessage(bagClickMessage);
        System.out.println("SENT BAG CLICKED OK");
    }

    /**
     * This method creates a new ChosenAssistantCard message and sends it to the server.
     * @param assistantChosen is the assistant chosen.
     */
    public void sendChosenAssistantCardMessage(int assistantChosen){
        ChosenAssistantCardMessage chosenAssistantCardMessage = new ChosenAssistantCardMessage(assistantChosen);
        chosenAssistantCardMessage.setSender_ID(playerID);
        sendMessage(chosenAssistantCardMessage);
    }

    /**
     * This method is used to update the modelView after receiving the matchStartMessage.
     * @param matchStartMessage is the matchStartMatchMessage received.
     */
    public void updateStartModelView(MatchStartMessage matchStartMessage){
        modelView.setNumberOfPlayersGame(matchStartMessage.getNumPlayer());                                     //setto il numero di giocatori della partita
        modelView.setExpertModeGame(matchStartMessage.isExpertMode());                                           //setto exepert mode  a true se la partita è in expertmo
        for(Integer i : matchStartMessage.getStudentsInEntrance().keySet()){
            ArrayList<Creature> creatureInEntranceAtStart = matchStartMessage.getStudentsInEntrance().get(i);           //Questo dovrebbe essere l'update degli studenti nell'entrance
            modelView.getSchoolBoardPlayers().put(i, new SchoolBoardView(modelView));
            modelView.getSchoolBoardPlayers().get(i).getEntrancePlayer().setStudentsInTheEntrancePlayer(creatureInEntranceAtStart);
        }
    }



}
