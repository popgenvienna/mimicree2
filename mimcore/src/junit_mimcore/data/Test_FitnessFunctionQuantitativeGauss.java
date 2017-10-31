package junit_mimcore.data;

import junit_mimcore.factories.QsDataFactory;
import mimcore.data.gpf.fitness.FitnessFunctionArbitraryLandscape;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("Basic")
    void test1() {

        FitnessFunctionQuantitativeGauss ff=new FitnessFunctionQuantitativeGauss(0.7,1.2,2,2);
        assertEquals(ff.getFitness(null,2.0),1.2,0.00001);
        assertEquals(ff.getFitness(null,1.0),1.141248,0.00001);
        assertEquals(ff.getFitness(null,0),1.003265,0.00001);
    }


    @Test
    @DisplayName("Outlier")
    void test2() {

        FitnessFunctionQuantitativeGauss ff=new FitnessFunctionQuantitativeGauss(0.7,1.2,2,2);
        assertEquals(ff.getFitness(null,100.0),0.7,0.00001);
        assertEquals(ff.getFitness(null,-100.0),0.7,0.00001);
    }



    @Test
    @DisplayName("Basic large variance")
    void test3() {

        FitnessFunctionQuantitativeGauss ff=new FitnessFunctionQuantitativeGauss(0.0,2,20,10);
        assertEquals(ff.getFitness(null,20.0),2,0.00001);
        assertEquals(ff.getFitness(null,0.0),0.2706706,0.00001);
        assertEquals(ff.getFitness(null,40.0),0.2706706,0.00001);
        assertEquals(ff.getFitness(null,50.0),0.02221799,0.00001);

    }
}
