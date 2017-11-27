package junit_mimcore.data;

import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDimRet;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDirectionalSelection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by robertkofler on 30/10/2017.
 * Validated with the results of the following R function
 directional<-function(min,max,s,r,beta,x)
 {
 delta<-max-min

 term1<-1+s*exp(r*(x+beta))
 term1<-delta/(term1^(1/s))
 w<-min+term1
 df<-data.frame(f=w,x=x)
 return(df)
 }
 */



public class Test_FitnessFunctionDirectionalSelection {

    @Test
    public void correct_fitness_normal_phenotypes() {

        FitnessFunctionQuantitativeDirectionalSelection ff=new FitnessFunctionQuantitativeDirectionalSelection(0.3,1.2,1,-0.4,0);
        assertEquals(ff.getFitness(null,-5.0),0.4072826,0.00001);
        assertEquals(ff.getFitness(null,0.0),0.7500000,0.00001);
        assertEquals(ff.getFitness(null,5.0),1.0927174,0.00001);
    }


    @Test
    public void correct_fitness_extremes() {

        FitnessFunctionQuantitativeDirectionalSelection ff=new FitnessFunctionQuantitativeDirectionalSelection(0.3,1.2,1,-0.4,0);
        assertEquals(ff.getFitness(null,-20.0),0.3003018,0.00001);
        assertEquals(ff.getFitness(null,20.0),1.1996982,0.00001);
      }

    @Test
    public void correct_fitness_pos_r() {

        FitnessFunctionQuantitativeDirectionalSelection ff=new FitnessFunctionQuantitativeDirectionalSelection(0.3,1.2,1,0.4,0);
        assertEquals(ff.getFitness(null,5.0),0.4072826,0.00001);
        assertEquals(ff.getFitness(null,0.0),0.7500000,0.00001);
        assertEquals(ff.getFitness(null,-5.0),1.0927174,0.00001);
    }

    @Test
    public void correct_fitness_beta_shift() {

        FitnessFunctionQuantitativeDirectionalSelection ff=new FitnessFunctionQuantitativeDirectionalSelection(0.3,1.2,1,-0.4,5);
        assertEquals(ff.getFitness(null,-5.0),0.750000,0.00001);
        assertEquals(ff.getFitness(null,0.0),1.0927174,0.00001);
        assertEquals(ff.getFitness(null,5.0),1.183812,0.00001);
    }



}
