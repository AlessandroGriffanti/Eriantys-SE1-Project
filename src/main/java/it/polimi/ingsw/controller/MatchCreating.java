package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.MatchSpecsMessage;
import it.polimi.ingsw.network.messages.MatchWaitingMessage;

/**
 * This class represents the state of the controller in which it's waiting for other players to join the match
 * RECEIVED: MatchSpecsMessage
 * RESPONSE: MatchWaiting
 */
public class MatchCreating implements ControllerState{
    @Override
    public void nextState(Controller controller) {
        controller.setState(new ChooseAssistantCard());
    }

    @Override
    public void stateExecution(Controller controller) {
        //read the message in controller and map it in MatchSpecs
        String jsonMessage = controller.getMsg();
        MatchSpecsMessage msgMapped = gson.fromJson(jsonMessage, MatchSpecsMessage.class);

        //TODO: do we need to run a control on the message received or only one type of message can be received

        //initialize numberOfPlayers (in controller and model) and expertMode (only inside the model)
        controller.setNumberOfPlayers(msgMapped.getNumOfPlayers());
        controller.setExpertMode(msgMapped.isExpertMode());

        //notify the player with a MatchWaiting message
        controller.sendMessageToPlayer(0, new MatchWaitingMessage());

    }
}
