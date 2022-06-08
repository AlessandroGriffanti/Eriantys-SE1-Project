package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the different controls over the end of the match
 */
public class EndOfMatchMessage extends Message {

    /**
     * This attribute is the ID of the winner of the match;
     * it could be -1 if the match ended with a tie
     */
    private int winner;
    /**
     * This attribute is the nickname of the winner
     */
    private String winnerNickname = "";
    /**
     * This attribute is the reason why the match ended
     */
    private String reason = "";


    public EndOfMatchMessage(){
        this.object = "end";
    }

    public EndOfMatchMessage(int winner_ID, String winnerNickname, String reason){
        this.object = "end";
        this.winner = winner_ID;
        this.reason = reason;
        this.winnerNickname = winnerNickname;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinnerNickname(String winnerNickname) {
        this.winnerNickname = winnerNickname;
    }

    public String getWinnerNickname() {
        return winnerNickname;
    }
}

/*POSSIBLE VALUES OF 'reason' ATTRIBUTE:
        1. 'empty_bag':
            it means that

*       2. 'towers_finished':
*           it means that one player used all of his towers

        3. 'three islands:
            it means that there are only three islands (group of islands) left'

        4. 'assistants_finished' */
