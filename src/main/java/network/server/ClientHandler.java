package network.server;

//import network.messages.message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** this class is the one really dealing with the client connected, it communicates with the client-slide socket.
 * it should deserialize the json received and pass the information to the controller
 */
public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Server server;

    /** input and output stream */
    private PrintWriter outputHandler;
    private BufferedReader inputHandler;

    /** Gson object "gsonObj" to deserialize the json message received */
    private Gson gsonObj = new Gson();

    public ClientHandler(Socket socket){
        this.clientSocket = socket;
    }
    @Override
    public void run(){
        try {
            outputHandler = new PrintWriter(clientSocket.getOutputStream());
            inputHandler = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            /*
            String outputFromServer = inputHandler.readLine();
            System.out.println("nickreceived: " + outputFromServer); //ricevuto dal client

            outputHandler.println(outputFromServer + " ok\n");
            outputHandler.flush();
            System.out.println("sent ok");
            */

            analysisOfTheReceivedMessage(inputHandler.readLine());


            outputHandler.close();
            inputHandler.close();
            clientSocket.close();
            System.out.println("Client " + clientSocket.getInetAddress() + "disconnected from server.");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }


    }

    public void analysisOfTheReceivedMessage(String receivedMessage){
        switch () //per l'analisi del messaggio
    }
}
