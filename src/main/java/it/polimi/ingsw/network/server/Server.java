package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** server class is the main class in the server side of the application and, through it, clients can connect */

public class Server {

    /** the main server will deliver to a ClientHandler the handling of the client connected */
    private ClientHandler clientHandler;

    private int numberOfPort;           //passed in the main

    private ServerSocket serverSocket;  //initialized in the start method


    /**
     * this is the list of the players nicknames registered, connected, to the server.
     */
    private ArrayList<String> playersNicknames;


    /** hashmap that links the player's name to the lobby ID the player is registered */
    //private HashMap<String, String> playerInTheLobby;       //questa non serve


    /** this HashMap links the lobby ID with the corresponding Controller that handles the corresponding game */
    private HashMap <String, Controller> lobbies;
    private ArrayList<Boolean> lobbiesEnd;      //index of array list corresponds to the string id of lobbies, false if match not ended

    /**
     * This Hashmap links the lobby ID with the maximum number of players for that lobby.
     */
    //private HashMap<String, Integer> lobbyMaxNumPlayers;       //neanche questa serve




    //posso aggiungere un hashmap di string(o int) che indica il numero della partita, della lobby, e di boolean
    //in cui true mi indica ad esempio se la partita è in attesa e false se è completa

    /**
     * This constructor creates a new instance of the server.
     * @param numberOfPort is the server port.
     */
    public Server(int numberOfPort){
        this.numberOfPort = numberOfPort;
        //this.playerInTheLobby = new HashMap<>();
        this.playersNicknames = new ArrayList<String>();
        this.lobbies = new HashMap<>();
        this.lobbiesEnd = new ArrayList<>();
    }



    /**
     * Method getLobbyIDByPlayer returns the lobby id of the game the player participates.
     * @param nickname player's name.
     * @return the lobby id.
     */
    //public String getLobbyIDByPlayerName(String nickname) {
    //   return playerInTheLobby.get(nickname);
    //}

    /**
     * This method returns the list of players that have joined the selected lobby.
     // * @param lobbyID the lobby id.
     * @return a list of player's nickname.
     */
    /*
    public ArrayList<String> getPlayersNameByLobby(String lobbyID) {
        ArrayList<String> players = new ArrayList<>();
        for (String nickname : playerInTheLobby.keySet()) {
            if (getLobbyIDByPlayerName(nickname).equals(lobbyID)) {
                players.add(nickname);
            }
        }
        return players;
    }

     */

    public ArrayList<String> getPlayersNicknames() {
        return this.playersNicknames;
    }

    public HashMap<String, Controller> getLobbies() {
        return lobbies;
    }

    public ArrayList<Boolean> getLobbiesEnd() {
        return lobbiesEnd;
    }


    public void setPlayersNicknames(ArrayList<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }



    /*
    public static void main(String[] args) {
        //System.out.println("Insert number of server port: ");
        //Scanner in = new Scanner(System.in);
        //int numberOfPort = in.nextInt();
        Server server = new Server(4444);
        try{
            server.start();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error in the server launch");
        }
    }

    this method launches the server.
     It initializes the serverSocket.
     executorService creates a new pool of threads, the ones really dealing with clients.
     In the loop we accept a connection from a client and we 'deliver' it to a new thread through the submit method.

    public void start() throws IOException{
        serverSocket = new ServerSocket(4444);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.err.println("Waiting for connections...");
        while(true){
            Socket clientSocket = serverSocket.accept();
            connections.add(clientSocket);
            System.err.println("Client connected " + clientSocket.getRemoteSocketAddress() + ", number of clients: " + connections.size());
            executorService.submit(new ClientHandler(clientSocket, this));
        }
    }

    */

    /**
     * Main method of the server
     * @param args are the main args.
     */
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Server server = new Server(4444);   //ripetitivo

        try{
            System.out.println("Server ready");
            server.start();

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error in server launch");
        }


    }

    /**
     * this method launches the server.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(numberOfPort);
        Socket clientSocket = null;
        while(true){
            try{
                clientSocket = serverSocket.accept();
                System.out.println("Client connected " + clientSocket.getRemoteSocketAddress());
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }

            // new thread for a client
            new ClientHandler(clientSocket, this).start();

        }
    }

}