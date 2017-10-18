package mimcore.data;


import mimcore.data.recombination.*;
import mimcore.data.haplotypes.HaploidGenome;
import java.util.Random;

/**
 * Represent a single specimen which can be imagined as a phenotype.
 * A specimen has a diplod genome, a fitness and a recombination rate
 * Immutable
 * @author robertkofler
 *
 */
public class Specimen {




	private final double quantGenotype;
	private final double quantPhenotype;
	private final double fitness;
	private final DiploidGenome genome;

	public Specimen( double quantGenotype, double quantPhenotype, double fitness, DiploidGenome genome)
	{
		this.quantGenotype=quantGenotype;
		this.quantPhenotype=quantPhenotype;
		this.fitness=fitness;
		this.genome=genome;
	}

	public DiploidGenome getGenome()
	{
		return this.genome;
	}
	
	/**
	 * Obtain a gamete from the given Specimen i.e.: get a semen or an egg
	 * The gamete is a haploid and recombined product of the diploid genome
	 * @return
	 */
	public HaploidGenome getGamete(RecombinationGenerator recGen, Random random)
	{
		RecombinationEvent recEv=recGen.getRecombinationEvent(random);
		return recEv.getGamete(this.genome);
	}
	
	public double quantGenotype()
	{
		return this.quantGenotype;
	}
	
	public double quantPhenotype()
	{
		 return this.quantPhenotype;
	}

	public double fitness()
	{
		return this.fitness;
	}





}
