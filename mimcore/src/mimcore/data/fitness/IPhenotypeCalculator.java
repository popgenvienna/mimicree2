package qmimcore.data.fitness;

import java.util.Random;

/**
 * Created by robertkofler on 8/28/14.
 */
public interface IPhenotypeCalculator {
	public abstract double getPhenotype(double genotype,Random random);
}
