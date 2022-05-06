package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;

public class VirtualView {

    /**
     * This constructor call the match in order to add this view as its observer
     * @param match
     */
    public VirtualView(Match match){
        //Match.addViewAsObserver
    }

    //Called by the match when it needs to notify the players' view after some changes occurred
    public void update(){
        //TODO: receives an update from the model and propagates it to the server and then to the client-side view
    }
}
