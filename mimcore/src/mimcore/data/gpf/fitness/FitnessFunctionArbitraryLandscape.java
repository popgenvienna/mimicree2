package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.statistic.Gaussian;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessFunctionArbitraryLandscape implements IFitnessCalculator {

	private final double minFitness;
	private final double maxFitness;
	private final double deltaFitness;
	private final double mean;
	private final double stdev;
	private final double scale;

	public FitnessFunctionArbitraryLandscape(double minFitness, double maxFitness, double mean, double stdev)
	{
		this.minFitness=minFitness;
		this.maxFitness=maxFitness;
		if(maxFitness<minFitness) throw new IllegalArgumentException("Max. fitness needs to be larger than min. fitness");
		this.mean=mean;
		this.stdev=stdev;
		this.scale= Gaussian.pdf(mean,mean,stdev);
		this.deltaFitness=this.maxFitness-this.minFitness;
	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype)
	{
		double gaussvalue=Gaussian.pdf(phenotype,this.mean,this.stdev);
		double fitness=gaussvalue*this.deltaFitness/this.scale;
		fitness+=this.minFitness;
		return fitness;
	}
}
