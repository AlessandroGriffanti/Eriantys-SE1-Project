package it.polimi.ingsw.controller;

public interface ControllerState {

    public void nextState(Controller controller);

    public void stateExecution(Controller controller);
}
