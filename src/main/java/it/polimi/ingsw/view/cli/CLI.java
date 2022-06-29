package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.Client.NetworkHandler;

import java.io.IOException;
import java.util.*;

/**
 * This class represents the CLI: it manages the game through the command line interface.
 */
public class CLI {
    private NetworkHandler networkHandler;
    Scanner scannerCLI = new Scanner(System.in);


    /**
     * Cli constructor creates a new instance of the cli and sets the connection between the client and the server through the startClient method.
     * @param ip is the server ip
     * @param port is the server port
     */
    public CLI(String ip, int port) throws IOException, InterruptedException {
        this.networkHandler = new NetworkHandler(ip, port, this);
        this.networkHandler.startClient();
    }

    /**
     * Main method
     * @param args are the main args.
     */
    public static void main(String[] args) {
        System.out.println(" _______   ________  ___  ________  ________   _________    ___    ___ ________      \n" +
                "|\\  ___ \\ |\\   __  \\|\\  \\|\\   __  \\|\\   ___  \\|\\___   ___\\ |\\  \\  /  /|\\   ____\\     \n" +
                "\\ \\   __/|\\ \\  \\|\\  \\ \\  \\ \\  \\|\\  \\ \\  \\\\ \\  \\|___ \\  \\_| \\ \\  \\/  / | \\  \\___|_    \n" +
                " \\ \\  \\_|/_\\ \\   _  _\\ \\  \\ \\   __  \\ \\  \\\\ \\  \\   \\ \\  \\   \\ \\    / / \\ \\_____  \\   \n" +
                "  \\ \\  \\_|\\ \\ \\  \\\\  \\\\ \\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\   \\ \\  \\   \\/  /  /   \\|____|\\  \\  \n" +
                "   \\ \\_______\\ \\__\\\\ _\\\\ \\__\\ \\__\\ \\__\\ \\__\\\\ \\__\\   \\ \\__\\__/  / /       ____\\_\\  \\ \n" +
                "    \\|_______|\\|__|\\|__|\\|__|\\|__|\\|__|\\|__| \\|__|    \\|__|\\___/ /       |\\_________\\\n" +
                "                                                          \\|___|/        \\|_________|\n" +
                "                                                                                     \n" +
                "                                                                                     ");

        try {
            /*
            System.out.println("Insert server ip address: ");
            String ip = new Scanner(System.in).next();
            System.out.println("Insert server port: ");
            int port = new Scanner(System.in).nextInt();
            CLI cli = new CLI(ip, port);
             */
            CLI cli = new CLI("localhost", 4444);

        } catch (InputMismatchException e){
            System.out.println("Integer requested for the server port, restart the application. ");
            System.exit(0);
        } catch (IOException e) {           //TODO no longer available, disconnected, you're offline
            System.out.println("Server no longer available :(  " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();            //TODO is alive socket
        }


    }

    /**
     * This method is used to ask the nickname the player wants.
     * @return the nickname chosen.
     */
    public String loginNickname(){
        println("Insert nickname: ");
        String nickNamePlayer = scannerCLI.nextLine();
        println("ok");

        return nickNamePlayer;
    }

    /**
     * This method is used to ask if the player wants to create a new match or not.
     * @return true if he wants to create a new match, false otherwise.
     */
    public boolean newMatchBoolean(){
        println("do you want to create a new match? (y/n) ");
        String newMatchStr = scannerCLI.nextLine();

        while ( !( (newMatchStr.equals("y")) || (newMatchStr.equals("n")))){
            println("please insert 'y' if you want to create a new match or 'n' if you don't: ");
            newMatchStr = scannerCLI.nextLine();
        }
        println("ok");

        return newMatchStr.equals("y");
    }

    /**
     * this method is used to notify the user the login has been successful and that he has created a new match.
     */
    public void loginSuccess(){
        println("Login success! ");
    }

    /**
     * This method is used to ask the number of players wanted in the match by the player creating a new game.
     * @return the number of players chosen.
     */
    public int numberOfPlayer(){
        println("Please insert the number of the player you want in the lobby: ");

        int numberOfPlayerInTheLobby = scannerCLI.nextInt();
        printlnInt(numberOfPlayerInTheLobby);
        while( (numberOfPlayerInTheLobby <= 1) || (numberOfPlayerInTheLobby >= 4) ){
            println("Please, insert a valid number of players");
            numberOfPlayerInTheLobby = scannerCLI.nextInt();
        }

        return numberOfPlayerInTheLobby;
    }

    /**
     * This method is used to ask whether the player wants to play with the expert mode enabled or not.
     * @return true if the player wants to play with the expert mode, false otherwise.
     */
    public boolean expertModeSelection(){
        println("Do you want to play in expert mode? (y/n)");
        String expertMode = scannerCLI.nextLine();

        while(!(expertMode.equals("y") || expertMode.equals("n"))){
            println("Please, insert y or n: ");
            expertMode = scannerCLI.nextLine();
        }

        return expertMode.equals("y");
    }

    /**
     * This method is used to ask the player which lobby he wants to join among the ones already existing.
     * @param arrayLobby is the arraylist of boolean representing the existing lobby: true means the lobby is full, false means it's not full.
     * @param arrayExpert list of boolean values specifying if the match with ID equals to the index of the array is played in
     *                    expert mode or note
     * @param arrayNumPlayer list of integers specifying what is the number of players required to start the match
     * @param arrayEnd list of boolean values specifying if the match with ID equals to the index of the array is already ended or not
     * @return ID of the lobby chosen by the player.
     */


    public int lobbyToChoose(ArrayList<Boolean> arrayLobby, ArrayList<Boolean> arrayExpert, ArrayList<Integer> arrayNumPlayer, ArrayList<Boolean> arrayEnd){
        println("Choose a Lobby: \n");

        printLobbies(arrayLobby, arrayExpert, arrayNumPlayer, arrayEnd);

        //SCANNER DELLE LOBBY
        int lobbyIDChosenByPlayer = scannerCLI.nextInt();

        boolean rightLobbyChoice = false;

        while(!rightLobbyChoice) {
            if( lobbyIDChosenByPlayer < arrayLobby.size() && lobbyIDChosenByPlayer >= 0) {
                if (!arrayLobby.get(lobbyIDChosenByPlayer)) {

                    if (arrayEnd.get(lobbyIDChosenByPlayer)) {
                        println("Lobby " + lobbyIDChosenByPlayer + ": the match in this lobby is ended. Not available to join, select another one from the list below. ");
                        println(" ");

                        printLobbies(arrayLobby, arrayExpert, arrayNumPlayer, arrayEnd);

                        lobbyIDChosenByPlayer = scannerCLI.nextInt();

                    } else {
                        println("You can't join this lobby, match selected already started. Select another one from the list below. ");
                        println(" ");

                        printLobbies(arrayLobby, arrayExpert, arrayNumPlayer, arrayEnd);

                        lobbyIDChosenByPlayer = scannerCLI.nextInt();
                    }
                } else {
                    rightLobbyChoice = true;
                }

            }else {
                println("Lobby not existing, insert a right number from the list of Lobbies: ");
                println(" ");

                printLobbies(arrayLobby, arrayExpert, arrayNumPlayer, arrayEnd);

                lobbyIDChosenByPlayer = scannerCLI.nextInt();
            }
        }

        return lobbyIDChosenByPlayer;
    }

    /**
     * This method is used to print the lobbies in the game, their expert mode option, their maximum number of players and their availability.
     * @param arrayLobby contains booleans which represent if a lobby is available to join or not (f.e. if the match is already started -> false).
     * @param arrayExpert contains booleans which represents the expert mode option (index is the id of the lobby).
     * @param arrayNumPlayer contains the maximum number of players in the lobbies (index is the id of the lobby).
     * @param arrayEnd contains a booleans which represent if the game is ended (index is the id of the lobby).
     */
    public void printLobbies(ArrayList<Boolean> arrayLobby, ArrayList<Boolean> arrayExpert, ArrayList<Integer> arrayNumPlayer, ArrayList<Boolean> arrayEnd){
        //STAMPA DELLE LOBBIES
        println("ERIANTYS LOBBIES: ");
        for (int i = 0; i < arrayLobby.size(); i++) {
            if(!arrayEnd.get(i)) {
                if (!arrayLobby.get(i)) {
                    print("Lobby " + i + ": full. :(  [There are " + arrayNumPlayer.get(i) + " players in this Lobby] ");
                    if (arrayExpert.get(i)) {
                        print(" - This match is in Expert Mode - ");
                    } else {
                        print(" - This match is NOT in Expert Mode - ");
                    }
                    println(" ");

                } else {
                    print("Lobby " + i + ": available! :)  [Maximum number of players for this game is " + arrayNumPlayer.get(i) + "] ");
                    if (arrayExpert.get(i)) {
                        print(" - This match is in Expert Mode - ");
                    } else {
                        print(" - This match is NOT in Expert Mode - ");
                    }
                    println(" ");
                }
            }else {
                print("Lobby " + i + ": the match in this lobby is ended. Not available to join.  ");
                println(" ");
            }
        }
    }

    /**
     * This method is used to notify the player the nickname he had chosen is not available.
     */
    public void nicknameNotAvailable (){
        println("Nickname not available, insert a new nickname: ");
    }

    /**
     * This method is used to notify the player he can't join a lobby, and he needs to create a new one.
     */
    public void lobbyNotAvailable(){
        println("No lobby available, creating a new one...");
    }

    /**
     * This method is used to notify the player he can't join the chose lobby because is full.
     * @param explanation is the explanation of the nack message.
     */
    public void lobbyChosenNotAvailable(String explanation){
        println(explanation);
        println("Try to reconnect to the server!");
    }

    /**
     * This method is used to notify the player that he received a wrong message.
     */
    public void errorObject () {
        println("Error with the object of the message. ");
    }

    /**
     * This method is used to notify the player that the match is waiting for other players to start.
     */
    public void ackWaiting () { println("Waiting for match to start...  "); }

    /**
     * This method is used to notify the player that the game has started.
     */
    public void startAlert () {
        println("\n - - - GAME IS STARTED ! - - - \n");
    }

    /**
     * This method is used to notify the player that it's not his turn, so he has to wait.
     * @param nowPlayingPlayerId ID of the player that is currently playing his turn
     */
    public void turnWaiting (int nowPlayingPlayerId) {
        if(nowPlayingPlayerId == -1){
            println("He is now playing... Wait for your turn!");
            println(" ");
        }else {
            println(" ");
            println("Player " + nowPlayingPlayerId + " is now playing... Wait for your turn!");
            println(" ");
        }
    }

    /**
     * This method is used to notify the player that it's not his turn when choosing towers color, so he has to wait.
     * @param nowPlayingPlayerId is the int ID of the player who is currently choosing.
     */
    public void turnWaitingTowers (int nowPlayingPlayerId) {
        println(" ");
        println("Player " + nowPlayingPlayerId + " is now choosing the Color for his Towers... Wait for your turn! ");
        println(" ");
    }

    /**
     * This method is used to notify the player that it's not his turn when choosing wizard for the decks, so he has to wait.
     * @param nowPlayingPlayerId is the int ID of the player who is currently choosing.
     */
    public void turnWaitingDecks (int nowPlayingPlayerId) {
        println(" ");
        println("Player " + nowPlayingPlayerId + " is now choosing the Wizard for his Deck... Wait for your turn! ");
        println(" ");
    }

    /**
     * This method is used to notify the player that it's not his turn when choosing the assistant, so he has to wait.
     * @param nowPlayingPlayerId is the int ID of the player who is currently choosing.
     */
    public void turnWaitingAssistant (int nowPlayingPlayerId) {
        println(" ");
        println("Player " + nowPlayingPlayerId + " is now choosing the Assistant card for this round... Wait for your turn! ");
        println(" ");
    }

    /**
     * This method is used to notify the player that it's not his turn and another player is choosing the cloud he wants to take the students from, so he has to wait.
     * @param nowPlayingPlayerId is the int ID of the player who is currently choosing.
     */
    public void turnWaitingClouds (int nowPlayingPlayerId) {
        println(" ");
        println("Player " + nowPlayingPlayerId + " is now choosing the Cloud he wants to take the students from... Wait for your turn! ");
        println(" ");
    }

    /**
     * This method is used when another player has used a character, so the client is notified and has to wait.
     * @param characterUsed name of the character used by this player or another one
     * @param playerID ID of the player that used the character
     */
    public void characterUsed (String characterUsed, int playerID) {
        println("Player " + playerID + " has used the " + characterUsed + " character card!");
    }

    /**
     * This method is used to notify the player that it's his turn, so he has to act.
     */
    public void isYourTurn() {
        println("It is your Turn! Make your choice: ");
    }

    /**
     * This method is used to ask the first player which color he wants his towers to be.
     * @param modelView reference to the modelView
     * @return the color chosen in Tower type.
     */
    public  Tower towerChoice (ModelView modelView) {
        /*
        println("                                                |>>>\n" +
                "                                                |\n" +
                "                                            _  _|_  _\n" +
                "                                           |;|_|;|_|;|\n" +
                " MAKE YOUR CHOICE!                       \\\\.    .  /\n" +
                "                                          \\\\:  .  /\n" +
                "                                             ||:   |\n" +
                "                                             ||:.  |\n" +
                "                                             ||:  .|\n" +
                "                                             ||:   |\n" +
                "                                             ||: , |\n" +
                "     ____--`~    '--~~__            __ ----~    ~`---,              ___\n" +
                "-~--~                   ~---__ ,--~'                  ~~----_____-~'   `~----~~\n");
         */

        String strTowerColorChosen = null;
        println("Choose the COLOR of your tower: ");
        if(modelView.getNumberOfPlayersGame() == 3) {
            println("BLACK , WHITE or GREY");
            strTowerColorChosen = scannerCLI.next();

            while (!(strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") || strTowerColorChosen.equals("GREY"))) {
                println("Please, insert the right color: ");
                strTowerColorChosen = scannerCLI.next();
            }

        }else if(modelView.getNumberOfPlayersGame() == 2){
            println("BLACK or WHITE");
            strTowerColorChosen = scannerCLI.next();

            while (!(strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE"))) {
                println("Please, insert the right color: ");
                strTowerColorChosen = scannerCLI.next();
            }
        }

        return Tower.valueOf(strTowerColorChosen);
    }

    /**
     * This method is used to ask the following players which color they want their towers to be.
     * @param notAvailableTowerColors is the list of  colors that have already been chosen.
     * @param modelView reference to the modelView
     * @return the color chosen.
     */
    public Tower towerChoiceNext (ArrayList<Tower> notAvailableTowerColors, ModelView modelView) {

        ArrayList<String> availableTowerColors = new ArrayList<>();

        if(modelView.getNumberOfPlayersGame() == 3) {
            if(notAvailableTowerColors.size() == 2) {
                switch (String.valueOf(notAvailableTowerColors.get(0))) {
                    case "BLACK":
                        if (String.valueOf(notAvailableTowerColors.get(1)).equals("WHITE")) {
                            availableTowerColors.add("GREY");
                        } else {
                            availableTowerColors.add("WHITE");
                        }
                        break;
                    case "WHITE":
                        if (String.valueOf(notAvailableTowerColors.get(1)).equals("BLACK")) {
                            availableTowerColors.add("GREY");
                        } else {
                            availableTowerColors.add("BLACK");
                        }
                        break;
                    case "GREY":
                        if (String.valueOf(notAvailableTowerColors.get(1)).equals("WHITE")) {
                            availableTowerColors.add("BLACK");
                        } else {
                            availableTowerColors.add("WHITE");
                        }
                        break;
                }
            }else {
                switch (String.valueOf(notAvailableTowerColors.get(0))) {
                    case "BLACK":
                        availableTowerColors.add("GREY");
                        availableTowerColors.add("WHITE");
                        break;
                    case "WHITE":
                        availableTowerColors.add("GREY");
                        availableTowerColors.add("BLACK");
                        break;
                    case "GREY":
                        availableTowerColors.add("BLACK");
                        availableTowerColors.add("WHITE");
                        break;
                }
            }
        }else if(modelView.getNumberOfPlayersGame() == 2){
            if(String.valueOf(notAvailableTowerColors.get(0)).equals("BLACK")){
                availableTowerColors.add("WHITE");
            }else if(String.valueOf(notAvailableTowerColors.get(0)).equals("WHITE")){
                availableTowerColors.add("BLACK");
            }
        }

        println("Choose your tower color:");
        print("Available tower colors: ");
        for(int i = 0; i < availableTowerColors.size(); i++){
            print(availableTowerColors.get(i));
            if(i+1 != availableTowerColors.size()){
                print(", ");
            }
        }
        println(" ");
        print("(Already chosen: ");
        for (int i = 0; i < notAvailableTowerColors.size(); i++){
            print(String.valueOf(notAvailableTowerColors.get(i)));
            if(i+1 != notAvailableTowerColors.size()){
                print(", ");
            }
        }
        println(").");
        
        String strTowerColorChosen = scannerCLI.next();

        boolean rightTowerChoice = false;

        if(modelView.getNumberOfPlayersGame() == 3) {
            while (!rightTowerChoice) {
                if (!(strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") || strTowerColorChosen.equals("GREY"))) {
                    println("Please insert a valid color tower: ");
                    strTowerColorChosen = scannerCLI.next();
                } else if (notAvailableTowerColors.contains(Tower.valueOf(strTowerColorChosen))) {
                    println("This color has already been selected, please choose another one: ");
                    strTowerColorChosen = scannerCLI.next();
                } else {
                    rightTowerChoice = true;
                }
            }
        }else if(modelView.getNumberOfPlayersGame() == 2) {
            while (!rightTowerChoice) {
                if (!(strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") )) {
                    println("Please insert a valid color tower: ");
                    strTowerColorChosen = scannerCLI.next();
                } else if (notAvailableTowerColors.contains(Tower.valueOf(strTowerColorChosen))) {
                    println("This color has already been selected, please choose another one: ");
                    strTowerColorChosen = scannerCLI.next();
                } else {
                    rightTowerChoice = true;
                }
            }
        }

        return Tower.valueOf(strTowerColorChosen);
    }


    /**
     * This method is used to ask the player which deck he wants to play with.
     * @return the deck chosen in Wizard type.
     */
    public Wizard deckChoice(){
        println("Please choose your deck among the following: ");
        println("FORESTWIZARD, DESERTWIZARD, CLOUDWITCH, LIGHTNINGWIZARD");

        String strDeckChosen = scannerCLI.next();

        while(!( strDeckChosen.equals("FORESTWIZARD") || strDeckChosen.equals("DESERTWIZARD") || strDeckChosen.equals("CLOUDWITCH") || strDeckChosen.equals("LIGHTNINGWIZARD")) ){
            println("Please insert a correct deck: ");
            strDeckChosen = scannerCLI.next();
        }

        return Wizard.valueOf(strDeckChosen);
    }

    /**
     * This method is used to ask the following players which deck they want to use.
     * @param notAvailableDecks is the list of decks that have been already chosen.
     * @return the deck chosen.
     */
    public Wizard deckChoiceNext(ArrayList<Wizard> notAvailableDecks) {

        ArrayList<String> availableDeckWizard = new ArrayList<>();
        availableDeckWizard.add("FORESTWIZARD");
        availableDeckWizard.add("DESERTWIZARD");
        availableDeckWizard.add("CLOUDWITCH");
        availableDeckWizard.add("LIGHTNINGWIZARD");

        for (Wizard notAvailableDeck : notAvailableDecks) {
            availableDeckWizard.remove(String.valueOf(notAvailableDeck));
        }

        println("Please choose your deck among the following: ");
        for(int i = 0; i < availableDeckWizard.size(); i++){
            print(availableDeckWizard.get(i));
            if(i+1 != availableDeckWizard.size()){
                print(", ");
            }
        }
        println(" ");
        print("(Already chosen: ");
        for (int i = 0; i < notAvailableDecks.size(); i++){
            print(String.valueOf(notAvailableDecks.get(i)));
            if(i+1 != notAvailableDecks.size()){
                print(", ");
            }
        }
        println(").");

        String strDeckChosen = scannerCLI.nextLine();

        boolean rightDeckChoice = false;

        while(!rightDeckChoice){
            if(!(strDeckChosen.equals("FORESTWIZARD") || strDeckChosen.equals("DESERTWIZARD") || strDeckChosen.equals("CLOUDWITCH") || strDeckChosen.equals("LIGHTNINGWIZARD"))){
                println("Please, insert a correct deck: ");
                strDeckChosen = scannerCLI.next();
            } else if(notAvailableDecks.contains(Wizard.valueOf(strDeckChosen))) {
                println("This deck has already been chosen, please select another one: ");
                strDeckChosen = scannerCLI.next();
            }else{
                rightDeckChoice = true;
            }
        }

        return Wizard.valueOf(strDeckChosen);
    }

    /**
     * This method is used to notify the player he has clicked the bag to refill the clouds.
     */
    public void bagClick(){
        println("You drew the students from the bag");
    }

    /**
     * This method is used to ask the first player which assistant card he wants to play.
     * @param availableAssistantCard is the list of assistant cards the player has not used yet in the game.
     * @return the assistant card chosen.
     */
    public int assistantChoice(HashMap <Integer, Integer> availableAssistantCard){
        boolean rightAssistantChosen = false;
        String request;
        int assistantChosen = -1;
        println("Which assistant do you want to play?");
        int counter = 0;
        for(Integer i : availableAssistantCard.keySet()){
            printInt(i);
            if(counter +1 != availableAssistantCard.size()){
                print(", ");
            }
            counter++;
        }
        println(" ");

        request = scannerCLI.next();

        while(!rightAssistantChosen){
            try {
                assistantChosen = Integer.parseInt(request);
                if(!(availableAssistantCard.containsKey(assistantChosen))){
                    println("You can't use this assistant, it has already been used in the previous rounds. Please select another one: ");
                    request = scannerCLI.next();
                }else {
                    rightAssistantChosen = true;
                }
            }catch (NumberFormatException e){
                println("Please insert a valid assistant id: (1-10)");
                request = scannerCLI.next();
            }
        }

        return assistantChosen;
    }

    /**
     * This method is used to ask the following players which assistant card they want to use.
     * @param availableAssistantCard is the list of assistant cards the player has not used yet in the game.
     * @param assistantCardsAlreadyUsedThisRound is the list of the assistant cards already used in this round which can't be used by the other players.
     * @return the assistant card chosen.
     */
    public int assistantChoiceNext(HashMap <Integer, Integer> availableAssistantCard, ArrayList<Integer> assistantCardsAlreadyUsedThisRound){
        boolean rightAssistantChosen = false;
        int assistantChosen = -1;
        String request;
        int counter = 0;
        boolean checkNoAvailability;

        ArrayList<Integer> availableAssistantCardArray = new ArrayList<>();

        println("Which assistant do you want to play from your deck?");
        for(Integer i : availableAssistantCard.keySet()){
            printInt(i);
            availableAssistantCardArray.add(i);
            if(counter +1 != availableAssistantCard.size()){
                print(", ");
            }
            counter++;
        }

        println(" ");
        print("(Assistants chosen by other players: ");
        for(int i = 0; i< assistantCardsAlreadyUsedThisRound.size(); i++){
            printInt(assistantCardsAlreadyUsedThisRound.get(i));
            if(i+1 != assistantCardsAlreadyUsedThisRound.size()){
                print(", ");
            }
        }
        println(") ");

        //check equality:
        checkNoAvailability = availableAssistantCardArray.equals(assistantCardsAlreadyUsedThisRound);

        request = scannerCLI.next();

        while(!rightAssistantChosen) {
            try {
                assistantChosen = Integer.parseInt(request);
                if (assistantCardsAlreadyUsedThisRound.contains(assistantChosen)) {
                    if(checkNoAvailability){
                        println("You can use this assistant even though it has already been played in this round by another player (You only have cards already played by other players).");
                        rightAssistantChosen = true;
                    }else {
                        println("You can't use this assistant, it has already been used in this round by another player. Please select another one: ");
                        request = scannerCLI.next();
                    }
                }else if (!(availableAssistantCard.containsKey(assistantChosen))) {
                    println("You can't use this assistant, you have already used it in the previous rounds. Please select another one: ");
                    request = scannerCLI.next();
                }else {
                    rightAssistantChosen = true;
                }
            } catch (NumberFormatException e) {
                println("Please insert a valid assistant id: (1-10)");
                request = scannerCLI.next();
            }
        }
        return  assistantChosen;
    }

    /**
     * This method is used to show the three characters cards available in the game.
     * It controls if the game is in Expert Mode before showing them.
     * @param modelView is the reference to the modelView.
     */
    public void showCharacterCardsInTheGame(ModelView modelView){
        if(modelView.isExpertModeGame()){
            println("The character cards in this game are: ");
            int i = 0;
            for(String s : modelView.getCharacterCardsInTheGame()){
                print(s.toUpperCase());
                if(i + 1 < modelView.getCharacterCardsInTheGame().size()){
                    print(", ");
                }else{
                    print(". ");
                }
            }
            println(" ");
        }
        println(" ");
    }

    /**
     * This method is used to show the player's SchoolBoard.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showSchoolBoard(int playerID, ModelView modelView){
        println("Your school board currently is: ");
        println(" ");
        showStudentsInEntrancePlayer(playerID, modelView);
        showStudentsInDiningRoomPlayer(playerID, modelView);
        showProfessorTablePlayer(playerID, modelView);
        showNumberOfTowers(playerID, modelView);
    }

    /**
     * This method is used to show to the player his students in the Entrance of his board.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsInEntrancePlayer(int playerID, ModelView modelView){
        println("Your students in the entrance are: ");
        int i = 0;
        for(Creature c : modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer()){
            if(c == null){
                println(i + ": --- ");
                i++;
            }else {
                println(i + ": " + c);
                i++;
            }
        }
        println(" ");
    }
    /**
     * This method is used to show to the player the students in the Entrance of all players.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsInEntranceOfAllPlayer(int playerID, ModelView modelView){
        println("The students in the entrance of the player " + playerID +" are: ");
        int i = 0;
        for(Creature c : modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer()){
            if(c == null){
                println(i + ": --- ");
                i++;
            }else {
                println(i + ": " + c);
                i++;
            }
        }
        println(" ");
    }

    /**
     * This method is used to show the number of towers of all players.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showTowersPlayer(int playerID, ModelView modelView){
        print("This is the number of towers of player " + playerID +": " + modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer());
        println(" ");
    }
    /**
     * This method is used to show to the player his students in the Dining Room of his board.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsInDiningRoomPlayer(int playerID, ModelView modelView){
        println("This is the dining room of the player " + playerID);
        for(Creature c : Creature.values() ) {
            println("Number of " + c + ": " + modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(c));
        }
        println(" ");
    }

    /**
     * This method is used to show to the player the presence of the professor in the Professor Table of his board.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showProfessorTablePlayer(int playerID, ModelView modelView){
        println("Professor table of player: " + playerID);
        for(Creature c : Creature.values() ) {
            println("Professor of " + c + ": " + modelView.getSchoolBoardPlayers().get(playerID).getProfessorTablePlayer().getOccupiedSeatsPlayer().get(c));
        }
        println(" ");
    }

    public void showCoinsReserve(ModelView modelView){
        println("Number of coins in reserve: " + modelView.getCoinGame() );
    }

    public void showCoinsPlayer(int playerID, ModelView modelView){
        println("Number of coins of player " + playerID + ": " + modelView.getCoinPlayer().get(playerID));
    }
    /**
     * This method is used to show to the player the number of his remaining towers in the Tower area of his schoolBoard.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showNumberOfTowers(int playerID, ModelView modelView) {
        println("Your number of remaining Towers is: " + modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer() );
        println(" ");
    }


    /**
     * This method is used to ask the player which students he wants to move from the entrance.
     * @param modelView is the reference to the modelView.
     */
    public int choiceOfStudentsToMove(int playerID, ModelView modelView ) {
        String request;
        int studentChosen = - 1;                                                  //settato a -1 a causa del try catch, ma non verrà mai restituito -1
        boolean rightStudentChosen = false;                                       //viene settata a true solo se inserisco "character" oppure se inserisco uno studente valido
        println("Which student do you want to move from the entrance?");
        showStudentsInEntrancePlayer(playerID, modelView);
        while(!rightStudentChosen) {
            request = scannerCLI.next();
            while (request.equals("show")) {
                show(playerID, modelView);
                println("Which student do you want to move from the entrance?");
                showStudentsInEntrancePlayer(playerID, modelView);
                request = scannerCLI.next();
            }
            if (request.equals("character")) {
                if(modelView.isExpertModeGame()) {
                    if (networkHandler.isCharacterUsed()) {
                        println("You already used a character in this round. Insert another request: ");
                    } else {
                        studentChosen = -2;
                        // set rightIslandChosen to true so that we exit the while loop
                        rightStudentChosen = true;
                    }
                }else{
                    println("You are not playing in expert mode, so you can't use a character card!");
                    println("Which student do you want to move from the entrance?");
                    showStudentsInEntrancePlayer(playerID, modelView);
                }
            } else {
                try {
                    studentChosen = Integer.parseInt(request);
                    if (studentChosen < 0 || studentChosen >= modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size()) {
                        println("This student id doesn't exist, please insert a valid student: ");
                    } else if (modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().get(studentChosen) == null) {
                        println("You already chose this student, please insert a valid one: ");
                    } else {
                        rightStudentChosen = true;                                  //settiamo a true solo se viene inserito uno studente valido
                    }
                } catch (NumberFormatException e) {                                 //se l'utente inserisce qualcosa che non va bene tipo "pluto", entriamo qui e di nuovo nel while principale
                    println("Wrong insert: please insert show/character or the student you want to move: ");
                    showStudentsInEntrancePlayer(playerID, modelView);
                }
            }

        }
        return studentChosen;
    }


    /**
     * This method is used to ask the player where he wants to move the student he chose.
     * @return the island ID chosen or -1 if the location chosen is 'diningRoom'.
     */
    public int choiceLocationToMove(int playerID, ModelView modelView) {
        println("Now you have to move the student you chose to the diningroom or on an island: where do you want to move him? ");
        int islandChosen = -1;
        String request;
        boolean rightLocationChosen = false;

        while(!rightLocationChosen) {
            request = scannerCLI.next();
            while (request.equals("show")) {
                show(playerID, modelView);
                println("Where do you want to move the student you chose?");
                request = scannerCLI.next();
            }
            switch (request) {
                case "character":
                    if (modelView.isExpertModeGame()) {
                        if (networkHandler.isCharacterUsed()) {
                            println("You already used a character in this round. Insert another request: ");
                        } else {
                            islandChosen = -2;
                            rightLocationChosen = true;
                        }
                    } else {
                        println("You are not playing in expert mode, so you can't use a character card!");
                        println("Where do you want to move the student you chose from the entrance?");
                    }
                    break;
                case "island":                                     //se sceglie isola, chiedo su quale isola voglia muoverlo, se sceglie diningroom, ritorno -1 come specificato
                    println(" ");
                    println("On which island do you want to move your students? ");

                    for (int i = 0; i < 12; i++) {
                        if (modelView.getIslandGame().get(i) != null) {
                            print(i + " ");
                        }
                    }
                    println(" ");
                    islandChosen = scannerCLI.nextInt();

                    while (islandChosen < 0 || islandChosen >= 12 || modelView.getIslandGame().get(islandChosen) == null) {
                        println("Please insert a valid island ID: ");
                        islandChosen = scannerCLI.nextInt();
                    }
                    rightLocationChosen = true;
                    break;
                case "diningroom":
                    rightLocationChosen = true;
                    break;
                default:
                    println("Please insert island/diningroom/show/character");
                    break;
            }
        }
        return islandChosen;
    }

    /**
     * This method is used by the client to view what he desires.
     * @param modelView is the reference to the modelView.
     */
    public void show(int playerID, ModelView modelView){

        println("What do you want to view? Insert entrance/professorTable/diningroom/islands/both/characters/clouds/coins/towers/nothing");
        String str = scannerCLI.next();

        while(!(str.equals("diningroom") || str.equals("islands") ||  str.equals("both") || str.equals("professorTable") ||str.equals("characters") || str.equals("nothing") ||str.equals("clouds") || str.equals("coins")|| str.equals("entrance")||str.equals("towers"))){   //possono essere viste queste cose
            println("Please insert one of the following: entrance/professorTable/diningroom/islands/both/characters/clouds/coins/towers/nothing");
            str = scannerCLI.nextLine();
        }

        if(str.equals("islands")){                                          //mostro le isole se sceglie di vedere isole
            showIslandsSituation(modelView);
        }else if(str.equals("diningroom")){                                 //mostro diningroom se sceglie di vedere diningroom di tutti i giocatori
            for( int i = 0; i<= modelView.getNumberOfPlayersGame()-1; i++){
                showStudentsInDiningRoomPlayer(i, modelView);
            }
        }else if(str.equals("both")){                                       //mostro entrambi se sceglie both
            showIslandsSituation(modelView);
            for( int i = 0; i<= modelView.getNumberOfPlayersGame()-1; i++) {
                showStudentsInDiningRoomPlayer(i, modelView);
            }
        }else if(str.equals("professorTable")){
            for( int i = 0; i<= modelView.getNumberOfPlayersGame()-1; i++) {
                showProfessorTablePlayer(i, modelView);
            }
        }else if(str.equals("characters")){
            if(modelView.isExpertModeGame()) {
                printCharacters(modelView);
            }else{
                println("There are no characters in this game since you are not playing in expert mode!");
                println(" ");
            }
            println(" ");
        }else if(str.equals("clouds")){
            showClouds(modelView);
            println(" ");
        }else if(str.equals("coins")){
            if(modelView.isExpertModeGame()){
                for(int i = 0; i<= modelView.getNumberOfPlayersGame()-1; i++){
                    showCoinsPlayer(i, modelView);
                }
                println(" ");
                showCoinsReserve(modelView);
            }else{
                println("There are no coins in the game since you are not playing in expert mode!");
            }
        }else if(str.equals("entrance")){
            for(int i = 0; i <= modelView.getNumberOfPlayersGame()-1; i++) {
                showStudentsInEntranceOfAllPlayer(i,modelView);
            }
        }else if(str.equals("towers")){
            for(int i = 0; i<= modelView.getNumberOfPlayersGame()-1; i++){
                showTowersPlayer(i, modelView);
            }
        }else if(str.equals("helpguide")){
            helpGuide();
        }else if(str.equals("helpCharacter")){
            helpCharacter();
        }

    }

    /**
     * This method is used to ask the player on which island he wants to move mother nature.
     * @param motherNatureIslandID is the ID of the island where mother nature currently stands.
     * @param modelView is the reference to the modelView.
     * @return ID of the island where mother nature will be moved or -2 if the player decided to use a character
     */
    public int choiceMotherNatureMovement(int playerID, int motherNatureIslandID, ModelView modelView){
        println("Now you have to move mother nature, which currently is on Island " + motherNatureIslandID);
        if(networkHandler.isMessengerActive()) {
            println("You can move mother nature up to: " + (modelView.getAssistantCardsValuesPlayer().get(modelView.getLastAssistantChosen()) + 2) + " seat(s) clockwise ");
        }else{
            println("You can move mother nature up to: " + modelView.getAssistantCardsValuesPlayer().get(modelView.getLastAssistantChosen()) + " seat(s) clockwise ");
        }

        println("On which island do you want to move your mother nature? Insert the island ID where you want to move her: ");
        println("Islands: ");
        for (int i = 0; i < 12; i++) {
            if (modelView.getIslandGame().get(i) != null) {
                print(i + " ");
            }
        }
        println(" ");

        boolean rightIslandChosen = false;
        int chosenIslandID = -1;
        String request;
        while(!rightIslandChosen) {
            request = scannerCLI.next();
            while (request.equals("show")) {
                show(playerID, modelView);
                println("Insert the island ID where you want to move mother nature: ");
                request = scannerCLI.next();
            }
            if (request.equals("character")) {
                if(modelView.isExpertModeGame()) {
                    if (networkHandler.isCharacterUsed()) {
                        println("You already used a character in this round. Insert another request: ");
                    } else {
                        chosenIslandID = -2;
                        // set rightIslandChosen to true so that we exit the while loop
                        rightIslandChosen = true;
                    }
                }else{
                    println("You are not playing in expert mode, so you can't use a character card!");
                    println("Where do you want to move mother nature?");
                }
            } else {
                try {
                    chosenIslandID = Integer.parseInt(request);
                    if(chosenIslandID < 0 || chosenIslandID >= 12 || modelView.getIslandGame().get(chosenIslandID) == null){
                        println("This island ID doesn't exist, please insert a valid one: ");
                    }else{
                        //if the island ID is valid
                        rightIslandChosen = true;
                    }
                }catch (NumberFormatException e){
                    println("wrong insert: please insert show/character or the island ID where you want to move mother nature:");
                }
            }
        }
       return chosenIslandID;
    }

    /**
     * This method is used to show to the player that he chose an invalid island ID for mother nature movement.
     */
    public void invalidMotherNatureMovement(){
        println("Invalid Mother Nature movement. ");
    }

    /**
     * This method is used to notify the player that he is the new master on the island where he moved mother nature.
     * @param modelView is the reference to the modelView.
     * @param playerID is the player ID.
     */
    public void newMaster(ModelView modelView, int playerID){
        println("You are the new Master on the Island selected! ");
        println("New number of Tower in your Tower Area: " + modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer() );
    }

    /**
     * This method is used to notify the player that he is the new master on the island where he moved mother nature.
     * @param modelView is the reference to the modelView.
     * @param motherNatureIslandID is the island ID where the player is the master on (it corresponds to the island ID where mother nature stands).
     * @param playerID is the player ID.
     */
    public void oldMaster(ModelView modelView, int motherNatureIslandID, int playerID){
        println("You are no more the master on island " + motherNatureIslandID +"!");
        println("New number of Tower in your Tower Area: " + modelView.getSchoolBoardPlayers().get(playerID).getTowerAreaPlayer().getCurrentNumberOfTowersPlayer());
    }


    /**
     * This method is used to notify the player that 2, or more, islands have been unified.
     * @param islandLanding is the island ID where mother nature lands after action_2, so it is the island to which the other one(s) unify.
     * @param islandToUnifyFlag is the flag to understand which island have been unified (the previous (-1), the following (+1) or both (0).
     */
    public void showUnion(int islandLanding, int islandToUnifyFlag, ArrayList<Integer> islandConnected){
        if(islandToUnifyFlag == -1) {
            println("The chosen island ("+ islandLanding +") has been unified with the previous island (" + islandConnected.get(0) +").");
        }else if(islandToUnifyFlag == 1){
            println("The chosen island ("+ islandLanding +") has been unified with the following island (" + islandConnected.get(0) +").");
        }else if(islandToUnifyFlag == 0){
            int secondIsland = islandLanding + 1;
            println("The chosen island ("+ islandLanding +") has been unified with the previous and the following islands (" + islandConnected.get(0) + ", " + islandConnected.get(1) +").");
        }
        println(" ");
    }


    /**
     * This method is used to show the initial situation on the islands, including mother nature position.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsOnIslands(ModelView modelView){
        println("The ISLAND SITUATION is:");
        for(int i = 0; i < 12; i++){
            if(modelView.getIslandGame().get(i) != null) {
                print("Island " + i + ": ");
                if (modelView.getIslandGame().get(i).getTotalNumberOfStudents() == 0) {           //se il numero di studenti su quell'isola è zero (all'inizio solo se è l'isola di madre natura oppure è l'isola opposta)
                    if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                        print("nobody, here stands mother nature.");
                    } else {
                        print("nobody");
                    }

                }

                //Print of students on islands
                for (Creature c : Creature.values()) {
                    if (modelView.getIslandGame().get(i).getStudentsOfType(c) != 0) {
                        print(c + ": " + modelView.getIslandGame().get(i).getStudentsOfType(c) + "; ");
                    }
                }
                print(" ");

                //Print of masters of the islands
                if(modelView.getIslandGame().get(i).getMasterOfArchipelago() != -1){
                    print("[Master on this island is Player " + modelView.getIslandGame().get(i).getMasterOfArchipelago() + "] ");
                }
                print(" ");

                //Print of no entry tiles
                if(modelView.getIslandGame().get(i).getNoEntryTiles() != 0){
                    print("[NO ENTRY TILES: " + modelView.getIslandGame().get(i).getNoEntryTiles() + "] ");
                }
                println(" ");

            }
        }
        println(" ");
    }

    /**
     * This method is used to invoke the real methods showing the islands' situation, which means the number of student on each one.
     * @param modelView is the reference to the modelView.
     */
    public void showIslandsSituation(ModelView modelView) {
        showStudentsOnIslands(modelView);
        showMotherNaturePosition(modelView);
    }

    /**
     * This method is used to show the mother nature position.
     * @param modelView is the reference to the modelView.
     */
    public void showMotherNaturePosition(ModelView modelView){
        for(int i = 0; i < 12; i++) {
            if (modelView.getIslandGame().get(i) != null) {
                if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                    println("Mother Nature is on the island: " + i);
                }
            }
        }
        println(" ");
    }

    /**
     * This method is used to show the new mother nature position after action_2 movement.
     * @param motherNatureIslandPosition is the new ID of the island where mother nature stands.
     */
    public void newMotherNaturePosition(int motherNatureIslandPosition){
        println("Mother Nature is now on the island: " + motherNatureIslandPosition);
        println(" ");
    }

    /**
     * This method is used to show to the player the clouds' situation.
     * @param modelView is the reference to the modelView.
     */
    public void showClouds(ModelView modelView){
        println("The Clouds situation is: ");
        if(modelView.getNumberOfPlayersGame() == 2){
            print("Cloud 0: ");
            for (int i = 0; i < 3; i++) {
                //print(modelView.getStudentsOnClouds().get(i).toString() + " ");
                Creature student = modelView.getStudentsOnClouds().get(i);
                if(student == null){
                    print("---");
                }else{
                    print(student + " ");
                }
            }
            println(" ");
            print("Cloud 1: ");
            for (int i = 3; i < 6; i++) {
                //print(modelView.getStudentsOnClouds().get(i).toString() + " ");
                Creature student = modelView.getStudentsOnClouds().get(i);
                if(student == null){
                    print("---");
                }else{
                    print(student + " ");
                }
            }
            println(" ");
        }else if(modelView.getNumberOfPlayersGame() == 3) {
            print("Cloud 0: ");
            for (int i = 0; i < 4; i++) {
                //print(modelView.getStudentsOnClouds().get(i).toString() + " ");
                Creature student = modelView.getStudentsOnClouds().get(i);
                if(student == null){
                    print("---");
                }else{
                    print(student + " ");
                }
            }
            println(" ");
            print("Cloud 1: ");
            for (int i = 4; i < 8; i++) {
                //print(modelView.getStudentsOnClouds().get(i).toString() + " ");
                Creature student = modelView.getStudentsOnClouds().get(i);
                if(student == null){
                    print("---");
                }else{
                    print(student + " ");
                }
            }
            println(" ");
            print("Cloud 2: ");
            for (int i = 8; i < 12; i++) {
                //print(modelView.getStudentsOnClouds().get(i).toString() + " ");
                Creature student = modelView.getStudentsOnClouds().get(i);
                if(student == null){
                    print("---");
                }else{
                    print(student + " ");
                }
            }
            println(" ");
        }

    }

    /**
     * This method is used to ask the player which cloud he wants to take the student from.
     * @param playerID is the player ID.
     * @param modelView is the reference to the modelView.
     * @return the cloud ID chosen.
     */
    public int chooseCloud(int playerID, ModelView modelView){
        int cloudChosen = -1;                               //setto a -1 per il try catch ma non restituirà mai -1
        String request;
        showClouds(modelView);
        boolean rightCloudChosen = false;
        println("Which cloud do you want to take the students from? They will be moved to your entrance: ");
        while(!rightCloudChosen) {
            request = scannerCLI.next();
            while (request.equals("show")) {
                show(playerID, modelView);
                println("Which cloud do you want to take the students from? ");
                request = scannerCLI.next();
            }

            if (request.equals("character")) {
                if(modelView.isExpertModeGame()) {
                    if (networkHandler.isCharacterUsed()) {
                        println("You already used a character in this round. Insert another request: ");
                    } else {
                        cloudChosen = -2;                                             //character nel net. handler
                        rightCloudChosen = true;
                    }
                }else{
                    println("You are not playing in expert mode, so you can't use a character card!");
                    println("Which cloud do you want to take the students from? They will be moved to your entrance: ");
                    showClouds(modelView);
                }
            } else {
                try {
                    cloudChosen = Integer.parseInt(request);
                    int cloudNumber = modelView.getNumberOfPlayersGame();
                    if(cloudChosen < 0 || cloudChosen >= cloudNumber ) {
                        println("This cloud ID doesn't exist, please select a valid cloud ID: ");
                    }else{
                        rightCloudChosen = true;
                    }
                }catch (NumberFormatException e){
                    println("Wrong insert: please insert show/character or the cloud ID you want to take the students from: ");
                }
            }
        }
        return  cloudChosen;
    }

    /**
     * This method is used to show to the player that he chose an invalid Cloud ID to take the students from.
     * @param player_ID is the player ID.
     * @param modelView is the reference to the modelView.
     * @return the new chosen cloud ID.
     */
    public int invalidCloudSelection(int player_ID, ModelView modelView){
        println("Invalid Cloud ID: this cloud has already been chosen. Please select a new one: ");
        return chooseCloud(player_ID, modelView);
    }

    /**
     * This method is used to show the winner of the match.
     * @param winnerNickname is the nickname of the winner.
     * @param winnerReason is the reason why a player wins.
     * @param playerIDwinner is the id of the winner.
     */
    public void matchEnd(String winnerNickname, String winnerReason, int playerIDwinner, int playerID){
        println(" ");
        println("THE MATCH IS ENDED, reason: " + winnerReason);
        if(playerIDwinner == playerID){
            println("YOU ARE THE WINNER! CONGRATULATIONS!! ");
        }else if(playerIDwinner == -1){
            println("A player disconnected: it a TIE! No one won this match :/  ");
        }else {
            println("You lost this game, the winner is " + playerIDwinner +": " + winnerNickname);
        }
        println(" ");
        println(" ");
    }

    /**
     * This method is used to notify a player that a new round is beginning.
     */
    public void newRoundBeginning(){
        println(" ");
        println(" ");
        println("The round is over, a new one is beginning! ");
    }

    /**
     * This method is used by the client when he wants to use one of the character of his match.
     * @param modelView is the reference to the modelView.
     */
    public String characterChoice(ModelView modelView){
        println("Which character do you want to use? ");
        printCharacters(modelView);

        println("Insert one of the character: ");
        String characterChosen = scannerCLI.next();

        while(!(modelView.getCharacterCardsInTheGame().contains(characterChosen.toLowerCase()))){
            println("Please insert a right character: ");
            if(modelView.isExpertModeGame()) {
                printCharacters(modelView);
            }else{
                println("There are no characters in this game since you are not playing in expert mode!");
                println(" ");
            }
            characterChosen = scannerCLI.next();
        }
        println(" ");
        println("You choose to use: " + characterChosen);
        return  characterChosen.toLowerCase();

    }

    /**
     * This method is used by the client when he uses the Monk character.
     * @param modelView is the reference to the modelView.
     * @return the id of the chosen student.
     */
    public int choiceStudentMonk(ModelView modelView) {
        int studentChosen;
        println("Which student do you want to move from this card? (0-3)");
        int i = 0;
        for (Creature c : modelView.getCharactersDataView().getMonkStudents()) {
            print(i + ": " + c);
            i++;
            if(i < modelView.getCharactersDataView().getMonkStudents().size()){
                println("; ");
            }else{
                println(". ");
                println(" ");
            }
        }
        println(" ");
        studentChosen = scannerCLI.nextInt();
        while (studentChosen < 0 || studentChosen > 3) {
            println("Invalid student, please insert a valide one: (0-3)");
            studentChosen = scannerCLI.nextInt();
        }
        return studentChosen;
    }

    /**
     * This method is used by the client when he uses the Monk character.
     * @param modelView is the reference to the modelView.
     * @return the id of the chosen island.
     */
    public int choiceIslandMonk(ModelView modelView){
        int islandChosen;
        println("On which island do you want to move the student you chose?");
        showStudentsOnIslands(modelView);
        islandChosen = scannerCLI.nextInt();
        while(islandChosen < 0 || islandChosen > 11 || modelView.getIslandGame().get(islandChosen)==null){
            println("Please insert a valid island: ");
            showStudentsOnIslands(modelView);
            islandChosen = scannerCLI.nextInt();
        }
        return  islandChosen;
    }

    /**
     * This method is used by the client when he uses the Jester character.
     * @param playerID is the player ID.
     * @param modelView is the reference to the modelView.
     * @return the arraylist of students the player wants to move from the entrance.
     */
    public ArrayList<Integer> choiceStudentEntranceJester(int playerID, ModelView modelView) {
        ArrayList<Integer> studentsFromEntranceJester = new ArrayList<>();
        int studentChosen;
        String request;
        int maxNumberOfStudents = 0;

        println("Student on the Jester Card: ");
        int i = 0;
        for(Creature c : modelView.getCharactersDataView().getJesterStudents()){
            print( i + ": " + c);
            i++;
            if(i < modelView.getCharactersDataView().getJesterStudents().size()){
                println("; ");
            }else{
                println(". ");
                println(" ");
            }
        }

        println("Which student do you want to move from the entrance? (Insert the ID)");
        showStudentsInEntrancePlayer(playerID, modelView);

        while(maxNumberOfStudents < 3) {
            request = scannerCLI.next();
            if (!request.equals("stop")) {
                studentChosen = Integer.parseInt(request);
               if (studentChosen < 0 || studentChosen > modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size() || modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().get(studentChosen) == null) {
                    println("Please insert a valid student or write 'stop': ");
                }else{
                   studentsFromEntranceJester.add(studentChosen);
                   maxNumberOfStudents ++;
                   println("Ok, insert another one or write 'stop':");
               }
            }else{
                networkHandler.setJesterNumber(maxNumberOfStudents);
                break;
            }
        }
        if(maxNumberOfStudents == 3){
            networkHandler.setJesterNumber(maxNumberOfStudents);
        }
        return studentsFromEntranceJester;
    }

    /**
     * This method is used by the client when he uses the Jester character.
     * @param modelView is the reference to the modelView.
     * @return  the arraylist of students the player wants to move from the card.
     */
    public ArrayList<Integer> choiceStudentCardJester(ModelView modelView){
        ArrayList<Integer> studentsFromCardJester = new ArrayList<>();
        int studentChosen;
        int maxNumberOfStudents = 0;
        int i = 0;
        println("Which student do you want to take from the card? Please insert their id: ");
        for(Creature c : modelView.getCharactersDataView().getJesterStudents()){
            print( i + ": " + c + " ");
            i++;
            if(i < modelView.getCharactersDataView().getJesterStudents().size()){
                println("; ");
            }else{
                println(" ");
            }
        }
        println(" ");
        while(maxNumberOfStudents < networkHandler.getJesterNumber()) { //qui non ci vuole l'uguale, ma sono <=
            studentChosen = scannerCLI.nextInt();
            while (studentChosen < 0 || studentChosen > modelView.getCharactersDataView().getJesterStudents().size()) {
                println("Please insert a valid student: ");
                studentChosen = scannerCLI.nextInt();
            }
            studentsFromCardJester.add(studentChosen);
            maxNumberOfStudents ++;
            if(maxNumberOfStudents < networkHandler.getJesterNumber()) {
                println("Ok, insert another one: ");
            }else{
                println("Ok!");
            }
        }
        return studentsFromCardJester;
    }

    /**
     * This method is used by the client when he uses the Herbalist character.
     * @param modelView is the reference to the modelView.
     * @return the id of the chosen island.
     */
    public int choiceHerbalist(ModelView modelView){
        println("On which island do you want to put a No Entry Tile? ");
        showIslandsSituation(modelView);
        int islandIDChosenByClient = scannerCLI.nextInt();

        while(islandIDChosenByClient < 0 || islandIDChosenByClient > 11){
            println("Not a valid island ID, please insert a valid number: ");
            showIslandsSituation(modelView);
            islandIDChosenByClient = scannerCLI.nextInt();
        }

        return islandIDChosenByClient;
    }

    /**
     * This method is used by the client when he uses the Ambassador character.
     * @param modelView is the reference to the modelView.
     * @return the id of the chosen island.
     */
    public int choiceAmbassador(ModelView modelView){
        println("On which island do you want to compute the influence? ");
        showIslandsSituation(modelView);
        int islandIDChosenByClient = scannerCLI.nextInt();

        while(islandIDChosenByClient < 0 || islandIDChosenByClient > 11 || modelView.getIslandGame().get(islandIDChosenByClient) == null){
            println("Not a valid island ID, please insert a valid number: ");
            showIslandsSituation(modelView);
            islandIDChosenByClient = scannerCLI.nextInt();
        }

        return islandIDChosenByClient;
    }

    /**
     * This method is used by the client when he uses the MushroomsMerchant character card.
     * @return the Creature chosen.
     */
    public Creature choiceMushroomsMerchant(){
        ArrayList <String> availableStudents = new ArrayList<>();
        println("Which student do you want to keep out from the influence computation? ");
        for(Creature c : Creature.values()){
            print(c.toString());
            availableStudents.add(c.toString());
        }
        println(" ");
        String chosenStudentByClientStr = scannerCLI.next();
        while(!availableStudents.contains(chosenStudentByClientStr)){
            println("Not a valid student, please insert a valid one: ");
            chosenStudentByClientStr = scannerCLI.next();
        }

        return Creature.valueOf(chosenStudentByClientStr);
    }

    /**
     * This method is used by the client when he uses the Bard character.
     * @param playerID is the player ID.
     * @param modelView is the reference to the modelView.
     * @return the arraylist of students the player wants to move from the entrance.
     */
    public ArrayList<Integer> choiceStudentEntranceBard(int playerID, ModelView modelView) {
        ArrayList<Integer> studentsFromEntranceBard = new ArrayList<>();
        int studentChosen;
        int maxNumberOfStudents = 0;
        String request;

        int dragon = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.DRAGON);
        int unicorn = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.UNICORN);
        int fairy = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.FAIRY);
        int gnome = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.GNOME);
        int frog = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.FROG);


        int studentsInTheDiningRoom = dragon + unicorn + fairy + gnome + frog;
        if(studentsInTheDiningRoom > 2){
            studentsInTheDiningRoom = 2;
        }
        if(studentsInTheDiningRoom == 0) {
            println("You have not enough students in your diningroom to use the bard character card, you wasted it! ");
            studentsFromEntranceBard = null;
        }else {
            println("Which student do you want to move from the entrance? (bard)");
            showStudentsInEntrancePlayer(playerID, modelView);
            while (maxNumberOfStudents < studentsInTheDiningRoom) {
                request = scannerCLI.next();
                if (!request.equals("stop")) {
                    studentChosen = Integer.parseInt(request);
                    if (studentChosen < 0 || studentChosen > modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size() || modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().get(studentChosen) == null) {
                        println("Please insert a valid student or write 'stop': ");
                    } else {
                        studentsFromEntranceBard.add(studentChosen);
                        maxNumberOfStudents++;
                        println("Ok, insert another one or write 'stop':");
                    }
                } else {
                    networkHandler.setBardNumber(maxNumberOfStudents);
                    break;
                }

            }

        }
        if(maxNumberOfStudents == 2){
            networkHandler.setBardNumber(maxNumberOfStudents);
        }
        return studentsFromEntranceBard;
    }

    /**
     * This method is used by the client when he uses the Bard character.
     * @param playerID is the player ID.
     * @param modelView is the reference to the modelView.
     * @return the arraylist of students the player wants to move from the diningroom.
     */
    public ArrayList<Creature> choiceStudentDiningRoomBard(int playerID, ModelView modelView) {
        ArrayList<Creature> studentsFromDiningRoom = new ArrayList<>();
        boolean rightStudentChoice = false;
        String studentChosen;
        int maxNumberOfStudents = 0;

        int dragon = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.DRAGON);
        int unicorn = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.UNICORN);
        int fairy = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.FAIRY);
        int gnome = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.GNOME);
        int frog = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.FROG);

        println("Which student(s) do you want to move from your dining room?  ");
        showStudentsInDiningRoomPlayer(playerID, modelView);
        while (maxNumberOfStudents < networkHandler.getBardNumber()){
            studentChosen = scannerCLI.next();
            while (!rightStudentChoice) {
                if (!(studentChosen.equals("DRAGON") || studentChosen.equals("UNICORN") || studentChosen.equals("FROG") || studentChosen.equals("GNOME") || studentChosen.equals("FAIRY"))) {
                    println("Please insert a valid student: (DRAGON/UNICORN/FROG/GNOME/FAIRY");
                    studentChosen = scannerCLI.next();
                } else if (studentChosen.equals("DRAGON")) {
                    if (dragon == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        dragon -= 1;
                        rightStudentChoice = true;
                    }
                } else if (studentChosen.equals("UNICORN")) {
                    if (unicorn == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        unicorn -= 1;
                        rightStudentChoice = true;
                    }
                } else if (studentChosen.equals("FAIRY")) {
                    if (fairy == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        fairy -= 1;
                        rightStudentChoice = true;
                    }
                } else if (studentChosen.equals("FROG")) {
                    if (frog == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        frog -= 1;
                        rightStudentChoice = true;
                    }
                } else {
                    if (gnome == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        gnome -= 1;
                        rightStudentChoice = true;
                    }
                }
            }

            studentsFromDiningRoom.add(Creature.valueOf(studentChosen));
            maxNumberOfStudents++;
            rightStudentChoice = false;
        }
            return studentsFromDiningRoom;
    }

    /**
     * This method is used by the client when he uses the Trafficker character card.
     * @return the Creature chosen.
     */
    public Creature choiceTrafficker(){
        Creature chosenTypeOfStudent = null;
        println("CHOOSE A TYPE OF STUDENT\nPlease insert the number corresponding to the type you chose.");

        int i = 1;
        for(Creature c : Creature.values()){
            println(i + ": " + c.toString());
            i++;
        }
        println(" ");
        String studentTypeInput;

        while(true){
            try{
                studentTypeInput = scannerCLI.next();
                while(Integer.parseInt(studentTypeInput)  < 1 || Integer.parseInt(studentTypeInput) > 5){
                    println("Please insert a number between 1 and 5.");
                    studentTypeInput = scannerCLI.next();
                }
                break;
            }catch (NumberFormatException e){
                println("ERROR: Please insert a valid numeric value!");
            }
        }

        switch (studentTypeInput){
            case "1":
                chosenTypeOfStudent = Creature.DRAGON;
                break;
            case "2":
                chosenTypeOfStudent = Creature.FAIRY;
                break;
            case "3":
                chosenTypeOfStudent = Creature.UNICORN;
                break;
            case "4":
                chosenTypeOfStudent = Creature.GNOME;
                break;
            case "5":
                chosenTypeOfStudent = Creature.FROG;
                break;
        }
        return chosenTypeOfStudent;
    }

    /**
     * This method is used by the client when he uses the Princess character card.
     * @return  the student ID chosen;
     */
    public int choicePrincess(ModelView modelView){
        println("Which student do you choose? You will move it from the card to your Dining Room");
        int i = 0;
        for(Creature c : modelView.getCharactersDataView().getPrincessStudents()){
            print(i + ": " + c);
            i++;
            if(i < modelView.getCharactersDataView().getPrincessStudents().size()){
                println("; ");
            }else{
                println(". ");
                println(" ");
            }
        }

        int chosenStudentIDByClient = scannerCLI.nextInt();
        while ((chosenStudentIDByClient < 0 || chosenStudentIDByClient > 3 )){
            println("Not a valid student ID, please choose a valid one: ");
            chosenStudentIDByClient = scannerCLI.nextInt();
        }

        return chosenStudentIDByClient;
    }

    /**
     * This method show all the characters with their students if necessary and their price
     * @param modelView reference to the model view
     */
    public void printCharacters(ModelView modelView){
        int characterIndex;
        boolean characterAlreadyUsed;
        int characterPrice;

        for (String s : modelView.getCharacterCardsInTheGame()) {
            characterIndex = modelView.getCharacterCardsInTheGame().indexOf(s);
            characterPrice = modelView.getCharactersPrice().get(characterIndex);
            characterAlreadyUsed = modelView.getCharactersPriceIncreased().get(characterIndex);

            switch (s) {
                case "monk":
                    println("MONK: " + modelView.getCharactersDataView().getMonkStudents() +
                            "   Price: " + (characterAlreadyUsed ? characterPrice+1 : characterPrice));
                    break;
                case "princess":
                    println("PRINCESS: " + modelView.getCharactersDataView().getPrincessStudents() +
                            "   Price: " + (characterAlreadyUsed ? characterPrice+1 : characterPrice));
                    break;
                case "jester":
                    println("JESTER: " + modelView.getCharactersDataView().getJesterStudents() +
                            "   Price: " + (characterAlreadyUsed ? characterPrice+1 : characterPrice));
                    break;
                default:
                    println(s.toUpperCase() + "   Price: " + (characterAlreadyUsed ? characterPrice+1 : characterPrice));
                    break;
            }
        }
    }

    /**
     * This method used to notify the player he can't use the herbalist character card.
     * @param herbalistNackExplanation is the reason why he can't.
     */
    public void invalidHerbalistChoice(String herbalistNackExplanation){
        println(herbalistNackExplanation);
        println(" ");
    }

    /**
     * This method used to notify the player he can't use the princess character card.
     * @param princessNackExplanation is the reason why he can't.
     */
    public void invalidPrincessChoice(String princessNackExplanation){
        println(princessNackExplanation);
        println(" ");
    }

    /**
     * This method is used to notify the player he can't use a certain character.
     * @param invalidCharacterNackExplanation is the reason why he can't.
     */
    public void invalidCharacter(String invalidCharacterNackExplanation){
        println(invalidCharacterNackExplanation);
        println(" ");
    }

    /**
     * This method is used to notify the players that a certain character card has been successfully used.
     * @param characterUsed is the character used.
     */
    public void characterConfirm(String characterUsed){
        println(characterUsed + " used correctly!");
    }

    /**
     * This method prints a help guide which could be useful for the player during the game.
     */
    public void helpGuide(){
        println("PLANNING PHASE: You have to choose the assistant card you want to play: the player whose assistant card's order value is the lowest ");
        println("will be the first one to play in the action phase");
        print("ACTION1: After planning phase, the player has to move 3 students (or 4 if 3 players are playing) to the diningroom or on an island of his choice.");
        println(" Whenever a player has more students in the diningroom than the others, he gets the professor of the creature moved.");
        print("ACTION2: After that, the player has to move mother nature on an island. He can move her clockwise depending on ");
        print("the assistant card he had played in the planning phase (e.g. he used the assistant card whose order value is 7,");
        println("  he can move mother nature up to 4 steps.");
        println("After moving mother nature, it will be computed the influence over the island where she lands, meaning that the player who has ");
        println("the highest influence (highest number of students with the controlled professor + towers), will conquer the island and build a tower over it.");
        println("If a player conquers 2, or more, adjacent islands, they get unified.");
        println("ACTION3: after that, the players has to choose a cloud among the 2 (or 3). The students on the cloud will be added to his entrance and a new round will begin.");
    }

    /**
     * This method prints a help guide on what each character card does.
     */
    public void helpCharacter(){
        println("MONK: You can take a student from this card and place it on an island of your choice.");
        println("COOK: During this turn, you take control of any number of professors even if you have the same number of students as the player");
        print(" who currently controls them");
        println("AMBASSADOR: Choose an island and resolve it as if mother nature had ended her movement there.");
        println("MESSENGER: you may move mother nature up to 2 additional islands that is indicated by the assistant card you've played.");
        println("HERBALIST: place a no entry tile on an island of your choice: the first time mother nature ends her movement there, do not calculate the influence on the island. ");
        println("CENTAUR: When resolving a conquering on an island, towers do not count towards influence");
        println("JESTER: You may take up to 3 students from this card and exchange them with the same number of students from your entrance.");
        println("KNIGHT: During the influence calculation this turn, you count as having 2 more influence.");
        println("MUSHROOM MERCHANT: Choose a color of  student: during the influence calculation this turn, that color adds no influence." );
        println("BARD: You may exchange up to 2 students between your entrance and your diningroom.");
        println("PRINCESS: Take 1 student from this card and place it in your diningroom.");
        println("TRAFFICKER: Choose a type of student. Every player (including yourself) must return 3 students of that type from their diningroom to the bag. ");
        println("If any player has fewer than 3 students of that type, return as many students as they have.");
    }




    public void print(String strToPrint){
        System.out.print(strToPrint);
    }

    public void println(String strToPrint){
        System.out.println(strToPrint);
    }

    public void printlnInt(int intToPrint){
        System.out.println(intToPrint);
    }

    public void printInt(int intToPrint){
        System.out.print(intToPrint);
    }


}