package mimcore.data.gpf.fitness;

import mimcore.data.GenomicPosition;
import mimcore.data.sex.Sex;


/**
 * Immutable representation of a additive effect of a single SNP
 * @author robertkofler
 *
 */
public class FitnessOfSNPSexSpecific implements IFitnessOfSNP {
	private final FitnessOfSNP male;
	private final FitnessOfSNP female;
	private final FitnessOfSNP hermaphrodite;

	public FitnessOfSNPSexSpecific(FitnessOfSNP male, FitnessOfSNP female, FitnessOfSNP hermaphrodite)
	{
			this.male=male;
			this.female=female;
			this.hermaphrodite=hermaphrodite;

	}
	
	/**
	 * Calculate the additive gpf effect for the given genotype.
	 * The ordering of the alleles is not important
	 * @return
	 */
	public double getEffectSizeOfGenotype(char[] genotype, Sex sex)
	{
		if(sex==Sex.Male) return this.male.getEffectSizeOfGenotype(genotype,sex);
		else if (sex==Sex.Female) return this.female.getEffectSizeOfGenotype(genotype,sex);
		else if(sex==Sex.Hermaphrodite) return this.hermaphrodite.getEffectSizeOfGenotype(genotype,sex);
		else throw new IllegalArgumentException("Unknown sex "+sex);
	}
	
	/**
	 * Retrieve the genomic position for the given genotype
	 * @return
	 */
	public GenomicPosition getPosition()
	{
		return this.male.getPosition();
	}
	
	
	public char get_achar()
	{
		return this.male.get_achar();
	}
	public char get_Achar() { return this.male.get_Achar();}


	public boolean acharIsAncestral(char ancestral)
	{
		return this.male.acharIsAncestral(ancestral);
	}
	
	
	
	
}
