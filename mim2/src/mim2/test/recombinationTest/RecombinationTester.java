package mim2.test.recombinationTest;

import mimcore.data.misc.Tuple;
import mimcore.data.recombination.RecombinationWindow;
import mimcore.io.recombination.RRRRecFraction;
import mimcore.io.recombination.RRRcMpMb;
import mimcore.io.recombination.RecombinationRateReader;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robertkofler on 08/11/2017.
 */
public class RecombinationTester {

    public static void centiMorgan()
    {
        double[] rfs={2.5, 5.0, 7.5, 10.0, 12.5, 15.0, 17.5, 20.0, 22.5, 25.0, 27.5, 30.0, 32.5, 35.0, 37.5, 40.0 , 42.5, 45.,47.5};
        int n=10000;
        Random r=new Random();
        ArrayList<Tuple<Double,Integer>> tups=new ArrayList<Tuple<Double,Integer>>();

        for(double rf: rfs)
        {
            double lambda = RRRcMpMb.haldanetransform(rf,100000);
            RecombinationWindow rw=new RecombinationWindow(null,0,1,lambda);
            for(int i=0; i<n; i++){

                int recs=0;
                for(int k=0; k<10; k++) recs+=rw.getRecombinationEvents(r); // ten windows with size 100kb
                tups.add(new Tuple<Double,Integer>(rf,recs));


            }
        }

        RecombinationWriter w=new RecombinationWriter("/Users/robertkofler/dev/mimicree2/theoreyvalidation/recombination/centiMorgan.txt");
        w.write(tups);


    }

    public static void haldanesMapFunction()
    {
        double[] rfs={0.025,0.05,0.075,0.1,0.125,0.15,0.175,0.2,0.225,0.25,0.275,0.3,0.325,0.35,0.375,0.4,0.425,0.45,0.475};
        int n=10000;
        Random r=new Random();
        ArrayList<Tuple<Double,Integer>> tups=new ArrayList<Tuple<Double,Integer>>();

        for(double rf: rfs)
        {
            double lambda = RRRRecFraction.haldane1919mapFunction(rf);
            RecombinationWindow rw=new RecombinationWindow(null,0,1,lambda);
            for(int i=0; i<n; i++){
                int recs=rw.getRecombinationEvents(r);
                tups.add(new Tuple<Double,Integer>(rf,recs));


            }
        }

        RecombinationWriter w=new RecombinationWriter("/Users/robertkofler/dev/mimicree2/theoreyvalidation/recombination/haldane1919.txt");
        w.write(tups);


    }

    public static void generatePoisson()
    {
        //double[] rfs={0.025,0.05,0.075,0.1,0.125,0.15,0.175,0.2,0.225,0.25,0.275,0.3,0.325,0.35,0.375,0.4,0.425,0.45,0.475};
        double[] poissonvalues={0.5,1,4,10};
        int n=30000;
        Random r=new Random();
        ArrayList<Tuple<Double,Integer>> tups=new ArrayList<Tuple<Double,Integer>>();

        for(double d: poissonvalues)
        {
            RecombinationWindow rw=new RecombinationWindow(null,0,1,d);
            for(int i=0; i<n; i++){
                int recs=rw.getRecombinationEvents(r);
                tups.add(new Tuple<Double,Integer>(d,recs));


            }
        }

        RecombinationWriter w=new RecombinationWriter("/Users/robertkofler/dev/mimicree2/theoreyvalidation/recombination/poisson.txt");
        w.write(tups);


    }
}
