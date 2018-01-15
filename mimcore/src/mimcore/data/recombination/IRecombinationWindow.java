package mimcore.data.recombination;

import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.sex.Sex;

import java.util.Random;

/**
 * Created by robertkofler on 7/16/14.
 */
public interface IRecombinationWindow {

	public abstract int getRecombinationEvents(Sex sex, Random random);
	public abstract GenomicPosition getRandomPosition(Random random);
	public abstract Chromosome getChromosome();
	public abstract int getStartPosition();
	public abstract int getEndPosition();


}
