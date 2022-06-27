package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the message sent by the client the player has chosen his assistant card.
 */
public class ChosenAssistantCardMessage extends Message {
    /**
     * This attribute represents the assistant chosen by the player.
     */
    private int assistantChosen;

    public ChosenAssistantCardMessage(){
        this.object = "assistant_chosen";
    }

    public ChosenAssistantCardMessage(int assistantChosenReceived){
        this.object = "assistant_chosen";
        this.assistantChosen = assistantChosenReceived;
    }


    public void setAssistantChosen(int assistantChosen) {
        this.assistantChosen = assistantChosen;
    }

    public int getAssistantChosen() {
        return assistantChosen;
    }
}
