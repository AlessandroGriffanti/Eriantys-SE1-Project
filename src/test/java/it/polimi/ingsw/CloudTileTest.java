package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Creature;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class CloudTileTest {
    CloudTile cloud;
    Bag bag;

    /**
     * Creates a cloud and the bag to be used during the testing.
     */
    @Before
    public void init(){
        bag = new Bag();
        cloud = new CloudTile(0, 2, bag);

        System.out.println("Initial state of the cloud: " + cloud.getStudents().toString());
    }

    /**
     * We test the correct functioning of the takeStudents method considering a generic case.
     * To control if the test is passed successfully we must read the terminal output:
     * - "initial students" must correspond to "students taken"
     * - assert: in the bag there should be 6 students less (120 - 6 = 114)
     */
    @Test
    public void takeStudentsTest(){
        ArrayList<Creature> takenStudents = cloud.takeStudents();
        System.out.println("Students taken from the cloud" + takenStudents);

        System.out.println("New set of students: " + cloud.getStudents());
        System.out.println("In the bag there are still:  " + bag.getNumberOfRemainingStudents() + "students");
        assertTrue(bag.getNumberOfRemainingStudents() == 120 - 6);
    }
}
