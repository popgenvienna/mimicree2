package junit_mimcore.factories;

import mimcore.data.gpf.fitness.ArbitraryLandscapeEntry;
import mimcore.data.gpf.fitness.FitnessFunctionArbitraryLandscape;

import java.util.ArrayList;

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
}
