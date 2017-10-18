package mimcore.data.fitness.quantitative;

import java.util.Random;

/**
 * Represents a summary of the additive fitness effects of SNPs
 * Immutable; Also allows to calculate the additive fitness effect of SNPs
 */
public class PhenotypeCalculatorAllEqual implements IPhenotypeCalculator {



	public PhenotypeCalculatorAllEqual()
	{

	}

	

	public double getPhenotype(double genotype,Random random)
	{
		return 1.0;
	}

}
