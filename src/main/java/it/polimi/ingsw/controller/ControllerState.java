package it.polimi.ingsw.controller;

import com.google.gson.Gson;

public interface ControllerState {
    Gson gson = new Gson();

    public void nextState(Controller controller);

    public void controlMessageAndExecute(Controller controller);

    public void stateExecution(Controller controller);
}
