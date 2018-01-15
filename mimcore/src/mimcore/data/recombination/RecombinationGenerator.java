package mimcore.data.recombination;

import mimcore.data.*;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.sex.Sex;

import java.util.ArrayList;
import java.util.Random;


/**
 * Summarizes the recombination rate and the random assortment of chromosomes
 * @author robertkofler
 */
public class RecombinationGenerator {
	private final CrossoverGenerator recRate;
	private final RandomAssortmentGenerator assortGen;
	
	public RecombinationGenerator(CrossoverGenerator recRate, RandomAssortmentGenerator assortGen)
	{
		this.recRate=recRate;
		this.assortGen=assortGen;
	}	
	
	/**
	 * Obtain a new randomly generated recombination event, including crossovers and random assortments
	 * This method will return a new random recombination event upon each call
	 * @return
	 */
	public RecombinationEvent getRecombinationEvent(Sex sex, Random random)
	{
		CrossoverEvents crossovers=recRate.generateCrossoverEvents(random, sex);
		RandomAssortment randas=assortGen.getRandomAssortment(crossovers,random);
		return new RecombinationEvent(randas,crossovers);
	}

	public boolean isValid(ArrayList<DiploidGenome> genomes)
	{
		SNPCollection snpcol=genomes.get(0).getHaplotypeA().getSNPCollection();
		for(Chromosome chr: snpcol.getChromosomes() )
		{
			if(!assortGen.containsChromosome(chr)) return false;
		}
		return true;
	}
	
	

}
