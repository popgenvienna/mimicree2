package mimcore.data.fitness;

import mimcore.data.DiploidGenome;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculatorDefault implements IFitnessCalculator {

	public  double getFitness(DiploidGenome dipGenome)
	{return 1.0;}
}
