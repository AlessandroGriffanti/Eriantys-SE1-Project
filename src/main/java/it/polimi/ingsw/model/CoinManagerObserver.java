package it.polimi.ingsw.model;

public class CoinManagerObserver {
    private Player player;

    public CoinManagerObserver(Player p){
        this.player = p;
    }

    /**
     * This method gives one coin to the player
     */
    public void depositCoin(){
        this.player.earnCoin();
    }
}
