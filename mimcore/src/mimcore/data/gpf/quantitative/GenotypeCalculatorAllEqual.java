package mimcore.data.gpf.quantitative;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

/**
 * Represents a summary of the additive gpf effects of SNPs
 * Immutable; Also allows to calculate the additive gpf effect of SNPs
 */
public class GenotypeCalculatorAllEqual implements IGenotypeCalculator{



	public GenotypeCalculatorAllEqual()
	{
	}

	

	public double getGenotype(DiploidGenome dipGenome, Sex sex)
	{
		return 1.0;
	}
}
