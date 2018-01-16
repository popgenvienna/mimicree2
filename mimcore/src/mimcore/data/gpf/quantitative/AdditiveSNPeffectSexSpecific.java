package mimcore.data.gpf.quantitative;

import mimcore.data.GenomicPosition;
import mimcore.data.sex.Sex;


/**
 * Immutable representation of a additive effect of a single SNP
 * @author robertkofler
 *
 */
public class AdditiveSNPeffectSexSpecific implements IAdditiveSNPeffect {
	private final AdditiveSNPeffect male;
	private final AdditiveSNPeffect female;
	private final AdditiveSNPeffect hermaphrodite;

	public AdditiveSNPeffectSexSpecific(AdditiveSNPeffect male, AdditiveSNPeffect female, AdditiveSNPeffect hermaphrodite)
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
		if(sex==Sex.Male)return this.male.getEffectSizeOfGenotype(genotype,sex);
		else if(sex==Sex.Female)return this.female.getEffectSizeOfGenotype(genotype,sex);
		else if(sex==Sex.Hermaphrodite)return this.hermaphrodite.getEffectSizeOfGenotype(genotype,sex);
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
	
	
	public char achar()
	{
		return this.male.achar();
	}

	public char altchar()
	{
		return this.male.altchar();
	}

	public double a()
	{
		return this.male.a();
	}

	public double d()
	{
		return this.male.d();
	}


	public boolean acharIsAncestral(char ancestral)
	{
		return this.male.acharIsAncestral(ancestral);
	}
	
	
	
	
}
