package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.server.ClientHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SupportFunctions {

    /**
     * This method finds out who is the first player of the upcoming new round
     * @param controller reference to the controller
     * @return ID of the first player of next round, who is also the nextPlayer
     */
    static int findFirstPlayerOfNewRound(Controller controller){

        int tempFirstPlayer = -1;
        int index = 0;

        while (controller.getPlayersDisconnected().get(tempFirstPlayer) && index < controller.getNumberOfPlayers()){
            tempFirstPlayer = controller.getActionPhaseOrder().get(index);
            index++;
        }

        return tempFirstPlayer;
    }

    /**
     *  This method controls if there are only three islands left, if so the match will end
     * @param match reference to the match in question
     * @return true if there are 3 islands left
     *         false if there are more than three islands
     */
    static boolean onlyThreeIslandsLeft(Match match){
        int islandsLeftCounter = 0;

        ArrayList<Archipelago> islands = match.getRealmOfTheMatch().getArchipelagos();
        for(Archipelago a: islands){
            if(a != null){
                islandsLeftCounter++;
            }
        }

        if(islandsLeftCounter <= 3){
            assert islandsLeftCounter == 3 : "ERROR: there are less than 3 islands left";
        }
        return islandsLeftCounter == 3;
    }

    /**
     * This method controls if there are no more students in the bag to be drawn,
     * if so the match will end
     * @param match reference to the match in question
     * @return true if there are no more students to draw
     *         false if there are some more students in the bag
     */
    static boolean noMoreStudentsToDraw(Match match){
        Bag bag = match.getBagOfTheMatch();

        assert bag.getNumberOfRemainingStudents() >= 0 : "ERROR: the number of students in the bag is less than zero.";
        return bag.getNumberOfRemainingStudents() == 0;
    }

    /**
     * This method controls if there are no more students inside the bag (the last one was drawn during the last round)
     * @param controller reference to the controller of the match
     * @return true if the match must end, false otherwise
     */
    static boolean emptyBag_control(Controller controller){
        boolean matchMustEnd = false;

        // control if a player has no more assistant cards
        ArrayList<Player> players = controller.getMatch().getPlayers();

        for(Player p: players){
            if(p.getAssistantsDeck().getNumberOfRemainingCards() == 0){
                matchMustEnd = true;
            }
        }

        return matchMustEnd;
    }

    /**
     * This method controls if one between all the players has no more assistant cards to use
     * @param controller reference to the controller of the match
     * @return true if the match must end, false otherwise
     */
    static boolean playerWithNoMoreAssistants_control(Controller controller){
        boolean matchMustEnd = false;


        Bag bag = controller.getMatch().getBagOfTheMatch();

        if(bag.getNumberOfRemainingStudents() == 0){
            matchMustEnd = true;
        }

        return matchMustEnd;
    }

    /**
     * This method will end the match by sending an end-of-match message to all the players and by
     * notifying each ClientHandler that the connection to the clients can be turned off
     * @param controller reference to the controller of the match
     * @param reason reason why the match ended, put in the message
     * @param winner ID of the player who's the winner of this match
     */
    static void endMatch(Controller controller, String reason, int winner){
        String winnerNickname = controller.getPlayersNickname().get(winner);

        EndOfMatchMessage finalMessage = new EndOfMatchMessage(winner, winnerNickname, reason);
        controller.sendMessageAsBroadcast(finalMessage);

        for(ClientHandler c: controller.getClientHandlers()){
            // TODO: notify each clientHandler that the connection can be turned off
        }

        // set to true the attribute matchEnded inside the Controller
        controller.setMatchEnded(true);
    }

    /**
     * This method will end the match, computing the winner and sending an end-of-match message
     * to all the players; finally it notifies each ClientHandler that the connection to the clients
     * can be turned off
     * @param controller reference to the controller of the match that must end
     * @param reason reason why the match ended
     */
    static void endMatch(Controller controller, String reason){
        int winner = computeWinner(controller.getMatch());
        String winnerNickname = controller.getPlayersNickname().get(winner);

        EndOfMatchMessage finalMessage = new EndOfMatchMessage(winner, winnerNickname, reason);
        controller.sendMessageAsBroadcast(finalMessage);

        for(ClientHandler c: controller.getClientHandlers()){
            // TODO: notify each clientHandler that the connection can be turned off
        }

        // set to true the attribute matchEnded inside the Controller
        controller.setMatchEnded(true);
    }

    /**
     * This method finds who is the winner of the specified match
     * @param match reference to the match that must end
     * @return ID of the winner or -1 if there is a tie
     */
    static int computeWinner(Match match){

        Player tempWinner = null;
        int winnerTowers = 100;
        int tempTowersOfPlayer;

        int winnerProfessors;
        int tempPlayerProfessors;
        boolean tie = false;

        for(Player p: match.getPlayers()){
            tempTowersOfPlayer = p.getSchoolBoard().getTowerArea().getCurrentNumberOfTowers();

            if(tempTowersOfPlayer < winnerTowers || tempWinner == null){
                winnerTowers = tempTowersOfPlayer;
                tempWinner = p;
                tie = false;
            }else if(tempTowersOfPlayer == winnerTowers){

                winnerProfessors = tempWinner.getMyProfessors().size();
                tempPlayerProfessors = p.getMyProfessors().size();

                if(tempPlayerProfessors > winnerProfessors){
                    winnerTowers = tempTowersOfPlayer;
                    tempWinner = p;
                    tie = false;
                }else if(tempPlayerProfessors == winnerProfessors){
                    tie = true;
                }
            }
        }

        assert tempWinner != null;
        return tie ? -1 : tempWinner.getID();
    }

}
