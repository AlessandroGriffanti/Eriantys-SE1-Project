package it.polimi.ingsw.view.cli;

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
     * @param modelView is the reference to the modelView created in the NetworkHandler.
     * @param numberOfTotalPlayers number of players playing the match
     */
    public SchoolBoardView(ModelView modelView, int numberOfTotalPlayers){
        this.modelview = modelView;
        this.towerAreaPlayer = new TowerAreaView(numberOfTotalPlayers);
        this.diningRoomPlayer = new DiningRoomView();
        this.entrancePlayer = new EntranceView(diningRoomPlayer);
        this.professorTablePlayer = new ProfessorTableView();
    }

    public synchronized ModelView getModelView() {
        return modelview;
    }

    public synchronized TowerAreaView getTowerAreaPlayer() {
        return towerAreaPlayer;
    }

    public synchronized DiningRoomView getDiningRoomPlayer() {
        return diningRoomPlayer;
    }

    public synchronized EntranceView getEntrancePlayer() {
        return entrancePlayer;
    }

    public synchronized ProfessorTableView getProfessorTablePlayer() {
        return professorTablePlayer;
    }

    public void setDiningRoomPlayer(DiningRoomView diningRoomPlayer) {
        this.diningRoomPlayer = diningRoomPlayer;
    }
}
