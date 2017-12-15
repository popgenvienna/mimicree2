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
	private final Specimen male;
	private final Specimen female;

	public MatePair(Specimen male, Specimen female)
	{
		this.female=female;
		this.male=male;
	}

	public DiploidGenome getChild(RecombinationGenerator recGenerator, IMutator mutator, Random random)
	{

		HaploidGenome semen	=male.getGamete(recGenerator, mutator, random);
		HaploidGenome egg	=female.getGamete(recGenerator, mutator, random);
		DiploidGenome fertilizedEgg=new DiploidGenome(semen,egg);
		return fertilizedEgg;
	}

	public Specimen getMale(){return this.male;}
	public Specimen getFemale(){return this.female;}


}
