package it.polimi.ingsw.view.cli;

/**
 * This class represents a miniature of the TowerArea of the player.
 */
public class TowerAreaView {
    private int currentNumberOfTowersPlayer;

    public TowerAreaView(int numberOfTotalPlayers){
        if(numberOfTotalPlayers == 2 || numberOfTotalPlayers == 4){
            this.currentNumberOfTowersPlayer = 8;
        }else if(numberOfTotalPlayers == 3){
            this.currentNumberOfTowersPlayer = 6;
         }
      }


    public int getCurrentNumberOfTowersPlayer() {
        return currentNumberOfTowersPlayer;
    }

    public void setCurrentNumberOfTowersPlayer(int currentNumberOfTowersPlayer) {
        this.currentNumberOfTowersPlayer = currentNumberOfTowersPlayer;
    }
}
