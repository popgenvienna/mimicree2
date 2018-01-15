package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculatorAllEqual implements IFitnessCalculator {

	public  double getFitness(DiploidGenome dipGenome, double phenotpye, Sex sex)
	{return 1.0;}
}
