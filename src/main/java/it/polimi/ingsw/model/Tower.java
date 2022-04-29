package it.polimi.ingsw.model;

public enum Tower {
    BLACK,
    WHITE,
    GREY,

    /** Transparent is used when the centaur character card is called so that, if a tower is transparent, we do not count its influence */
    TRANSPARENT
}
