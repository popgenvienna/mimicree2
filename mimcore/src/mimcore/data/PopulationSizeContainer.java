package mimcore.data;

import mimcore.data.gpf.fitness.IFitnessCalculator;

import java.util.HashMap;

/**
 * Created by robertkofler on 27/10/2017.
 */
public class PopulationSizeContainer {
    private final HashMap<Integer,Integer> popsizes;

    public PopulationSizeContainer(HashMap<Integer,Integer> popsizes)
    {
        if(popsizes.size()<1) throw new IllegalArgumentException("At least one population size needs to be provided for fitness function containers");
        if(!popsizes.containsKey(1)) throw new IllegalArgumentException("A population size must be provided for the first generation");
        this.popsizes=new HashMap<Integer,Integer>(popsizes);
    }


    public PopulationSizeContainer(int basepopsize)
    {
        HashMap<Integer,Integer> tostore =new HashMap<Integer,Integer>();
        tostore.put(1,basepopsize);
        popsizes=tostore;
    }



    public int getPopulationSize( int generation,int replicate)
    {
            if(replicate<1 || generation<1) throw new IllegalArgumentException("Generations and replicates must be larger than zero");
            for(int i=generation; i>0; i--)
            {
                if(this.popsizes.containsKey(i)) return this.popsizes.get(i);

            }
            throw new IllegalArgumentException("Illegal operation, no entry found for generation "+generation);
    }
}
