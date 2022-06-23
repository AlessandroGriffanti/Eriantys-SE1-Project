package it.polimi.ingsw.model;

import java.util.HashMap;

/**
 * This Class represents one group of islands-tile (at the beginning it consists of one single island-tile)
 */
public class Archipelago {
    /**
     * This attribute is the identifier of the archipelago
     */
    private int ID;
    /**
     * This attribute is the number of single island-tiles that belong to the archipelago
     */
    private int numberOfIslands;
    /**
     * This attribute is the number of no-entry-tiles currently on the archipelago
     */
    private int noEntryTiles;

    /**
     * This attribute is the color of the Towers currently on the archipelago, or 'null' if there is no
     * tower built on it
     */
    private Tower towerColor;
    /**
     * This attribute is the player that currently has the higher influence
     * over the archipelago
     */
    private Player masterOfArchipelago;
    /**
     * This attribute is the population of students of each kind currently on the island
     */
    private HashMap<Creature, Integer> studentsPopulation;

    public Archipelago(int ID){
        this.ID = ID;
        this.numberOfIslands = 1;
        this.noEntryTiles = 0;
        this.masterOfArchipelago = null;

        this.studentsPopulation = new HashMap<Creature, Integer>();
        this.studentsPopulation.put(Creature.DRAGON, 0);
        this.studentsPopulation.put(Creature.FAIRY, 0);
        this.studentsPopulation.put(Creature.FROG, 0);
        this.studentsPopulation.put(Creature.UNICORN, 0);
        this.studentsPopulation.put(Creature.GNOME, 0);
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    private void setTowerColor(){
        towerColor = masterOfArchipelago.getTowerColor();
    }

    public int getNumberOfIslands() {
        return numberOfIslands;
    }

    public int getID() {
        return ID;
    }

    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    public Player getMasterOfArchipelago() {
        return masterOfArchipelago;
    }

    public HashMap<Creature, Integer> getStudentsPopulation() {
        return studentsPopulation;
    }

    /**
     * Compute the number of students of a particular type on the archipelago
     * @param c type of student
     * @return number of students of the specified type
     */
    public int getStudentsOfType(Creature c){
        return studentsPopulation.get(c);
    }

    /**
     * Computes the total number of students on the archipelago
     * @return total number of students
     */
    public int getTotalNumberOfStudents(){
        int sum = 0;

        for(Creature c: Creature.values()){
            sum += studentsPopulation.get(c);
        }
        return sum;
    }

    /**
     * Add one single student on the archipelago
     * @param c type of student
     */
    public void addStudent(Creature c){
        int previousValue = studentsPopulation.get(c);
        studentsPopulation.put(c, previousValue + 1);
    }

    /**
     * Add more than one student, but all of them are the same type
     * @param c type of students
     * @param quantityToAdd number of students to put on the island
     */
    public void addStudents(Creature c, int quantityToAdd){
        int previousValue = studentsPopulation.get(c);
        studentsPopulation.put(c, previousValue + quantityToAdd);
    }

    /**
     * Adds one or more islands to the archipelago, and increases "numberOfIslands" accordingly
     * @param islandsToAdd number of islands to add to the archipelago
     */
    public void addIslands(int islandsToAdd){
        numberOfIslands += islandsToAdd;
    }

    /**
     * Adds only one no-entry-tile to the archipelago, and increases noEntryTiles by 1
     */
    public void addNoEntryTile(){
        noEntryTiles ++;
    }

    /**
     * Adds more than one no-entry-tile to the archipelago and increases noEntryTile accordingly
     * @param quantityToAdd number of no-entry-tile to add
     */
    public void addNoEntryTiles(int quantityToAdd){
        noEntryTiles += quantityToAdd;
    }

    /**
     * Removes one single no-entry-tile from the archipelago and decreases noEntryTile by 1
     */
    public void removeNoEntryTile(){
        if(noEntryTiles > 0){
            noEntryTiles --;
        }
    }

    /**
     * Controls if the master is the same or has changed, in this last case retrieves towers from the new
     * master and returns the same number of towers to the old master
     * @param p reference to the Player for the highest influence over the archipelago
     * @return true if the match must end because the player has no more towers left
     *         false if the match can continue
     */
    public boolean setMasterOfArchipelago(Player p){
        boolean endOfMatch = false;

        if(masterOfArchipelago == null){
            masterOfArchipelago = p;
            // take towers from the player's tower-area
            endOfMatch = masterOfArchipelago.getSchoolBoard().takeTowers(this.numberOfIslands);

        }else if (!masterOfArchipelago.equals(p)){
            masterOfArchipelago.getSchoolBoard().putTowers(this.numberOfIslands);
            masterOfArchipelago = p;
            // take towers from the player's tower-area
            endOfMatch = masterOfArchipelago.getSchoolBoard().takeTowers(this.numberOfIslands);
        }
        // update tower(s)' color
        setTowerColor();

        return endOfMatch;
    }

}
