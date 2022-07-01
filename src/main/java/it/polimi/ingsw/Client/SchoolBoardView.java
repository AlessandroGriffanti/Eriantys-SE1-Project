package it.polimi.ingsw.Client;

/**
 * This class represents a miniature of the SchoolBoard of the player.
 */
public class SchoolBoardView {
    private ModelView modelview;
    /**
     * This attribute is a reference to the tower area of the player.
     */
    private TowerAreaView towerAreaPlayer;
    /**
     * This attribute is a reference to the diningroom of the player.
     */
    private DiningRoomView diningRoomPlayer;
    /**
     * This attribute is a reference to the entrnace of the player.
     */
    private EntranceView entrancePlayer;

    /**
     * This attribute is a reference to the professor table of the player.
     */
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

    public  ModelView getModelView() {
        return modelview;
    }

    public TowerAreaView getTowerAreaPlayer() {
        return towerAreaPlayer;
    }

    public DiningRoomView getDiningRoomPlayer() {
        return diningRoomPlayer;
    }

    public EntranceView getEntrancePlayer() {
        return entrancePlayer;
    }

    public ProfessorTableView getProfessorTablePlayer() {
        return professorTablePlayer;
    }

    public void setDiningRoomPlayer(DiningRoomView diningRoomPlayer) {
        this.diningRoomPlayer = diningRoomPlayer;
    }
}
