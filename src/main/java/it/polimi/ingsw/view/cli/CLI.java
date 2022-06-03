package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.Client.NetworkHandler;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

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
    public CLI(String ip, int port) throws IOException {
        NetworkHandler networkHandler = new NetworkHandler(ip, port, this);
        networkHandler.startClient();
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
            println("please insert y if you want to create a new match: (y/n) ");
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
        println("login success");
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
    public int lobbyToChoose(ArrayList<Boolean> arrayLobby){
        println("Choose a Lobby. \n");

        println("ERIANTYS LOBBIES: ");
        for (int i = 0; i < arrayLobby.size(); i++) {
            if(arrayLobby.get(i) == true){
                println("Lobby " + i + " full. :( ");
            }else {
                println("Lobby " + i + " available! :) ");
            }
        }
        int lobbyIDchosenByPlayer = scannerCLI.nextInt();
        while(arrayLobby.get(lobbyIDchosenByPlayer) == true){
            println("You can't join this lobby, match selected already started. Select another one. \n");

            println("ERIANTYS LOBBIES: ");
            for (int i = 0; i < arrayLobby.size(); i++) {
                if(arrayLobby.get(i) == true){
                    println("Lobby " + i + " full. :( ");
                }else {
                    println("Lobby " + i + " available! :) ");
                }
            }
            lobbyIDchosenByPlayer = scannerCLI.nextInt();
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
    public Tower towerChoice () {
        println("Choose the COLOR of your tower: ");
        println("BLACK , WHITE, GREY");
        String strTowerColorChosen = scannerCLI.nextLine();

        while( !( strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") || strTowerColorChosen.equals("GREY") ) ){
            println("Please, insert the right color: ");
            strTowerColorChosen = scannerCLI.nextLine();
        }

        return Tower.valueOf(strTowerColorChosen);
    }

    /**
     * This method is used to ask the following players which color they want their towers to be.
     * @param notAvailableTowerColors is the list of  colors that have already been chosen.
     * @return the color chosen.
     */
    public synchronized Tower towerChoiceNext (ArrayList<Tower> notAvailableTowerColors) {
        println("Choose your tower color: BLACK, WHITE, GREY ");
        String strTowerColorChosen = scannerCLI.nextLine();

        while(!(strTowerColorChosen.equals("BLACK") || strTowerColorChosen.equals("WHITE") || strTowerColorChosen.equals("GREY"))){
            println("Please insert a valid color tower: ");
            strTowerColorChosen = scannerCLI.nextLine();
        }
        while(notAvailableTowerColors.contains(Tower.valueOf(strTowerColorChosen))){
            println("This color has already been selected, please choose another one: ");
            strTowerColorChosen = scannerCLI.nextLine();
        }

        return Tower.valueOf(strTowerColorChosen);
    }


    /**
     * This method is used to ask the player which deck he wants to play with.
     * @return the deck chosen in Wizard type.
     */
    public synchronized Wizard deckChoice(){
        println("Please choose your deck among the following: ");
        println("FORESTWIZARD, DESERTWIZARD, CLOUDWITCH, LIGHTNINGWIZARD");

        String strDeckChosen = scannerCLI.nextLine();

        while(!( strDeckChosen.equals("FORESTWIZARD") || strDeckChosen.equals("DESERTWIZARD") || strDeckChosen.equals("CLOUDWITCH") || strDeckChosen.equals("LIGHTNINGWIZARD")) ){
            println("Please insert a correct deck: ");
            strDeckChosen = scannerCLI.nextLine();
        }

        return Wizard.valueOf(strDeckChosen);
    }

    /**
     * This method is used to ask the following players which deck they want to use.
     * @param notAvailableDecks is the list of decks that have been already chosen.
     * @return the deck chosen.
     */
    public synchronized Wizard deckChoiceNext(ArrayList<Wizard> notAvailableDecks) {
        println("Choose your deck among the following: ");
        println("FORESTWIZARD, DESERTWIZARD, CLOUDWITCH, LIGHTNINGWIZARD");

        String strDeckChosen = scannerCLI.nextLine();

        while(!(strDeckChosen.equals("FORESTWIZARD") || strDeckChosen.equals("DESERTWIZARD") || strDeckChosen.equals("CLOUDWITCH") || strDeckChosen.equals("LIGHTNINGWIZARD"))){
            println("Please, insert a correct deck: ");
            strDeckChosen = scannerCLI.nextLine();
        }
        while(notAvailableDecks.contains(Wizard.valueOf(strDeckChosen))){
            println("This deck has already been chosen, plase select another one: ");
            strDeckChosen = scannerCLI.nextLine();
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
        println("Which assistant do you want to play?");
        for(Integer i : availableAssistantCard.keySet()){
            print(i + " ");
        }
        print("\n");
        int assistantChosen = scannerCLI.nextInt();
        while(!(availableAssistantCard.keySet().contains(assistantChosen))){
            println("You can't use this assistant, please select another one from this list: ");
            for(Integer i : availableAssistantCard.keySet()){
                print(i + " ");
            }
            assistantChosen = scannerCLI.nextInt();
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
        println("Which assistant do you want to play?");
        for(Integer i : availableAssistantCard.keySet()){
            print(i + " ");
        }
        print("\n");
        int assistantChosen = scannerCLI.nextInt();
        while(assistantCardsAlreadyUsedThisRound.contains(assistantChosen)){
            println("You can't use this assistant, it has already been used this round by another player. Please select another one");
            assistantChosen = scannerCLI.nextInt();
        }
        return  assistantChosen;
    }

    /**
     * This method is used to show to the player his students at the beginning of the match, after receiving the MatchStartMessage.
     * and the character cards available for the game, if the game is in expert mode.
     * @param playerID is the player id, used to get the corresponding SchoolBoardView.
     * @param modelView is the reference to the modelView.
     */
    public synchronized void showStudentsInEntrancePlayer(int playerID, ModelView modelView){
        println("Your students in the entrance at the beginning of the game are: ");
        for(Creature c : modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer()){
            print(c + " ");
        }
        print("\n");

        if(modelView.isExpertModeGame() == true){
            println("The character cards in this game are: ");
            for(String s : modelView.getCharacterCardsInTheGame()){
                print(s + " ");
            }
            print("\n");
        }
        println("Numero di giocatori totali della partita: " + modelView.getNumberOfPlayersGame());
        println("Partita in expertmode? " + String.valueOf(modelView.isExpertModeGame()));
    }

    /**
     * This method is used to ask the player which students he wants to move from the entrance.
     * @param modelView is the reference to the modelView.
     */
    public synchronized int choiceOfStudentsToMove(int playerID, ModelView modelView ){
        println("Which student do you want to move from the entrance? ");
        int studentChosen = scannerCLI.nextInt();                         //lo studente è rappresentato da un intero come si vede nel messaggio di MovedStudentsFromEntrance
        while(studentChosen < 0 || studentChosen >= modelView.getSchoolBoardPlayers().get(playerID).getEntrancePlayer().getStudentsInTheEntrancePlayer().size()){
            println("Plase insert a valid student: ");
            studentChosen = scannerCLI.nextInt();
        }
        return studentChosen;
    }

    /**
     * This method is used to ask the player where he wants to move the student he chose.
     * @param modelView is the reference to the modelView.
     * @return the island ID chosen or -1 if the location chosen is 'diningRoom'.
     */
    public synchronized int choiceLocationToMove(ModelView modelView){
        println("Where do you want to move the student you chose? diningroom/island");
        String locationChosen = scannerCLI.nextLine();
        while(!(locationChosen.equals("diningroom") || (locationChosen.equals("island")))){
            println("Please insert 'diningroom' or 'island': ");
            locationChosen = scannerCLI.nextLine();
        }
        int islandChosen;
        if(locationChosen.equals("island")){                                    //se sceglie isola, chiedo su quale isola voglia muoverlo, se sceglie diningroom, ritorno -1 come specificato
            println("On which island do you want to move your students? ");     //nel messaggio movedstudentsFromEntrance.
            for(int i = 0; i < 12; i++){
                print(i + " ");
            }
            print("\n");
            islandChosen = scannerCLI.nextInt();
            while(islandChosen < 0 || islandChosen >= 12){
                println("Please insert a valid island ID: ");
                islandChosen = scannerCLI.nextInt();
            }
        }else{
            return -1;
        }
        return islandChosen;

    }

    /**
     * This method is used to show the initial situation on the islands, including mother nature position.
     * @param modelView is the reference to the modelView.
     */
    public void showStudentsOnIslands(ModelView modelView){
        for(int i = 0; i < 12; i++){
            if(modelView.getIslandGame().get(i).getTotalNumberOfStudents() == 0){           //se il numero di studenti su quell'isola è zero (all'inizio solo se è l'isola di madre natura oppure è l'isola opposta)
                if(modelView.getIslandGame().get(i).isMotherNaturePresence()){
                    print("Studenti sull'isola " + i +": nessuno, qui c'è madre natura.");
                }else {
                    print("Studenti sull'isola " + i + ": nessuno");
                }
            }
            for(Creature c : Creature.values()) {
                if(modelView.getIslandGame().get(i).getStudentsOfType(c) != 0){
                    print("Studenti sull'isola " + i + " : " + c + ": " + modelView.getIslandGame().get(i).getStudentsOfType(c));
                }
            }
            print("\n");
        }
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