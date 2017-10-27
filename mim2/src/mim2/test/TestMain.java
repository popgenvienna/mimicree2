package mim2.test;

import mimcore.data.DiploidGenome;
import mimcore.data.gpf.fitness.ArbitraryLandscapeEntry;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import mimcore.data.statistic.Gaussian;

import java.util.ArrayList;

/**
 * Created by robertkofler on 25/10/2017.
 */
public class TestMain {

    public codeToTest()
    {

    }

    public static double binarySearchAndInterpolateFitness(double phenotypicValue, ArrayList<ArbitraryLandscapeEntry> tosearch )
    {

                int low = 0;
                int high = tosearch.size() - 1;

                while(high >= low) {
                        int middle = (low + high) / 2;
                       if(tosearch.get(middle).getPhenotypicValue() == phenotypicValue) {
                           return tosearch.get(middle).getFitness();
                           }
                         if(tosearch.get(middle).getPhenotypicValue() < phenotypicValue) {
                                low = middle + 1;
                           }
                        if(tosearch.get(middle).getPhenotypicValue() > phenotypicValue) {
                                high = middle - 1;
                            }
                   }
               return false;
    }


    public static void codeToTestFitnessFunctionGauss()
    {
        FitnessFunctionQuantitativeGauss ffg=new FitnessFunctionQuantitativeGauss(0.7,1.2,6,2);
        for(double i=0.0; i<12.0; i+=0.1)
        {
            double res=ffg.getFitness(null,i);
            System.out.printf("%f\t%f\n",i,res);
        }

    }
}
