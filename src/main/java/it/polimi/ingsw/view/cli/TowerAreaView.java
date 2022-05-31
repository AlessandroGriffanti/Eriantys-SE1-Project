package it.polimi.ingsw.view.cli;

/**
 * This class represents a miniature of the TowerArea of the player.
 */
public class TowerAreaView {
    private int currentNumberOfTowersPlayer;

    /*public TowerAreaView( * numero giocatori totali della partita *){
        if(numero == 2 || numero == 4){
            this.currentNumberOfTowersPlayer = 8;
        }else if(numero == 3){
            this.currentNumberOfTowersPlayer = 6;
         }
      } */


    public int getCurrentNumberOfTowersPlayer() {
        return currentNumberOfTowersPlayer;
    }

    public void setCurrentNumberOfTowersPlayer(int currentNumberOfTowersPlayer) {
        this.currentNumberOfTowersPlayer = currentNumberOfTowersPlayer;
    }
}
