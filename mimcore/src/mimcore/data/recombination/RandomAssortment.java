package qmimcore.data.recombination;

import qmimcore.data.*;

import java.util.HashMap;


/**
 * Represents a random assortment of chromosomes, tied to a collection of crossing over events
 * Will basically contain for every chromosome an randomly assigned haplotype (either true or false, true is haplotype A and false haplotype B)
 * The problem is that actually not all chromosomes are randomly assorted as for example 2L and 2R are linked.
 * For example the haplotype of 2R will depend on i.) random assortment of 2L and ii.) crossover events on 2L
 * For linked Chromosomes random recombination events are considered
 * @author robertkofler
 *
 */
public class RandomAssortment {
	
	private final HashMap<Chromosome,Boolean> randomAssortment;
	
	public RandomAssortment(HashMap<Chromosome,Boolean> randomAssortment)
	{
		this.randomAssortment=new HashMap<Chromosome,Boolean>(randomAssortment);
	}
	/**
	 * Does the given chromosome start with the first haplotype;
	 * If false the given chromsome starts with the second haplotype
	 * @param chromosome
	 * @return
	 */
	public boolean startWithFirstHaplotype(Chromosome chromosome)
	{
		return this.randomAssortment.get(chromosome);
	}

}
