package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Player;
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
    private ModelView modelView;
    private boolean matchEnd = false;

    //questi potrebbero essere spostati in mv
    private Tower towerColor;
    private Wizard wizard;


    /**
     * We use this flag to understand if the player has already used the assistant card.
     */
    private boolean assistantChoiceFlag = false;
    /**
     * We use this attribute to know how many students have been moved by the player.
     */
    private int numberOfChosenStudent = 0;


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
    public void startClient() throws IOException {
        clientSocket = new Socket(ip, port);

        inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputPrintClient = new PrintWriter(clientSocket.getOutputStream());

        // 1- per prima cosa il client avviato fa un login sul server
        loginFromClient();

        while (!matchEnd) {            // TODO: controllo connessione
            //System.out.println("Still connected");
            String msgFromServer = inputBufferClient.readLine();
            System.out.println("messaggio dal server: " + msgFromServer);
            analysisOfReceivedMessageServer(msgFromServer);
        }


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
    public synchronized void analysisOfReceivedMessageServer(String receivedMessageInJson) {
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
                } else {
                    sendAckFromClient();
                }
                break;

            case "join match":
                AskMatchToJoinMessage askMatchToJoinMessage = gsonObj.fromJson(receivedMessageInJson, AskMatchToJoinMessage.class);
                //System.out.println("player id" + playerID);

                int lobbyIDchosenByPlayer = cli.lobbyToChoose(askMatchToJoinMessage.getLobbiesTmp());

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
                modelView = new ModelView(playerID);
                MatchStartMessage matchStartMessage = new MatchStartMessage();
                matchStartMessage = gsonObj.fromJson(receivedMessageInJson, MatchStartMessage.class);

                System.out.println("NUMERO DI GIOCATORI TOTALI: " + matchStartMessage.getNumPlayer());
                System.out.println("PARTITA IN EXPERT MODE: " + matchStartMessage.isExpertMode());

                System.out.println("player id: " + playerID);
                System.out.println("first id: " + matchStartMessage.getFirstPlayer());

                updateStartModelView(matchStartMessage);                                            //primo update della cli, gli passo il messaggio ricevuto dal server così posso inizializzare


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
            case "end":
                EndOfMatchMessage endOfMatchMessage = gsonObj.fromJson(receivedMessageInJson, EndOfMatchMessage.class);
                cli.matchEnd(endOfMatchMessage.getWinnerNickname(), endOfMatchMessage.getReason(), endOfMatchMessage.getWinner());

                matchEnd = true;
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
                            towerColor = cli.towerChoiceNext(notAvailableTowerColors);

                            ChosenTowerColorMessage chosenTowerColorMessage = new ChosenTowerColorMessage();
                            chosenTowerColorMessage.setColor(towerColor);
                            chosenTowerColorMessage.setSender_ID(playerID);
                            sendMessage(chosenTowerColorMessage);
                            System.out.println("sent ok");

                            break;
                        } else if ((ackMessageMapped.getNextPlayer() == playerID) && (towerColor != null)) {
                            //sendAckFromClient();
                            //cli.turnWaiting();
                            wizard = cli.deckChoice();
                            ChosenDeckMessage chosenDeckMessage = new ChosenDeckMessage();
                            chosenDeckMessage.setDeck(wizard);
                            chosenDeckMessage.setSender_ID(playerID);
                            //System.out.println("DECK SCELTO NON NELL'ACK: " + chosenDeckMessage.getDeck());
                            sendMessage(chosenDeckMessage);
                            break;
                        } else if ((ackMessageMapped.getNextPlayer() != playerID) && (towerColor != null)) {
                            cli.turnWaiting();
                            break;
                        }

                    case "deck":
                        if ((ackMessageMapped.getNextPlayer() == playerID) && (wizard == null)) {
                            ArrayList<Wizard> notAvailableDecks = ackMessageMapped.getNotAvailableDecks();

                            wizard = cli.deckChoiceNext(notAvailableDecks);
                            ChosenDeckMessage chosenDeckMessage = new ChosenDeckMessage();
                            chosenDeckMessage.setDeck(wizard);
                            chosenDeckMessage.setSender_ID(playerID);
                            //System.out.println("DECK SCELTO NELL'ACK: " + chosenDeckMessage.getDeck());
                            sendMessage(chosenDeckMessage);
                            break;
                        } else if (ackMessageMapped.getNextPlayer() != playerID && (wizard != null)) {
                            cli.turnWaiting();
                            break;
                        } else if ((ackMessageMapped.getNextPlayer() == playerID) && (wizard != null)) {
                            cli.bagClick();
                            sendBagClickedByFirstClient();
                            break;
                        }
                    case "refillClouds":
                        cli.showSchoolboard(playerID, modelView);
                        cli.showCharacterCardsInTheGame(modelView);
                        cli.showStudentsOnIslands(modelView);                                           //e sempre qui possiamo mettere la stampa della la situazione iniziale delle isole (con uno studente su ciascuna tranne dove c'è MN e nell'isola opposta) quando viene messa nel messaggio di start match

                        modelView.setStudentsOnClouds(ackMessageMapped.getStudents());                  //riempiamo le nuvole

                        if (ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag == false) {
                            int assistantChosen = cli.assistantChoice(modelView.getAssistantCardsValuesPlayer());
                            modelView.setLastAssistantChosen(assistantChosen);

                            assistantChoiceFlag = true;
                            sendChosenAssistantCardMessage(assistantChosen);
                        } else if (ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == false) {                 //se non tocca a te e non l'hai ancora scelta
                            cli.turnWaiting();
                        }

                        break;

                    case "assistant":                                                                          //significa che il giocatore in questione non è il primo giocatore a scegliere l'assistente
                        if (ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag == false) {
                            ArrayList<Integer> assistantAlreadyUsedInThisRound = ackMessageMapped.getAssistantAlreadyUsedInThisRound();
                            int assistantChosen = cli.assistantChoiceNext(modelView.getAssistantCardsValuesPlayer(), assistantAlreadyUsedInThisRound);

                            modelView.setLastAssistantChosen(assistantChosen);

                            assistantChoiceFlag = true;
                            sendChosenAssistantCardMessage(assistantChosen);

                        } else if (ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == false) {
                            cli.turnWaiting();

                        } else if (ackMessageMapped.getNextPlayer() != playerID && assistantChoiceFlag == true) {
                            cli.turnWaiting();

                        } else if (ackMessageMapped.getNextPlayer() == playerID && assistantChoiceFlag == true) { //tocca a te e hai già scelto, mandi il messaggio movedstudentsfromentrance
                            int studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);                //facciamo scegliere quale studente muovere, gli passo la model view così nella cli posso avere accesso agli studenti e l'id del player.
                            int locationChosen = cli.choiceLocationToMove(modelView);                           //facciamo scegliere dove voglia muovere lo studente, isola o diningroom;
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;
                            assistantChoiceFlag = false;                                                        //qui cambia la flag il primo giocatore
                        }

                        break;

                    case "action_1_dining_room":
                        updateModelViewActionOne(ackMessageMapped);

                        if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent < 3) {            //tocca ancora  a lui e ha scelto meno di 3 studenti e il precedente l'ha mosso su diningroom
                            int studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                            int locationChosen = cli.choiceLocationToMove(modelView);
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;

                        } else if (ackMessageMapped.getNextPlayer() != playerID && numberOfChosenStudent <= 3) {
                            cli.turnWaiting();

                        } else if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent == 3) {
                            int motherNatureIslandID = 0;
                            for (int i = 0; i < 12; i++) {
                                if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                    motherNatureIslandID = i;
                                }
                            }
                            int chosenIslandID = cli.choiceMotherNatureMovement(motherNatureIslandID, modelView);

                            sendMovedMotherNature(chosenIslandID);
                        }

                        break;

                    case "action_1_island":
                        updateModelViewActionOne(ackMessageMapped);
                        if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent < 3) {          //tocca ancora  a lui e ha scelto meno di 3 studenti e il precedente l'ha mosso su isola
                            int studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);
                            int i = 0;
                            cli.showStudentsInEntrancePlayer(playerID, modelView);
                            int locationChosen = cli.choiceLocationToMove(modelView);
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;

                        } else if (ackMessageMapped.getNextPlayer() != playerID && numberOfChosenStudent <= 3) {
                            cli.turnWaiting();

                        } else if (ackMessageMapped.getNextPlayer() == playerID && numberOfChosenStudent == 3) {
                            int motherNatureIslandID = 0;
                            for (int i = 0; i < 12; i++) {
                                if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                    motherNatureIslandID = i;
                                }
                            }
                            int chosenIslandID = cli.choiceMotherNatureMovement(motherNatureIslandID, modelView);

                            sendMovedMotherNature(chosenIslandID);
                        }
                        break;

                    case "action_2_movement":
                        updateModelViewActionTwo(ackMessageMapped);
                        cli.newMotherNaturePosition(ackMessageMapped.getDestinationIsland_ID());
                        if(ackMessageMapped.getNextPlayer() == playerID) {
                            int cloudChosenID = cli.chooseCloud(modelView);
                            sendChosenCloudMessage(cloudChosenID);
                        }else if(ackMessageMapped.getNextPlayer() != playerID){
                            cli.turnWaiting();
                        }
                        break;

                    case "action_2_influence":
                        updateModelViewActionTwo(ackMessageMapped);
                        int motherNatureIslandID = 0;
                        for (int i = 0; i < 12; i++) {
                            if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                motherNatureIslandID = i;
                            }
                        }
                        if (ackMessageMapped.isMasterChanged()) {
                            if (ackMessageMapped.getNewMaster_ID() == playerID && ackMessageMapped.getPreviousMaster_ID() != playerID) {
                                cli.newMaster(modelView, playerID);
                            }else if(ackMessageMapped.getNewMaster_ID() != playerID && ackMessageMapped.getPreviousMaster_ID() == playerID){
                                cli.oldMaster(modelView, motherNatureIslandID, playerID);
                            }
                        }

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
                            cli.showUnion(modelView, ackMessageMapped.getDestinationIsland_ID(), islandUnifiedFlag);
                        }

                        break;
                    case "action_3":
                        updateModelViewActionThree(ackMessageMapped);
                        if(ackMessageMapped.getNextPlayer() == playerID && ackMessageMapped.isNextPlanningPhase()){
                            cli.newRoundBeginning();
                            cli.bagClick();
                            sendBagClickedByFirstClient();
                        }else if(ackMessageMapped.getNextPlayer() != playerID && ackMessageMapped.isNextPlanningPhase()){
                            cli.newRoundBeginning();
                            cli.turnWaiting();
                        }else if(ackMessageMapped.getNextPlayer() != playerID && !ackMessageMapped.isNextPlanningPhase()){
                            cli.turnWaiting();
                        }else if(ackMessageMapped.getNextPlayer() == playerID && !ackMessageMapped.isNextPlanningPhase()){
                            //System.out.println("ERRORE BRUTTO");
                            int studentChosen = cli.choiceOfStudentsToMove(playerID, modelView);                //facciamo scegliere quale studente muovere, gli passo la model view così nella cli posso avere accesso agli studenti e l'id del player.
                            int locationChosen = cli.choiceLocationToMove(modelView);                           //facciamo scegliere dove voglia muovere lo studente, isola o diningroom;
                            sendMovedStudentsFromEntrance(studentChosen, locationChosen);
                            numberOfChosenStudent++;
                            assistantChoiceFlag = false;
                        }
                        break;
                }
                break;

            case "nack":
                NackMessage nackMessageMapped = gsonObj.fromJson(receivedMessageInJson, NackMessage.class);
                switch (nackMessageMapped.getSubObject()) {
                    case "invalid_mother_nature_movement":
                        cli.invalidMotherNatureMovement();

                        int motherNatureIslandID = 0;
                        for (int i = 0; i < 12; i++) {
                            if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                                motherNatureIslandID = i;
                            }
                        }
                        int chosenIslandID = cli.choiceMotherNatureMovement(motherNatureIslandID, modelView);

                        sendMovedMotherNature(chosenIslandID);

                        break;
                    case "invalid_cloud":
                        cli.invalidCloudSelection(modelView);
                        break;
                }
                break;

            case "no lobby available":
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
     * This method is used to update the modelView after receiving the matchStartMessage.
     *
     * @param matchStartMessage is the matchStartMatchMessage received.
     */
    public void updateStartModelView(MatchStartMessage matchStartMessage) {
        modelView.setNumberOfPlayersGame(matchStartMessage.getNumPlayer());                                      //setto il numero di giocatori totali della partita
        modelView.setExpertModeGame(matchStartMessage.isExpertMode());                                           //setto exepert mode  a true se la partita è in expertmode
        if (modelView.isExpertModeGame() == true) {                                                                //se la partita è in expert mode, setto il numero di coin della partita a 20
            modelView.getCoinPlayer().put(playerID, 1);                                                          //se la partita è in expert mode, setto il numero di coin di ciascun giocatore a 1
            modelView.setCoinGame(20);
            for (String s : matchStartMessage.getCharacters()) {
                modelView.getCharacterCardsInTheGame().add(s);
            }
        }
        //System.out.println( "NUMERO DI COIN NELLA PARTITA: " + modelView.getCoinGame());                          //stampe di controllo
        //System.out.println("partita in expert mode??? " + modelView.isExpertModeGame());
        modelView.getIslandGame().get(matchStartMessage.getMotherNaturePosition()).setMotherNaturePresence(true);   //setto madre natura sull'isola corretta passata nel messaggio di match start

        int motherNaturePosition = matchStartMessage.getMotherNaturePosition();                 //metto gli studenti iniziali (uno per isola tranne dove c'è MN e isola opposta) sulle giuste isole
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
        if (ackMessageMapped.getRecipient() == playerID) {
            if (ackMessageMapped.getSubObject().equals("action_1_dining_room")) {
                modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().set(ackMessageMapped.getStudentMoved_ID(), null);                              //setto a null il corrispondente valore nell'entrance
                int numberOfStudentOfType = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved());
                modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(), numberOfStudentOfType + 1);            //updato la diningroom del giocatore
                modelView.getSchoolBoardPlayers().get(playerID).getProfessorTablePlayer().getOccupiedSeatsPlayer().replace(ackMessageMapped.getTypeOfStudentMoved(), ackMessageMapped.isProfessorTaken());
                if (modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(ackMessageMapped.getTypeOfStudentMoved()) % 3 == 0) {
                    int newPlayerCoin = modelView.getCoinPlayer().get(playerID) + 1;
                    modelView.getCoinPlayer().replace(playerID, newPlayerCoin);
                    modelView.setCoinGame(modelView.getCoinGame() - 1);
                }

            } else if (ackMessageMapped.getSubObject().equals("action_1_island")) {
                modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().set(ackMessageMapped.getStudentMoved_ID(), null);
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
        if (ackMessageMapped.getRecipient() == playerID) {
            if (ackMessageMapped.getSubObject().equals("action_2_movement")) {
                modelView.getAssistantCardsValuesPlayer().remove(modelView.getLastAssistantChosen());   //rimuovo assistente
                for (int i = 0; i < 12; i++) {
                    if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {                    //la posizione di MN è cambiata, setto a false il booleano corrispondente nell'isola dove c'era
                        modelView.getIslandGame().get(i).setMotherNaturePresence(false);
                    } else if (i == ackMessageMapped.getDestinationIsland_ID()) {                       //setto a true l'attributo nella nuova isola dove si trova MN dopo lo spostamento
                        modelView.getIslandGame().get(i).setMotherNaturePresence(true);
                    }
                }
                if (modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                    modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).removeNoEntryTile();
                }
                /*if (ackMessageMapped.isEndOfMatch()) {
                    matchEnd = true;

                } */

            } else if (ackMessageMapped.getSubObject().equals("action_2_influence")) { //cambiamenti relativi al calcolo dell'influenza
                if (ackMessageMapped.isMasterChanged()) {
                    int motherNatureIsland = 0;                                                                  //id dell'isola dove c'è madre natura.
                    for (int i = 0; i < 12; i++) {
                        if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                            motherNatureIsland = i;
                        }
                    }
                    if (ackMessageMapped.getNewMaster_ID() == playerID && ackMessageMapped.getPreviousMaster_ID() != playerID) {
                        //update islands
                        modelView.getIslandGame().get(motherNatureIsland).setTowerColor(towerColor);                                //o stampiamo questo nuovo colore o gli altri non lo sanno
                        //update player's board
                        int numberTowerMotherIsland = modelView.getIslandGame().get(motherNatureIsland).getNumberOfTower();         //prendiamo quante torri sono sull'isola dove arriva MN
                        if (numberTowerMotherIsland == 0) {
                            numberTowerMotherIsland++;                                                                             //diventa almeno una che viene tolta dalla schoolboard
                        }
                        int numberCurrentTowerSchoolBoard = modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer();
                        modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().setCurrentNumberOfTowersPlayer(numberCurrentTowerSchoolBoard - numberTowerMotherIsland);       //viene aggiornata la plancia con il nuovo numero corretto di torri


                    } else if (ackMessageMapped.getNewMaster_ID() != playerID && ackMessageMapped.getPreviousMaster_ID() == playerID) {  //non sono più il master ma lo ero
                        //update islands
                        ////GLI ALTRI GIOCATORI NON SANNO IL NUOVO COLORE SULL'ISOLA

                        //update player's board
                        int numberTowerMotherIsland = modelView.getIslandGame().get(motherNatureIsland).getNumberOfTower();         //prendiamo quante torri sono sull'isola dove arriva MN

                        int numberCurrentTowerSchoolBoard = modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer();
                        modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().setCurrentNumberOfTowersPlayer(numberCurrentTowerSchoolBoard + numberTowerMotherIsland);       //viene aggiornata la plancia con il nuovo numero corretto di torri

                    } else if (ackMessageMapped.getNewMaster_ID() != playerID && ackMessageMapped.getPreviousMaster_ID() != playerID) {   //non sono il master nuovo e nemmeno vecchio
                        //bisogna aggiornare la situazione delle isola e delle relative torri.
                    }
                    /*if (ackMessageMapped.isEndOfMatch()) {
                        matchEnd = true;
                    }*/

                    //update islands


                }
            } else if (ackMessageMapped.getSubObject().equals("action_2_union")) {
                if (ackMessageMapped.getIslandsUnified().equals("previous")) {               //copio le creature dall'isola previous a quella nuova dove arriva madre natura
                    for (Creature c : Creature.values()) {
                        int numberPrevious = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() - 1).getStudentsPopulation().get(c).intValue();
                        int numberCurrent = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getStudentsPopulation().get(c).intValue();            //cambiamo il numero di studenti sull'isola rimasta
                        modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getStudentsPopulation().replace(c, numberPrevious + numberCurrent);
                    }
                    int numberTowerPrevious = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() - 1).getNumberOfTower();
                    int numberTowerCurrent = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNumberOfTower();                  //cambiamo il numero di torri sull'isola

                    if (numberTowerPrevious == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                        numberTowerPrevious++;
                    }
                    if (numberTowerCurrent == 0) {
                        numberTowerCurrent++;
                    }
                    if (modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                        modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).removeNoEntryTile();
                    }
                    modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).setNumberOfTower(numberTowerCurrent + numberTowerPrevious);
                }else if(ackMessageMapped.getIslandsUnified().equals("next")){
                    for (Creature c : Creature.values()) {
                        int numberNext = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() + 1).getStudentsPopulation().get(c).intValue();
                        int numberCurrent = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getStudentsPopulation().get(c).intValue();            //cambiamo il numero di studenti sull'isola rimasta
                        modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getStudentsPopulation().replace(c, numberNext + numberCurrent);
                    }
                    int numberTowerNext = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() + 1).getNumberOfTower();
                    int numberTowerCurrent = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNumberOfTower();                  //cambiamo il numero di torri sull'isola

                    if (numberTowerNext == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                        numberTowerNext++;
                    }
                    if (numberTowerCurrent == 0) {
                        numberTowerCurrent++;
                    }
                    if (modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                        modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).removeNoEntryTile();
                    }
                    modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).setNumberOfTower(numberTowerCurrent + numberTowerNext);

                }else if(ackMessageMapped.getIslandsUnified().equals("both")){
                    for (Creature c : Creature.values()) {
                        int numberPrevious = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() - 1).getStudentsPopulation().get(c).intValue();
                        int numberNext = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() + 1).getStudentsPopulation().get(c).intValue();
                        int numberCurrent = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getStudentsPopulation().get(c).intValue();            //cambiamo il numero di studenti sull'isola rimasta
                        modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getStudentsPopulation().replace(c, numberPrevious + numberCurrent + numberNext);
                    }
                    int numberTowerPrevious = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() - 1).getNumberOfTower();
                    int numberTowerNext = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID() + 1).getNumberOfTower();
                    int numberTowerCurrent = modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNumberOfTower();                  //cambiamo il numero di torri sull'isola

                    if (numberTowerPrevious == 0) {                                                                                                          //caso in cui siano 0 perchè la logica stiamo facendo noi e arrivano tutti insieme i 3 messsaggi di action 2
                        numberTowerPrevious++;
                    }
                    if(numberTowerNext == 0){
                        numberTowerNext++;
                    }
                    if (numberTowerCurrent == 0) {
                        numberTowerCurrent++;
                    }
                    if (modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).getNoEntryTiles() > 0) {           //rimozione di no entry tile
                        modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).removeNoEntryTile();
                    }
                    modelView.getIslandGame().get(ackMessageMapped.getDestinationIsland_ID()).setNumberOfTower(numberTowerCurrent + numberTowerPrevious + numberTowerNext);

                }
                /*if (ackMessageMapped.isEndOfMatch()) {
                    matchEnd = true;
                } */
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
                    modelView.getStudentsOnClouds().set(i, Creature.FROG);
                }

            }else if(ackMessageMapped.getCloudChosen_ID() == 1){
                for(int i = 3; i< 6; i++){
                    modelView.getStudentsOnClouds().set(i, Creature.FROG);
                }

            }
        }else if(modelView.getNumberOfPlayersGame() == 3){
            if(ackMessageMapped.getCloudChosen_ID() == 0){
                for(int i = 0; i< 4; i++){
                    modelView.getStudentsOnClouds().set(i, Creature.FROG);
                }

            }else if(ackMessageMapped.getCloudChosen_ID() == 1){
                for(int i = 4; i< 8; i++){
                    modelView.getStudentsOnClouds().set(i, Creature.FROG);
                }

            }else if(ackMessageMapped.getCloudChosen_ID() == 2){
                for(int i = 8; i< 12; i++){
                    modelView.getStudentsOnClouds().set(i, Creature.FROG);
                }
            }
        }
        if(ackMessageMapped.getRecipient() == playerID) {
            ArrayList<Creature> creatureInEntranceAfterClouds = ackMessageMapped.getStudents();                                                 //update dell'entrance dopo scelta nuvola
            modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().setStudentsInTheEntrancePlayer(creatureInEntranceAfterClouds);
        }

    }
}

