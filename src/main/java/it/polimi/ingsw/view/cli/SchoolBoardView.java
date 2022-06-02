package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.schoolboard.DiningRoom;
import it.polimi.ingsw.model.schoolboard.Entrance;
import it.polimi.ingsw.model.schoolboard.ProfessorTable;
import it.polimi.ingsw.model.schoolboard.TowerArea;

/**
 * This class represents a miniature of the SchoolBoard of the player.
 */
public class SchoolBoardView {
    private ModelView modelview;
    private TowerAreaView towerAreaPlayer;
    private DiningRoomView diningRoomPlayer;
    private EntranceView entrancePlayer;
    private ProfessorTableView professorTablePlayer;

    /**
     * This constructor creates a new instance of the SchoolBoardView.
     * @param modelview is the reference to the modelView created in the NetworkHandler.
     */
    public SchoolBoardView(ModelView modelview, int numberOfTotalPlayers){
        this.modelview = modelview;
        this.towerAreaPlayer = new TowerAreaView(numberOfTotalPlayers);
        this.diningRoomPlayer = new DiningRoomView();
        this.entrancePlayer = new EntranceView(diningRoomPlayer);
    }

    public synchronized ModelView getModelview() {
        return modelview;
    }

    public synchronized void setModelview(ModelView modelview) {
        this.modelview = modelview;
    }

    public synchronized TowerAreaView getTowerAreaPlayer() {
        return towerAreaPlayer;
    }

    public synchronized void setTowerAreaPlayer(TowerAreaView towerAreaPlayer) {
        this.towerAreaPlayer = towerAreaPlayer;
    }

    public synchronized DiningRoomView getDiningRoomPlayer() {
        return diningRoomPlayer;
    }

    public synchronized void setDiningRoomPlayer(DiningRoomView diningRoomPlayer) {
        this.diningRoomPlayer = diningRoomPlayer;
    }

    public synchronized EntranceView getEntrancePlayer() {
        return entrancePlayer;
    }

    public synchronized void setEntrancePlayer(EntranceView entrancePlayer) {
        this.entrancePlayer = entrancePlayer;
    }

    public synchronized ProfessorTableView getProfessorTablePlayer() {
        return professorTablePlayer;
    }

    public synchronized void setProfessorTablePlayer(ProfessorTableView professorTablePlayer) {
        this.professorTablePlayer = professorTablePlayer;
    }
}
