package junit_mimcore.data;

import junit_mimcore.factories.QsDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import mimcore.data.gpf.fitness.FitnessFunctionArbitraryLandscape;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Created by robertkofler on 30/10/2017.
 * http://junit.org/junit5/docs/current/user-guide/
 */
class Test_FitnessFunctionArbitraryLandscape {

    @Test
    @DisplayName("Interpolation between 0 and 1")
    void test1() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getLinearIncrease();


        assertEquals(ff.getFitness(null,0.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.2),0.2,0.0000000001);
        assertEquals(ff.getFitness(null,0.5),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,0.666),0.666,0.0000000001);
        assertEquals(ff.getFitness(null,1.0),1.0,0.0000000001);
    }


    @Test
    @DisplayName("Outside of boundaries 0 and 1")
    void test2() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getLinearIncrease();


        assertEquals(ff.getFitness(null,-1.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,2.0),1.0,0.0000000001);

    }


    @Test
    @DisplayName("Boundaries in rugged landscape")
    void test3() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,-1.0),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,20.0),1.0,0.0000000001);

    }

    @Test
    @DisplayName("Corner points in rugged landscape")
    void test4() {

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
    @DisplayName("Interpolation in rugged landscape")
    void test5() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,0.5),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,2.0),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,5.0),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,9.5),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,15.0),0.5,0.0000000001);
    }


    @Test
    @DisplayName("Interpolation in rugged landscape 2")
    void test6() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();


        assertEquals(ff.getFitness(null,3.0),0,0.0000000001);
        assertEquals(ff.getFitness(null,4.0),0.25,0.0000000001);
        assertEquals(ff.getFitness(null,5.0),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,6.0),0.75,0.0000000001);
        assertEquals(ff.getFitness(null,7.0),1.0,0.0000000001);
    }

    @Test
    @DisplayName("Interpolation in steeply increasing landscape")
    void test7() {


        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getHighIncrease();


        assertEquals(ff.getFitness(null,0.0),10.0,0.0000000001);
        assertEquals(ff.getFitness(null,1.0),20.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.5),15.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.75),17.5,0.0000000001);
        assertEquals(ff.getFitness(null,0.25),12.5,0.0000000001);
    }

}