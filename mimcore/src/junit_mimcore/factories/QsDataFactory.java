package junit_mimcore.factories;

import mimcore.data.gpf.fitness.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robertkofler on 30/10/2017.
 */
public class QsDataFactory {


    /**
     * Increase from 0/0 to 1/1
     * @return
     */
    public static FitnessFunctionArbitraryLandscape getLinearIncrease()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,1));
        e.add(new ArbitraryLandscapeEntry(0,0));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;

    }

    /**
     *  0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
     * @return
     */
    public static FitnessFunctionArbitraryLandscape getRugged()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(3,0));
        e.add(new ArbitraryLandscapeEntry(1,1));
        e.add(new ArbitraryLandscapeEntry(7,1));
        e.add(new ArbitraryLandscapeEntry(0,0));
        e.add(new ArbitraryLandscapeEntry(12,0));
        e.add(new ArbitraryLandscapeEntry(18,1));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;

    }


    /**
     * Increase from 0/10 to 1/20
     * @return
     */
    public static FitnessFunctionArbitraryLandscape getHighIncrease()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,20));
        e.add(new ArbitraryLandscapeEntry(0,10));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;
    }


    /**
     * phenotype betwen 0 and 1 (use 0.5)
     * Value changes at 1, 5 and 30 generations to fitness 1,5 and 30 respectively
     * @return
     */
    public static FitnessFunctionContainer getFitnessFunctionContainer()
    {
        HashMap<Integer,IFitnessCalculator> tos=new HashMap<Integer,IFitnessCalculator>();

        // 1
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,1));
        e.add(new ArbitraryLandscapeEntry(0,1));
        tos.put(1,new FitnessFunctionArbitraryLandscape(e));

        e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,5));
        e.add(new ArbitraryLandscapeEntry(0,5));
        tos.put(5,new FitnessFunctionArbitraryLandscape(e));

        e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,30));
        e.add(new ArbitraryLandscapeEntry(0,30));
        tos.put(30,new FitnessFunctionArbitraryLandscape(e));

        FitnessFunctionContainer ffc = new FitnessFunctionContainer(tos);
        return ffc;


    }


}
