package mimcore.data.gpf.quantitative;

import mimcore.data.sex.Sex;

import java.util.Random;

/**
 * Created by robertkofler on 8/28/14.
 */
public interface IPhenotypeCalculator {
	public abstract double getPhenotype(Sex sex, double genotype, Random random);
}
