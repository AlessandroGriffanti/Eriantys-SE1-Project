package it.polimi.ingsw.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * This Class represents the realm of the game,regarded as the union of islands and clouds
 */
public class Realm{
    /**
     * This attribute is the archipelago's ID where motherNature currently stands
     */
    private int positionOfMotherNature;
    /**
     * This attribute is the list of references to all the islands/group of islands in the realm
     */
    private ArrayList<Archipelago> archipelagos;
    /**
     * This attribute is the list of references to all the clouds in the realm
     */
    private final ArrayList<CloudTile> cloudRegion;

    /**
     * Constructor of Realm: it creates islands and clouds, positions motherNature and puts one
     * student on each island
     * @param numberOfPlayers number of Players
     * @param bag reference to the Bag used in the match
     */
    public Realm(int numberOfPlayers, Bag bag){
        archipelagos = new ArrayList<Archipelago>();
        cloudRegion = new ArrayList<CloudTile>();

        //clouds creation
        for(int i =0; i<numberOfPlayers; i++){
            cloudRegion.add(new CloudTile(i, numberOfPlayers, bag));
        }

        //archipelagos creation
        for(int j = 0; j<12; j++){
            archipelagos.add(new Archipelago(j));
        }

        //choose motherNature start position
        Random random = new Random();
        positionOfMotherNature = random.nextInt(12);

        // put one student on each island(Archipelago)
        ArrayList<Creature> setUpStudents = bag.drawSetUpStudents();
        int k = (positionOfMotherNature+1) % 12;
        for(Creature c: setUpStudents){
            if(k != (positionOfMotherNature+6)%12){
                archipelagos.get(k).addStudent(c);
            }
            k = (k+1)%12;
        }
    }

    public ArrayList<Archipelago> getArchipelagos(){
        ArrayList<Archipelago> returnArray = new ArrayList<Archipelago>();
        for(Archipelago a: archipelagos){
            returnArray.add(a);
        }

        return returnArray;
    }

    public ArrayList<CloudTile> getCloudRegion(){
        ArrayList<CloudTile> returnArray = new ArrayList<CloudTile>();
        for(CloudTile c: cloudRegion){
            returnArray.add(c);
        }

        return returnArray;
    }

    public int getPositionOfMotherNature(){return positionOfMotherNature;}

    /**
     * Unifies two groups of islands together, updating the attributes of the first (a1) one passed as
     * parameter and replacing the second one (a2) with a null pointer so that the correspondence between
     * island's IDs and arrayList's index doesn't change.
     * @param ID_1 first group of islands, its attributes will be updated
     * @param ID_2 second group of islands, it will be replaced with 'null'
     */
    public void unifyArchipelago(int ID_1, int ID_2){

        Archipelago a1 = archipelagos.get(ID_1);
        Archipelago a2 = archipelagos.get(ID_2);

        //the idea is to set a2 to null and transfer all its attributes' values into a1

        //update numberOfIslands in a1
        a1.addIslands(a2.getNumberOfIslands());

        //update noEntryTiles in a1
        a1.addNoEntryTiles(a2.getNoEntryTiles());

        //update studentsPopulation in a1
        for(Creature c: Creature.values()){
            a1.addStudents(c, a2.getStudentsOfType(c));
        }

        archipelagos.set(ID_2, null);
    }

    /**
     * Takes all the students on the specified cloud
     * @param cloudID identifier of the cloud
     * @return ArrayList with all the students found on the cloud
     */
    public ArrayList<Creature> takeStudentsFromCloud(int cloudID){

        ArrayList<Creature> temp = new ArrayList<Creature>();
        CloudTile currentCloud = cloudRegion.get(cloudID);
        temp = currentCloud.takeStudents();

        return temp;
    }

    /**
     * Tells to the current Archipelago (where motherNature stands) to add a student to its population
     * @param c the kind of student that will be added to the current Archipelago
     */
    public void addStudentToIsland(Creature c){
        archipelagos.get(positionOfMotherNature).addStudent(c);
    }

    /**
     * Tells the current archipelago to set the master
     * @param p reference to the Player with the highest influence over the archipelago
     */
    public void setMasterOfCurrentArchipelago(Player p){
        archipelagos.get(positionOfMotherNature).setMasterOfArchipelago(p);
    }

    /**
     * Moves mother nature by given steps, paying attention to the fact that there could be some positions
     * inside the ArrayList archipelagos that are null pointer; the next position of mother nature will
     * certainly correspond to a non-null element of the array archipelagos.
     * @param steps number of steps motherNature will take
     */
    public void moveMotherNature(int steps){
        int i = 1;
        while(steps > 0){
            if(archipelagos.get((positionOfMotherNature+i)%12) != null){
                steps--;
            }
            i++;
        }
        positionOfMotherNature = (positionOfMotherNature + (i-1)) % 12;
    }

}
