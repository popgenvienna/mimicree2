package qmimcore.data.recombination;

import qmimcore.data.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Contains information about linkage of chromosomes, e.g.: which chromosome arms constitute a chromsomse
 * Immutable
 * @author robertkofler
 *
 */
public class RandomAssortmentGenerator {
	
	private ArrayList<ArrayList<Chromosome>> chroms;
	public RandomAssortmentGenerator(ArrayList<ArrayList<Chromosome>> chromosomes)
	{

		// in order to have the thing immutable  create a deep copy;
		ArrayList<ArrayList<Chromosome>> toret=new ArrayList<ArrayList<Chromosome>>();
		for(ArrayList<Chromosome> list:chromosomes)
		{
			toret.add(new ArrayList<Chromosome>(list));
		}
		this.chroms=toret;
	}
	
	/**
	 * Create a random assortment of chromosomes;
	 * @param crossoverEvents
	 * @return
	 */
	public RandomAssortment getRandomAssortment(CrossoverEvents crossoverEvents,Random random)
	{
		HashMap<Chromosome,Boolean> randAssort=new HashMap<Chromosome,Boolean>();
		for(ArrayList<Chromosome> chromlist:chroms)
		{
			// Assign a random haplotype to the first chromosome in the list
			assert(chromlist.size()>0);
			boolean randGenotype= random.nextDouble()<0.5?true:false;
			Chromosome c=chromlist.get(0);
			randAssort.put(c, randGenotype);
			
			// if there are more than one chromosome in the list the haplotype of the current chromosomes (i for i>0) is determined by the 
			// crossover events of the previous chromosome (i-1)
			if(chromlist.size()>1)
			{
				
				for(int i=1; i<chromlist.size(); i++)
				{
					Chromosome activeChr=chromlist.get(i);
					Chromosome prevChr=chromlist.get(i-1);
					boolean prevHaplo=randAssort.get(prevChr);
					boolean activeHaplo=recombineWithinChromosome(crossoverEvents,prevChr,prevHaplo);
					randAssort.put(activeChr, activeHaplo);
				}
			}
		}
		return new RandomAssortment(randAssort);
	}
	
	
	
	private boolean recombineWithinChromosome(CrossoverEvents crossoverEvents,Chromosome chr, boolean haplotype)
	{
		if(crossoverEvents.getCrossovers(chr).size()%2==0)
		{
			// if the number of crossovers for the given chromosome is divisable by two, we are again on the same haplotype
			// thus we return the previous haplotype
			return haplotype;
		}
		else
		{
			// if the number of crossovers is not even we switched to a different haplotype
			return !haplotype;
		}
		
	}


}
