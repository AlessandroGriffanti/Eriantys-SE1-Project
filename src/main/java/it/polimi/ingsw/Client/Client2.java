package it.polimi.ingsw.Client;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {


    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        BufferedReader inputBufferClient = null;
        PrintWriter outputPrintClient = null;
        Gson gsonObj = new Gson();

        try{
            clientSocket = new Socket("localhost", 4444);
            inputBufferClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputPrintClient = new PrintWriter(clientSocket.getOutputStream());


            System.out.println("Write your nickname: " );
            String nickWritten;
            Scanner input = new Scanner(System.in);
            nickWritten = input.next();
            outputPrintClient.println(nickWritten);
            outputPrintClient.flush();



            String msgFromServer = inputBufferClient.readLine();
            System.out.println("Still connected");
            System.out.println(msgFromServer);

            while(true){

            }

           /* inputBufferClient.close();
            outputPrintClient.close();
            clientSocket.close(); */

        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
