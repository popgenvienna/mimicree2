package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculatorSexSpecific implements IFitnessCalculator {

	private final IFitnessCalculator male;
	private final IFitnessCalculator female;
	private final IFitnessCalculator hermaphrodite;

	public FitnessCalculatorSexSpecific(IFitnessCalculator male, IFitnessCalculator female, IFitnessCalculator hermaphrodite)
	{
		this.male=male;
		this.female=female;
		this.hermaphrodite=hermaphrodite;
	}

	public  double getFitness(DiploidGenome dipGenome, double phenotpye, Sex sex)
	{
		if(sex==Sex.Male) return this.male.getFitness(dipGenome,phenotpye,sex);
		else if(sex==Sex.Female) return this.female.getFitness(dipGenome,phenotpye,sex);
		else if(sex==Sex.Hermaphrodite) return this.hermaphrodite.getFitness(dipGenome,phenotpye,sex);
		else throw new IllegalArgumentException("Unknown sex "+sex);
	}


	public IFitnessCalculator getMale(){return this.male;}
	public IFitnessCalculator getFemale(){return this.female;}
	public IFitnessCalculator getHermaphrodite(){return this.hermaphrodite;}
}
