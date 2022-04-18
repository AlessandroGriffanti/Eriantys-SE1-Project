package it.polimi.ingsw.model;

public class Assistant {
    private final int playOrderValue;
    private final int motherNatureMovementValue;

    public int getPlayOrderValue() {
        return playOrderValue;
    }

    public int getMotherNatureMovementValue() {
        return motherNatureMovementValue;
    }

    public Assistant(int playOrderValue, int motherNatureMovementValue) {
        this.playOrderValue = playOrderValue;
        this.motherNatureMovementValue = motherNatureMovementValue;
    }

}
