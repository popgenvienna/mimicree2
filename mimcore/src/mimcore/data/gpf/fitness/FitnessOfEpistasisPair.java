package mimcore.data.gpf.fitness;

import mimcore.data.GenomicPosition;


/**
 * Immutable representation of a additive effect of a single SNP
 * @author robertkofler
 *
 */
public class FitnessOfEpistasisPair {
	private final GenomicPosition position_1;
	private final GenomicPosition position_2;
	private final char achar;
	private final char Achar;
	private final char bchar;
	private final char Bchar;


	private final double[] epistaticFitness;
	// 0		1		2		3			4		5		6			7		8
	//waabb   waabB    waaBB    waAbb    waAbB    waABB    wAAbb    wAAbB    wAABB
	// lets go for the index; first compbute index of a (0,3,6) than index of b (0,1,2) and than sum it for the final index

	public FitnessOfEpistasisPair(GenomicPosition position_1, GenomicPosition position_2, char achar, char Achar,char bchar, char Bchar, double[] epistaticFitness)
	{
		this.position_1=position_1;
		this.position_2=position_2;
		this.achar=achar;
		this.Achar=Achar;
		this.bchar=bchar;
		this.Bchar=Bchar;
		for(double ef:epistaticFitness) if(ef<0) throw new IllegalArgumentException("Fitness of genotype must not be smaller than zero");
		this.epistaticFitness=epistaticFitness;

	}
	
	/**
	 * Calculate the additive gpf effect for the given genotype.
	 * The ordering of the alleles is not important
	 * @return
	 */
	public double getEffectSizeOfGenotype(char[] genotype_1, char[] genotype_2)
	{

		// twice get_index; get index of a and b

		char aa1=genotype_a[0];
		char aa2=genotype_b[1];
		if(aa1  == achar && aa2 == achar)
		{
			// homozygous: waa
			return this.waa;
		}
		else if(aa1  == Achar && aa2 == Achar)
		{
			// homozygous: wAA
			return this.wAA;
		}
		else if ((aa1 ==achar && aa2 == Achar) || (aa1 ==Achar && aa2 == achar))
		{
			// heterozygous  
			return this.waA;
		}
		else
		{
			throw new IllegalArgumentException("Invalid outcome for fitness of SNPs; not valid alleles "+allele1 +" "+ allele2);
		}
	}
	
	/**
	 * Retrieve the genomic position for the given genotype
	 * @return
	 */
	public GenomicPosition getPosition_1()
	{
		return this.position_1;
	}

	public GenomicPosition getPosition_2()
	{
		return this.position_2;
	}
	
	



	public boolean acharIsAncestral(char ancestral)
	{
		if(ancestral == this.achar)
		{
			// anc = A    w11 = A  thus neutral
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	
	
}
