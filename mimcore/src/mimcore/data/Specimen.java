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
	public BitArrayBuilder getGamete(RecombinationGenerator recGen, IMutator mutator, Random random)
	{

		SNPCollection snpCollection=genome.getHaplotypeA().getSNPCollection();
		RecombinationEvent recEv=recGen.getRecombinationEvent(this.getSex(),random);
		BitArrayBuilder pristineGamete=recEv.getGamete(this.genome);
		BitArrayBuilder mutatedGamete=mutator.introduceMutations(pristineGamete,snpCollection,random);
		return mutatedGamete;
	}
	public BitArrayBuilder[] getClonalGametes(IMutator mutator, Random random, boolean haploid)
	{

		SNPCollection snpCollection=genome.getHaplotypeA().getSNPCollection();
		BitArrayBuilder hap1=this.genome.getHaplotypeA().getRawGenome().getBitArrayBuilder();
		BitArrayBuilder hap2=this.genome.getHaplotypeB().getRawGenome().getBitArrayBuilder();

		hap1=mutator.introduceMutations(hap1,snpCollection,random);

		// if haploid just mutate once
		if(haploid) hap2=hap1;
		else hap2 =mutator.introduceMutations(hap2,snpCollection,random);

		BitArrayBuilder[] toret=new BitArrayBuilder[2];
		toret[0]=hap1;
		toret[1]=hap2;
		return toret;
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
