package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChosenAssistantCardMessage extends Message {
    private int assistantChosen;

    public ChosenAssistantCardMessage(){
        this.object = "assistant_chosen";
    }

    public void setAssistantChosen(int assistantChosen) {
        this.assistantChosen = assistantChosen;
    }

    public int getAssistantChosen() {
        return assistantChosen;
    }
}