package mimcore.data.fitness.quantitative;

import mimcore.data.DiploidGenome;

/**
 * Represents a summary of the additive fitness effects of SNPs
 * Immutable; Also allows to calculate the additive fitness effect of SNPs
 */
public class GenotypeCalculatorAllEqual implements IGenotypeCalculator{



	public GenotypeCalculatorAllEqual()
	{
	}

	

	public double getGenotype(DiploidGenome dipGenome)
	{
		return 1.0;
	}
}
