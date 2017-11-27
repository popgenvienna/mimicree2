package mim2.test;

import mim2.test.recombinationTest.RecombinationTester;
import mimcore.data.DiploidGenome;
import mimcore.data.gpf.fitness.ArbitraryLandscapeEntry;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import mimcore.data.statistic.Gaussian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by robertkofler on 25/10/2017.
 */
public class TestMain {

    public static void testMain()
    {
        int bla=0;
    }
    public static void codeToTest()
    {
        ArrayList<ArbitraryLandscapeEntry> entries=new ArrayList<ArbitraryLandscapeEntry>();
        entries.add(new ArbitraryLandscapeEntry(2,0.9));
        entries.add(new ArbitraryLandscapeEntry(1,0.8));
        entries.add(new ArbitraryLandscapeEntry(0,0.7));
        entries.add(new ArbitraryLandscapeEntry(3,1.0));
        entries.add(new ArbitraryLandscapeEntry(4,1.1));
        entries.add(new ArbitraryLandscapeEntry(5,1.2));
        entries.add(new ArbitraryLandscapeEntry(6,1.3));
        entries.add(new ArbitraryLandscapeEntry(7,1.4));

        double fitness=binarySearchAndInterpolateFitness(0.75,entries);

    }

    public static double binarySearchAndInterpolateFitness(double phenotypicValue, ArrayList<ArbitraryLandscapeEntry> tosearch )
    {
                //sort ascending
                Collections.sort(tosearch,new Comparator<ArbitraryLandscapeEntry>() {
                    @Override
                    public int compare(ArbitraryLandscapeEntry i1, ArbitraryLandscapeEntry i2) {
                        if(i1.getPhenotypicValue() < i2.getPhenotypicValue())return -1;
                        if(i1.getPhenotypicValue() > i2.getPhenotypicValue() )return 1;
                        return 0;
                    }
                });

                //TODO DONT forget to use the fitness of the smallest phenotypic value or the largest for values exceeding the boundaries
                // Implement urgent unit tests of this

                // lowest and highest index
                int low = 0;
                int high = tosearch.size() - 1;

                while(high > low) {

                    int middle = (low + high) / 2;


                    if(tosearch.get(middle).getPhenotypicValue() == phenotypicValue) {
                        // Jackpot: we found the value; just return the fitness
                           return tosearch.get(middle).getFitness();
                    }
                    else if(tosearch.get(middle).getPhenotypicValue() < phenotypicValue) {
                                low = middle + 1;
                    }
                    else if(tosearch.get(middle).getPhenotypicValue() > phenotypicValue) {
                                high = middle - 1;
                    }
                    else throw new IllegalArgumentException("OK something went terribly wrong during binary search");
                }

                assert low==high;

                //if tosearch.get(low).getPhenotypicValue()<phenotypicValue interpolate( low, low+1);
                //else if tosearch.get(low).getPhenotypicValue()>phenotypicValue interpolate( low-1, low);
                // validate super careful with unit tests

                return 0.0;
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
