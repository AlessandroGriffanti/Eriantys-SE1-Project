package it.polimi.ingsw.model;

public class CoinManagerObserver {
    /**
     * This attribute is the reference to the player object to which this coin-manager
     * refers to
     */
    private final Player player;
    /**
     * This attribute is the reference to the match object
     */
    private final Match match;

    public CoinManagerObserver(Match match, Player p){
        this.player = p;
        this.match = match;
    }

    /**
     * This method gives one coin to the player
     */
    public void depositCoin(){
        this.player.earnCoin();
        match.takeCoinsFromReserve(1);
    }
}
