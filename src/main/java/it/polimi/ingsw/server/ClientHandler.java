package it.polimi.ingsw.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/** this class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * it should deserialize the json received and pass the information to the controller
 */
public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Server server;

    /** input and output stream */
    private PrintWriter outputHandler;
    private BufferedReader inputHandler;

    public ClientHandler(Socket socket){
        this.clientSocket = socket;
    }
    @Override
    public void run(){
        try {
            outputHandler = new PrintWriter(clientSocket.getOutputStream());
            inputHandler = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String outputFromServer = inputHandler.readLine();

            System.out.println("nickreceived: " + outputFromServer); //ricevuto dal client

            outputHandler.println(outputFromServer + "ok\n");

            outputHandler.flush();

            System.out.println("sent ok");

            outputHandler.close();
            inputHandler.close();
            clientSocket.close();
            System.out.println("disconnected");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }


    }

    /** when a login is successful we register the player's nickname in the arraylist in the server */
    public void loginSuccess(){

      //  server.getPlayersNicknames().add();
    }
}
