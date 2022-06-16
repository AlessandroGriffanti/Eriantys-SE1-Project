package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Creature;

import java.util.ArrayList;

public class CharactersDataView {
    /**
     * This attribute is the list of students on the character monk;
     * it's NULL if the character monk was not chosen or if the match is not in
     * expert mode
     */
    private ArrayList<Creature> monkStudents = null;
    /**
     * This attribute is the list of students on the character jester;
     * it's NULL if the character monk was not chosen or if the match is not in
     * expert mode
     */
    private ArrayList<Creature> jesterStudents = null;
    /**
     * This attribute is the list of students on the character princess;
     * it's NULL if the character monk was not chosen or if the match is not in
     * expert mode
     */
    private ArrayList<Creature> princessStudents = null;


    /**
     * This attributes indicates the number of no entry tile on the Herbalist character card.
     */
    private int herbalistNumberOfNoEntryTile;

    public void incrementHerbalistNoEntryTile(){
        herbalistNumberOfNoEntryTile ++;
    }

    public void setMonkStudents(ArrayList<Creature> monkStudents) {
        this.monkStudents = monkStudents;
    }

    public ArrayList<Creature> getMonkStudents() {
        return monkStudents;
    }

    public void setJesterStudents(ArrayList<Creature> jesterStudents) {
        this.jesterStudents = jesterStudents;
    }

    public ArrayList<Creature> getJesterStudents() {
        return jesterStudents;
    }

    public void setPrincessStudents(ArrayList<Creature> princessStudents) {
        this.princessStudents = princessStudents;
    }

    public ArrayList<Creature> getPrincessStudents() {
        return princessStudents;
    }

    public int getHerbalistNumberOfNoEntryTile() {
        return herbalistNumberOfNoEntryTile;
    }

    public void setHerbalistNumberOfNoEntryTile(int herbalistNumberOfNoEntryTile) {
        this.herbalistNumberOfNoEntryTile = herbalistNumberOfNoEntryTile;
    }

}
