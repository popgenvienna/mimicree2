package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculator_SNPandEpistasis implements IFitnessCalculator {
	private final IFitnessCalculator snpFitnessCalculator;
	private final IFitnessCalculator epistasisFitnessCalculator;

	public FitnessCalculator_SNPandEpistasis(IFitnessCalculator snpFitnessCalculator, IFitnessCalculator epistasisFitnessCalculator)
	{
		this.snpFitnessCalculator=snpFitnessCalculator;
		this.epistasisFitnessCalculator=epistasisFitnessCalculator;
	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype, Sex sex)
	{
		return this.snpFitnessCalculator.getFitness(dipGenome,phenotype, sex)*this.epistasisFitnessCalculator.getFitness(dipGenome,phenotype,sex);
	}
}
