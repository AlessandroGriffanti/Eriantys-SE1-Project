package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Creature;

import java.util.HashMap;

public class ArchipelagoView {
    /**
     * This attribute is the identifier of the archipelago.
     */
    private int archipelagoID;

    /**
     * This attribute is the number of single island-tiles that belong to the archipelago.
     */
    private int numberOfIsland;
    /**
     * This attribute tells if mother nature is on a certain island or not.
     */
    private boolean motherNaturePresence; //false se non c'è, true se c'è

    /**
     * This attribute is the population of students of each kind currently on the island.
     */
    private HashMap<Creature, Integer> studentsPopulation;

    /**
     * This constructor creates a new archipelago (as a single island).
     * @param archipelagoID is the ID of the archipelago.
     */
    public ArchipelagoView(int archipelagoID){
        this.archipelagoID = archipelagoID;
        this.numberOfIsland = 1;
        this.motherNaturePresence = false;

        this.studentsPopulation = new HashMap<Creature, Integer>();
        this.studentsPopulation.put(Creature.DRAGON, 0);
        this.studentsPopulation.put(Creature.FAIRY, 0);
        this.studentsPopulation.put(Creature.FROG, 0);
        this.studentsPopulation.put(Creature.UNICORN, 0);
        this.studentsPopulation.put(Creature.GNOME, 0);
    }

    /**
     * Add one single student on the archipelago.
     * @param c type of student.
     */
    public void addStudent(Creature c){
        int previousValue = studentsPopulation.get(c);
        studentsPopulation.put(c, previousValue + 1);
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

    public synchronized int getArchipelagoID() {
        return archipelagoID;
    }

    public synchronized  void setArchipelagoID(int archipelagoID) {
        this.archipelagoID = archipelagoID;
    }

    public synchronized int getNumberOfIsland() {
        return numberOfIsland;
    }

    public synchronized void setNumberOfIsland(int numberOfIsland) {
        this.numberOfIsland = numberOfIsland;
    }

    public synchronized boolean isMotherNaturePresence() {
        return motherNaturePresence;
    }

    public synchronized void setMotherNaturePresence(boolean motherNaturePresence) {
        this.motherNaturePresence = motherNaturePresence;
    }
}
