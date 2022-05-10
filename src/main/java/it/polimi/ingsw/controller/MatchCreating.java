package it.polimi.ingsw.controller;

public class MatchCreating implements ControllerState{
    @Override
    public void nextState(Controller controller) {
        controller.setState(new ChooseAssistantCard());
    }

    @Override
    public void stateExecution(Controller controller) {
        //read the message in controller and map it in MatchSpecs
        //TODO: do we need to run a control on the message received or only one type of message can be received
        //initialize numberOfPlayers (in controller and model) and expertMode (only inside the model)
    }
}
