package it.polimi.ingsw.controller.characterCards;

public class FruitSeller extends Character {
    private int remainingNoEntryTiles;
    private int islandID;

    public FruitSeller() {
        this.price = 2;
        this.remainingNoEntryTiles = 4;
    }

    /**
     * effect method takes one of the remainingNoEntryTiles and places it on an island tile.
     * it decrease the number of the remainingNoEntryTiles on the FruitSeller card.
     */
    public void effect() {
        getMatch().getRealmOfTheMatch().getArchipelagos().get(islandID).addNoEntryTile();
        this.remainingNoEntryTiles = this.remainingNoEntryTiles - 1;
    }



    //TODO: il metodo rimette la carta no entry su fruit seller dopo che è stata usata.
    // Bisogna capire quando chiamare questo metodo (dopo che madre natura è finita sull'isola con su noentrytile) -> in reame? in arcipelago?
    // Sentire luca e capire come l'haimplementato, perchè deve essere diminuito il numero di carte noentrytile presenti.
    // Il metodo in reame deve fare tutto e chiamare questo sotto ->remove no entry tile?

    public void noEntryTileComingBackFromIsland(){
        this.remainingNoEntryTiles = this.remainingNoEntryTiles + 1;
    }

}
