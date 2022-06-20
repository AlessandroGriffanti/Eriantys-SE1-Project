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
    ArrayList<String> keyword = new ArrayList<>();

    /**
     * Cli constructor creates a new instance of the cli and sets the connection between the client and the server through the startClient method.
     * @param ip is the server ip
     * @param port is the server port
     */
    public CLI(String ip, int port) throws IOException, InterruptedException {
        this.networkHandler = new NetworkHandler(ip, port, this);

        this.keyword.add("character");
        this.keyword.add("show");

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
        } catch (IOException e) {
            System.out.println("Server no longer available :(  " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        if(newMatchStr.equals("y")){
            return true;
        }else {
            return false;
        }
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
        printInt(numberOfPlayerInTheLobby);
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

        if(expertMode.equals("y")) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method is used to ask the player which lobby he wants to join among the ones already existing.
     * @param arrayLobby is the arraylist of boolean representing the existing lobby: true means the lobby is full, false means it's not full.
     * @return the lobby chosen by the player.
     */


    public int lobbyToChoose(ArrayList<Boolean> arrayLobby, ArrayList<Boolean> arrayExpert, ArrayList<Integer> arrayNumPlayer, ArrayList<Boolean> arrayEnd){
        println("Choose a Lobby: \n");

        //STAMPA DELLE LOBBIES          //todo metodo stampa
        println("ERIANTYS LOBBIES: ");
        for (int i = 0; i < arrayLobby.size(); i++) {
            if(arrayEnd.get(i) == false) {
                if (arrayLobby.get(i) == false) {
                    print("Lobby " + i + ": full. :(  [There are " + arrayNumPlayer.get(i) + " players in this Lobby] ");
                    if (arrayExpert.get(i) == true) {
                        print(" - This match is in Expert Mode - ");
                    } else if (arrayExpert.get(i) == false) {
                        print(" - This match is NOT in Expert Mode - ");
                    }
                    println(" ");

                } else if (arrayLobby.get(i) == true) {
                    print("Lobby " + i + ": available! :)  [Maximum number of players for this game is " + arrayNumPlayer.get(i) + "] ");
                    if (arrayExpert.get(i) == true) {
                        print(" - This match is in Expert Mode - ");
                    } else if (arrayExpert.get(i) == false) {
                        print(" - This match is NOT in Expert Mode - ");
                    }
                    println(" ");
                }
            }else if(arrayEnd.get(i) == true){
                print("Lobby " + i + ": the match in this lobby is ended. Not available to join.  ");
                println(" ");
            }
        }

        //SCANNER DELLE LOBBY
        int lobbyIDchosenByPlayer = scannerCLI.nextInt();

        boolean rightLobbyChoice = false;

        while(!rightLobbyChoice) {
            if( lobbyIDchosenByPlayer < arrayLobby.size() ) {
                if (arrayLobby.get(lobbyIDchosenByPlayer) == false) {
                    if(arrayEnd.get(lobbyIDchosenByPlayer) == true){
                        println("Lobby " + lobbyIDchosenByPlayer + ": the match in this lobby is ended. Not available to join, select another one from the list below. ");
                        println(" ");

                        //STAMPA DELLE LOBBIES
                        println("ERIANTYS LOBBIES: ");
                        for (int i = 0; i < arrayLobby.size(); i++) {
                            if (arrayEnd.get(i) == false) {
                                if (arrayLobby.get(i) == false) {
                                    print("Lobby " + i + ": full. :(  [There are " + arrayNumPlayer.get(i) + " players in this Lobby] ");
                                    if (arrayExpert.get(i) == true) {
                                        print(" - This match is in Expert Mode - ");
                                    } else if (arrayExpert.get(i) == false) {
                                        print(" - This match is NOT in Expert Mode - ");
                                    }
                                    println(" ");

                                } else if (arrayLobby.get(i) == true) {
                                    print("Lobby " + i + ": available! :)  [Maximum number of players for this game is " + arrayNumPlayer.get(i) + "] ");
                                    if (arrayExpert.get(i) == true) {
                                        print(" - This match is in Expert Mode - ");
                                    } else if (arrayExpert.get(i) == false) {
                                        print(" - This match is NOT in Expert Mode - ");
                                    }
                                    println(" ");
                                }
                            } else if (arrayEnd.get(i) == true) {
                                print("Lobby " + i + ": the match in this lobby is ended. Not available to join.  ");
                                println(" ");
                            }
                        }

                        lobbyIDchosenByPlayer = scannerCLI.nextInt();
                    }else {
                        println("You can't join this lobby, match selected already started. Select another one from the list below. ");
                        println(" ");

                        //STAMPA DELLE LOBBIES
                        println("ERIANTYS LOBBIES: ");
                        for (int i = 0; i < arrayLobby.size(); i++) {
                            if (arrayEnd.get(i) == false) {
                                if (arrayLobby.get(i) == false) {
                                    print("Lobby " + i + ": full. :(  [There are " + arrayNumPlayer.get(i) + " players in this Lobby] ");
                                    if (arrayExpert.get(i) == true) {
                                        print(" - This match is in Expert Mode - ");
                                    } else if (arrayExpert.get(i) == false) {
                                        print(" - This match is NOT in Expert Mode - ");
                                    }
                                    println(" ");

                                } else if (arrayLobby.get(i) == true) {
                                    print("Lobby " + i + ": available! :)  [Maximum number of players for this game is " + arrayNumPlayer.get(i) + "] ");
                                    if (arrayExpert.get(i) == true) {
                                        print(" - This match is in Expert Mode - ");
                                    } else if (arrayExpert.get(i) == false) {
                                        print(" - This match is NOT in Expert Mode - ");
                                    }
                                    println(" ");
                                }
                            } else if (arrayEnd.get(i) == true) {
                                print("Lobby " + i + ": the match in this lobby is ended. Not available to join.  ");
                                println(" ");
                            }
                        }

                        lobbyIDchosenByPlayer = scannerCLI.nextInt();
                    }
                } else {
                    rightLobbyChoice = true;
                }
            }else {
                println("Lobby not existing, insert a right number from the list of Lobbies: ");
                println(" ");

                //STAMPA DELLE LOBBIES
                println("ERIANTYS LOBBIES: ");
                for (int i = 0; i < arrayLobby.size(); i++) {
                    if(arrayEnd.get(i) == false) {
                        if (arrayLobby.get(i) == false) {
                            print("Lobby " + i + ": full. :(  [There are " + arrayNumPlayer.get(i) + " players in this Lobby] ");
                            if (arrayExpert.get(i) == true) {
                                print(" - This match is in Expert Mode - ");
                            } else if (arrayExpert.get(i) == false) {
                                print(" - This match is NOT in Expert Mode - ");
                            }
                            println(" ");

                        } else if (arrayLobby.get(i) == true) {
                            print("Lobby " + i + ": available! :)  [Maximum number of players for this game is " + arrayNumPlayer.get(i) + "] ");
                            if (arrayExpert.get(i) == true) {
                                print(" - This match is in Expert Mode - ");
                            } else if (arrayExpert.get(i) == false) {
                                print(" - This match is NOT in Expert Mode - ");
                            }
                            println(" ");
                        }
                    }else if(arrayEnd.get(i) == true){
                        print("Lobby " + i + ": the match in this lobby is ended. Not available to join.  ");
                        println(" ");
                    }
                }
                lobbyIDchosenByPlayer = scannerCLI.nextInt();
            }
        }
        return lobbyIDchosenByPlayer;
    }

    /**
     * This method is used to notify the player the nickname he had chosen is not available.
     */
    public void nicknameNotAvailable (){
        println("Nickname not available, insert a new nickname: ");
    }

    /**
     * This method is used to notify the player he can't join a lobby and he needs to create a new one.
     */
    public void lobbyNotAvailable(){
        println("No lobby available, creating a new one...");
    }

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
        println("\n - - - GAME IS STARTED !!! - - - \n");
    }

    /**
     * This method is used to notify the player that it's not his turn, so he has to wait.
     */
    public void turnWaiting () {
        println("Wait for your turn!");
    }

    /**
     * This method is used to notify the player that it's his turn, so he has to act.
     */
    public void isYourTurn() {
        println("Is your Turn! Make your choice: ");
    }

    /**
     * This method is used to ask the first player which color he wants his towers to be.
     * @return the color chosen in Tower type.
     */
    public  Tower towerChoice () {
        println("Choose the COLOR of your tower: ");
        println("BLACK , WHITE, GREY");
        String strTowerColorChosen = scannerCLI.next();

        while( !( strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") || strTowerColorChosen.equals("GREY") ) ){
            println("Please, insert the right color: ");
            strTowerColorChosen = scannerCLI.next();
        }

        return Tower.valueOf(strTowerColorChosen);
    }

    /**
     * This method is used to ask the following players which color they want their towers to be.
     * @param notAvailableTowerColors is the list of  colors that have already been chosen.
     * @return the color chosen.
     */
    public Tower towerChoiceNext (ArrayList<Tower> notAvailableTowerColors) {
        println("Choose your tower color: BLACK, WHITE, GREY ");
        String strTowerColorChosen = scannerCLI.next();

        boolean rightTowerChoice = false;

        while(!rightTowerChoice){
            if(! (strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") || strTowerColorChosen.equals("GREY")) ){
                println("Please insert a valid color tower: ");
                strTowerColorChosen = scannerCLI.next();
            }else if( notAvailableTowerColors.contains(Tower.valueOf(strTowerColorChosen)) ){
                println("This color has already been selected, please choose another one: ");
                strTowerColorChosen = scannerCLI.next();
            }else {
                rightTowerChoice = true;
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
        println("Choose your deck among the following: ");
        println("FORESTWIZARD, DESERTWIZARD, CLOUDWITCH, LIGHTNINGWIZARD");

        String strDeckChosen = scannerCLI.nextLine();

        boolean rightDeckChoice = false;

        while(!rightDeckChoice){
            if(!(strDeckChosen.equals("FORESTWIZARD") || strDeckChosen.equals("DESERTWIZARD") || strDeckChosen.equals("CLOUDWITCH") || strDeckChosen.equals("LIGHTNINGWIZARD"))){
                println("Please, insert a correct deck: ");
                strDeckChosen = scannerCLI.next();
            } else if(notAvailableDecks.contains(Wizard.valueOf(strDeckChosen))) {
                println("This deck has already been chosen, plase select another one: ");
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
        for(Integer i : availableAssistantCard.keySet()){
            print(i + " ");
        }
        println(" ");
        request = scannerCLI.next();
        while(rightAssistantChosen == false){
            try {
                assistantChosen = Integer.parseInt(request);
                if(!(availableAssistantCard.containsKey(assistantChosen))){
                    println("You can't use this assistant, it doesn't exist. Please select another one: (1-10) ");
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
        println("Which assistant do you want to play?");
        for(Integer i : availableAssistantCard.keySet()){
            print(i + " ");
        }
        println(" ");
        request = scannerCLI.next();
        while(rightAssistantChosen == false) {
            try {
                assistantChosen = Integer.parseInt(request);
                if (assistantCardsAlreadyUsedThisRound.contains(assistantChosen)) {
                    println("You can't use this assistant, it has already been used this round by another player. Please select another one: ");
                    request = scannerCLI.next();
                }else if (!(availableAssistantCard.containsKey(assistantChosen))) {
                    println("You can't use this assistant, it doesn't exist. Please select another one: ");
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

    /*
    public synchronized void controlStatusGame(int playerID, ModelView modelView){
        println("Numero di giocatori totali della partita: " + modelView.getNumberOfPlayersGame());
        println("Partita in expertmode? " + String.valueOf(modelView.isExpertModeGame()));
    }
    */

    /**
     * This method is used to show the three characters cards available in the game.
     * It controls if the game is in Expert Mode before showing them.
     * @param modelView is the reference to the modelView.
     */
    public void showCharacterCardsInTheGame(ModelView modelView){
        if(modelView.isExpertModeGame() == true){
            println("The character cards in this game are: ");
            for(String s : modelView.getCharacterCardsInTheGame()){
                print(s + " ");
            }
            println(" ");
        }
    }

    /**
     * This method is used to show the player's SchoolBoard.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showSchoolboard(int playerID, ModelView modelView){
        println("Your schoolboard currently is: ");
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
     * This method is used to show to the player his students in the Dining Room of his board.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsInDiningRoomPlayer(int playerID, ModelView modelView){
        println("This is the diningroom of the player " + playerID);
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
            println("Professor of " + c + " " + modelView.getSchoolBoardPlayers().get(playerID).getProfessorTablePlayer().getOccupiedSeatsPlayer().get(c));
        }
        println(" ");
    }

    /**
     * This method is used to show to the player the number of his remainig towers in the Tower area of his schoolboard.
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
    public synchronized int choiceOfStudentsToMove(int playerID, ModelView modelView ) {
        String request;
        int studentChosen = - 1;                                                  //settato a -1 a causa del try catch, ma non verrà mai restituito -1
        boolean rightStudentChosen = false;                                       //viene settata a true solo se inserisco "character" oppure se inserisco uno studente valido
        println("Which student do you want to move from the entrance?");
        showStudentsInEntrancePlayer(playerID, modelView);
        request = scannerCLI.next();
        while(rightStudentChosen == false) {
            while (request.equals("show")) {                                      //gestiamo lo show direttamente nella cli, finchè chiede "show", invochiamo il metodo show e rimaniamo qui
                show(playerID, modelView);
                println("Which student do you want to move from the entrance?");
                showStudentsInEntrancePlayer(playerID, modelView);
                request = scannerCLI.next();
            }
            if (request.equals("character")) {                                    //se inserisce character, ritorniamo convenzionalmente -2 e gestiamo la scelta del
                studentChosen = -2;                                               //character nel net. handler
                rightStudentChosen = true;                                        //settiamo a true perchè vogliamo uscire dal while principale e, di fatto, dall'intero metodo
            } else {
                try {
                    studentChosen = Integer.parseInt(request);
                    if (studentChosen < 0 || studentChosen >= modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size()) {
                        println("This student id doesn't exist, please insert a valid student: ");
                        request = scannerCLI.next();
                    } else if (modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().get(studentChosen) == null) {
                        println("You already chose this student, please insert a valid one: ");
                        request = scannerCLI.next();
                    } else {
                        rightStudentChosen = true;                                  //settiamo a true solo se viene inserito uno studente valido
                    }
                } catch (NumberFormatException e) {                                 //se l'utente inserisce qualcosa che non va bene tipo "pluto", entriamo qui e di nuovo nel while principale
                    println("Wrong insert: please insert show/character or the student you want to move: ");
                    showStudentsInEntrancePlayer(playerID, modelView);
                    request = scannerCLI.next();
                }
            }

        }


        return studentChosen;
    }


    /**
     * This method is used to ask the player where he wants to move the student he chose.
     * @return the island ID chosen or -1 if the location chosen is 'diningRoom'.
     */
    public synchronized int choiceLocationToMove(int playerID, ModelView modelView) {
        println("Now you have to move the student you chose to the diningroom or on an island: where do you want to move him? ");
        int islandChosen;
        String request = scannerCLI.next();
        //String locationChosen = null;

        /*if (keyword.contains(str)) {
            additionalFunction(str, playerID, modelView, "choiceLocationToMove");
        }*/
        while(!(request.equals("diningroom") || request.equals("island") || request.equals("show") || request.equals("character"))){    //qui possono essere inserito solo queste stringhe, se non inserisce una di queste richiediamo
            println("Please insert a valid request: (diningroom/island/show/character");
            request = scannerCLI.next();
        }
        while (request.equals("show")) {                                      //gestiamo lo show direttamente nella cli, finchè chiede "show", invochiamo il metodo show e rimaniamo qui
            show(playerID, modelView);
            println("Where do you want to move the student you chose?");
            request = scannerCLI.next();
        }
        if(request.equals("character")) {                                    //se inserisce character, ritorniamo convenzionalmente -2 e gestiamo la scelta del
            islandChosen = -2;
        }else if (request.equals("island")) {                                    //se sceglie isola, chiedo su quale isola voglia muoverlo, se sceglie diningroom, ritorno -1 come specificato
            println(" ");
            println("On which island do you want to move your students? ");     //nel messaggio movedstudentsFromEntrance.

            for (int i = 0; i < 12; i++) {
                if (modelView.getIslandGame().get(i) != null) {
                    print(i + " ");
                }
            }
            println(" ");
            islandChosen = scannerCLI.nextInt();

            while (islandChosen < 0 || islandChosen >= 12) {
                println("Please insert a valid island ID: ");
                islandChosen = scannerCLI.nextInt();
            }
        } else {
           islandChosen = -1;                                           //significa che è stata scelta diningroom
        }
        return islandChosen;
    }

        /*while(!(str.equals("diningroom") || str.equals("islands") || str.equals("no") || str.equals("both"))){
            println("Please insert 'diningroom', 'islands', 'no' or 'both'");
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
        }*/
        /*println("Where do you want to move the student you chose? (diningroom/island)");
        String locationChosen = scannerCLI.nextLine();
        while(!(locationChosen.equals("diningroom") || (locationChosen.equals("island")))){
            println("Please insert 'diningroom' or 'island': ");
            locationChosen = scannerCLI.nextLine();
        }

        int islandChosen;

        if(locationChosen.equals("island")){                                    //se sceglie isola, chiedo su quale isola voglia muoverlo, se sceglie diningroom, ritorno -1 come specificato

            println(" ");
            println("On which island do you want to move your students? ");     //nel messaggio movedstudentsFromEntrance.

            for(int i = 0; i < 12; i++){        //TODO da fare con modelview
                print(i + " ");
            }
            println(" ");
            islandChosen = scannerCLI.nextInt();

            while(islandChosen < 0 || islandChosen >= 12){ //TODO da fare con modelview
                println("Please insert a valid island ID: ");
                islandChosen = scannerCLI.nextInt();
            }
        }else{

            return -1;
        }
        return islandChosen;

    } */
    /**
     * This methods is used by the client to view what he desires.
     * @param modelView is the reference to the modelView.
     */
    public void show(int playerID, ModelView modelView){

        ///INSERIRE TUTTE LE COSE E GLI IF DEI CASI VARI choiceOfStudentsToMove, ECC

        println("What do you want to view? Insert diningroom/islands/both/professortable/characters/clouds/nothing");  //TODO EVENTUALMENTE STAMPARE ANCHE LE MONETE E LE NUVOLE
        String str = scannerCLI.next();

        while(!(str.equals("diningroom") || str.equals("islands") ||  str.equals("both") || str.equals("professortable") ||str.equals("characters") || str.equals("nothing") ||str.equals("clouds"))){   //possono essere viste queste cose
            println("Please insert one of the following: diningroom/islands/both/professortable/characters/nothing");
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
        }else if(str.equals("professortable")){
            for( int i = 0; i<= modelView.getNumberOfPlayersGame()-1; i++) {
                showProfessorTablePlayer(i, modelView);
            }
        }else if(str.equals("characters")){
            for(String s : modelView.getCharacterCardsInTheGame()){
                print(s + " ");
            }
            println(" ");
        }else if(str.equals("clouds")){
            showClouds(modelView);
            println(" ");
        }

    }
    /*
    public void callFromCheck(int playerID, ModelView modelView, String callFrom){
        if(callFrom.equals("choiceOfStudentsToMove")){
            choiceOfStudentsToMove(playerID, modelView);
        }else if(callFrom.equals("choiceLocationToMove")){
            choiceLocationToMove(playerID, modelView);
        }
    } */

    /**
     * This method is used to ask the player on which island he wants to move mother nature.
     * @param motherNatureIslandID is the ID of the island where mother nature currently stands.
     * @param modelView is the reference to the modelView.
     * @return
     */
    public int choiceMotherNatureMovement(int playerID, int motherNatureIslandID, ModelView modelView){
        println("Now you have to move mother nature, which currently is on Island " + motherNatureIslandID);
        println("You can move mother nature up to: " + modelView.getAssistantCardsValuesPlayer().get(modelView.getLastAssistantChosen()) + " seat(s) clockwise ");
        println("Insert the island ID where you want to move her:  ");
        boolean rightIslandChosen = false;                              //viene settato a true solo se inserisce character o un'isola valida
        int chosenIslandID = -1;
        String request;
        request = scannerCLI.next();
        while(rightIslandChosen == false) {
            while (request.equals("show")) {
                show(playerID, modelView);
                println("Insert the island ID where you want to move mother nature: ");
                request = scannerCLI.next();
            }
            if (request.equals("character")) {                                    //se inserisce character, ritorniamo convenzionalmente -2 e gestiamo la scelta del
                chosenIslandID = -2;                                             //character nel net. handler
                rightIslandChosen = true;                                       //settiamo a true perchè vogliamo uscire dal while e quindi dal metodo
            } else {
                try {
                    chosenIslandID = Integer.parseInt(request);
                    if(chosenIslandID < 0 || chosenIslandID >= 12 || modelView.getIslandGame().get(chosenIslandID) == null){
                        println("This island ID doesn't exist, please insert a valid one: ");
                        request = scannerCLI.next();
                    }else{
                        rightIslandChosen = true;                       //settiamo a tru se viene inserita un'isola valida
                    }
                }catch (NumberFormatException e){
                    println("wrong insert: please insert show/character or the island ID where you want to move mother nature:");
                    request = scannerCLI.next();
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
     * @param modelView is the reference to the modelView.
     * @param islandLanding is the island ID where mother nature lands after action_2, so it is the island to which the other one(s) unify.
     * @param islandToUnifyFlag is the flag to understand which island have been unified (the previous (-1), the following (+1) or both (0).
     */
    public void showUnion(ModelView modelView, int islandLanding, int islandToUnifyFlag){
        int otherIsland = islandLanding;
        if(islandToUnifyFlag == -1) {
            otherIsland -= 1;
            println("The chosen island ("+ islandLanding +") has been unified with the previous island (" + otherIsland +").");
        }else if(islandToUnifyFlag == 1){
            otherIsland += 1;
            println("The chosen island ("+ islandLanding +") has been unified with the following island (" + otherIsland +").");
        }else if(islandToUnifyFlag == 0){
            otherIsland -= 1;
            int secondIsland = islandLanding + 1;
            println("The chosen island ("+ islandLanding +") has been unified with the previous and the following islands (" + otherIsland + ", " + secondIsland +").");
        }
    }


    /**
     * This method is used to show the initial situation on the islands, including mother nature position.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsOnIslands(ModelView modelView){
        println("The ISLAND SITUATION is:");
        for(int i = 0; i < 12; i++){                                                    //TODO da fare con modelview
            if(modelView.getIslandGame().get(i) != null) {
                print("Students on the island " + i + ": ");
                if (modelView.getIslandGame().get(i).getTotalNumberOfStudents() == 0) {           //se il numero di studenti su quell'isola è zero (all'inizio solo se è l'isola di madre natura oppure è l'isola opposta)
                    if (modelView.getIslandGame().get(i).isMotherNaturePresence()) {
                        print("nobody, here stands mother nature.");
                    } else {
                        print("nobody");
                    }

                }
                for (Creature c : Creature.values()) {
                    if (modelView.getIslandGame().get(i).getStudentsOfType(c) != 0) {
                        print(c + ": " + modelView.getIslandGame().get(i).getStudentsOfType(c) + "; ");
                    }
                }
                println(" ");
            }
        }
        println(" ");
    }

    /**
     * This method is used to invoke the real methods showing the islands situation, which means the number of student on each one.
     * @param modelView is the reference to the modelView.
     */
    public void showIslandsSituation(ModelView modelView) {
        showStudentsOnIslands(modelView);
        showMotherNaturePosition(modelView);
    }

    /*
    public void showNewStudentOnIsland(ModelView modelView){

        for (int k = 0; k < 12; k++) {      //TODO da fare con modelview
            println("Island " + k + ": ");
            for (Creature c : Creature.values()) {
                println("Student " + c + " " + modelView.getIslandGame().get(k).getStudentsOfType(c));
            }
        }
    } */

    /**
     * This method is used to show the mother nature position.
     * @param modelView is the reference to the modelView.
     */
    public void showMotherNaturePosition(ModelView modelView){
        for(int i = 0; i < 12; i++) {           //TODO da fare con modelview
            if (modelView.getIslandGame().get(i) != null) {
                if (modelView.getIslandGame().get(i).isMotherNaturePresence() == true) {
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
     * This method is used to show to the player the clouds situation.
     * @param modelView is the reference to the modelView.
     */
    public void showClouds(ModelView modelView){
        println("The Clouds situation is: ");
        if(modelView.getNumberOfPlayersGame() == 2){
            print("Cloud 0: ");
            for (int i = 0; i < 3; i++) {
                print(modelView.getStudentsOnClouds().get(i).toString() + " ");
            }
            println(" ");
            print("Cloud 1: ");
            for (int i = 3; i < 6; i++) {
                print(modelView.getStudentsOnClouds().get(i).toString() + " ");
            }
            println(" ");
        }else if(modelView.getNumberOfPlayersGame() == 3) {
            print("Cloud 0: ");
            for (int i = 0; i < 4; i++) {
                print(modelView.getStudentsOnClouds().get(i).toString() + " ");
            }
            println(" ");
            print("Cloud 1: ");
            for (int i = 4; i < 8; i++) {
                print(modelView.getStudentsOnClouds().get(i).toString() + " ");
            }
            println(" ");
            print("Cloud 2: ");
            for (int i = 8; i < 12; i++) {
                print(modelView.getStudentsOnClouds().get(i).toString() + " ");
            }
            println(" ");
        }

    }

    /**
     * This method is used to ask the player which cloud he wants to take the student from.
     * @param modelView is the reference to the modelView.
     * @return the cloud ID chosen.
     */
    public int chooseCloud(int playerID, ModelView modelView){
        int cloudChosen = -1;                               //setto a -1 per il try catch ma non restituirà mai -1
        String request;
        showClouds(modelView);
        boolean rightCloudChosen = false;
        println("Which cloud do you want to take the students from? They will be moved to your entrance: ");
        request = scannerCLI.next();
        while(rightCloudChosen == false) {
            while (request.equals("show")) {                                      //gestiamo lo show direttamente nella cli, finchè chiede show, rimaniamo qui
                show(playerID, modelView);
                println("Which cloud do you want to take the students from? ");
                request = scannerCLI.next();
            }

            if (request.equals("character")) {                                    //se inserisce character, ritorniamo convenzionalmente -2 e gestiamo la scelta del
                cloudChosen = -2;                                             //character nel net. handler
                rightCloudChosen = true;
            } else {
                try {
                    cloudChosen = Integer.parseInt(request);
                    int cloudNumber = modelView.getNumberOfPlayersGame();
                    if(cloudChosen < 0 || cloudChosen >= cloudNumber ) {
                        println("This cloud ID doesn't exist, please select a valid cloud ID: ");
                        request = scannerCLI.next();
                    }else{
                        rightCloudChosen = true;
                    }
                }catch (NumberFormatException e){
                    println("Wrong insert: please insert show/character or the cloud ID you want to take the students from: ");
                    request = scannerCLI.next();
                }
            }
        }


        return  cloudChosen;
    }

    /**
     * This method is used to show to the player that he chose an invalid Cloud ID to take the students from.
     * @param modelView is the reference to the modelView.
     * @return the new chosen cloud ID.
     */
    public int invalidCloudSelection(int playerdID, ModelView modelView){
        println("Invalid Cloud ID: this cloud has already been chosen. Please select a new one: ");
        int chosenCloud = chooseCloud(playerdID, modelView);
        return chosenCloud;
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
        println(" ");
        if(playerIDwinner == playerID){
            println("YOU ARE THE WINNER! CONGRATULATIONS!! ");
        }else {
            println("You lost this game, the winner is " + playerIDwinner +": " + winnerNickname);
        }
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

    public void additionalFunction(String strTmp, int playerID, ModelView modelView, String callFrom){
        if(strTmp.equals("character")){
            if(modelView.isExpertModeGame()) {
                characterChoice(modelView);
            }else{
                println("You are not playing in expert mode, you can't use a character. ");
            }
        }else if(strTmp.equals("show") ){
            show(playerID, modelView);
        }
    }

    /**
     * This method is used by the client when he wants to use one of the character of his match.
     * @param modelView is the reference to the modelView.
     */
    public String characterChoice(ModelView modelView){ //, String callFrom
        println("Which character do you want to use? ");
        for(String s : modelView.getCharacterCardsInTheGame()){
            print(s + " ");
        }
        println(" ");
        println("Insert one of the character: ");
        String characterChosen = scannerCLI.next();

        while(!(modelView.getCharacterCardsInTheGame().contains(characterChosen))){
            println("Please insert a right character: ");
            for(String s : modelView.getCharacterCardsInTheGame()){
                print(s + " ");
            }
            println(" ");
            characterChosen = scannerCLI.next();
        }
        println(" ");
        println("You choose to use: " + characterChosen);

        return  characterChosen;
        //networkHandler.sendRequestCharacterMessage(characterChosen, callFrom);

    }

    /**
     * This method is used by the client when he uses the Monk character.
     * @param modelView is the reference to the modelView.
     * @return the id of the chosen student.
     */
    public int choiceStudentMonk(ModelView modelView) {
        int studentChosen;
        println("Which student do you want to move from this card? (0-3)");
        for (Creature c : modelView.getCharactersDataView().getMonkStudents()) {
            print(c + " ");
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
     * @param modelview is the reference to the modelView.
     * @return the id of the chosen island.
     */
    public int choiceIslandMonk(ModelView modelview){
        int islandChosen;
        println("On which island do you want to move the student you chose?");
        showStudentsOnIslands(modelview);
        islandChosen = scannerCLI.nextInt();
        while(islandChosen < 0 || islandChosen > 11 || modelview.getIslandGame().get(islandChosen)==null){
            println("Please insert a valid island: ");
            showStudentsOnIslands(modelview);
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
        int maxNumberOfStudents = 0;
        println("Which student do you want to move from the entrance? ");
        showStudentsInEntrancePlayer(playerID, modelView);
        while(maxNumberOfStudents < 3) {
            studentChosen = scannerCLI.nextInt();
            while (studentChosen < 0 || studentChosen > modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size()) {
                println("Please insert a valid student: ");
                studentChosen = scannerCLI.nextInt();
            }
            studentsFromEntranceJester.add(studentChosen);
            maxNumberOfStudents ++;
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
        }
        println(" ");
        while(maxNumberOfStudents < 3) {
            studentChosen = scannerCLI.nextInt();
            while (studentChosen < 0 || studentChosen > modelView.getCharactersDataView().getJesterStudents().size()) {
                println("Please insert a valid student: ");
                studentChosen = scannerCLI.nextInt();
            }
            studentsFromCardJester.add(studentChosen);
            maxNumberOfStudents ++;

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

        while(islandIDChosenByClient < 0 || islandIDChosenByClient > 11){
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

        Creature chosenStudentByClient = Creature.valueOf(chosenStudentByClientStr);
        return chosenStudentByClient;
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
        println("Which student do you want to move from the entrance? ");
        showStudentsInEntrancePlayer(playerID, modelView);
        while(maxNumberOfStudents < 2) {
            studentChosen = scannerCLI.nextInt();
            while (studentChosen < 0 || studentChosen > modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size()) {
                println("Please insert a valid student: ");
                studentChosen = scannerCLI.nextInt();
            }
            studentsFromEntranceBard.add(studentChosen);
            maxNumberOfStudents ++;
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
        int dragon = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.DRAGON);
        int unicorn = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.UNICORN);
        int fairy = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.FAIRY);
        int gnome = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.GNOME);
        int frog = modelView.getSchoolBoardPlayers().get(playerID).getDiningRoomPlayer().getOccupiedSeatsPlayer().get(Creature.FROG);

        boolean rightStudentChoice = false;

        String studentChosen;
        int maxNumberOfStudents = 0;
        println("Which student do you want to move from the dining room? ");
        showStudentsInDiningRoomPlayer(playerID, modelView);
        while(maxNumberOfStudents < 2) {
            studentChosen = scannerCLI.next();
            while (!rightStudentChoice){
                if(!(studentChosen.equals("DRAGON") || studentChosen.equals("UNICORN") ||studentChosen.equals("FROG") || studentChosen.equals("GNOME") || studentChosen.equals("FAIRY"))) {
                    println("Please insert a valid student: (DRAGON/UNICORN/FROG/GNOME/FAIRY");
                    studentChosen = scannerCLI.next();
                }else if(studentChosen.equals("DRAGON")){
                    if(dragon == 0){
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    }else{
                        dragon -= 1;
                        rightStudentChoice = true;
                    }
                }else if(studentChosen.equals("UNICORN")) {
                    if (unicorn == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        unicorn -= 1;
                        rightStudentChoice = true;
                    }
                }else if(studentChosen.equals("FAIRY")) {
                    if (fairy == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        fairy -= 1;
                        rightStudentChoice = true;
                    }
                }else if(studentChosen.equals("FROG")) {
                    if (frog == 0) {
                        println("There are not enough of that type, choose another one.");
                        studentChosen = scannerCLI.next();
                    } else {
                        frog -= 1;
                        rightStudentChoice = true;
                    }
                }else if(studentChosen.equals("GNOME")) {
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
            maxNumberOfStudents ++;
            rightStudentChoice = false;
        }
        return studentsFromDiningRoom;
    }

    /**
     * This method is used by the client when he uses the Trafficker character card.
     * @return the Creature chosen.
     */
    public Creature choiceTrafficker(){
        ArrayList <String> availableStudents = new ArrayList<>();
        println("Which student do you choose? ");
        for(Creature c : Creature.values()){
            print(c.toString());
            availableStudents.add(c.toString());
        }
        println(" ");
        String chosenStudentByClientStr = scannerCLI.next();
        while(!availableStudents.contains(chosenStudentByClientStr)){
            println("Not a valid student, please choose a valid one: ");
            chosenStudentByClientStr = scannerCLI.next();
        }

        Creature chosenStudentByClient = Creature.valueOf(chosenStudentByClientStr);
        return chosenStudentByClient;
    }

    /**
     * This method is used by the client when he uses the Princess character card.
     * @param studentsOnCard is the list of students on the Princess character card.
     * @return  the student ID chosen;
     */
    public int choicePrincess(ArrayList<Creature> studentsOnCard){
        println("Which student do you choose? You will move it from the card to your Dining Room");
        int i = 0;
        for(Creature c : studentsOnCard){
            print(i + ": " + c + " ");
            i++;
        }
        println(" ");

        int chosenStudentIDByClient = scannerCLI.nextInt();
        while ((chosenStudentIDByClient < 0 || chosenStudentIDByClient > 3 )){
            println("Not a valid student ID, please choose a valid one: ");
            chosenStudentIDByClient = scannerCLI.nextInt();
        }

        return chosenStudentIDByClient;
    }

    /**
     * This method used to notify the player he can't use the herbalist character card.
     * @param herbalistNackExplanation is the reason why he can't.
     */
    public void invalidHerbalistChoice(String herbalistNackExplanation){
        println(herbalistNackExplanation);

    }

    /**
     * This method is used to notify the player he can't use a certain character.
     * @param invalidCharacterNackExplanation is the reason why he can't.
     */
    public void invalidCharacter(String invalidCharacterNackExplanation){
        println(invalidCharacterNackExplanation);
    }

    /**
     * This method is used to ask the player if he wants to use another character card.
     * @param modelView is the reference to the modelView.
     */
    public void choiceAnotherCharacter(ModelView modelView){
        String str;
        println("Do you want to choose another character? y/n ");
        str = scannerCLI.next();
        while(!(str.equals("y") || str.equals("n"))){
            println("Please insert 'y' or 'n'");
            str = scannerCLI.next();
        }
        if(str.equals("y")){
            //characterChoice(modelView, "anotherChoice");
        }
    }

    /**
     * This method is used to notify the players that a certain character card has been successfully used.
     * @param characterUsed is the character used.
     */
    public void characterConfirm(String characterUsed){
        println(characterUsed + " used correctly!");

    }




    public void print(String strToPrint){
        System.out.print(strToPrint);
    }

    public void println(String strToPrint){
        System.out.println(strToPrint);
    }

    public void printInt(int intToPrint){
        System.out.println(intToPrint);
    }


}