package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.Client.NetworkHandler;
import it.polimi.ingsw.network.messages.clientMessages.MatchSpecsMessage;

import java.io.IOException;
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
            System.out.println("Insert server ip address: ");
            String ip = new Scanner(System.in).next();
            System.out.println("Insert server port: ");
            int port = new Scanner(System.in).nextInt();
            CLI cli = new CLI(ip, port);

        } catch (InputMismatchException e){
            System.out.println("Integer requested for the server port, restart the application. ");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Server no longer available :(  " + e.getMessage());
        }



    }

    public String loginNickname(){
        print("Insert nickname: ");
        String nickNamePlayer = scannerCLI.nextLine();
        print("ok");

        return nickNamePlayer;
    }

    public boolean newMatchBoolean(){
        print("do you wat to create a new match? (y/n) ");
        String newMatchStr = scannerCLI.nextLine();

        while ( !( (newMatchStr.equals("y")) || (newMatchStr.equals("n")))){
            print("please insert y if you want to create a new match: (y/n) ");
            newMatchStr = scannerCLI.nextLine();
        }
        print("ok");

        if(newMatchStr.equals("y")){
            return true;
        }else {
            return false;
        }
    }

    public void loginSuccess(){
        print("login success");
    }

    public int numberOfPlayer(){
        print("Please insert the number of the player you want in the lobby: ");

        int numberOfPlayerInTheLobby = scannerCLI.nextInt();
        printInt(numberOfPlayerInTheLobby);
        while( (numberOfPlayerInTheLobby <= 1) || (numberOfPlayerInTheLobby >= 4) ){
            print("Please, insert a valid number of players");
            numberOfPlayerInTheLobby = scannerCLI.nextInt();
        }

        return numberOfPlayerInTheLobby;
    }

    public boolean expertModeSelection(){
        print("Do you want to play in expert mode? (y/n)");
        String expertMode = scannerCLI.nextLine();

        while(!(expertMode.equals("y") || expertMode.equals("n"))){
            print("Please, insert y or n: ");
            expertMode = scannerCLI.nextLine();
        }

        if(expertMode.equals("y")) {
            return true;
        }else{
            return false;
        }
    }

    public int lobbyToChoose(ArrayList<Boolean> arrayLobby){
        print("Choose a Lobby. \n");

        print("ERIANTYS LOBBIES: ");
        for (int i = 0; i < arrayLobby.size(); i++) {
            if(arrayLobby.get(i) == true){
                print( i + "Lobby full. ");
            }else {
                print( i + "Lobby available! ");
            }
        }
        int lobbyIDchosenByPlayer = scannerCLI.nextInt();
        while(arrayLobby.get(lobbyIDchosenByPlayer) == true){
            print("You can't join this lobby, match selected already started. Select another one. \n");

            print("ERIANTYS LOBBIES: ");
            for (int i = 0; i < arrayLobby.size(); i++) {
                if(arrayLobby.get(i) == true){
                    print( i + "Lobby full. ");
                }else {
                    print( i + "Lobby available! ");
                }
            }
            lobbyIDchosenByPlayer = scannerCLI.nextInt();
        }
        return lobbyIDchosenByPlayer;
    }

    public void nicknameNotAvailable (){
        print("Nickname not available, insert a new nickname: ");
    }

    public void lobbyNotAvailable(){
        print("No lobby available, creating a new one...");
    }

    public void errorObject () {
        print("Error with the object of the message. ");
    }

}