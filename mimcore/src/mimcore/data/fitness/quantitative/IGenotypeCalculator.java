package mimcore.data.fitness.quantitative;

import mimcore.data.DiploidGenome;

/**
 * Created by robertkofler on 8/28/14.
 */
public interface IGenotypeCalculator {

	public abstract double getGenotype(DiploidGenome dipGenome);
}
