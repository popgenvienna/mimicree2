package mimcore.data.gpf.fitness;

import mimcore.io.misc.ISummaryWriter;

import java.util.HashMap;

/**
 * Created by robertkofler on 27/10/2017.
 */
public class FitnessFunctionContainer {
    private final HashMap<Integer,IFitnessCalculator> fitcalcs;

    public FitnessFunctionContainer(HashMap<Integer,IFitnessCalculator> fitcalcs)
    {
        if(fitcalcs.size()<1) throw new IllegalArgumentException("At least one fitness calculator needs to be provided for fitness function containers");
        this.fitcalcs=new HashMap<Integer,IFitnessCalculator>(fitcalcs);
    }



    public IFitnessCalculator getFitnessCalculator(int replicate, int generation)
    {
            if(replicate<1 || generation<1) throw new IllegalArgumentException("Generations and replicates must be larger than zero");
            for(int i=generation; i>0; i--)
            {
                if(this.fitcalcs.containsKey(i)) return this.fitcalcs.get(i);

            }
            throw new IllegalArgumentException("Illegal operation, no entry found for generation "+generation);
    }
}
