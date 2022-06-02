package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Archipelago;

public class ArchipelagoView {
    /**
     * This attribute is the identifier of the archipelago.
     */
    private int archipelagoID;

    /**
     * This attribute is the number of single island-tiles that belong to the archipelago.
     */
    private int numberOfIsland;
    /**
     * This attribute tells if mother nature is on a certain island or not.
     */
    private boolean motherNaturePresence; //false se non c'è, true se c'è

    /**
     * This constructor creates a new archipelago (as a single island).
     * @param archipelagoID is the ID of the archipelago.
     */
    public ArchipelagoView(int archipelagoID){
        this.archipelagoID = archipelagoID;
        this.numberOfIsland = 1;
        this.motherNaturePresence = false;
    }


    public synchronized int getArchipelagoID() {
        return archipelagoID;
    }

    public synchronized  void setArchipelagoID(int archipelagoID) {
        this.archipelagoID = archipelagoID;
    }

    public synchronized int getNumberOfIsland() {
        return numberOfIsland;
    }

    public synchronized void setNumberOfIsland(int numberOfIsland) {
        this.numberOfIsland = numberOfIsland;
    }

    public synchronized boolean isMotherNaturePresence() {
        return motherNaturePresence;
    }

    public synchronized void setMotherNaturePresence(boolean motherNaturePresence) {
        this.motherNaturePresence = motherNaturePresence;
    }
}
