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
		int aindex=getAindex(genotype_1);
		int bindex=getBindex(genotype_2);
		int index=aindex+bindex;
		return this.epistaticFitness[index];

	}


	private int getAindex(char[] gen_a)
	{
		// twice get_index; get index of a and b

		char a1=gen_a[0];
		char a2=gen_a[1];
		if(a1  == achar && a2 == achar)
		{
			// homozygous: waa
			return 0;
		}
		else if(a1  == Achar && a2 == Achar)
		{
			// homozygous: wAA
			return 6;
		}
		else if ((a1 ==achar && a2 == Achar) || (a1 ==Achar && a2 == achar))
		{
			// heterozygous waA
			return 3;
		}
		else
		{
			throw new IllegalArgumentException("Invalid outcome for epistatic fitness of SNPs; not valid alleles "+a1 +" "+ a2);
		}
	}


	private int getBindex(char[] gen_b)
	{
		// twice get_index; get index of a and b

		char b1=gen_b[0];
		char b2=gen_b[1];
		if(b1  == bchar && b2 == bchar)
		{
			// homozygous: waa
			return 0;
		}
		else if(b1  == Bchar && b2 == Bchar)
		{
			// homozygous: wAA
			return 2;
		}
		else if ((b1 ==bchar && b2 == Bchar) || (b1 ==Bchar && b2 == bchar))
		{
			// heterozygous waA
			return 1;
		}
		else
		{
			throw new IllegalArgumentException("Invalid outcome for epistatic fitness of SNPs; not valid alleles "+b1 +" "+ b2);
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

	public char getAchar(){return this.Achar;}
	public char getachar(){return this.achar;}
	public char getBchar(){return this.Bchar;}
	public char getbchar(){return this.bchar;}
	
	



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
