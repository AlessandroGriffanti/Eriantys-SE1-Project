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
        TowerArea towerArea = new TowerArea(2);
        towerArea.removeTower(1);
        assertEquals(7, towerArea.getCurrentNumberOfTowers());
    }
    @Test
    void removeTowerWith3Players() {
        TowerArea towerArea = new TowerArea(3);
        towerArea.removeTower(1);
        assertEquals(5, towerArea.getCurrentNumberOfTowers());
    }
    @Test
    void removeTowerWith4Players() {
        TowerArea towerArea = new TowerArea(4);
        towerArea.removeTower(1);
        assertEquals(7, towerArea.getCurrentNumberOfTowers());
    }
}