package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 11/20/16.
 */
public interface IFitnessCalculator {

	public abstract double getFitness(DiploidGenome dipGenome, double phenotype, Sex sex);

}
