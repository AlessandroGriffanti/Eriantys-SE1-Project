package com.gui.network;

import com.gui.messages.Message;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerHandler {
    private Thread thread;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Consumer<Boolean> connectionCompleteObserver;
    private Consumer<String> messageArrivedObserver;
    private Runnable connectionClosedObserver;
    private final Gson gson = new Gson();


    synchronized public void setConnectionCompleteObserver(Consumer<Boolean> connectionCompleteObserver)
    {
        this.connectionCompleteObserver = connectionCompleteObserver;
    }


    synchronized public void setMessageArrivedObserver(Consumer<String> messageArrivedObserver)
    {
        this.messageArrivedObserver = messageArrivedObserver;
    }


    synchronized public void setConnectionClosedObserver(Runnable connectionClosedObserver)
    {
        this.connectionClosedObserver = connectionClosedObserver;
    }


    synchronized public void notifyConnectionComplete(boolean success)
    {
        if (connectionCompleteObserver != null)
            connectionCompleteObserver.accept(success);
    }


    synchronized public void notifyMessageArrived(String msg)
    {
        if (messageArrivedObserver != null)
            messageArrivedObserver.accept(msg);
    }


    synchronized public void notifyConnectionClosed()
    {
        if (connectionClosedObserver != null)
            connectionClosedObserver.run();
    }


    public void attemptConnection(String ip, int port)
    {
        thread = new Thread(() -> connectionThread(ip, port));
        thread.start();
    }


    private void connectionThread(String ip, int port)
    {
        boolean success = openConnection(ip, port);
        notifyConnectionComplete(success);
        if (!success)
            return;

        try {
            while (true) {
                /* this loop terminates when the socket is closed */
                String jsonMessage = (String) input.readLine();
                notifyMessageArrived(jsonMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeConnection();
    }


    synchronized private boolean openConnection(String ip, int port)
    {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            return false;
        }

        try {
            output = new PrintWriter(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            output = null;
            input = null;
            try {
                socket.close();
            } catch (IOException e2) { }
            return false;
        }

        return true;
    }


    /**
     * This method sends the message, after serializing it, to the server via socket connection
     * @param message the message that must be sent
     */
    synchronized public void sendMessage(Message message)
    {
        assert output != null;
        // send message
        output.println(gson.toJson(message));
        output.flush();
    }

    synchronized public void closeConnection()
    {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) { }
            notifyConnectionClosed();
        }
        output = null;
        input = null;
        thread = null;
    }


    synchronized public boolean isConnected()
    {
        return thread != null;
    }
}

