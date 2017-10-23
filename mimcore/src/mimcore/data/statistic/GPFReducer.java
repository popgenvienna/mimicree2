package mimcore.data.statistic;

import mimcore.data.Population;
import mimcore.data.Specimen;

import java.util.ArrayList;

/**
 * Created by robertkofler on 18/10/2017.
 */
public class GPFReducer {
    private final Population pop;
    private final int generation;
    private final int replicate;

    public GPFReducer(Population pop, int replicate, int generation)
    {
        this.pop=pop;
        this.replicate=replicate;
        this.generation=generation;
    }


    public GPFCollection reduce()
    {
        ArrayList<GPF> gpfs=new ArrayList<GPF>();

        for(Specimen s :this.pop.getSpecimen())
        {
            GPF toadd=new GPF(s.quantGenotype(),s.quantPhenotype(),s.fitness());
            gpfs.add(toadd);
        }
        return new GPFCollection(gpfs,this.replicate,this.generation);
    }
}
