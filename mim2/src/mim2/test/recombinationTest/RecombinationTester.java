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

            RecombinationWindow rw1=new RecombinationWindow(null,0,4000000,RRRcMpMb.haldanetransform(rf,400000));
            RecombinationWindow rw2=new RecombinationWindow(null,0,2500000,RRRcMpMb.haldanetransform(rf,250000));
            RecombinationWindow rw3=new RecombinationWindow(null,0,1500000,RRRcMpMb.haldanetransform(rf,150000));
            RecombinationWindow rw4=new RecombinationWindow(null,0,1000000,RRRcMpMb.haldanetransform(rf,100000));
            RecombinationWindow rw5=new RecombinationWindow(null,0,500000,RRRcMpMb.haldanetransform(rf,50000));
            RecombinationWindow rw6=new RecombinationWindow(null,0,250000,RRRcMpMb.haldanetransform(rf,25000));
            RecombinationWindow rw7=new RecombinationWindow(null,0,150000,RRRcMpMb.haldanetransform(rf,15000));
            RecombinationWindow rw8=new RecombinationWindow(null,0,100000,RRRcMpMb.haldanetransform(rf,10000));
            for(int i=0; i<n; i++){

                int recs=rw1.getRecombinationEvents(r)+
                        rw2.getRecombinationEvents(r)+
                        rw3.getRecombinationEvents(r)+
                        rw4.getRecombinationEvents(r)+
                        rw5.getRecombinationEvents(r)+
                        rw6.getRecombinationEvents(r)+
                        rw7.getRecombinationEvents(r)+
                        rw8.getRecombinationEvents(r);
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
