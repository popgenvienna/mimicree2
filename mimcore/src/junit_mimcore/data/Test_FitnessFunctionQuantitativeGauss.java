package junit_mimcore.data;

import junit_mimcore.factories.QsDataFactory;
import mimcore.data.gpf.fitness.FitnessFunctionArbitraryLandscape;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import static org.junit.Assert.*;

import mimcore.data.sex.Sex;
import org.junit.Test;

/**
 * Created by robertkofler on 30/10/2017.
 * Validated with the results of the following R function
 *
 * getfitness<-function(min,max,mean,stddev,phenotype)
 {
 delta<-max-min
 scale<-dnorm(mean,mean,stddev)
 rawfit<-dnorm(phenotype,mean,stddev)
 scalefit<-(rawfit/scale)*delta
 return(min+scalefit)
 }
 *
 * eg. getfitness(0.0,2.0 , 20,10 ,0)
 */



public class Test_FitnessFunctionQuantitativeGauss {

    @Test
    public void correct_fitness_normal_phenotypes() {

        FitnessFunctionQuantitativeGauss ff=new FitnessFunctionQuantitativeGauss(0.7,1.2,2,2);
        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,2.0,m),1.2,0.00001);
        assertEquals(ff.getFitness(null,1.0,m),1.141248,0.00001);
        assertEquals(ff.getFitness(null,0,m),1.003265,0.00001);
    }


    @Test
    public void correct_fitness_extreme_phenotypes() {

        Sex m= Sex.Male;
        FitnessFunctionQuantitativeGauss ff=new FitnessFunctionQuantitativeGauss(0.7,1.2,2,2);
        assertEquals(ff.getFitness(null,100.0,m),0.7,0.00001);
        assertEquals(ff.getFitness(null,-100.0,m),0.7,0.00001);
    }



    @Test
    public void correct_fitness_flat_gaussian() {

        FitnessFunctionQuantitativeGauss ff=new FitnessFunctionQuantitativeGauss(0.0,2,20,10);
        Sex m= Sex.Male;
        assertEquals(ff.getFitness(null,20.0,m),2,0.00001);
        assertEquals(ff.getFitness(null,0.0,m),0.2706706,0.00001);
        assertEquals(ff.getFitness(null,40.0,m),0.2706706,0.00001);
        assertEquals(ff.getFitness(null,50.0,m),0.02221799,0.00001);

    }
}
