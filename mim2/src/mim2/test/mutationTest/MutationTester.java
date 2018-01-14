package mim2.test.mutationTest;

import mimcore.data.*;
import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Mutator.MutatorGenomeWideRate;
import mimcore.data.gpf.fitness.FitnessCalculatorAllEqual;
import mimcore.data.sex.MatingFunctionRandomMating;
import mimcore.data.gpf.quantitative.GenotypeCalculatorAllEqual;
import mimcore.data.gpf.quantitative.PhenotypeCalculatorAllEqual;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.misc.Tuple;
import mimcore.data.recombination.CrossoverGenerator;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.recombination.RecombinationWindow;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.misc.MimicreeThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertkofler on 08/11/2017.
 */
public class MutationTester {

    public static Population getPopulation()
    {
        ArrayList<SNP> snps = new ArrayList<SNP>();

        // chr1 2500
        // chr2 1200
        // chr3 700
        // chr4 400
        // chr5 200
        for(int i=0; i<2500; i++) snps.add(new SNP(new GenomicPosition(Chromosome.getChromosome("chr1"),i+1),'A','A','T'));
        for(int i=0; i<1200; i++) snps.add(new SNP(new GenomicPosition(Chromosome.getChromosome("chr2"),i+1),'A','A','T'));
        for(int i=0; i<700; i++) snps.add(new SNP(new GenomicPosition(Chromosome.getChromosome("chr3"),i+1),'A','A','T'));
        for(int i=0; i<400; i++) snps.add(new SNP(new GenomicPosition(Chromosome.getChromosome("chr4"),i+1),'A','A','T'));
        for(int i=0; i<200; i++) snps.add(new SNP(new GenomicPosition(Chromosome.getChromosome("chr5"),i+1),'A','A','T'));
        SNPCollection sc=new SNPCollection(snps);

        ArrayList<Specimen> diploids=new ArrayList<Specimen>();
        for(int i=0; i<10; i++)
        {
            HaploidGenome hap1 =new HaploidGenome(new BitArrayBuilder(5000).getBitArray(),sc);
            HaploidGenome hap2 =new HaploidGenome(new BitArrayBuilder(5000).getBitArray(),sc);
            DiploidGenome dg=new DiploidGenome(hap1,hap2);
            Specimen s=new Specimen(1.0,1.0,1.0, dg);
            diploids.add(s);
        }

        return new Population(diploids);


    }






    public static void testMutations()
    {
        MimicreeThreadPool.setThreads(8);
        HashMap<GenomicPosition,Integer> stat=new HashMap<GenomicPosition,Integer>();


        Population pop=getPopulation();
        IMutator mutator=new MutatorGenomeWideRate(0.0002);
        RecombinationGenerator recgen=new RecombinationGenerator(new CrossoverGenerator(new ArrayList<RecombinationWindow>()),new ChromosomeDefinitionReader("").getRandomAssortmentGenerator());

        for(int i=0;i<250000; i++) {

           Population next= pop.getNextGeneration(new GenotypeCalculatorAllEqual(), new PhenotypeCalculatorAllEqual(),
                    new FitnessCalculatorAllEqual(), new MatingFunctionRandomMating(), recgen, mutator, pop.size());
           for(Specimen s: next.getSpecimen())
           {
               for(SNP snp: s.getGenome().getHaplotypeA().getSNPCollection().getSNPs())
               {
                   GenomicPosition pos=snp.genomicPosition();
                   if(!stat.containsKey(pos))stat.put(pos,0);
                   int currentcount=stat.get(pos);

                   boolean ahasAncestral=s.getGenome().getHaplotypeA().hasAncestral(pos);
                   boolean bhasAncestral=s.getGenome().getHaplotypeB().hasAncestral(pos);
                   if(ahasAncestral)currentcount++;
                   if(bhasAncestral)currentcount++;
                   stat.put(pos,currentcount);
               }
           }
        }

        ArrayList<Tuple<String,Integer>> toprint=new ArrayList<Tuple<String,Integer>>();

        for(Map.Entry<GenomicPosition,Integer> me: stat.entrySet())
        {

            toprint.add(new Tuple<String,Integer>(me.getKey().toString(),me.getValue()));
        }

        MutationWriter w=new MutationWriter("/Users/robertkofler/dev/mimicree2/theoreyvalidation/mutation/mutations.txt");
        w.write(toprint);




    }


}
