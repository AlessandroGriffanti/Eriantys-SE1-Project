package it.polimi.ingsw.controller;

public class WaitingForPlayers implements ControllerState{
    private int playerAdded = 0;

    @Override
    public void nextState(Controller controller) {
        controller.setState(new SettingFirstPlayerOfRound());
    }

    /**
     * This method adds a new Player at the Match and controls if the match can finally start
     * (there must be the right number of player ready)
     * @param controller controller of the Match
     */
    public void stateExecution(Controller controller) {
        //Takes the nickname of the user from the msg passed by the server (after deserialization)
        String player_nickname = controller.getMsg().getNickname();
        //Calls the creation of a new Player inside the Match
        controller.getMatch().addPlayer(player_nickname);
        //Count the new player as added player
        playerAdded ++;
        //Controls if there is the right number of player waiting for the match to start
        if(playerAdded == controller.getNumberOfPlayers()){
            controller.startMatch();
        }
    }
}
