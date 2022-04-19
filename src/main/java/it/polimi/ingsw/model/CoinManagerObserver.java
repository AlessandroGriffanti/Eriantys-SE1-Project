package it.polimi.ingsw.model;

public class CoinManagerObserver {
    private Player player;

    public CoinManagerObserver(Player p){
        this.player = p;
    }
    public void depositCoin(){
        this.player.earnCoin();
    }

    public void withdrawCoins(int coinsTaken){
        player.spendCoins(coinsTaken);
    }


}
