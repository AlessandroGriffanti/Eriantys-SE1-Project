package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.network.Client.NetworkHandler;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class CLI {
    private NetworkHandler networkHandler;
    Scanner scannerCLI = new Scanner(System.in);


    public CLI(String ip, int port) throws IOException {
        NetworkHandler networkHandler = new NetworkHandler(ip, port, this);
        networkHandler.startClient();
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

    public String loginNickname(){
        println("Insert nickname: ");
        String nickNamePlayer = scannerCLI.nextLine();
        println("ok");

        return nickNamePlayer;
    }

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

    public void loginSuccess(){
        println("login success");
    }

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

    public void nicknameNotAvailable (){
        println("Nickname not available, insert a new nickname: ");
    }

    public void lobbyNotAvailable(){
        println("No lobby available, creating a new one...");
    }

    public void errorObject () {
        println("Error with the object of the message. ");
    }

    public void ackWaiting () { println("Waiting for match to start... :/ "); }

    public void startAlert () {
        println("\n - - - GAME IS STARTED !!! - - - \n");
    }

    public void turnWaiting () {
        println("Wait for your turn!");
    }

    public void isYourTurn() {
        println("Is your Turn! Make your choice: ");
    }

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

    public synchronized String towerChoiceNext (boolean blackAvailability, boolean greyAvailability, boolean whiteAvailability) {
        System.out.println("B: " + blackAvailability);
        System.out.println("W: " + whiteAvailability);
        System.out.println("G: " + greyAvailability);

        String strTowerColorChosenNext = null;
        boolean flag = true;
        
        println("Choose the COLOR of your tower: ");
        if (blackAvailability) {
            print("BLACK ");
        }
        if (greyAvailability) {
            print("GREY ");
        }
        if (whiteAvailability) {
            print("WHITE ");
        }
        println(" ");

        strTowerColorChosenNext = scannerCLI.next();
        println(strTowerColorChosenNext);


        while( !( strTowerColorChosenNext.equals("BLACK") || strTowerColorChosenNext.equals("WHITE") || strTowerColorChosenNext.equals("GREY") ) ){
            if(!blackAvailability){
                println("BLACK Towers not available. Please, insert the right color: ");
            }
            if(!greyAvailability){
                println("GREY Towers not available. Please, insert the right color: ");
            }
            if(whiteAvailability){
                println("WHITE Towers not available. Please, insert the right color: ");
            } {
                println("Please, insert the right color: ");
            }
            strTowerColorChosenNext = scannerCLI.next();
        }

        return strTowerColorChosenNext;
    }

}