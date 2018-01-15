package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessFunctionQuantitativeDirectionalSelection implements IFitnessCalculator {

	private final double minFitness;
	private final double maxFitness;
	private final double deltaFitness;
	private final double s;
	private final double r;
	private final double beta;


	public FitnessFunctionQuantitativeDirectionalSelection(double minFitness, double maxFitness, double s, double r, double beta)
	{
		this.minFitness=minFitness;
		this.maxFitness=maxFitness;
		if(maxFitness<minFitness) throw new IllegalArgumentException("Max. fitness needs to be larger than min. fitness");
		if(s<0) throw new IllegalArgumentException("s must be larger than zero");
		this.s=s;
		this.r=r;
		this.beta=beta;

		this.deltaFitness=this.maxFitness-this.minFitness;
	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype, Sex sex)
	{
		double part1= 1 + this.s * Math.exp(r * (phenotype+this.beta));
		double part2=deltaFitness/Math.pow(part1,(1/this.s));
		double fitness=this.minFitness+part2;
		if(fitness<0) throw new IllegalArgumentException("Parameter yielded an invalid fitness: fitness lower than zero ; adjust parameters");
		return fitness;
	}

	public double getMinFitness(){return this.minFitness;}
	public double getMaxFitness(){return this.maxFitness;}
	public double getS(){return this.s;}
	public double getR(){return this.r;}
	public double getBeta(){return this.beta;}
}
