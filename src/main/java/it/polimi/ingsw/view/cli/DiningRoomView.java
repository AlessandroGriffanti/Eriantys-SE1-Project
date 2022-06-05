package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;

import java.util.HashMap;

/**
 * This class represents a miniature of the DiningRoom of the player.
 */
public class DiningRoomView {

    private HashMap<Creature, Integer> occupiedSeatsPlayer;

    /**
     * This constructor creates a new instance of the DiningRoomView.
     */
    public DiningRoomView (){
        occupiedSeatsPlayer = new HashMap<>();
        occupiedSeatsPlayer.put(Creature.DRAGON, 0);
        occupiedSeatsPlayer.put(Creature.FAIRY, 0);
        occupiedSeatsPlayer.put(Creature.FROG, 0);
        occupiedSeatsPlayer.put(Creature.GNOME, 0);
        occupiedSeatsPlayer.put(Creature.UNICORN, 0);
    }

    public synchronized HashMap<Creature, Integer> getOccupiedSeatsPlayer() {
        return occupiedSeatsPlayer;
    }

    public synchronized  void setOccupiedSeatsPlayer(HashMap<Creature, Integer> occupiedSeatsPlayer) {
        this.occupiedSeatsPlayer = occupiedSeatsPlayer;
    }
}
