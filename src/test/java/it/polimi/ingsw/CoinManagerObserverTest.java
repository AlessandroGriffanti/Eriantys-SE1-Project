package it.polimi.ingsw;

import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoinManagerObserverTest {

    /**
     * This method controls if the deposit of coins in the player's 'treasure'
     * is performed correctly along with the withdrawal from the public coins' reserve
     */
    @Test
    public void depositCoinTest(){
        Match match = new Match(0, 3, true);
        assert match.getCoinsReserve() == 20;

        match.addPlayer("mario");
        Player mario = match.getPlayerByID(0);
        assert match.getCoinsReserve() == 19;

        assertEquals(1, mario.getCoinsOwned());
        // add 3 FROG students to get a coin
        mario.getSchoolBoard().getDiningRoom().addStudent(Creature.FROG);
        mario.getSchoolBoard().getDiningRoom().addStudent(Creature.FROG);
        mario.getSchoolBoard().getDiningRoom().addStudent(Creature.FROG);

        // mario earned one coin
        assertEquals(2, mario.getCoinsOwned());
        // one coin was taken from the public reserve
        assertEquals(18, match.getCoinsReserve());
    }
}
