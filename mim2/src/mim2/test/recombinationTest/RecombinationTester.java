package mim2.test.recombinationTest;

import mimcore.data.misc.Tuple;
import mimcore.data.recombination.RecombinationWindow;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robertkofler on 08/11/2017.
 */
public class RecombinationTester {
    public static void generatePoisson()
    {
        double[] rfs={0.025,0.05,0.075,0.1,0.125,0.15,0.175,0.2,0.225,0.25,0.275,0.3,0.325,0.35,0.375,0.4,0.425,0.45,0.475};
        int n=10000;
        Random r=new Random();
        ArrayList<Tuple<Double,Integer>> tups=new ArrayList<Tuple<Double,Integer>>();

        for(double d: rfs)
        {
            RecombinationWindow rw=new RecombinationWindow(null,0,1,d);
            for(int i=0; i<n; i++){
                int recs=rw.getRecombinationEvents(r);
                tups.add(new Tuple<Double,Integer>(d,recs));


            }
        }

        RecombinationWriter w=new RecombinationWriter("/Users/robertkofler/dev/mimicree2/theoreyvalidation/recombination/recombination.txt");
        w.write(tups);


    }
}
