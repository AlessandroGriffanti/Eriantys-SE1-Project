package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/** server class is the main class in the server side of the application and, through it, clients can connect */

public class Server {

    /**
     * the main server will deliver to a ClientHandler the handling of the client connected
     */
    private ClientHandler clientHandler;

    /**
     * This attributes represents the number of port of the server.
     */
    private int numberOfPort;

    /**
     * This attribute represents the server socket.
     */
    private ServerSocket serverSocket;


    /**
     * this is the list of the players nicknames registered, connected, to the server.
     */
    private ArrayList<String> playersNicknames;


    /**
     * this HashMap links the lobby ID with the corresponding Controller that handles the corresponding game
     */
    private HashMap <String, Controller> lobbies;

    /**
     * This arraylist is used to track the matches that have ended.
     * The index of the array corresponds to the id of the lobby.
     * True if the match is ended, false otherwise.
     */
    private ArrayList<Boolean> lobbiesEnd;

    /**
     * This double arraylist is used to check the players connections.
     * index of the main arraylist is the lobby id;
     * indexes of secondary arraylist are the players ID;
     * boolean values are the connection status: true if connected, false if not.
     */
    //private ArrayList<ArrayList<Boolean>> lobbiesPlayersConnection;


    /**
     * This constructor creates a new instance of the server.
     * @param numberOfPort is the server port.
     */
    public Server(int numberOfPort){
        this.numberOfPort = numberOfPort;
        this.playersNicknames = new ArrayList<String>();
        this.lobbies = new HashMap<>();
        this.lobbiesEnd = new ArrayList<>();
        //this.lobbiesPlayersConnection = new ArrayList<ArrayList<Boolean>>();
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
    */

    /**
     * Main method of the server
     * @param args are the main args.
     */
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Server server = new Server(4444);

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

    public synchronized ArrayList<String> getPlayersNicknames() {
        return this.playersNicknames;
    }

    public synchronized HashMap<String, Controller> getLobbies() {
        return lobbies;
    }

    public synchronized ArrayList<Boolean> getLobbiesEnd() {
        return lobbiesEnd;
    }


    public synchronized void setPlayersNicknames(ArrayList<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }

    /*public synchronized ArrayList<ArrayList<Boolean>> getLobbiesPlayersConnection() {
        return lobbiesPlayersConnection;
    }

    public synchronized void setLobbiesPlayersConnection(ArrayList<ArrayList<Boolean>> lobbiesPlayersConnection) {
        this.lobbiesPlayersConnection = lobbiesPlayersConnection;
    }*/

    /**
     * This method is used to check that all players are connected at the beginning of the game.
     * @param lobbyIDpassed
     * @param playerIDpassed
     * @param numberOfPlayerInTheLobby
     */
    /*public synchronized void checkPlayersConnectionOnStart(int lobbyIDpassed, int playerIDpassed, int numberOfPlayerInTheLobby) {
        System.out.println("num player" + numberOfPlayerInTheLobby);
        if (playerIDpassed == 0) {
            if (lobbiesPlayersConnection.get(lobbyIDpassed).get(0)) {
                System.out.println("Check 0");
                for (int i = 0; i < numberOfPlayerInTheLobby; i++) {
                    if (!(lobbiesPlayersConnection.get(lobbyIDpassed).get(i).booleanValue())) {
                        lobbies.get(String.valueOf(lobbyIDpassed)).onePlayerDisconnected(i);
                    }
                }
            }
        } else if (playerIDpassed == 1) {
            if (!lobbiesPlayersConnection.get(lobbyIDpassed).get(0)) {
                System.out.println("Check 0");
                for (int i = 0; i < numberOfPlayerInTheLobby; i++) {
                    if (!(lobbiesPlayersConnection.get(lobbyIDpassed).get(i).booleanValue())) {
                        lobbies.get(String.valueOf(lobbyIDpassed)).onePlayerDisconnected(i);
                    }
                }
            }
        } else if (playerIDpassed == 2) {
            if (!lobbiesPlayersConnection.get(lobbyIDpassed).get(0)) {
                if (!lobbiesPlayersConnection.get(lobbyIDpassed).get(1)) {
                    System.out.println("Check 0");
                    for (int i = 0; i < numberOfPlayerInTheLobby; i++) {
                        if (!(lobbiesPlayersConnection.get(lobbyIDpassed).get(i).booleanValue())) {
                            lobbies.get(String.valueOf(lobbyIDpassed)).onePlayerDisconnected(i);
                        }
                    }

                }
            }

        }
    }*/

}