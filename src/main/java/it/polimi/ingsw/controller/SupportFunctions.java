package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.characterCards.MushroomsMerchant;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;

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
    static public boolean onlyThreeIslandsLeft(Match match){
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
    static public void endMatch(Controller controller, String reason, int winner){
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
    static public void endMatch(Controller controller, String reason){
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
                    /* N.B. We don't need to assign tempTowersOfPlayer to winnerTowers
                    * because they are already the same value (in fact we are inside the else if(...)) */
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

    /**
     * This method finds which player is currently controlling the professor given as argument
     * @param match reference to the match (model)
     * @param creature the kind of professor we are interested in
     * @return ID of the player controlling the professor or -1 if no player controls it
     */
    static public int whoControlsTheProfessor(Match match, Creature creature){
        int player_ID = -1;

        for(int i = 0; i < match.getPlayers().size(); i++){

            if(match.getPlayers().get(i).getSchoolBoard().getProfessorTable().isOccupied(creature)){
                assert player_ID == -1 : "ACTION_1 controller state:\ntwo players simultaneously " +
                        "controlling the" + creature + "creature";
                player_ID = i;
            }
        }

        // if nobody controls the creature it should be in the notControlledProfessors list
        if(player_ID == -1){
            assert match.getNotControlledProfessors().contains(creature) :
                    "The creature is not yet controlled but it is not inside the notoControlledProfessors attribute" +
                            "inside the class match";
        }

        return player_ID;
    }

    /**
     * This method computes the influence of all players on the island and returns the player who is the
     * master of the island
     * @param controller reference to the controller of the match
     * @param island_ID the ID of the island whose influence we are interested in
     * @return ID of the player or if the there are two players with the same influence value, which is the maximum
     *         value, then -1 is returned
     */
    static public int influenceComputation(Controller controller, int island_ID){
        Match match = controller.getMatch();

        // in this variable we store for each player the influence on the island : HashMap<player_ID, influence>
        HashMap<Integer, Integer> allPlayersInfluence = new HashMap<Integer, Integer>();

        Archipelago island = match.getRealmOfTheMatch().getArchipelagos().get(island_ID);
        ArrayList<Creature> playerProfessors;

        for(int player_ID = 0; player_ID < match.getPlayers().size(); player_ID++){
            Player player = match.getPlayers().get(player_ID);
            playerProfessors = player.getMyProfessors();

            // count the number of students of each type on the island
            int playerInfluence = 0;
            for(Creature creature: playerProfessors){

                // control if mushroom-merchant has been used
                if(controller.getCharactersManager().isMushroomsMerchantActive()){
                    MushroomsMerchant mushroomsMerchant = (MushroomsMerchant) controller.getCharactersManager().getCards().get("mushroomsMerchant");
                    /* we count the number of students only if they are not of the type
                    to which the effect of the mushrooms-merchant card is applied*/
                    if(!(mushroomsMerchant.getCreatureChosen().equals(creature))){
                        playerInfluence += island.getStudentsOfType(creature);
                    }
                }else{
                    playerInfluence += island.getStudentsOfType(creature);
                }
            }

            if(!(controller.getCharactersManager().isCentaurActive())){
                // if the players owns the tower(s) then we also count them in the influence
                playerInfluence += match.numberOfTowersOnTheIsland(player_ID, island_ID);
            }

            // if the player is using the knight character two points are added to the influence
            if(controller.getCharactersManager().getKnightUser() == player_ID){
                allPlayersInfluence.put(player_ID, playerInfluence + 2);
                // reset knightUser
                controller.getCharactersManager().setKnightUser(-1);
            }else{
                allPlayersInfluence.put(player_ID, playerInfluence);
            }
        }
        // RESET centaur character card
        controller.getCharactersManager().setCentaurActive(false);

        // control who has the higher influence
        int maxInfluence = 0;
        int playerWithMaxInfluence = -1;
        int repetitions = 0;

        for(int i = 0; i < match.getPlayers().size(); i++){
            if(allPlayersInfluence.get(i) > maxInfluence){
                repetitions = 1;
                playerWithMaxInfluence = i;
                maxInfluence = allPlayersInfluence.get(i);
            }else if(allPlayersInfluence.get(i) == maxInfluence){
                repetitions++;
            }
        }

        /* if we found a max value more than once then the influence is not valid, meaning no one is getting the
        control over the island */
        if(repetitions > 1){
            return -1;
        }else {
            return playerWithMaxInfluence;
        }
    }

    /**
     * This method change the status of the professors' table of each player if necessary,
     * checking the previous situation and the current one after some action occurred,
     * for example when a character card is used (bard, princess or trafficker)
     * @param controller reference to the controller of th match
     * @param previousProfessorsMaster for each type of professor (creature) the ID of
     *                                 the previous master of the professor
     * @param currentProfessorsMaster for each type of professor (creature) the ID of
     *      *                         the current master of the professor
     */
    static public void checkProfessorsControl(Controller controller, HashMap<Creature, Integer> previousProfessorsMaster , HashMap<Creature, Integer> currentProfessorsMaster){
        int previousMaster_ID;
        int currenMaster_ID;
        for(Creature c: Creature.values()){
            previousMaster_ID = previousProfessorsMaster.get(c);
            currenMaster_ID = currentProfessorsMaster.get(c);

            if(previousMaster_ID != currenMaster_ID){
                // remove professor from the previous controller
                controller.getMatch().getPlayerByID(previousMaster_ID).getSchoolBoard().getProfessorTable().removeProfessor(c);
                // give professor to the current controller
                controller.getMatch().getPlayerByID(currenMaster_ID).getSchoolBoard().getProfessorTable().addProfessor(c);
            }
        }
    }

}
