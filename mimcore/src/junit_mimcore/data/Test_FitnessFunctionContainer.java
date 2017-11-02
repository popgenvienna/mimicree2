package junit_mimcore.data;

import junit_mimcore.factories.QsDataFactory;
import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Created by robertkofler on 30/10/2017.
 */
public class Test_FitnessFunctionContainer {
    @Test
    public void test1() {
        // phenotype betwen 0 and 1 (use 0.5)
        // Value changes at 1, 5 and 30 generations to fitness 1,5 and 30 respectively

        FitnessFunctionContainer ffc= QsDataFactory.getFitnessFunctionContainer();

        assertEquals(ffc.getFitnessCalculator(1,1).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(2,1).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(10,1).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(50,1).getFitness(null,0.5),1.0,0.00000001);

    }


    @Test
    public void test2() {
        // phenotype betwen 0 and 1 (use 0.5)
        // Value changes at 1, 5 and 30 generations to fitness 1,5 and 30 respectively

        FitnessFunctionContainer ffc= QsDataFactory.getFitnessFunctionContainer();

        assertEquals(ffc.getFitnessCalculator(1,1).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,2).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,3).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,4).getFitness(null,0.5),1.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,5).getFitness(null,0.5),5.0,0.00000001);

    }
    @Test
    public void test3() {
        // phenotype betwen 0 and 1 (use 0.5)
        // Value changes at 1, 5 and 30 generations to fitness 1,5 and 30 respectively

        FitnessFunctionContainer ffc= QsDataFactory.getFitnessFunctionContainer();

        assertEquals(ffc.getFitnessCalculator(1,5).getFitness(null,0.5),5.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,6).getFitness(null,0.5),5.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,10).getFitness(null,0.5),5.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,20).getFitness(null,0.5),5.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,29).getFitness(null,0.5),5.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,30).getFitness(null,0.5),30.0,0.00000001);

    }


    @Test
    public void test4() {
        // phenotype betwen 0 and 1 (use 0.5)
        // Value changes at 1, 5 and 30 generations to fitness 1,5 and 30 respectively

        FitnessFunctionContainer ffc= QsDataFactory.getFitnessFunctionContainer();

        assertEquals(ffc.getFitnessCalculator(1,30).getFitness(null,0.5),30.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,31).getFitness(null,0.5),30.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,50).getFitness(null,0.5),30.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,100).getFitness(null,0.5),30.0,0.00000001);
        assertEquals(ffc.getFitnessCalculator(1,1000).getFitness(null,0.5),30.0,0.00000001);


    }

}
