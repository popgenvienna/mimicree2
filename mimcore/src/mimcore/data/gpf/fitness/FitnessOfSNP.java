package mimcore.data.gpf.fitness;

import mimcore.data.GenomicPosition;


/**
 * Immutable representation of a additive effect of a single SNP
 * @author robertkofler
 *
 */
public class FitnessOfSNP {
	private final GenomicPosition position;
	private final char achar;
	private final char Achar;
	private final double waa;
	private final double waA;
	private final double wAA;

	public FitnessOfSNP(GenomicPosition position, char achar, char Achar, double waa, double waA, double wAA)
	{
		this.position=position;
		this.achar=achar;
		this.Achar=Achar;
		if(waa < 0 || waA < 0 || wAA < 0)throw new IllegalArgumentException("Fitness of genotype must not be smaller than zero");
		this.waa=waa;
		this.waA=waA;
		this.wAA=wAA;

	}
	
	/**
	 * Calculate the additive gpf effect for the given genotype.
	 * The ordering of the alleles is not important
	 * @return
	 */
	public double getEffectSizeOfGenotype(char[] genotype)
	{
		char allele1=genotype[0];
		char allele2=genotype[1];
		if(allele1  == achar && allele2 == achar)
		{
			// homozygous: waa
			return this.waa;
		}
		else if(allele1  == Achar && allele2 == Achar)
		{
			// homozygous: wAA
			return this.wAA;
		}
		else if ((allele1 ==achar && allele2 == Achar) || (allele1 ==Achar && allele2 == achar))
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
	public GenomicPosition getPosition()
	{
		return this.position;
	}
	
	
	public char get_achar()
	{
		return this.achar;
	}
	public char get_Achar() { return this.Achar; }
	
	public double waa()
	{
		return this.waa;
	}

	public double waA()
	{
		return this.waA;
	}

	public double wAA()
	{
		return this.wAA;
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
