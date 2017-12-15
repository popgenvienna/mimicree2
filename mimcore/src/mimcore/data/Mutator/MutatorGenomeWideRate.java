package mimcore.data.Mutator;

import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.statistic.Poisson;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by robertkofler on 14/12/2017.
 */
public class MutatorGenomeWideRate implements IMutator {
    private final double mutationRate;

    public MutatorGenomeWideRate(double mutationRate)
    {
        this.mutationRate=mutationRate;
    }



    public BitArrayBuilder introduceMutations(BitArrayBuilder toMutate, SNPCollection snpCollection, Random random)
    {
        int snpCount=snpCollection.size();
        double mutationLambda=snpCount*mutationRate;
        int targetMutations= Poisson.getPoisson(mutationLambda,random);
        if(targetMutations>snpCount) targetMutations=snpCount;


        if(targetMutations<(snpCount/2)) return getLowMutationCount(toMutate, targetMutations, snpCount, random);
        else return getHighMutationCount(toMutate, targetMutations, snpCount, random);

    }

    private BitArrayBuilder getLowMutationCount(BitArrayBuilder toMutate, int targetMutations, int snpCount, Random random)
    {
        HashSet<Integer> mutpos = new HashSet<Integer>();
        while(mutpos.size()<targetMutations)
        {
            int targetPos=random.nextInt(snpCount);
            mutpos.add(targetPos);
        }
        for(int i: mutpos)
        {
            toMutate.flipBit(i);
        }
        return toMutate;
    }

    private BitArrayBuilder getHighMutationCount(BitArrayBuilder toMutate, int targetMutations, int snpCount, Random random)
    {
        // great idea; if the number of mutations gets very high, ie. > 0.5*sites
        // instead of sampling the mutated sites, I just randomly sample the not mutated sites
        int notMutated=snpCount-targetMutations;
        HashSet<Integer> notMutpos = new HashSet<Integer>(); // contains all not mutated positions, i.e protected positions
        while(notMutpos.size()<notMutated)
        {
            int targetPos=random.nextInt(snpCount);
            notMutpos.add(targetPos);
        }

        //check every genomic site
        for(int i=0; i<snpCount;i++)
        {
            // mutate if the position is not protected
            if(!notMutpos.contains(i)) toMutate.flipBit(i);
        }
        return toMutate;
    }



}
