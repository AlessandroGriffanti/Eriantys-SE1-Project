package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Realm{
    private int positionOfMotherNature; //it corresponds to the archipelago's ID
    private ArrayList<Archipelago> archipelagos;
    private ArrayList<CloudTile> cloudRegion;

    //potremmo usare questo attributo per tenere il conto delle volte che viene chiamato il costruttore, non deve mai superare l'uno
    private int constructorCnt;

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

        constructorCnt++; //maybe we could avoid it
    }

    public void unifyArchipelago(int ID_1, int ID_2){

        Archipelago a1 = archipelagos.get(ID_1);
        Archipelago a2 = archipelagos.get(ID_2);

        //the idea is to delete a2 and transfer all its attributes'values into a1

        //update numberOfIslands in a1
        a1.addIslands(a2.getNumberOfIslands());

        //update noEntryTiles in a1
        a1.addNoEntryTile(a2.getNoEntryTiles());

        //update studentsPopulation in a1
        for(Creature c: Creature.values()){
            a1.addStudents(c, a2.getStudentsOfType(c));
        }

        //cancellando a2 dall'Array perdiamo la corrispondenza tra ID e indice dell'Array;
        //quindi possono sostituirlo con null in modo da preservare la corrispondenza.
        archipelagos.set(ID_2, null);
    }

    public ArrayList<Creature> takeStudentsFromCloud(int cloudID){

        ArrayList<Creature> temp = new ArrayList<Creature>();
        CloudTile currentCloud = cloudRegion.get(cloudID);
        temp = currentCloud.takeStudents();

        return temp;
    }

}
