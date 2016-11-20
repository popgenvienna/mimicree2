package mimcore.data.recombination;

import mimcore.data.Chromosome;
import mimcore.data.DiploidGenome;
import mimcore.data.GenomicPosition;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Represents a set of crossover events
 * @author robertkofler
 *
 */
public class CrossoverEvents {
	private final ArrayList<GenomicPosition> crossoverEvents;
	private final HashMap<Chromosome,LinkedList<GenomicPosition>> reorganized;
	
	public CrossoverEvents(ArrayList<GenomicPosition> crossoverEvents)
	{
		this.crossoverEvents=new ArrayList<GenomicPosition>(crossoverEvents);
		

		HashMap<Chromosome,LinkedList<GenomicPosition>> toret=new HashMap<Chromosome,LinkedList<GenomicPosition>>();
		
		for(GenomicPosition pos: crossoverEvents)
		{
			if(!toret.containsKey(pos.chromosome()))toret.put(pos.chromosome(),new LinkedList<GenomicPosition>());
			toret.get(pos.chromosome()).add(pos);
		}
		this.reorganized=toret;
	}

	
	
	/**
	 * Get a sorted list of crossover events for the given chromosome
	 * @param chromosome
	 * @return
	 */
	public LinkedList<GenomicPosition> getCrossovers(Chromosome chromosome)
	{
		LinkedList<GenomicPosition> toret=new LinkedList<GenomicPosition>();
		if(reorganized.containsKey(chromosome)) toret=new LinkedList<GenomicPosition>(reorganized.get(chromosome));
		Collections.sort(toret);
		return toret;
		
	}
}
