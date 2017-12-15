package mimcore.data.Mutator;

import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.DiploidGenome;
import mimcore.data.haplotypes.SNPCollection;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robertkofler on 7/16/14.
 */
public interface IMutator {

	public abstract BitArrayBuilder introduceMutations(BitArrayBuilder toMutate, SNPCollection snpCollection, Random random);
}
