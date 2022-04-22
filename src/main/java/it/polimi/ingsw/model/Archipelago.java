package it.polimi.ingsw.model;

import java.util.HashMap;

public class Archipelago {
    private int archipelagoID;
    private int numberOfIslands; //dobbiamo sapere quante isole fanno parte di un agglomerato
    private int noEntryTiles;
    private Tower towerColor; //the towers' color is the same for every island-tile belonging to the archipelago
    private HashMap<Creature, Integer> studentsPopulation;

    public Archipelago(int ID){
        this.archipelagoID = ID;
        this.numberOfIslands = 1;
        this.noEntryTiles = 0;

        studentsPopulation = new HashMap<Creature, Integer>();
        studentsPopulation.put(Creature.DRAGON, 0);
        studentsPopulation.put(Creature.FAIRY, 0);
        studentsPopulation.put(Creature.FROG, 0);
        studentsPopulation.put(Creature.UNICORN, 0);
        studentsPopulation.put(Creature.GNOME, 0);
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public int getNumberOfIslands() {
        return numberOfIslands;
    }

    public int getArchipelagoID() {
        return archipelagoID;
    }

    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    public int getTotalNumberOfStudents(){
        int sum = 0;

        for(Creature c: Creature.values()){
            sum += studentsPopulation.get(c);
        }
        return sum;
    }

    public int getStudentsOfType(Creature c){
        return studentsPopulation.get(c);
    }

    public void addStudent(Creature c){
        int previouseValue = studentsPopulation.get(c);
        studentsPopulation.put(c, previouseValue + 1);
    }

    public void addStudents(Creature c, int quantityToAdd){
        int previouseValue = studentsPopulation.get(c);
        studentsPopulation.put(c, previouseValue + quantityToAdd);
    }

    public void addIslands(int islandsToAdd){
        numberOfIslands += islandsToAdd;
    }

    public void addNoEntryTile(){
        noEntryTiles ++;
    }

    public void addNoEntryTile(int quantityToAdd){
        noEntryTiles += quantityToAdd;
    }

    public void removeNoEntryTile(){
        noEntryTiles --;
    }

    public void buildTowers(Tower color){
        //Lascerei al controller il compito di controllare se ci sono giá torri di altri giocatori costruite su un determinato arcipelago
        towerColor = color;
    }
}
