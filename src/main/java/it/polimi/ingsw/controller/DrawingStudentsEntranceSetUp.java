package it.polimi.ingsw.controller;

public class DrawingStudentsEntranceSetUp implements ControllerState{
    @Override
    public void nextState(Controller controller) {
        controller.setState(new ChooseAssistantCard());
    }

    @Override
    public void stateExecution(Controller controller) {

    }
}
