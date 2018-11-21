package junit_mimcore.data;

import junit_mimcore.factories.QsDataFactory;
import static org.junit.Assert.*;

import mimcore.data.sex.Sex;
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

        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,0.0,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.2,m),0.2,0.0000000001);
        assertEquals(ff.getFitness(null,0.5,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,0.666,m),0.666,0.0000000001);
        assertEquals(ff.getFitness(null,1.0,m),1.0,0.0000000001);
    }

    @Test
    public void negative_landscape() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getNegPos();

        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,-2,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,-1.5,m),0.25,0.0000000001);
        assertEquals(ff.getFitness(null,-1,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,-0.5,m),0.75,0.0000000001);
        assertEquals(ff.getFitness(null,0,m),1,0.0000000001);
        assertEquals(ff.getFitness(null,1,m),0.5,0.0000000001);

    }

    @Test
    public void negative_landscape_benjaminWoelfl() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getErrorBenjamin();

        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,-0.005050505,m),0.988241199,0.000001);
        assertEquals(ff.getFitness(null,0,m),0.9879937,0.000001);
        assertEquals(ff.getFitness(null,0.4949495,m),0.9514924,0.000001);
        assertEquals(ff.getFitness(null,-0.4949495,m),0.9999975,0.000001);
        assertEquals(ff.getFitness(null,-0.5,m),0.999995,0.000001);
        assertEquals(ff.getFitness(null,0.5,m),0.950995,0.000001);
        assertEquals(ff.getFitness(null,0.489898989898,m),0.9519898984,0.000001);


    }


    @Test
    public void boundaries_of_linear_landscape() {

        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getLinearIncrease();

        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,-1.0,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,2.0,m),1.0,0.0000000001);

    }


    @Test
    public void outside_of_boundaries() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();

        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,-1.0,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,20.0,m),1.0,0.0000000001);

    }

    @Test
    public void boundaries_of_rugged_landscape() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();

        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,0.0,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,1.0,m),1.0,0.0000000001);
        assertEquals(ff.getFitness(null,3.0,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,7.0,m),1.0,0.0000000001);
        assertEquals(ff.getFitness(null,12.0,m),0.0,0.0000000001);
        assertEquals(ff.getFitness(null,18.0,m),1.0,0.0000000001);

    }

    @Test
    public void rugged_landscape() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();
        Sex m= Sex.Male;

        assertEquals(ff.getFitness(null,0.5,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,2.0,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,5.0,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,9.5,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,15.0,m),0.5,0.0000000001);
    }


    @Test
    public void rugged_landscape_v2() {

        // 0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getRugged();
        Sex m= Sex.Male;

        assertEquals(ff.getFitness(null,3.0,m),0,0.0000000001);
        assertEquals(ff.getFitness(null,4.0,m),0.25,0.0000000001);
        assertEquals(ff.getFitness(null,5.0,m),0.5,0.0000000001);
        assertEquals(ff.getFitness(null,6.0,m),0.75,0.0000000001);
        assertEquals(ff.getFitness(null,7.0,m),1.0,0.0000000001);
    }

    @Test
    public void steep_linear_increase() {


        FitnessFunctionArbitraryLandscape ff= QsDataFactory.getHighIncrease();
        Sex m= Sex.Male;

        assertEquals(ff.getFitness(null,0.0,m),10.0,0.0000000001);
        assertEquals(ff.getFitness(null,1.0,m),20.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.5,m),15.0,0.0000000001);
        assertEquals(ff.getFitness(null,0.75,m),17.5,0.0000000001);
        assertEquals(ff.getFitness(null,0.25,m),12.5,0.0000000001);
    }

}