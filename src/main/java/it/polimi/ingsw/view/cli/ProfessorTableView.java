package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;

import java.util.HashMap;

/**
 * This class represents a miniature of the ProfessorTable of the player.
 */
public class ProfessorTableView {
    private HashMap<Creature, Boolean> occupiedSeatsPlayer;

    /**
     * This constructor creates a new instance of the ProfessorTableView.
     */
    public ProfessorTableView(){
        occupiedSeatsPlayer = new HashMap<>();
    }



    public synchronized HashMap<Creature, Boolean> getOccupiedSeatsPlayer() {
        return occupiedSeatsPlayer;
    }

    public synchronized void setOccupiedSeatsPlayer(HashMap<Creature, Boolean> occupiedSeatsPlayer) {
        this.occupiedSeatsPlayer = occupiedSeatsPlayer;
    }

}
