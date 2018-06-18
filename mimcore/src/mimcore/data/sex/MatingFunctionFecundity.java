package mimcore.data.sex;

import mimcore.data.MatePair;
import mimcore.data.Population;
import mimcore.data.Specimen;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDirectionalSelection;

import java.util.ArrayList;
import java.util.Random;

/**
 * Mating function generates couples for sex for a given population of specimen.
 * Mating success is directly proportional to gpf
 * @author robertkofler
 *
 */
public class MatingFunctionFecundity implements IMatingFunction {



	private final ArrayList<FitnessTransformedSpecimen> fmh;
	private final ArrayList<FitnessTransformedSpecimen> fh;
	private final ArrayList<FitnessTransformedSpecimen> mh;
	private final double selfingRate;



	public MatingFunctionFecundity(Population pop, double selfingRate)
	{
		if(selfingRate<0.0 ||selfingRate>1.0) throw new IllegalArgumentException("Selfing rate must be between 0.0 and 1.0");
		this.selfingRate=selfingRate;
		ArrayList<Specimen> popSpecimen=pop.getSpecimen();

		if(popSpecimen.size()<1) throw new IllegalArgumentException("Population size to small; population died out");

		ArrayList<Specimen> afh=new ArrayList<Specimen>();
		ArrayList<Specimen> amh=new ArrayList<Specimen>();
		for(Specimen s: popSpecimen)
		{
			if(s.getSex()==Sex.Female) afh.add(s);
			else if(s.getSex()==Sex.Male)amh.add(s);
			else if(s.getSex()==Sex.Hermaphrodite)
			{
				afh.add(s);
				amh.add(s);
			}
			else throw new IllegalArgumentException("Unknown sex "+s.getSex());
		}

		fmh= transform(popSpecimen);
		fh=transform(afh);
		mh=transform(amh);

	}

	private final ArrayList<FitnessTransformedSpecimen> transform(ArrayList<Specimen> specimens)
	{
		// Total fitness sum of the population (will be scaled to a total of 1.0)
		double popfitSum=0.0;
		for(Specimen s: specimens) popfitSum+=s.fitness();

		// fitness transformed (scale to a total fitness of the population of 1.0 and use cumulative values, i.e. fitness is the difference between two consecutive individuals)
		ArrayList<FitnessTransformedSpecimen> toret=new ArrayList<FitnessTransformedSpecimen>();
		double fitnessEquivalent=1.0/popfitSum;
		double runningSum=0.0; // fitness so far
		for(int i=0; i<specimens.size(); i++)
		{
			Specimen spec=specimens.get(i);
			double transformedFitness=spec.fitness()*fitnessEquivalent;
			runningSum+=transformedFitness;
			toret.add(new FitnessTransformedSpecimen(spec,runningSum,i));
		}
		return toret;
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

	public MatingFunctionFecundity(double selfingRate)
	{
		this.fmh=new ArrayList<FitnessTransformedSpecimen>();
		this.fh=new ArrayList<FitnessTransformedSpecimen>();
		this.mh=new ArrayList<FitnessTransformedSpecimen>();
		this.selfingRate=selfingRate;
	}


	public  IMatingFunction factory(Population population)
	{
		return new MatingFunctionFecundity(population,this.selfingRate);
	}

	

	
	/**
	 * Choose a couple for sex; Allow selfing
	 * Note that most simulations tools allow selfing, so for the sake of comparability we do this too.
	 * (Although it would be simple to forbid)
	 * @return
	 */
	public MatePair getCouple(Random random)
	{
		FitnessTransformedSpecimen s1=getSpecimenForRandomNumber(random.nextDouble(),this.fmh);
		FitnessTransformedSpecimen s2=null;

		if(s1.specimen.getSex()==Sex.Male)
		{
			if(this.fh.size()<1) throw new IllegalArgumentException("No sex partner found for male; population died out");
			s2=getSpecimenForRandomNumber(random.nextDouble(), this.fh);
		}
		else if(s1.specimen.getSex()==Sex.Female)
		{
			if(this.mh.size()<1) throw new IllegalArgumentException("No sex partner found for female; population died out");
			s2=getSpecimenForRandomNumber(random.nextDouble(), this.mh);
		}
		else if(s1.specimen.getSex()==Sex.Hermaphrodite)
		{
			// hermaphrodite mates everything
			// test for selfing
			if(random.nextDouble()<this.selfingRate) s2=s1;
			else
			{
				// no selfing; ensure no selfing, not even randomly. thus s1==s2
				if(this.fmh.size()<2) throw new IllegalArgumentException("No sex partner found for hermaphrodite; population died out");
				s2=getSpecimenForRandomNumber(random.nextDouble(),this.fmh);
				while(s1.specimen==s2.specimen) s2=getSpecimenForRandomNumber(random.nextDouble(),this.fmh);
			}
		}
		else throw new IllegalArgumentException("unknown sex "+s1.specimen.getSex());

		return new MatePair(s1.specimen,s2.specimen);

	}


	/**
	 * Get the specimen for a given random number; no binary search yet...
	 * @param random
	 * @return
	 */
	private FitnessTransformedSpecimen getSpecimenForRandomNumber(double random, ArrayList<FitnessTransformedSpecimen> fts)
	{
		// example
		// 0.1  0.1  0.2  0.2   # relative value
		// 0.1  0.2  0.4  0.6	# cumulative value

		// random 0.01 -> das erste mit 0.1
		// random 0.0 -> das erste mit 0.1
		// random 0.1 -> das zweite mit 0.2
		// random 0.999 -> das letzte mit 1.0! das letzte sollte genau 1 haben
		for(FitnessTransformedSpecimen tspec:fts)
		{
			if(tspec.transformedFitnessSum > random) return tspec;
		}
		throw new IllegalArgumentException("State not allowed in sex function");
	}

}
