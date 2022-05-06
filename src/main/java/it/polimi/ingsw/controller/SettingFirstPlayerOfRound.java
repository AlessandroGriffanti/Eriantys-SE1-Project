package it.polimi.ingsw.controller;

import java.util.Random;

public class SettingFirstPlayerOfRound implements ControllerState{

    @Override
    public void nextState(Controller controller) {
        controller.setState(new DrawingStudentsEntranceSetUp());
    }

    @Override
    public void stateExecution(Controller controller) {
        Random random = new Random();
        int ID_firstPlayer = random.nextInt(controller.getNumberOfPlayers()); //randomly chooses a number between 0 and the greater player-index
        controller.getMatch().setCurrentPlayer(ID_firstPlayer);
    }
}
