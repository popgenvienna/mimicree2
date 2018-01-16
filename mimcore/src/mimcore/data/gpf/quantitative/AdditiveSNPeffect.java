package mimcore.data.gpf.quantitative;

import mimcore.data.*;
import mimcore.data.sex.Sex;


/**
 * Immutable representation of a additive effect of a single SNP
 * @author robertkofler
 *
 */
public class AdditiveSNPeffect implements IAdditiveSNPeffect {
	private final GenomicPosition position;
	private final char achar;
	private final char alternativeChar;
	private final double a;
	private final double d;
	public AdditiveSNPeffect(GenomicPosition position, char achar, char alternativeChar, double a, double d)
	{
		this.position=position;
		this.achar=achar;
		this.alternativeChar=alternativeChar;
		this.a=a;
		//if(a<0)throw new IllegalArgumentException("Effect size of SNP must not be smaller than zero");
		this.d=d;
	}
	
	/**
	 * Calculate the additive gpf effect for the given genotype.
	 * The ordering of the alleles is not important
	 * @return
	 */
	public double getEffectSizeOfGenotype(char[] genotype, Sex sex)
	{
		char allele1=genotype[0];
		char allele2=genotype[1];
		if(allele1  == achar && allele2 == achar)
		{
			// homozygous for w11 (gpf = 1.0)
			return this.a;
		}
		else if(allele1== alternativeChar && allele2==alternativeChar)
		{
			// homozygous for the other allele w22
			return -this.a;
		}
		else if((allele1==achar && allele2 ==alternativeChar) || (allele1==alternativeChar && allele2 ==achar))
		{

			return this.d;
		}
		else
		{
			throw new IllegalArgumentException("Invalid outcome for additive effect size; not valid alleles "+allele1 +" "+ allele2);
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
	
	
	public char achar()
	{
		return this.achar;
	}
	public char altchar()
	{
		return this.alternativeChar;
	}

	public double a()
	{
		return this.a;
	}

	public double d()
	{
		return this.d;
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
