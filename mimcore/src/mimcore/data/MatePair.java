package mimcore.data;

import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Mutator.IMutator;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.recombination.RecombinationGenerator;
import java.util.Random;

/**
 * Created by robertkofler on 8/28/14.
 */
public class MatePair {
	private final Specimen s1;
	private final Specimen s2;

	public MatePair(Specimen s1, Specimen s2)
	{
		this.s1=s1;
		this.s2=s2;
	}

	public DiploidGenome getChild(RecombinationGenerator recGenerator, IMutator mutator, Random random)
	{

		HaploidGenome semen	=s1.getGamete(recGenerator, mutator, random);
		HaploidGenome egg	=s2.getGamete(recGenerator, mutator, random);
		DiploidGenome fertilizedEgg=new DiploidGenome(semen,egg);
		return fertilizedEgg;
	}

	public Specimen getS1(){return this.s1;}
	public Specimen getS2(){return this.s2;}


}
