package it.polimi.ingsw;

import it.polimi.ingsw.model.schoolboard.TowerArea;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class TowerAreaTest {

    @Test
    void addTower() {
    }

    /** Testing if the currentNumberOfTowers is correctly initialized according to the number of players in the game
     * and if it is correctly increased or decreased according to the numberOfTowers we want to add or remove
     */
    @Test
    void removeTowerWith2Players() {
        TowerArea towerArea = new TowerArea(2);
        towerArea.removeTower(1);
        assertEquals(7, towerArea.getCurrentNumberOfTowers());
        towerArea.addTower(1);
        assertEquals(8, towerArea.getCurrentNumberOfTowers());
    }
    @Test
    void removeTowerWith3Players() {
        TowerArea towerArea = new TowerArea(3);
        towerArea.removeTower(1);
        assertEquals(5, towerArea.getCurrentNumberOfTowers());
        towerArea.addTower(1);
        assertEquals(6, towerArea.getCurrentNumberOfTowers());
    }
    @Test
    void removeTowerWith4Players() {
        TowerArea towerArea = new TowerArea(4);
        towerArea.removeTower(1);
        assertEquals(7, towerArea.getCurrentNumberOfTowers());
        towerArea.addTower(1);
        assertEquals(8, towerArea.getCurrentNumberOfTowers());
    }
}