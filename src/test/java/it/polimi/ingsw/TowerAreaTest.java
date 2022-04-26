package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Realm;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.TowerArea;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class TowerAreaTest {

    @Test
    void addTower() {
    }

    @Test
    void removeTowerWith2Players() {
        TowerArea towerArea = new TowerArea(2, new Realm(2, new Bag()), Tower.BLACK);
        towerArea.removeTower();
        assertEquals(7, towerArea.getCurrentnumberoftowers());
    }
    @Test
    void removeTowerWith3Players() {
        TowerArea towerArea = new TowerArea(3, new Realm(3, new Bag()), Tower.BLACK);
        towerArea.removeTower();
        assertEquals(5, towerArea.getCurrentnumberoftowers());
    }
    @Test
    void removeTowerWith4Players() {
        TowerArea towerArea = new TowerArea(4, new Realm(4, new Bag()), Tower.BLACK);
        towerArea.removeTower();
        assertEquals(7, towerArea.getCurrentnumberoftowers());
    }
}