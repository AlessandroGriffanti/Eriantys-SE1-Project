package com.gui;

import com.gui.messages.fromClient.LoginMessage;
import com.gui.network.ServerHandler;
import javafx.concurrent.Task;

public class SendLoginMessageTask extends Task<Double> {

    /**
     * This attribute is the nickname chosen by the player
     */
    private String nickname;
    /**
     * This attribute is true if the player chose to create a new match,
     * and false if he chose to enter a lobby
     */
    private boolean newMatch;

    public SendLoginMessageTask(boolean newMatch, String nickname){
        this.nickname = nickname;
        this.newMatch = newMatch;
    }

    @Override
    protected Double call() throws Exception {

        int progress;
        for(progress = 0; progress < 100; progress++){
            updateProgress(progress, 100.0);
            Thread.sleep(10);
        }

        ServerHandler serverHandler = EriantysApplication.getCurrentApplication().getServerHandler();

        // create login message
        LoginMessage loginRequest = new LoginMessage(nickname, newMatch);
        serverHandler.sendMessage(loginRequest);

        return null;
    }
}
