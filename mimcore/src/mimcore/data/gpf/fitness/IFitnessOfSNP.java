package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.GenomicPosition;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 11/20/16.
 */
public interface IFitnessOfSNP {

	public abstract double getEffectSizeOfGenotype(char[] genotype, Sex sex);
	public abstract GenomicPosition getPosition();

}
