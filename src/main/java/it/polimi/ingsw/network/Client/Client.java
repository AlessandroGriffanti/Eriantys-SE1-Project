package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import it.polimi.ingsw.network.messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket = null;
    private Gson gsonObj = new Gson();
    private BufferedReader inputBufferClient = null;
    private PrintWriter outputPrintClient = null;


    public static void main(String[] args) throws IOException {
        try{
            Client client = new Client();
            client.startClient();

        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    /**
     * This startClient method is called in the main method.
     * It starts the socket of client execution, and prepare it to be ready for a "conversation" with the server.
     * The first message is the login one: login().
     * @throws IOException
     */
    public void startClient() throws IOException{
        clientSocket = new Socket("localhost", 4444);

        inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputPrintClient = new PrintWriter(clientSocket.getOutputStream());

        //per prima cosa il client avviato fa un login sul server
        loginFromClient();



        String msgFromServer = inputBufferClient.readLine();
        System.out.println("Still connected");
        System.out.println(msgFromServer);


        inputBufferClient.close();
        outputPrintClient.close();
        clientSocket.close();
    }


    /**
     * This method is used by client (player) to access the game:
     * player's nickname is asked and then sent to the server in json format
     */
    public void loginFromClient() {
        Scanner loginScanner = new Scanner(System.in);
        System.out.println("Insert nickname: ");
        //String nickNamePlayer = loginScanner.nextLine();
        LoginMessage msgLogin = new LoginMessage(loginScanner.nextLine());

        System.out.println("ok");
        outputPrintClient.println(gsonObj.toJson(msgLogin));
        outputPrintClient.flush();
        System.out.println("sent");
    }
}

