package qmimcore.data;


import qmimcore.data.recombination.*;
import qmimcore.data.haplotypes.HaploidGenome;
import java.util.Random;

/**
 * Represent a single specimen which can be imagined as a phenotype.
 * A specimen has a diplod genome, a fitness and a recombination rate
 * Immutable
 * @author robertkofler
 *
 */
public class Specimen {




	private final double genotype;
	private final double phenotype;
	private final DiploidGenome genome;

	public Specimen( double genotype, double phenotype, DiploidGenome genome)
	{
		this.genotype=genotype;
		this.phenotype=phenotype;
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
	
	public double genotype()
	{
		return this.genotype;
	}
	
	public double phenotype()
	{
		 return this.phenotype;
	}




}
