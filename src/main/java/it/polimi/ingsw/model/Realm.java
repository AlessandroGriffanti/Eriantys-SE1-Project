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
     *This attribute
     */
    private Bag bag;

    /**
     * Constructor of Realm: it creates islands and clouds, positions motherNature and puts one
     * student on each island
     * @param numberOfPlayers number of Players
     * @param bag reference to the Bag used in the match
     */
    public Realm(int numberOfPlayers, Bag bag){
        archipelagos = new ArrayList<Archipelago>();
        cloudRegion = new ArrayList<CloudTile>();
        this.bag = bag;

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

    public Bag getBag(){
        return bag;
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
        //The idea is to set a2 to null and transfer all its attributes' values into a1

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
     * todo -> test
     * This method finds the next island or group of islands with respect to the island argument of the method
     * @param island_ID ID of the island taken as reference
     * @return ID of the next island/group of islands
     */
    public int nextIsland(int island_ID){
        int i = 1;

        while(archipelagos.get((island_ID + 1)%12) == null){
            i++;
        }

        return positionOfMotherNature+i;
    }

    /**
     * todo -> test
     * This method finds the previous island or group of islands with respect to the island argument of the method
     * @param island_ID ID of the island taken as reference
     * @return ID of the previous island/group of islands
     */
    public int previousIsland(int island_ID){
        int i = 0;
        int previousIndex;
        do {
            i++;
            if(island_ID - i < 0){
                previousIndex = (12 + (island_ID - i));
            }else{
                previousIndex = island_ID - i;
            }
        }while(archipelagos.get(previousIndex) == null);

        return previousIndex;
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
     * This method call each cloud belonging to the "cloudRegion" so that they can refill
     * themselves with neew students taken from the bag
     * @return the array of all the students taken (students on cloud0 - students on cloud1...)
     */
    public ArrayList<Creature> moveStudentsToClouds(){
        ArrayList<Creature> drawnStudents = new ArrayList<Creature>();
        for(CloudTile c : cloudRegion){
            drawnStudents.addAll(c.putStudents());
        }

        return drawnStudents;
    }

    /**
     * Tells to the current Archipelago (where motherNature stands) to add a student to its population
     * @param c the kind of student that will be added to the current Archipelago
     */
    public void addStudentToIsland(Creature c, int islandID){
        archipelagos.get(islandID).addStudent(c);
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
    public void moveMotherNatureWithSteps(int steps){
        int i = 1;
        while(steps > 0){
            if(archipelagos.get((positionOfMotherNature+i)%12) != null){
                steps--;
            }
            i++;
        }
        positionOfMotherNature = (positionOfMotherNature + (i-1)) % 12;
        // if there is one or more noEntry-tiles on this current archipelago, then one of theme will be removed
        archipelagos.get(positionOfMotherNature).removeNoEntryTile();
    }

    /**
     * This method set the new position of mother nature after mthe movement
     * @param destinationIsland_ID ID of the new island where mother nature is
     */
    public void setDestinationOfMotherNature(int destinationIsland_ID){
        positionOfMotherNature = destinationIsland_ID;

        archipelagos.get(positionOfMotherNature).removeNoEntryTile();
    }
}
