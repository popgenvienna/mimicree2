package mimcore.data;


import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Mutator.IMutator;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.recombination.*;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexInfo;

import java.util.Random;

/**
 * Represent a single specimen which can be imagined as a phenotype.
 * A specimen has a diplod genome, a gpf and a recombination rate
 * Immutable
 * @author robertkofler
 *
 */
public class Specimen {




	private final double quantGenotype;
	private final double quantPhenotype;
	private final double fitness;
	private final Sex sex;
	private final DiploidGenome genome;


	public Specimen(Sex sex, double quantGenotype, double quantPhenotype, double fitness, DiploidGenome genome)
	{
		this.sex=sex;
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
	public HaploidGenome getGamete(RecombinationGenerator recGen, IMutator mutator, Random random)
	{

		SNPCollection snpCollection=this.genome.getHaplotypeA().getSNPCollection();
		RecombinationEvent recEv=recGen.getRecombinationEvent(this.getSex(),random);
		BitArrayBuilder pristineGamete=recEv.getGamete(this.genome);
		BitArrayBuilder mutatedGameted=mutator.introduceMutations(pristineGamete,snpCollection,random);
		return new HaploidGenome(mutatedGameted.getBitArray(),snpCollection);
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

	public  Sex getSex(){return this.sex;}






}
