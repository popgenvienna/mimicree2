package junit_mimcore.data;

import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDimRet;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 30/10/2017.
 * Validated with the results of the following R function
 *
 dimretepi<-function(min,max,alpha,beta,x)
 {
 delta<-max-min
 part1<-1-1/exp(alpha*(x+beta))
 f<-min+delta*part1
 df<-data.frame(f=f,x=x)
 df[df$f<min,]$f<-min # set min as the minimum
 return(df)
 }
 * eg. dimretepi(0.0, 1.2, 0.2, 0.0)
 */



public class Test_FitnessFunctionDiminishingReturns {

    @Test
    public void correct_fitness_normal_phenotypes() {

        FitnessFunctionQuantitativeDimRet ff=new FitnessFunctionQuantitativeDimRet(0.0,1.2,0.2,0);
        assertEquals(ff.getFitness(null,1.0),0.2175231,0.00001);
        assertEquals(ff.getFitness(null,10.0),1.037598,0.00001);
        assertEquals(ff.getFitness(null,20),1.178021,0.00001);
    }


    @Test
    public void correct_fitness_min_fit() {

        FitnessFunctionQuantitativeDimRet ff=new FitnessFunctionQuantitativeDimRet(0.0,1.2,0.2,0);
        assertEquals(ff.getFitness(null,-1.0),0.0,0.00001);
        assertEquals(ff.getFitness(null,-10.0),0.0,0.00001);
        assertEquals(ff.getFitness(null,-20),0.0,0.00001);
    }

    @Test
    public void correct_fitness_min_fit_shift() {

        FitnessFunctionQuantitativeDimRet ff=new FitnessFunctionQuantitativeDimRet(0.5,1.2,0.2,0);
        assertEquals(ff.getFitness(null,-1.0),0.5,0.00001);
        assertEquals(ff.getFitness(null,-10.0),0.5,0.00001);
        assertEquals(ff.getFitness(null,-20),0.5,0.00001);
    }

    @Test
    public void correct_fitness_normal_fit_shift() {

        FitnessFunctionQuantitativeDimRet ff=new FitnessFunctionQuantitativeDimRet(0.5,1.2,0.2,0);
        assertEquals(ff.getFitness(null,1.0),0.6268885,0.00001);
        assertEquals(ff.getFitness(null,10.0),1.1052653,0.00001);
        assertEquals(ff.getFitness(null,20),1.1871791,0.00001);
    }



}
