package it.polimi.ingsw.network.server;

//import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** server class is the main class in the server side of the application and, through it, clients can connect */

public class Server {

    /** the main server will deliver to a client handler the handling of the client connected */
    private ClientHandler clientHandler;

    private int numberOfPort;           //passed in the main

    private ServerSocket serverSocket;  //initialized in the start method


    /**
     * this is the list of the players nicknames registered, connected, to the server
     */
    private ArrayList<String> playersNicknames;


    /** hashmap that links the player's name to the lobby ID the player is registered */
    private HashMap<String, String> playerInTheLobby;       //questa non serve

    /** this HashMap links the lobby ID with the corresponding Controller that handles the corresponding game */
    //private HashMap <String, Controller> lobbies;

    /**
     * This Hashmap links the lobby ID with the maximum number of players for that lobby.
     */
    private HashMap<String, Integer> lobbyMaxNumPlayers;       //neanche questa serve


    public Server(int numberOfPort){
        this.numberOfPort = numberOfPort;
        this.playerInTheLobby = new HashMap<>();
        //this.lobbies = new HashMap<>();
    }


    /**
     * Method createLobby creates a new lobby (managed by a GameHandler) and associates the lobby id with the new GameHandler instance.
     * @param lobbyID lobby id that represents a game.
     * @param numPlayers maximum number of players for that lobby.
     */
    /*
    public synchronized void createLobby(String lobbyID, int numPlayers){     //probabilmente va nel ClientHandler
        lobbies.put(lobbyID, new GameHandler(this, lobbyID));
        lobbyMaxNumPlayers.put(lobbyID, numPlayers);
    } */

    /**
     * Method getLobbyIDByPlayer returns the lobby id of the game the player participates.
     * @param nickname player's name.
     * @return the lobby id.
     */
    public String getLobbyIDByPlayerName(String nickname) {
        return playerInTheLobby.get(nickname);
    }

    /**
     * This method returns the list of players that have joined the selected lobby.
     * @param lobbyID the lobby id.
     * @return a list of player's nickname.
     */
    public ArrayList<String> getPlayersNameByLobby(String lobbyID) {
        ArrayList<String> players = new ArrayList<>();
        for (String nickname : playerInTheLobby.keySet()) {
            if (getLobbyIDByPlayerName(nickname).equals(lobbyID)) {
                players.add(nickname);
            }
        }
        return players;
    }

    /** this method launches the server.
     * It initializes the serverSocket.
     * executorService creates a new pool of threads, the ones really dealing with clients.
     * In the loop we accept a connection from a client and we 'deliver' it to a new thread through the submit method.
     */
    public void start() throws IOException{
        serverSocket = new ServerSocket(4444);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("Waiting for connections...");
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected " + clientSocket.getRemoteSocketAddress());
            executorService.submit(new ClientHandler(clientSocket));
        }
    }

    public ArrayList<String> getPlayersNicknames() {
        return this.playersNicknames;
    }

    public void setPlayersNicknames(ArrayList<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }


    public static void main(String[] args) {
        /*System.out.println("Insert number of server port: ");
        Scanner in = new Scanner(System.in);
        int numberOfPort = in.nextInt(); */
        Server server = new Server(4444);
        try{
            server.start();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error in the server launch");
        }
    }

}