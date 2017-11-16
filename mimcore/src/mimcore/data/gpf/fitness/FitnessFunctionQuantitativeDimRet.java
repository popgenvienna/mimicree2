package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.statistic.Gaussian;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessFunctionQuantitativeDimRet implements IFitnessCalculator {

	private final double minFitness;
	private final double maxFitness;
	private final double deltaFitness;
	private final double alpha;
	private final double beta;


	public FitnessFunctionQuantitativeDimRet(double minFitness, double maxFitness, double alpha, double beta)
	{
		this.minFitness=minFitness;
		this.maxFitness=maxFitness;
		if(maxFitness<minFitness) throw new IllegalArgumentException("Max. fitness needs to be larger than min. fitness");
		this.alpha=alpha;
		this.beta=beta;

		this.deltaFitness=this.maxFitness-this.minFitness;
	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype)
	{
		double part1=1.0-1.0/Math.exp(this.alpha*(phenotype+this.beta));
		double fitness=minFitness+deltaFitness*part1;
		if(fitness<minFitness)fitness=minFitness;
		return fitness;
	}

	public double getMinFitness(){return this.minFitness;}
	public double getMaxFitness(){return this.maxFitness;}
	public double getAlpha(){return this.alpha;}
	public double getBeta(){return this.beta;}
}
