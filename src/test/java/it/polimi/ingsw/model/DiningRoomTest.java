package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTest {

    @Test
    void addingOneDragonStudent() {
        DiningRoom diningroom = new DiningRoom();
        diningroom.addStudent(Creature.DRAGON);
        assertEquals(1, diningroom.getOccupiedSeats().get(Creature.DRAGON).intValue());


    @Test
    void removeStudent() {
    }

    @Test
    void setCoinObserver() {
    }

    @Test
    void setEntrance() {
    }
}