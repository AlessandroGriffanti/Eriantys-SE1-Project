package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.DiningRoomView;
import it.polimi.ingsw.view.cli.ModelView;
import it.polimi.ingsw.view.cli.SchoolBoardView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * The class NetworkHandler handles the client-side connection between the server and the client
 */
public class NetworkHandler {
    private CLI cli;
    private String nickNamePlayer;
    private int playerID;
    private Socket clientSocket = null;
    private final Gson gsonObj = new Gson();
    private BufferedReader inputBufferClient = null;
    private PrintWriter outputPrintClient = null;
    private String ip;
    private int port;



    private String lastCallFrom = null;

    /**
     * This attribute represents the personal modelView of a player, and it is created in the first
     * update method, after receiving the match start message.
     */
    private ModelView modelView;

    private boolean matchEnd = false;
    private boolean matchStarted = false;

    //questi potrebbero essere spostati in mv
    private Tower towerColor;
    private Wizard wizard;


    /**
     * We use this flag to understand if the player has already used the assistant card.
     */
    private boolean assistantChoiceFlag = false;
    /**
     * We use this attribute to track the number of student the player has moved from the entrance.
     */
    private int numberOfChosenStudent = 0;

    /**
     * This attribute indicates the student the player decided to move from the entrance in the action_1.
     */
    private int studentChosen;

    /**
     * This attribute tracks the position of mother nature in the game.
     */
    int motherNatureIslandID;

    /**
     * We use this attribute to know the maximum number of students that can be moved from the entrance.
     * It can be 3, if there are 2 players playing, or 4 if there are 3 players playing.
     */
    private int numberOfStudentToMoveAction1;

    private boolean messengerActive = false;

    private int jesterNumber = 0;

    private boolean characterUsed = false;

    private boolean action3valid = true;

    private int nextPlayerAction3NotValid;
    /**
     * NetworkHandler constructor which creates a new instance of the NetworkHandler.
     *
     * @param ipReceived   is the server ip.
     * @param portReceived is the server port.
     * @param cliReceived  is a reference to the cli.
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
    public void startClient() throws IOException, InterruptedException {
        clientSocket = new Socket(ip, port);
        //clientSocket.setSoTimeout(10000);
        inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputPrintClient = new PrintWriter(clientSocket.getOutputStream());

        // 1- per prima cosa il client avviato fa un login sul server
        loginFromClient();

        while (!matchEnd) {
            //System.out.println("Still connected");
               String msgFromServer = inputBufferClient.readLine();
               System.out.println("messaggio dal server: " + msgFromServer);
               System.out.println(" ");
               analysisOfReceivedMessageServer(msgFromServer);
        }

        //TimeUnit.MILLISECONDS.sleep(5000);


        inputBufferClient.close();
        outputPrintClient.close();
        clientSocket.close();


    }

    /**
     * This method serializes the client message in json and sends it to the server.
     *
     * @param msgToSend is the message to be sent.
     */
    public void sendMessage(Message msgToSend) {
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
     *
     * @param receivedMessageInJson is the string received in json format, which will be deserialized.
     */
    public void analysisOfReceivedMessageServer(String receivedMessageInJson) throws InterruptedException {
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

            case "MatchCreation":
                cli.loginSuccess();
                AckMatchCreationMessage msgLoginSuccess = gsonObj.fromJson(receivedMessageInJson, AckMatchCreationMessage.class);
                boolean newMatchNeeded = msgLoginSuccess.getNewMatchNeeded();
                playerID = msgLoginSuccess.getPlayerID();
                //System.out.println("player id " + playerID);
                if (newMatchNeeded) {
                    creatingNewSpecsFromClient();
                } else {            //TODO questo else non serve forse, DA VERIFICARE
                    sendAckFromClient();
                }
                break;

            case "join match":
                AskMatchToJoinMessage askMatchToJoinMessage = gsonObj.fromJson(receivedMessageInJson, AskMatchToJoinMessage.class);
                //System.out.println("player id" + playerID);

                int lobbyIDChosenByPlayer = cli.lobbyToChoose(askMatchToJoinMessage.getLobbiesTmp(), askMatchToJoinMessage.getLobbiesExpertMode(), askMatchToJoinMessage.getLobbiesNumberOfPlayers(), askMatchToJoinMessage.getLobbiesEnd());

                ReplyChosenLobbyToJoinMessage replyChosenLobbyToJoinMessage = new ReplyChosenLobbyToJoinMessage(lobbyIDChosenByPlayer);
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
                this.matchStarted = true;
                cli.startAlert();
                modelView = new ModelView(playerID);
                MatchStartMessage matchStartMessage;
                matchStartMessage = gsonObj.fromJson(receivedMessageInJson, MatchStartMessage.class);

                System.out.println("NUMERO DI GIOCATORI TOTALI: " + matchStartMessage.getNumPlayer());
                System.out.println("PARTITA IN EXPERT MODE: " + matchStartMessage.isExpertMode());

                System.out.println("player id: " + playerID);
                System.out.println("first id: " + matchStartMessage.getFirstPlayer());

                updateStartModelView(matchStartMessage);                                            //primo update della cli, gli passo il messaggio ricevuto dal server così posso inizializzare


                if (matchStartMessage.getFirstPlayer() == playerID) {
                    cli.isYourTurn();

                    towerColor = cli.towerChoice(modelView);

                    ChosenTowerColorMessage chosenTowerColorMessage = new ChosenTowerColorMessage();
                    chosenTowerColorMessage.setColor(towerColor);
                    chosenTowerColorMessage.setSender_ID(playerID);
                    sendMessage(chosenTowerColorMessage);
                    System.out.println("sent ok");


                } else if (matchStartMessage.getFirstPlayer() != playerID){
                    cli.turnWaitingTowers(matchStartMessage.getFirstPlayer());

                }
                break;
            case "end":
                matchIsEnded(receivedMessageInJson);

                break;

            case "ack":                                                                                     //abbiamo raggruppato alcuni messaggi del server in ack e/o nack, dunque il server generico ci manda un ack e nel subObject specifica di cosa si tratta
                AckMessage ackMessageMapped = gsonObj.fromJson(receivedMessageInJson, AckMessage.class);    //se vediamo che l'oggetto del messaggio è un ack, rimappiamo il messaggio in uno della classe AckMessage
                switch (ackMessageMapped.getSubObject()) {
                    case "waiting":
                        cli.ackWaiting();
                        break;

                    case "tower_color":
                        if ((ackMessageMapped.getNextPlayer() == playerID) && (towerColor == null)) {
                            ArrayList<Tower> notAvailableTowerColors = ackMessageMapped.getNotAvailableTowerColors();
                            towerColor = cli.towerChoiceNext(notAvailableTowerColors, modelView);

                            ChosenTowerColorMessage chosenTowerColorMessage = new ChosenTowerColorMessage();
                            chosenTowerColorMessage.setColor(towerColor);
                            chosenTowerColorMessage.setSender_ID(playerID);
                            sendMessage(chosenTowerColorMessage);
                            System.out.println("sent ok");

                            break;

                        } else if ((ackMessageMapped.getNextPlayer() == playerID) && (towerColor != null)) {
                            wizard = cli.deckChoice();
                            ChosenDeckMessage chosenDeckMessage = new ChosenDeckMessage();
                            chosenDeckMessage.setDeck(wizard);
                            chosenDeckMessage.setSender_ID(playerID);
                            sendMessage(chosenDeckMessage);
                            break;

                        } else if ((ackMessageMapped.getNextPlayer() != playerID) && (towerColor != null)) {
                            if(modelView.getNumberOfPlayersGame() == 3){
                                if(ackMessageMapped.getNotAvailableTowerColors().size() != 3){
                                    cli.turnWaitingTowers(ackMessageMapped.getNextPlayer());
                                }else{
                                    cli.turnWaitingDecks(ackMessageMapped.getNextPlayer());
                                }
                            }else{
                                if(ackMessageMapped.getNotAvailableTowerColors().size() != 2){
                                    cli.turnWaitingTowers(ackMessageMapped.getNextPlayer());
                                }else{
                                    cli.turnWaitingDecks(ackMessageMapped.getNextPlayer());
                                }
                            }
                            break;

                        } else if ((ackMessageMapped.getNextPlayer() != playerID) && (towerColor == null)) {
                            cli.turnWaitingTowers(ackMessageMapped.getNextPlayer());
                            break;

                        }
                        break;

                    case "deck":
                        if ((ackMessageMapped.getNextPlayer() == playerID) && (wizard == null)) {
                            ArrayList<Wizard> notAvailableDecks = ackMessageMapped.getNotAvailableDecks();

                            wizard = cli.deckChoiceNext(notAvailableDecks);
                            ChosenDeckMessage chosenDeckMessage = new ChosenDeckMessage();
                            chosenDeckMessage.setDeck(wizard);
                            chosenDeckMessage.setSender_ID(playerID);
                            sendMessage(chosenDeckMessage);
                            break;

                        } else if ((ackMessageMapped.getNextPlayer() == playerID) && (wizard != null)) {
                            cli.bagClick();
                            sendBagClickedByFirstClient();
                            break;

                        } else if (ackMessageMapped.getNextPlayer() != playerID && (wizard != null)) {
                            if(modelView.getNumberOfPlayersGame() == 3){
                                if(ackMessageMapped.getNotAvailableDecks().size() != 3){
                                    cli.turnWaitingDecks(ackMessageMapped.getNextPlayer());
                                }else{
                                    cli.turnWaiting(ackMessageMapped.getNextPlayer());
                                }
                            }else{
                                if(ackMessageMapped.getNotAvailableDecks().size() != 2){
                                    cli.turnWaitingDecks(ackMessageMapped.getNextPlayer());
                                }else{
                                    cli.turnWaiting(ackMessageMapped.getNextPlayer());
                                }
                            }
                            break;

                        } else if (ackMessageMapped.getNextPlayer() != playerID && (wizard == null)) {
                            cli.turnWaitingDecks(ackMessageMapped.getNextPlayer());
                            break;
                        }

                        break;

                    case "refillClouds":
                        modelView.setStudentsOnClouds(ackMessageMapped.getStudents());

                        cli.showSchoolBoard(playerID, modelView);
                        cli.showCharacterCardsInTheGame(modelView);
                        cli.showIslandsSituation(modelView);
                        cli.showClouds(modelView);

                        if (ackMessageMapped.getNextPlayer() == playerID && !assistantChoiceFlag) {
                            int assistantChosen = cli.assistantChoice(modelView.getAssistantCardsValuesPlayer());
                            modelView.setLastAssistantChosen(assistantChosen);

                            assistantChoiceFlag = true;
                            sendChosenAssistantCardMessage(assistantChosen);

                        } else if (ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == false) {                 //se non tocca a te e non l'hai ancora scelta
                            cli.turnWaitingAssistant(ackMessageMapped.getNextPlayer());
                        }

                        break;

                    case "assistant":                                                                          //significa che il giocatore in questione non è il primo giocatore a scegliere l'assistente
                        if (ackMessageMapped.getNextPlayer() == playerID && !assistantChoiceFlag) {
                            ArrayList<Integer> assistantAlreadyUsedInThisRound = ackMessageMapped.getAssistantAlreadyUsedInThisRound();
                            int assistantChosen = cli.assistantChoiceNext(modelView.getAssistantCardsValuesPlayer(), assistantAlreadyUsedInThisRound);

                            modelView.setLastAssistantChosen(assistantChosen);

                            assistantChoiceFlag = true;
                            sendChosenAssistantCardMessage(assistantChosen);

                        } else if (ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == false) {
                            cli.turnWaitingAssistant(ackMessageMapped.getNextPlayer());

                        } else if (ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == true) {
                            cli.turnWaitingAssistant(ackMessageMapped.getNextPlayer());

                        } else if (ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag) {   //tocca a te e hai già scelto, mandi il messaggio movedStudentsFromEntrance
                            assistantChoiceFlag = false;
                            studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);                    //facciamo scegliere quale studente muovere, gli passo la model view così nella cli posso avere accesso agli studenti e l'id del player.
                            if(studentChosen == -2){
                                lastCallFrom = "choiceOfStudentsToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            int locationChosen = cli.choiceLocationToMove(playerID, modelView); //facciamo scegliere dove voglia muovere lo studente, isola o diningRoom;
                            if(locationChosen == -2){
                                lastCallFrom = "choiceLocationToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);                       // qui ci arrivo solo se non ha scelto di usare un character (infatti negli if precedenti ho un break)
                            numberOfChosenStudent ++;

                        }

                        break;

                    case "action_1_dining_room":
                        updateModelViewActionOne(ackMessageMapped);

                        if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent < numberOfStudentToMoveAction1) {
                            studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                            if(studentChosen == -2){
                                lastCallFrom = "choiceOfStudentsToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            int locationChosen = cli.choiceLocationToMove(playerID, modelView);
                            if(locationChosen == -2){
                                lastCallFrom = "choiceLocationToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;
                        } else if (ackMessageMapped.getNextPlayer() != playerID && numberOfChosenStudent <= numberOfStudentToMoveAction1 ) {
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        } else if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent == numberOfStudentToMoveAction1) {
                            motherNatureIslandID = 0;
                            for (int i = 0; i < 12; i++) {
                                if(modelView.getIslandGame().get(i) != null) {
                                    if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                        motherNatureIslandID = i;
                                    }
                                }
                            }
                            int chosenIslandID = cli.choiceMotherNatureMovement(playerID, motherNatureIslandID, modelView);
                            if(chosenIslandID == -2){
                                lastCallFrom = "choiceMotherNatureMovement";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            sendMovedMotherNature(chosenIslandID);
                        }

                        break;

                    case "action_1_island":
                        updateModelViewActionOne(ackMessageMapped);
                        if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent < numberOfStudentToMoveAction1) {          //tocca ancora  a lui e ha scelto meno di 3 studenti e il precedente l'ha mosso su isola
                            studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                            if(studentChosen == -2){
                                lastCallFrom = "choiceOfStudentsToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            int locationChosen = cli.choiceLocationToMove(playerID, modelView);
                            if(locationChosen == -2){
                                lastCallFrom = "choiceLocationToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;
                        } else if (ackMessageMapped.getNextPlayer() != playerID && numberOfChosenStudent <= numberOfStudentToMoveAction1) {
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());

                        } else if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent == numberOfStudentToMoveAction1) {
                            motherNatureIslandID = 0;
                            for (int i = 0; i < 12; i++) {
                                if(modelView.getIslandGame().get(i) != null) {
                                    if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                        motherNatureIslandID = i;
                                    }
                                }
                            }
                            int chosenIslandID = cli.choiceMotherNatureMovement(playerID, motherNatureIslandID, modelView);
                            if(chosenIslandID == -2){
                                lastCallFrom = "choiceMotherNatureMovement";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            sendMovedMotherNature(chosenIslandID);
                        }
                        break;

                    case "action_2_movement":
                        if(ackMessageMapped.isAction3Valid() == false){
                            action3valid = false;
                            nextPlayerAction3NotValid = ackMessageMapped.getNextPlayer();
                        }
                        updateModelViewActionTwo(ackMessageMapped);
                        cli.newMotherNaturePosition(ackMessageMapped.getDestinationIsland_ID());
                        //TimeUnit.MILLISECONDS.sleep(500);
                        break;

                    case "action_2_influence":
                        updateModelViewActionTwo(ackMessageMapped);
                        for (int i = 0; i < 12; i++) {
                            if(modelView.getIslandGame().get(i) != null) {
                                if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                    motherNatureIslandID = i;
                                }
                            }
                        }
                        if (ackMessageMapped.isMasterChanged()) {
                            modelView.getIslandGame().get(motherNatureIslandID).setMasterOfArchipelago(ackMessageMapped.getNewMaster_ID());
                            if (ackMessageMapped.getNewMaster_ID() == playerID && ackMessageMapped.getPreviousMaster_ID() != playerID) {
                                cli.newMaster(modelView, playerID);
                            }else if(ackMessageMapped.getNewMaster_ID() != playerID && ackMessageMapped.getPreviousMaster_ID() == playerID){
                                cli.oldMaster(modelView, motherNatureIslandID, playerID);
                            }
                        }
                        //TimeUnit.MILLISECONDS.sleep(500);
                        break;

                    case "action_2_union":
                        updateModelViewActionTwo(ackMessageMapped);
                        if(!(ackMessageMapped.getIslandsUnified().equals("none"))){
                            int islandUnifiedFlag = -2;
                            if(ackMessageMapped.getIslandsUnified().equals("previous")){
                                islandUnifiedFlag = -1;
                            }else if(ackMessageMapped.getIslandsUnified().equals("next")){
                                islandUnifiedFlag = 1;
                            }else if(ackMessageMapped.getIslandsUnified().equals("both")){
                                islandUnifiedFlag = 0;
                            }
                            cli.showUnion(modelView, motherNatureIslandID, islandUnifiedFlag, ackMessageMapped.getIslands_ID());
                        }
                        if(action3valid) {
                            if (ackMessageMapped.getNextPlayer() == playerID) {
                                int cloudChosenID = cli.chooseCloud(playerID, modelView);
                                if (cloudChosenID == -2) {
                                    lastCallFrom = "chooseCloud";
                                    String characterChosen = cli.characterChoice(modelView);
                                    sendRequestCharacterMessage(characterChosen);
                                    break;
                                }
                                sendChosenCloudMessage(cloudChosenID);
                            } else if (ackMessageMapped.getNextPlayer() != playerID) {
                                cli.turnWaitingClouds(ackMessageMapped.getNextPlayer());
                            }
                        }else{
                            if(nextPlayerAction3NotValid == playerID){
                                studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                                if(studentChosen == -2){
                                    lastCallFrom = "choiceOfStudentsToMove";
                                    String characterChosen = cli.characterChoice(modelView);
                                    sendRequestCharacterMessage(characterChosen);
                                    break;
                                }
                                int locationChosen = cli.choiceLocationToMove(playerID, modelView);
                                if(locationChosen == -2){
                                    lastCallFrom = "choiceLocationToMove";
                                    String characterChosen = cli.characterChoice(modelView);
                                    sendRequestCharacterMessage(characterChosen);
                                    break;
                                }
                                sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                                numberOfChosenStudent++;
                            }else{
                                cli.turnWaiting(nextPlayerAction3NotValid);
                            }
                        }
                        break;

                    case "action_3":
                        updateModelViewActionThree(ackMessageMapped);
                        messengerActive = false;
                        characterUsed = false;
                        if(ackMessageMapped.getNextPlayer() == playerID && ackMessageMapped.isNextPlanningPhase()){
                            cli.newRoundBeginning();
                            cli.bagClick();
                            sendBagClickedByFirstClient();
                        }else if(ackMessageMapped.getNextPlayer() != playerID && ackMessageMapped.isNextPlanningPhase()){
                            cli.newRoundBeginning();
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }else if(ackMessageMapped.getNextPlayer() != playerID && !ackMessageMapped.isNextPlanningPhase()){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }else if(ackMessageMapped.getNextPlayer() == playerID && !ackMessageMapped.isNextPlanningPhase()){
                            int studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                            if(studentChosen == -2){
                                lastCallFrom = "choiceOfStudentsToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            int locationChosen = cli.choiceLocationToMove(playerID, modelView);
                            if(locationChosen == -2){
                                lastCallFrom = "choiceLocationToMove";
                                String characterChosen = cli.characterChoice(modelView);
                                sendRequestCharacterMessage(characterChosen);
                                break;
                            }
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;
                            assistantChoiceFlag = false;
                        }
                        break;
                    case "monk":        //TODO ? help stampiamo cosa fanno i char?
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            int studentChosen = cli.choiceStudentMonk(modelView);
                            int islandChosen = cli.choiceIslandMonk(modelView);
                            sendCharacterDataMonk(studentChosen, islandChosen);
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;
                    case "cook" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            sendCharacterDataCook();
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }

                        break;

                    case "centaur" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            sendCharacterDataCentaur();
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;
                    case "jester":
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            ArrayList<Integer> studentsFromEntranceJester = cli.choiceStudentEntranceJester(playerID, modelView);
                            ArrayList<Integer> studentsFromCardJester = cli.choiceStudentCardJester(modelView);
                            sendCharacterDataJester(studentsFromEntranceJester, studentsFromCardJester);

                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;
                    case "knight" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            sendCharacterDataKnight();
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;

                    case "messenger" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            sendCharacterDataMessenger();
                        } else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;

                    case "herbalist" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            int islandIDChosenHerbalist = cli.choiceHerbalist(modelView);

                            sendCharacterDataHerbalist(islandIDChosenHerbalist);
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;

                    case "ambassador" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            int islandIDChosenAmbassador = cli.choiceAmbassador(modelView);

                            sendCharacterDataAmbassador(islandIDChosenAmbassador);
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;

                    case "mushroomsMerchant" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            Creature chosenStudentMushroomsMerchant = cli.choiceMushroomsMerchant();

                            sendCharacterDataMushroomsMerchant(chosenStudentMushroomsMerchant);
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;
                    case "bard":
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            ArrayList<Integer> studentsFromEntranceBard = cli.choiceStudentEntranceBard(playerID, modelView);
                            ArrayList<Creature> studentsFromDiningRoomBard = cli.choiceStudentDiningRoomBard(playerID, modelView);
                            sendCharacterDataBard(studentsFromEntranceBard, studentsFromDiningRoomBard);
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }

                        break;
                    case "trafficker" :
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            Creature chosenStudentTrafficker = cli.choiceTrafficker();
                            sendCharacterDataTrafficker(chosenStudentTrafficker);
                        }else if (ackMessageMapped.getNextPlayer() != playerID) {
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }

                        break;

                    case "princess":
                        if (ackMessageMapped.getNextPlayer() == playerID) {
                            int chosenStudentID = cli.choicePrincess(modelView);

                            sendCharacterDataPrincess(chosenStudentID);
                        }else if (ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting(ackMessageMapped.getNextPlayer());
                        }
                        break;
                }

                break;

            case "nack":
                NackMessage nackMessageMapped = gsonObj.fromJson(receivedMessageInJson, NackMessage.class);
                switch (nackMessageMapped.getSubObject()) {
                    case "invalid_mother_nature_movement":
                        cli.invalidMotherNatureMovement();


                        for (int i = 0; i < 12; i++) {
                            if(modelView.getIslandGame().get(i) != null) {
                                if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                    motherNatureIslandID = i;
                                }
                            }
                        }
                        int chosenIslandID = cli.choiceMotherNatureMovement(playerID, motherNatureIslandID, modelView);
                        if(chosenIslandID == -2){
                            lastCallFrom = "choiceMotherNatureMovement";
                            String characterChosen = cli.characterChoice(modelView);
                            sendRequestCharacterMessage(characterChosen);
                            break;
                        }
                        sendMovedMotherNature(chosenIslandID);

                        break;

                    case "invalid_cloud":
                        int cloudChosenID = cli.invalidCloudSelection(playerID, modelView);
                        if(cloudChosenID == -2){
                            lastCallFrom = "chooseCloud";
                            String characterChosen = cli.characterChoice(modelView);
                            sendRequestCharacterMessage(characterChosen);
                            break;
                        }
                        sendChosenCloudMessage(cloudChosenID);
                        break;

                    case "herbalist":
                        characterUsed = false;
                        cli.invalidHerbalistChoice(nackMessageMapped.getExplanationMessage());
                        followingChoiceToMake(lastCallFrom);
                        break;

                    case "princess":
                        characterUsed = false;
                        cli.invalidPrincessChoice(nackMessageMapped.getExplanationMessage());
                        followingChoiceToMake(lastCallFrom);
                        break;

                    case "character_price":
                        characterUsed = false;
                        cli.invalidCharacter(nackMessageMapped.getExplanationMessage());
                        followingChoiceToMake(lastCallFrom);
                        break;

                    case "lobby_not_available":
                        matchEnd = true;
                        cli.lobbyChosenNotAvailable(nackMessageMapped.getExplanationMessage());
                        break;

                    case "table_full":
                        numberOfChosenStudent--;
                        studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                        if(studentChosen == -2){
                            lastCallFrom = "choiceOfStudentsToMove";
                            String characterChosen = cli.characterChoice(modelView);
                            sendRequestCharacterMessage(characterChosen);
                            break;
                        }
                        int locationChosen = cli.choiceLocationToMove(playerID, modelView);
                        if(locationChosen == -2){
                            lastCallFrom = "choiceLocationToMove";
                            String characterChosen = cli.characterChoice(modelView);
                            sendRequestCharacterMessage(characterChosen);
                            break;
                        }
                        sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                        numberOfChosenStudent++;

                        break;
                }
                break;

            case "no lobby available":
                NoLobbyAvailableMessage noLobbyAvailableMessage = gsonObj.fromJson(receivedMessageInJson, NoLobbyAvailableMessage.class);
                playerID = noLobbyAvailableMessage.getPlayerID();
                cli.lobbyNotAvailable();
                creatingNewSpecsFromClient();
                break;

            case "character_ack":
                characterUsed = true;
                AckCharactersMessage ackCharactersMessage = gsonObj.fromJson(receivedMessageInJson, AckCharactersMessage.class);
                if(ackCharactersMessage.getCharacter().equals("messenger")){
                    messengerActive = true;
                }
                //update:
                updateCharacterCard(ackCharactersMessage);
                //callFrom:
                if(ackCharactersMessage.getRecipient() == playerID) {                              //faccio queste cose se il mio player id corrisponde, per l'altro giocatore non esiterà ancora nessuna callfrom, mi darebbe null
                    followingChoiceToMake(lastCallFrom);
                    cli.characterUsed(ackCharactersMessage.getCharacter(), ackCharactersMessage.getRecipient());
                }else{
                    cli.characterUsed(ackCharactersMessage.getCharacter(), ackCharactersMessage.getRecipient());
                    cli.turnWaiting(ackCharactersMessage.getNextPlayer());
                }
                break;

            default:
                cli.errorObject();
        }
    }

    /**
     * This method is used to let the player make the following choice regarding
     * the moment when he chose to use the character card.
     * @param lastCallFrom is the moment when the player uses the character card.
     */
    public void followingChoiceToMake(String lastCallFrom){
        if (lastCallFrom.equals("choiceOfStudentsToMove")) {
            studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
            if(studentChosen == -2){
                this.lastCallFrom = "choiceOfStudentsToMove";
                String characterChosen = cli.characterChoice(modelView);
                sendRequestCharacterMessage(characterChosen);
            }else {
                int locationChosen = cli.choiceLocationToMove(playerID, modelView);
                if(locationChosen == -2){
                    this.lastCallFrom = "choiceLocationToMove";
                    String characterChosen = cli.characterChoice(modelView);
                    sendRequestCharacterMessage(characterChosen);
                }else {
                    sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                    numberOfChosenStudent++;
                }
            }
        } else if (lastCallFrom.equals("choiceLocationToMove")) {
            int locationChosen = cli.choiceLocationToMove(playerID, modelView);
            if(locationChosen == -2){
                this.lastCallFrom = "choiceLocationToMove";
                String characterChosen = cli.characterChoice(modelView);
                sendRequestCharacterMessage(characterChosen);
            }else {
                sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                numberOfChosenStudent++;
            }
        } else if (lastCallFrom.equals("choiceMotherNatureMovement")) {
            int chosenIslandID = cli.choiceMotherNatureMovement(playerID, motherNatureIslandID, modelView);
            if(chosenIslandID == -2) {
                this.lastCallFrom = "choiceMotherNatureMovement";
                String characterChosen = cli.characterChoice(modelView);
                sendRequestCharacterMessage(characterChosen);
            }else {
                sendMovedMotherNature(chosenIslandID);
            }
        }else if(lastCallFrom.equals("chooseCloud")){
            int cloudChosenID = cli.chooseCloud(playerID, modelView);
            if(cloudChosenID == -2){
                this.lastCallFrom = "chooseCloud";
                String characterChosen = cli.characterChoice(modelView);
                sendRequestCharacterMessage(characterChosen);
            }else {
                sendChosenCloudMessage(cloudChosenID);
            }
        }
    }
    /**
     * This method creates an Ack message and sends it to the server.
     */
    private void sendAckFromClient() {
        AckMessage ackMessage = new AckMessage();
        sendMessage(ackMessage);
    }


    /**
     * This method is used by the client after receiving a loginSuccess message from the server;
     * It creates a MatchSpecsMessage message with the number of players and the variant (expert mode) chosen by the player
     */
    public void creatingNewSpecsFromClient() {
        MatchSpecsMessage newMatchSpecsMessage;

        int numberOfPlayerInTheLobby = cli.numberOfPlayer();
        boolean expertMode = cli.expertModeSelection();

        newMatchSpecsMessage = new MatchSpecsMessage(numberOfPlayerInTheLobby, expertMode);

        sendMessage(newMatchSpecsMessage);
    }


    /**
     * This method creates a new  BagClick message and sends it to the server.
     */
    public void sendBagClickedByFirstClient() {
        BagClickMessage bagClickMessage = new BagClickMessage();
        bagClickMessage.setSender_ID(playerID);
        sendMessage(bagClickMessage);
        System.out.println("SENT BAG CLICKED OK");
    }

    /**
     * This method creates a new ChosenAssistantCard message and sends it to the server.
     *
     * @param assistantChosen is the assistant chosen.
     */
    public void sendChosenAssistantCardMessage(int assistantChosen) {
        ChosenAssistantCardMessage chosenAssistantCardMessage = new ChosenAssistantCardMessage(assistantChosen);
        chosenAssistantCardMessage.setSender_ID(playerID);
        sendMessage(chosenAssistantCardMessage);
    }

    /**
     * This method creates a new MovedStudentsFromEntrance message and sends it to the server.
     *
     * @param studentChosen  is the student chosen.
     * @param locationChosen is the location, island or dining room, chosen.
     */
    public void sendMovedStudentsFromEntrance(int studentChosen, int locationChosen) {
        MovedStudentsFromEntranceMessage movedStudentsFromEntranceMessage = new MovedStudentsFromEntranceMessage();
        movedStudentsFromEntranceMessage.setStudent_ID(studentChosen);
        movedStudentsFromEntranceMessage.setLocation(locationChosen);
        movedStudentsFromEntranceMessage.setSender_ID(playerID);

        sendMessage(movedStudentsFromEntranceMessage);
    }

    /**
     * This method creates a new MovedMotherNatureMessage and sends it to the server.
     *
     * @param destinationIsland_ID is the destination Island where to move Mother Nature.
     */
    public void sendMovedMotherNature(int destinationIsland_ID) {
        MovedMotherNatureMessage movedMotherNatureMessage = new MovedMotherNatureMessage(playerID);
        movedMotherNatureMessage.setDestinationIsland_ID(destinationIsland_ID);
        movedMotherNatureMessage.setSender_ID(playerID);
        sendMessage(movedMotherNatureMessage);
    }

    /**
     * This method is used to create and send the Chosen Cloud Message to the server.
     */
    public void sendChosenCloudMessage(int cloudChosen){
        ChosenCloudMessage chosenCloudMessage = new ChosenCloudMessage(cloudChosen);
        chosenCloudMessage.setSender_ID(playerID);
        sendMessage(chosenCloudMessage);
    }

    /**
     * This method is used to create and send the Chosen Character Message to the server.
     */
    public void sendRequestCharacterMessage(String characterChosen){ //, String callFrom
        //this.lastCallFrom = callFrom;
        CharacterRequestMessage characterRequestMessage = new CharacterRequestMessage(playerID, characterChosen);
        sendMessage(characterRequestMessage);
    }

    /**
     * This method is used to send character data for Monk character card.
     */
    public void sendCharacterDataMonk(int studentChosen, int islandChosen){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "monk");
        characterDataMessage.setStudent_ID(studentChosen);
        characterDataMessage.setIsland_ID(islandChosen);
        sendMessage(characterDataMessage);
    }
    /**
     * This method is used to send character data for Cook character card.
     */
    public void sendCharacterDataCook(){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "cook");
        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Centaur character card.
     */
    public void sendCharacterDataCentaur(){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "centaur");
        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Jester character card.
     * @param studentsFromEntranceJester is the arraylist of students to move the entrance.
     * @param studentsFromCardJester is the arraylist of students to move the Jester card.
     */
    public void sendCharacterDataJester(ArrayList<Integer> studentsFromEntranceJester, ArrayList <Integer> studentsFromCardJester){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "jester");
        characterDataMessage.setStudentsFromPlayerEntrance(studentsFromEntranceJester);
        characterDataMessage.setElementsFromCard(studentsFromCardJester);
        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Knight character card.
     */
    public void sendCharacterDataKnight(){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "knight");
        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Messenger character card.
     */
    public void sendCharacterDataMessenger(){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "messenger");
        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Herbalist character card.
     * @param islandIDChosenHerbalist is the ID of the chosen island where to put the No Entry Tile.
     */
    public void sendCharacterDataHerbalist(int islandIDChosenHerbalist){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "herbalist");
        characterDataMessage.setIsland_ID(islandIDChosenHerbalist);

        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for ambassador character card.
     * @param islandIDChosenAmbassador is the ID of the chosen island where to compute the influence.
     */
    public void sendCharacterDataAmbassador(int islandIDChosenAmbassador){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "ambassador");
        characterDataMessage.setIsland_ID(islandIDChosenAmbassador);

        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Mushrooms Merchant character card.
     * @param chosenStudentMushroomsMerchant is the Creature type of the chosen student by the client.
     */
    public void sendCharacterDataMushroomsMerchant(Creature chosenStudentMushroomsMerchant){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "mushroomsMerchant");
        characterDataMessage.setCreature(chosenStudentMushroomsMerchant);

        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Bard character card.
     * @param studentsFromEntranceBard is the arraylist of students to move the entrance.
     * @param studentsFromDiningRoomBard is the arraylist of students to move the Jester card.
     */
    public void sendCharacterDataBard(ArrayList<Integer> studentsFromEntranceBard, ArrayList <Creature> studentsFromDiningRoomBard ){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "bard");
        characterDataMessage.setStudentsFromPlayerEntrance(studentsFromEntranceBard);
        characterDataMessage.setStudentsFromPlayerDiningRoom(studentsFromDiningRoomBard);
        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Trafficker character card.
     * @param chosenStudentTrafficker is the Creature type of the chosen student by the client.
     */
    public void sendCharacterDataTrafficker(Creature chosenStudentTrafficker){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "trafficker");
        characterDataMessage.setCreature(chosenStudentTrafficker);

        sendMessage(characterDataMessage);
    }

    /**
     * This method is used to send character data for Princess character card.
     * @param chosenStudentIDPrincess is the id of the chosen student on the card by the client.
     */
    public void sendCharacterDataPrincess(int chosenStudentIDPrincess){
        CharacterDataMessage characterDataMessage = new CharacterDataMessage(playerID, "princess");
        characterDataMessage.setStudent_ID(chosenStudentIDPrincess);

        sendMessage(characterDataMessage);
    }





    /**
     * This method is used to update the modelView after receiving the matchStartMessage.
     *
     * @param matchStartMessage is the matchStartMatchMessage received.
     */
    public void updateStartModelView(MatchStartMessage matchStartMessage) {
        numberOfStudentToMoveAction1 = matchStartMessage.getNumPlayer() + 1;                                 //settiamo il numero di studenti massimi che possono essere mossi nell'action 1 in base al numero di giocatori totali

        assistantChoiceFlag = false;

        modelView.setNumberOfPlayersGame(matchStartMessage.getNumPlayer());                                      //setto il numero di giocatori totali della partita
        modelView.setExpertModeGame(matchStartMessage.isExpertMode());                                           //setto exepert mode  a true se la partita è in expertmode
        if (modelView.isExpertModeGame()) {
            if(matchStartMessage.getNumPlayer() == 2){
                modelView.setCoinGame(18);
            }else if(matchStartMessage.getNumPlayer() == 3){
                modelView.setCoinGame(17);
            }

            modelView.getCharacterCardsInTheGame().addAll(matchStartMessage.getCharacters());
            for (String s : modelView.getCharacterCardsInTheGame()) {
                if(s.equals("monk")){
                    modelView.getCharactersDataView().setMonkStudents(matchStartMessage.getMonkStudents());
                    //System.out.print("numero monk: " + modelView.getCharactersDataView().getMonkStudents().size());
                }else if(s.equals("jester")){
                    modelView.getCharactersDataView().setJesterStudents(matchStartMessage.getJesterStudents());
                    //System.out.print("numero jester: " + modelView.getCharactersDataView().getJesterStudents().size());
                }else if(s.equals("princess")){
                    modelView.getCharactersDataView().setPrincessStudents(matchStartMessage.getPrincessStudents());
                    //System.out.print("numero princess: " + modelView.getCharactersDataView().getPrincessStudents().size());
                }else if(s.equals("herbalist")){
                    modelView.getCharactersDataView().setHerbalistNumberOfNoEntryTile(4);
                }
            }
        }

        System.out.println( "NUMERO DI COIN NELLA PARTITA: " + modelView.getCoinGame());                          //stampe di controllo
        //System.out.println("partita in expert mode??? " + modelView.isExpertModeGame());
        modelView.getIslandGame().get(matchStartMessage.getMotherNaturePosition()).setMotherNaturePresence(true);   //setto madre natura sull'isola corretta passata nel messaggio di match start

        int motherNaturePosition = matchStartMessage.getMotherNaturePosition();                 //metto gli studenti iniziali (uno per isola tranne dove c'è MN e isola opposta) sulle giuste isole
        motherNatureIslandID = matchStartMessage.getMotherNaturePosition();
        int j = 1;
        for (Creature c : matchStartMessage.getStudentsOnIslands()) {
            int islandID = motherNaturePosition + j;
            if (j == 6) {
                modelView.getIslandGame().get((islandID + 1) % 12).addStudent(c);
                j += 2;
            } else {
                modelView.getIslandGame().get(islandID % 12).addStudent(c);
                j++;
            }
        }

        for (Integer i : matchStartMessage.getStudentsInEntrance().keySet()) {                                                        //integer è la key dell'hashmap del match start message, è l'id del player
            ArrayList<Creature> creatureInEntranceAtStart = matchStartMessage.getStudentsInEntrance().get(i);                       //Questo dovrebbe essere l'update degli studenti nell'entrance
            modelView.getSchoolBoardPlayers().put(i, new SchoolBoardView(modelView, matchStartMessage.getNumPlayer()));             //passo anche il numero di giocatori totali della partita così posso settare il numero di torri in towerAreaView
            modelView.getSchoolBoardPlayers().get(i).getEntrancePlayer().setStudentsInTheEntrancePlayer(creatureInEntranceAtStart);
        }


    }

    /**
     * This method is used to update the modelView after receiving the action_1 ack message.
     *
     * @param ackMessageMapped is the ack message received.
     */
    public void updateModelViewActionOne(AckMessage ackMessageMapped) {
        assistantChoiceFlag = false;
        if(ackMessageMapped.getPreviousOwnerOfProfessor() != -1 && ackMessageMapped.isProfessorTaken()){
            modelView.getSchoolBoardPlayers().get(ackMessageMapped.getPreviousOwnerOfProfessor()).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(),false);     //se c'era un precedente possessore del professore del tipo mosso, setto a false il corrispondente valore nella professortable
        }
        if (ackMessageMapped.getRecipient() == playerID) {
            if (ackMessageMapped.getSubObject().equals("action_1_dining_room")) {
                modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().set(ackMessageMapped.getStudentMoved_ID(), null);
                int numberOfStudentOfType = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved());
                modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(), (numberOfStudentOfType + 1) );
                if(ackMessageMapped.isProfessorTaken() && !modelView.getSchoolBoardPlayers().get(playerID).getProfessorTablePlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved())) {
                    modelView.getSchoolBoardPlayers().get(playerID).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(), ackMessageMapped.isProfessorTaken());
                }

                //UPDATE COINS:
                //System.out.println("get type student moved: " + modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved()));
                int module = (modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved())) % 3;
                //System.out.println("occupied seats: " + module);
                if (module == 0) {
                    //System.out.println("old coin reserve act1: " + modelView.getCoinGame());
                    //System.out.println("old coin player act1: " + ackMessageMapped.getRecipient() + modelView.getCoinPlayer().get(ackMessageMapped.getRecipient()));
                    int newPlayerCoin = modelView.getCoinPlayer().get(ackMessageMapped.getRecipient()) + 1;
                    //System.out.println("new coin player act1: " + newPlayerCoin);
                    modelView.getCoinPlayer().replace(ackMessageMapped.getRecipient(), newPlayerCoin);
                    modelView.setCoinGame(modelView.getCoinGame() - 1);
                    //System.out.println("new coin reserve act1: " + modelView.getCoinGame());
                }

            } else if (ackMessageMapped.getSubObject().equals("action_1_island")) {
                modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().set(ackMessageMapped.getStudentMoved_ID(), null);
                modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).addStudent(ackMessageMapped.getTypeOfStudentMoved());
            }
        }else if(ackMessageMapped.getRecipient()!= playerID){                                                                               //aggiorno la modelview anche se non sono io il giocatore interessato dalle mosse
            if(ackMessageMapped.getSubObject().equals("action_1_dining_room")){
                modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getEntrancePlayer().getStudentsInTheEntrancePlayer().set(ackMessageMapped.getStudentMoved_ID(), null);                              //setto a null il corrispondente valore nell'entrance
                int numberOfStudentOfType = modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved());
                modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(), numberOfStudentOfType + 1);            //updato la diningroom del giocatore

                if(ackMessageMapped.isProfessorTaken() && !modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getProfessorTablePlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved())) {  //controllo se acquisisco il controllo del professore e se precedentemente era falso il controllo sul prof, perchè altrimenti lo sovrascrivo
                    modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(), ackMessageMapped.isProfessorTaken());  //updato la professortable del giocatore
                }

                //UPDATE COINS:
                //System.out.println("get type student moved: " + modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved()));
                int module = (modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved())) % 3;
                //System.out.println("occupied seats: " + module);
                if (module == 0) {
                    //System.out.println("old coin reserve act1: " + modelView.getCoinGame());
                    //System.out.println("old coin player act1: " + ackMessageMapped.getRecipient() + modelView.getCoinPlayer().get(ackMessageMapped.getRecipient()));
                    int newPlayerCoin = modelView.getCoinPlayer().get(ackMessageMapped.getRecipient()) + 1;
                    //System.out.println("new coin player act1: " + newPlayerCoin);
                    modelView.getCoinPlayer().replace(ackMessageMapped.getRecipient(), newPlayerCoin);
                    modelView.setCoinGame(modelView.getCoinGame() - 1);
                    //System.out.println("new coin reserve act1: " + modelView.getCoinGame());
                }

            }else if (ackMessageMapped.getSubObject().equals("action_1_island")) {
                modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getEntrancePlayer().getStudentsInTheEntrancePlayer().set(ackMessageMapped.getStudentMoved_ID(), null);
                modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).addStudent(ackMessageMapped.getTypeOfStudentMoved());
            }
        }
    }

    /**
     * This method is used to update the modelView after receiving the action_2 ack message.
     *
     * @param ackMessageMapped is the ack message received.
     */
    public void updateModelViewActionTwo(AckMessage ackMessageMapped) {
        if (ackMessageMapped.getSubObject().equals("action_2_movement")) {
            if (ackMessageMapped.getRecipient() == playerID) {
                modelView.getAssistantCardsValuesPlayer().remove(modelView.getLastAssistantChosen());   //rimuovo assistente
                numberOfChosenStudent = 0;
            }
            for (int i = 0; i < 12; i++) {
                if(modelView.getIslandGame().get(i) != null) {
                    if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {                    //la posizione di MN è cambiata, setto a false il booleano corrispondente nell'isola dove c'era
                        modelView.getIslandGame().get(i).setMotherNaturePresence(false);
                    } else if (i == ackMessageMapped.getDestinationIsland_ID()) {                       //setto a true l'attributo nella nuova isola dove si trova MN dopo lo spostamento
                        modelView.getIslandGame().get(i).setMotherNaturePresence(true);
                        motherNatureIslandID = i;
                    }
                }
            }
            if (modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).removeNoEntryTile();
            }


        } else if (ackMessageMapped.getSubObject().equals("action_2_influence")) { //cambiamenti relativi al calcolo dell'influenza
            if (ackMessageMapped.isMasterChanged()) {
                int motherNatureIsland = 0;                                                                  //id dell'isola dove c'è madre natura.
                for (int i = 0; i < 12; i++) {
                    if(modelView.getIslandGame().get(i) != null) {
                        if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                            motherNatureIsland = i;
                        }
                    }
                }
                if (ackMessageMapped.getNewMaster_ID() != -1) { // && ackMessageMapped.getPreviousMaster_ID() != playerID) {
                    modelView.getIslandGame().get(motherNatureIsland).setTowerColor(towerColor);    //o stampiamo questo nuovo colore o gli altri non lo sanno
                    int numberTowerMotherIsland = modelView.getIslandGame().get(motherNatureIsland).getNumberOfTower();    //1     //prendiamo quante torri sono sull'isola dove arriva MN
                    if (numberTowerMotherIsland == 0) {
                        numberTowerMotherIsland++;                                                                             //diventa almeno una che viene tolta dalla schoolboard
                    }
                    modelView.getIslandGame().get(motherNatureIsland).setNumberOfTower(numberTowerMotherIsland);        //rimpiazzo con 1
                    int numberCurrentTowerSchoolBoard = modelView.getSchoolBoardPlayers().get(ackMessageMapped.getNewMaster_ID()).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer();
                    modelView.getSchoolBoardPlayers().get(ackMessageMapped.getNewMaster_ID()).getTowerAreaPlayer().setCurrentNumberOfTowersPlayer(numberCurrentTowerSchoolBoard - numberTowerMotherIsland);       //viene aggiornata la plancia con il nuovo numero corretto di torri

                    if(ackMessageMapped.getPreviousMaster_ID() != -1 ) {
                        numberCurrentTowerSchoolBoard = modelView.getSchoolBoardPlayers().get(ackMessageMapped.getPreviousMaster_ID()).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer();
                        modelView.getSchoolBoardPlayers().get(ackMessageMapped.getPreviousMaster_ID()).getTowerAreaPlayer().setCurrentNumberOfTowersPlayer(numberCurrentTowerSchoolBoard + numberTowerMotherIsland);       //viene aggiornata la plancia con il nuovo numero corretto di torri

                    }
                }

            }
        } else if (ackMessageMapped.getSubObject().equals("action_2_union")) {
            if (ackMessageMapped.getIslandsUnified().equals("previous")) {               //copio le creature dall'isola previous a quella nuova dove arriva madre natura

                //setting current and previous
                int currentIslandID = motherNatureIslandID;
                int previousIslandID = ackMessageMapped.getIslands_ID().get(0);

                //students
                for (Creature c : Creature.values()) {
                    int numberPreviousStudents = modelView.getIslandGame().get(previousIslandID).getStudentsPopulation().get(c);
                    int numberCurrentStudents = modelView.getIslandGame().get(currentIslandID).getStudentsPopulation().get(c);            //cambiamo il numero di studenti sull'isola rimasta
                    modelView.getIslandGame().get(currentIslandID).getStudentsPopulation().replace(c, numberPreviousStudents + numberCurrentStudents);
                }

                //towers
                int numberTowerPrevious = modelView.getIslandGame().get(previousIslandID).getNumberOfTower();
                int numberTowerCurrent = modelView.getIslandGame().get(currentIslandID).getNumberOfTower();                  //cambiamo il numero di torri sull'isola
                if (numberTowerPrevious == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                    numberTowerPrevious++;
                }
                if (numberTowerCurrent == 0) {
                    numberTowerCurrent++;
                }
                modelView.getIslandGame().get(currentIslandID).setNumberOfTower(numberTowerCurrent + numberTowerPrevious);

                //no entry tile
                if (modelView.getIslandGame().get(currentIslandID).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                    modelView.getIslandGame().get(currentIslandID).removeNoEntryTile();
                    modelView.getCharactersDataView().incrementHerbalistNoEntryTile();

                }

                //removing island
                modelView.getIslandGame().set(previousIslandID, null);

            } else if (ackMessageMapped.getIslandsUnified().equals("next")) {
                //setting current and next
                int currentIslandID = motherNatureIslandID;
                int nextIslandID = ackMessageMapped.getIslands_ID().get(0);

                //students
                for (Creature c : Creature.values()) {
                    int numberNextStudents = modelView.getIslandGame().get(nextIslandID).getStudentsPopulation().get(c);
                    int numberCurrentStudents = modelView.getIslandGame().get(currentIslandID).getStudentsPopulation().get(c);            //cambiamo il numero di studenti sull'isola rimasta
                    modelView.getIslandGame().get(currentIslandID).getStudentsPopulation().replace(c, numberNextStudents + numberCurrentStudents);
                }

                //towers
                int numberTowerNext = modelView.getIslandGame().get(nextIslandID).getNumberOfTower();
                int numberTowerCurrent = modelView.getIslandGame().get(currentIslandID).getNumberOfTower();                  //cambiamo il numero di torri sull'isola
                if (numberTowerNext == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                    numberTowerNext++;
                }
                if (numberTowerCurrent == 0) {
                    numberTowerCurrent++;
                }
                modelView.getIslandGame().get(currentIslandID).setNumberOfTower(numberTowerCurrent + numberTowerNext);

                //no entry tile
                if (modelView.getIslandGame().get(currentIslandID).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                    modelView.getIslandGame().get(currentIslandID).removeNoEntryTile();
                    modelView.getCharactersDataView().incrementHerbalistNoEntryTile();
                }

                //removing island
                modelView.getIslandGame().set(nextIslandID, null);

            } else if (ackMessageMapped.getIslandsUnified().equals("both")) {
                //setting current, next and previous
                int currentIslandID = motherNatureIslandID;
                int previousIslandID = ackMessageMapped.getIslands_ID().get(0);
                int nextIslandID = ackMessageMapped.getIslands_ID().get(1);

                //students
                for (Creature c : Creature.values()) {
                    int numberPreviousStudents = modelView.getIslandGame().get(previousIslandID).getStudentsPopulation().get(c);
                    int numberNextStudents = modelView.getIslandGame().get(nextIslandID).getStudentsPopulation().get(c);
                    int numberCurrentStudents = modelView.getIslandGame().get(currentIslandID).getStudentsPopulation().get(c);            //cambiamo il numero di studenti sull'isola rimasta
                    modelView.getIslandGame().get(currentIslandID).getStudentsPopulation().replace(c, numberPreviousStudents + numberNextStudents + numberCurrentStudents);
                }

                //towers
                int numberTowerPrevious = modelView.getIslandGame().get(previousIslandID).getNumberOfTower();
                int numberTowerNext = modelView.getIslandGame().get(nextIslandID).getNumberOfTower();
                int numberTowerCurrent = modelView.getIslandGame().get(currentIslandID).getNumberOfTower();                  //cambiamo il numero di torri sull'isola
                if (numberTowerPrevious == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                    numberTowerPrevious++;
                }
                if (numberTowerNext == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                    numberTowerNext++;
                }
                if (numberTowerCurrent == 0) {
                    numberTowerCurrent++;
                }
                modelView.getIslandGame().get(currentIslandID).setNumberOfTower(numberTowerCurrent + numberTowerNext + numberTowerPrevious);

                //no entry tile
                if (modelView.getIslandGame().get(currentIslandID).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                    modelView.getIslandGame().get(currentIslandID).removeNoEntryTile();
                    modelView.getCharactersDataView().incrementHerbalistNoEntryTile();

                }

                //removing island
                modelView.getIslandGame().set(previousIslandID, null);
                modelView.getIslandGame().set(nextIslandID, null);

            }
        }


    }

    /**
     * This method is used to update the modelView after receiving the action_3 ack message.
     * @param ackMessageMapped is the ack message received.
     */
    public void updateModelViewActionThree(AckMessage ackMessageMapped){
        if(modelView.getNumberOfPlayersGame() == 2){
            if(ackMessageMapped.getCloudChosen_ID() == 0){
                for(int i = 0; i< 3; i++){
                    modelView.getStudentsOnClouds().set(i, null);
                }

            }else if(ackMessageMapped.getCloudChosen_ID() == 1){
                for(int i = 3; i< 6; i++){
                    modelView.getStudentsOnClouds().set(i, null);
                }

            }
        }else if(modelView.getNumberOfPlayersGame() == 3){
            if(ackMessageMapped.getCloudChosen_ID() == 0){
                for(int i = 0; i< 4; i++){
                    modelView.getStudentsOnClouds().set(i, null);
                }

            }else if(ackMessageMapped.getCloudChosen_ID() == 1){
                for(int i = 4; i< 8; i++){
                    modelView.getStudentsOnClouds().set(i, null);
                }

            }else if(ackMessageMapped.getCloudChosen_ID() == 2){
                for(int i = 8; i< 12; i++){
                    modelView.getStudentsOnClouds().set(i, null);
                }
            }
        }
        ArrayList<Creature> creatureInEntranceAfterClouds = ackMessageMapped.getStudents();                                                 //update dell'entrance dopo scelta nuvola, lo facciamo per tutte le modelview visto che tutti possono vedere l'entrance di tutti
        modelView.getSchoolBoardPlayers().get(ackMessageMapped.getRecipient()).getEntrancePlayer().setStudentsInTheEntrancePlayer(creatureInEntranceAfterClouds);


    }

    /**
     *This method is used to update the modelView after using a character card.
     * @param ackCharactersMessage is the ack received after a character card effect is resolved.
     */
    public void updateCharacterCard(AckCharactersMessage ackCharactersMessage){
        String characterUsed = ackCharactersMessage.getCharacter();
        //cli.characterConfirm(characterUsed);

        if(characterUsed.equals("monk")){
            modelView.getIslandGame().get(ackCharactersMessage.getIsland_ID()).addStudent(ackCharactersMessage.getStudent());
        }else if(characterUsed.equals("bard")){
            modelView.getSchoolBoardPlayers().get(ackCharactersMessage.getRecipient()).getEntrancePlayer().setStudentsInTheEntrancePlayer(ackCharactersMessage.getEntranceOfPlayer());
            modelView.getSchoolBoardPlayers().get(ackCharactersMessage.getRecipient()).getDiningRoomPlayer().setOccupiedSeatsPlayer(ackCharactersMessage.getPlayerDiningRoom());
            for(Integer player : ackCharactersMessage.getAllPlayersProfessors().keySet()) {
                for (Creature c : Creature.values()) {
                    if (ackCharactersMessage.getAllPlayersProfessors().get(player).contains(c)) {
                        modelView.getSchoolBoardPlayers().get(player).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(c, true);
                    } else {
                        modelView.getSchoolBoardPlayers().get(player).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(c, false);

                    }
                }
            }
            modelView.getCoinPlayer().replace(ackCharactersMessage.getRecipient(), ackCharactersMessage.getPlayerCoins());
        }else if(characterUsed.equals("jester")){
            modelView.getCharactersDataView().setJesterStudents(ackCharactersMessage.getStudentsOnCard());
            modelView.getSchoolBoardPlayers().get(ackCharactersMessage.getRecipient()).getEntrancePlayer().setStudentsInTheEntrancePlayer(ackCharactersMessage.getEntranceOfPlayer());
        }else if(characterUsed.equals("ambassador_influence")){       //TODO towercolor da ackCharactersMessage
            matchEnd = ackCharactersMessage.isEndOfMatch();           //TODO controllo se ackCharactersMessage è accettato
            updateModelViewActionTwo(ackCharactersMessage);            //TODO controllare se update model view venga chiamato correttamente

        }else if (characterUsed.equals("ambassador_union")){
            matchEnd = ackCharactersMessage.isEndOfMatch();
            //updateModelViewActionTwo(ackCharactersMessage);


        }else if (characterUsed.equals("herbalist")){
            modelView.getIslandGame().get(ackCharactersMessage.getIsland_ID()).addNoEntryTile();
            modelView.getCharactersDataView().setHerbalistNumberOfNoEntryTile(ackCharactersMessage.getNumberOfElementsOnTheCard());

        }else if (characterUsed.equals("princess")){
            modelView.getCharactersDataView().setPrincessStudents(ackCharactersMessage.getStudentsOnCard());
            modelView.getSchoolBoardPlayers().get(ackCharactersMessage.getRecipient()).getDiningRoomPlayer().setOccupiedSeatsPlayer(ackCharactersMessage.getPlayerDiningRoom());
            for(Integer player : ackCharactersMessage.getAllPlayersProfessors().keySet()) {
                for (Creature c : Creature.values()) {
                    if (ackCharactersMessage.getAllPlayersProfessors().get(player).contains(c)) {
                        modelView.getSchoolBoardPlayers().get(player).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(c, true);
                    }else{
                        modelView.getSchoolBoardPlayers().get(player).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(c, false);
                    }
                }
            }
            int module = (modelView.getSchoolBoardPlayers().get(ackCharactersMessage.getRecipient()).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackCharactersMessage.getCreature())) % 3;
            //System.out.println("occupied seats: " + module);
            if (module == 0) {
                //System.out.println("old coin reserve chupd: " + modelView.getCoinGame());
                //System.out.println("old coin player chupd: " + ackCharactersMessage.getRecipient() + modelView.getCoinPlayer().get(ackCharactersMessage.getRecipient()));
                int newPlayerCoin = modelView.getCoinPlayer().get(ackCharactersMessage.getRecipient()) + 1;
                //System.out.println("new coin player chupd: " + newPlayerCoin);
                modelView.getCoinPlayer().replace(ackCharactersMessage.getRecipient(), newPlayerCoin);
                modelView.setCoinGame(modelView.getCoinGame() - 1);
                //System.out.println("new coin reserve chupd: " + modelView.getCoinGame());
            }
        }else if (characterUsed.equals("trafficker")){
            // set the professorTables
            for(int player_ID : ackCharactersMessage.getAllPlayersProfessors().keySet()) {
                for (Creature c : Creature.values()) {
                    if (ackCharactersMessage.getAllPlayersProfessors().get(player_ID).contains(c)) {
                        modelView.getSchoolBoardPlayers().get(player_ID).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(c, true);
                    }else{
                        modelView.getSchoolBoardPlayers().get(player_ID).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(c, false);
                    }
                }
            }

            // set the diningRooms
            HashMap<Integer, HashMap<Creature, Integer>> allDiningRooms = ackCharactersMessage.getAllPlayersDiningRoom();
            DiningRoomView diningRoom;
            for(int player_ID : allDiningRooms.keySet()){
                diningRoom = modelView.getSchoolBoardPlayers().get(player_ID).getDiningRoomPlayer();
                diningRoom.setOccupiedSeatsPlayer(allDiningRooms.get(player_ID));
            }
        }

        //System.out.println("old coin reserve chupd: " + modelView.getCoinGame());
        //System.out.println("old coin player chupd: " + ackCharactersMessage.getRecipient() + modelView.getCoinPlayer().get(ackCharactersMessage.getRecipient()));
        int newCoinPlayer = modelView.getCoinPlayer().get(ackCharactersMessage.getRecipient()) - (ackCharactersMessage.getCoinReserve() - modelView.getCoinGame());
        //System.out.println("new coin player chupd: " + newCoinPlayer);
        modelView.getCoinPlayer().replace(ackCharactersMessage.getRecipient(), newCoinPlayer);
        //System.out.println("new coin player after replacing chupd: " + modelView.getCoinPlayer().get(ackCharactersMessage.getRecipient()));
        modelView.setCoinGame(ackCharactersMessage.getCoinReserve());
        //System.out.println("new coin reserve chupd: " + modelView.getCoinGame());
    }

    public void matchIsEnded(String receivedMessageInJson){
        EndOfMatchMessage endOfMatchMessage = gsonObj.fromJson(receivedMessageInJson, EndOfMatchMessage.class);
        cli.matchEnd(endOfMatchMessage.getWinnerNickname(), endOfMatchMessage.getReason(), endOfMatchMessage.getWinner(), playerID);

        matchEnd = true;
    }

    public boolean isMessengerActive() {
        return messengerActive;
    }

    public int getJesterNumber() {
        return jesterNumber;
    }

    public void setJesterNumber(int jesterNumber) {
        this.jesterNumber = jesterNumber;
    }

    public void setLastCallFrom(String lastCallFrom) {
        this.lastCallFrom = lastCallFrom;
    }

    public boolean isMatchStarted() {
        return matchStarted;
    }

    public boolean isCharacterUsed() {
        return characterUsed;
    }

    public void setCharacterUsed(boolean characterUsed) {
        this.characterUsed = characterUsed;
    }
}

