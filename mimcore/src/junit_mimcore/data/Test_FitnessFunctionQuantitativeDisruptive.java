package junit_mimcore.data;

import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDisruptive;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 30/10/2017.
 * Validated with the results of the following R function
 *
 disruptive<-function(min,max,mean,stddev,phenotype)
 {
 delta<-max-min
 scale<-dnorm(mean,mean,stddev)
 rawfit<-dnorm(phenotype,mean,stddev)
 scalefit<-(rawfit/scale)*delta
 fit<-max-scalefit
 df<-data.frame(y=fit,x=phenotype)
 return(df)

 }
 */



public class Test_FitnessFunctionQuantitativeDisruptive {

    @Test
    public void correct_valey() {

        FitnessFunctionQuantitativeDisruptive ff=new FitnessFunctionQuantitativeDisruptive(0.1,1.2,0,2);
        assertEquals(ff.getFitness(null,-4.0),1.0511312,0.00001);
        assertEquals(ff.getFitness(null,-2.0),0.5328163,0.00001);
        assertEquals(ff.getFitness(null,0.0),0.1000000,0.00001);
        assertEquals(ff.getFitness(null,2.0),0.5328163,0.00001);
        assertEquals(ff.getFitness(null,4.0),1.0511312,0.00001);
    }



    @Test
    public void correct_fitness_extreme_phenotypes() {

        FitnessFunctionQuantitativeDisruptive ff=new FitnessFunctionQuantitativeDisruptive(0.7,1.2,2,2);
        assertEquals(ff.getFitness(null,100.0),1.2,0.00001);
        assertEquals(ff.getFitness(null,-100.0),1.2,0.00001);
    }



    @Test
    public void correct_fitness_flat_disruptive() {

        FitnessFunctionQuantitativeDisruptive ff=new FitnessFunctionQuantitativeDisruptive(0.0,2,20,10);
        assertEquals(ff.getFitness(null,20.0),0.000000,0.00001);
        assertEquals(ff.getFitness(null,0.0),1.729329,0.00001);
        assertEquals(ff.getFitness(null,40.0),1.729329,0.00001);
        assertEquals(ff.getFitness(null,50.0),1.977782,0.00001);

    }
}
