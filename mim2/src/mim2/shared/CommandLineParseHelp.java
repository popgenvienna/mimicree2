package mim2.shared;

import java.util.ArrayList;

/**
 * Created by robertkofler on 25/10/2017.
 */
public class CommandLineParseHelp {

    public static SimulationMode parseOutputGenerations(String outputGenerationsRaw)
    {
        // Parse a String consistent of a comma-separated list of numbers, to a array of integers
        SimulationMode simMode;
        String [] tmp;
        if(outputGenerationsRaw.contains(","))
        {
            tmp=outputGenerationsRaw.split(",");
        }
        else
        {
            tmp=new String[1];
            tmp[0]=outputGenerationsRaw;
        }
        // Convert everything to int
        ArrayList<Integer> ti=new ArrayList<Integer>();
        for(String s :tmp)
        {
            ti.add(Integer.parseInt(s));
        }
        simMode=SimulationMode.Timestamp;
        simMode.setTimestamps(ti);
        return simMode;
    }
}
