package mimcore.data;

import mimcore.data.haplotypes.HaploidGenome;
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

	public DiploidGenome getChild(RecombinationGenerator recGenerator, Random random)
	{
		HaploidGenome semen	=male.getGamete(recGenerator, random);
		HaploidGenome egg	=female.getGamete(recGenerator,random);
		DiploidGenome fertilizedEgg=new DiploidGenome(semen,egg);
		return fertilizedEgg;
	}


}
