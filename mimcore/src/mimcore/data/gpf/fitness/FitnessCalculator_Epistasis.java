package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;

import java.util.ArrayList;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculator_Epistasis implements IFitnessCalculator {
 	private final ArrayList<FitnessOfEpistasisPair> epistasis;

	public FitnessCalculator_Epistasis(ArrayList<FitnessOfEpistasisPair> epistasis)
	{
		this.epistasis=new ArrayList<FitnessOfEpistasisPair>(epistasis);
	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype)
	{
		double epiFitness=1.0;
		for(FitnessOfEpistasisPair epi:this.epistasis)
		{
			char[] loc1=dipGenome.getSNPGenotype(epi.getPosition_1());
			char[]	loc2=dipGenome.getSNPGenotype(epi.getPosition_2());
			double fitnessOfPair=epi.getEffectSizeOfGenotype(loc1,loc2);
			epiFitness*=fitnessOfPair;
		}
		return epiFitness;
	}
}
