package it.polimi.ingsw.Client;


import com.google.gson.Gson;

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

    public void startClient() throws IOException{
        clientSocket = new Socket("localhost", 4444);

        inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputPrintClient = new PrintWriter(clientSocket.getOutputStream());


        login();



        String msgFromServer = inputBufferClient.readLine();
        System.out.println("Still connected");
        System.out.println(msgFromServer);


        inputBufferClient.close();
        outputPrintClient.close();
        clientSocket.close();

    }
    public static void main(String[] args) throws IOException {
        try{
            Client client = new Client();
            client.startClient();

        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void login() {
        Scanner loginScanner = new Scanner(System.in);
        System.out.println("Insert nickname: ");
        String nickNamePlayer = loginScanner.nextLine();
        outputPrintClient.println(gsonObj.toJson(nickNamePlayer));
        outputPrintClient.flush();
    }
}
