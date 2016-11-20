package qmimcore.data.fitness;

import qmimcore.data.DiploidGenome;

/**
 * Created by robertkofler on 8/28/14.
 */
public interface IGenotypeCalculator {

	public abstract double getGenotype(DiploidGenome dipGenome);
}
