package mimcore.data.mating;

import mimcore.data.MatePair;
import mimcore.data.Population;
import mimcore.data.Specimen;

import java.util.ArrayList;
import java.util.Random;

/**
 * Mating function generates couples for mating for a given population of specimen.
 * Mating success is directly proportional to gpf
 * @author robertkofler
 *
 */
public class MatingFunctionFecundity implements IMatingFunction {



	private final ArrayList<FitnessTransformedSpecimen> specimens;
	private final int leng;


	public MatingFunctionFecundity(Population pop)
	{
		ArrayList<Specimen> popSpecimen=pop.getSpecimen();
		this.leng=popSpecimen.size();
		if(leng<=2)throw new IllegalArgumentException("Population size must be larger than two; Selection strength to high?");

		// Total fitness sum of the population (will be scaled to a total of 1.0)
		double popfitSum=0.0;
		for(Specimen s: popSpecimen) popfitSum+=s.fitness();

		// fitness transformed (scale to a total fitness of the population of 1.0 and use cumulative values, i.e. fitness is the difference between two consecutive individuals)
		specimens=new ArrayList<FitnessTransformedSpecimen>();
		double fitnessEquivalent=1.0/popfitSum;
		double runningSum=0.0; // fitness so far
		for(int i=0; i<popSpecimen.size(); i++)
		{
			Specimen spec=popSpecimen.get(i);
			double transformedFitness=spec.fitness()*fitnessEquivalent;
			runningSum+=transformedFitness;
			this.specimens.add(new FitnessTransformedSpecimen(spec,runningSum,i));
		}

	}


	public MatingFunctionFecundity()
	{
		this.specimens=new ArrayList<FitnessTransformedSpecimen>();
		this.leng=0;
	}

	private static class FitnessTransformedSpecimen
	{
		public Specimen specimen;
		public double transformedFitnessSum;
		public int index;
		public FitnessTransformedSpecimen(Specimen specimen, double transformedFitnessSum, int index)
		{
			this.specimen=specimen;
			this.transformedFitnessSum=transformedFitnessSum;
			this.index=index;
		}
	}


	public  IMatingFunction factory(Population population)
	{
		return new MatingFunctionFecundity(population);
	}

	

	
	/**
	 * Choose a couple for mating; Allow selfing
	 * Note that most simulations tools allow selfing, so for the sake of comparability we do this too.
	 * (Although it would be simple to forbid)
	 * @return
	 */
	public MatePair getCouple(Random random)
	{
		FitnessTransformedSpecimen s1=getSpecimenForRandomNumber(random.nextDouble());
		FitnessTransformedSpecimen s2=getSpecimenForRandomNumber(random.nextDouble());

		// the following code would disable selfing
		// included selfing since this is widely used
		//while(s1.index==s2.index)
		//{
		//	s2=getSpecimenForRandomNumber(random.nextDouble());
		//}
		return new MatePair(s1.specimen,s2.specimen);

	}


	/**
	 * Get the specimen for a given random number; no binary search yet...
	 * @param random
	 * @return
	 */
	private FitnessTransformedSpecimen getSpecimenForRandomNumber(double random)
	{
		// example
		// 0.1  0.1  0.2  0.2   # relative value
		// 0.1  0.2  0.4  0.6	# cumulative value

		// random 0.01 -> das erste mit 0.1
		// random 0.0 -> das erste mit 0.1
		// random 0.1 -> das zweite mit 0.2
		// random 0.999 -> das letzte mit 1.0! das letzte sollte genau 1 haben
		for(FitnessTransformedSpecimen tspec:this.specimens)
		{
			if(tspec.transformedFitnessSum > random) return tspec;
		}
		throw new IllegalArgumentException("State not allowed in mating function");
	}

}
