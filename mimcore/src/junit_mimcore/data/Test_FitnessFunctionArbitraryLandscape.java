package junit_mimcore.data;

import junit_mimcore.factories.QsDataFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import mimcore.data.gpf.fitness.FitnessFunctionArbitraryLandscape;




/**
 * Created by robertkofler on 30/10/2017.
 * http://junit.org/junit5/docs/current/user-guide/
 */
public class Test_FitnessFunctionArbitraryLandscape {

    @Test
    public void linear_landscape() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getLinearIncrease();


        assertEquals(ff.getFitness(null,0.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.2),0.2,0.0000000001);
        assertEquals(ff.getFitness(null,0.5),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,0.666),0.666,0.0000000001);
        assertEquals(ff.getFitness(null,1.0),1.0,0.0000000001);
    }


    @Test
    public void boundaries_of_linear_landscape() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getLinearIncrease();


        assertEquals(ff.getFitness(null,-1.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,2.0),1.0,0.0000000001);

    }


    @Test
    public void outside_of_boundaries() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,-1.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,20.0),1.0,0.0000000001);

    }

    @Test
    public void boundaries_of_rugged_landscape() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,0.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,1.0),1.0,0.0000000001);
        assertEquals(ff.getFitness(null,3.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,7.0),1.0,0.0000000001);
        assertEquals(ff.getFitness(null,12.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,18.0),1.0,0.0000000001);

    }

    @Test
    public void rugged_landscape() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,0.5),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,2.0),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,5.0),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,9.5),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,15.0),0.5,0.0000000001);
    }


    @Test
    public void rugged_landscape_v2() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,3.0),0,0.0000000001);
        assertEquals(ff.getFitness(null,4.0),0.25,0.0000000001);
        assertEquals(ff.getFitness(null,5.0),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,6.0),0.75,0.0000000001);
        assertEquals(ff.getFitness(null,7.0),1.0,0.0000000001);
    }

    @Test
    public void steep_linear_increase() {


        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getHighIncrease();


        assertEquals(ff.getFitness(null,0.0),10.0,0.0000000001);
        assertEquals(ff.getFitness(null,1.0),20.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.5),15.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.75),17.5,0.0000000001);
        assertEquals(ff.getFitness(null,0.25),12.5,0.0000000001);
    }

}