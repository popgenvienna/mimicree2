package mimcore.data.gpf.quantitative;

import mimcore.data.DiploidGenome;
import mimcore.data.GenomicPosition;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 8/28/14.
 */
public interface IAdditiveSNPeffect {

	public abstract double getEffectSizeOfGenotype(char[] genotype, Sex sex);
	public abstract GenomicPosition getPosition();
}
