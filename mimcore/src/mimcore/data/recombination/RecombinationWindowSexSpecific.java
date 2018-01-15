package mimcore.data.recombination;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.sex.Sex;
import mimcore.data.statistic.Poisson;

import java.util.Random;

public class RecombinationWindowSexSpecific implements IRecombinationWindow {
	private final Chromosome chromosome;
	private final int startPosition;
	private final int endPosition;
	private final double mlambda;
	private final double flambda;
	private final double hlambda;



	public RecombinationWindowSexSpecific(Chromosome chromosome, int startPosition, int endPosition, double mlambda, double flambda, double hlambda)
	{
		this.chromosome=chromosome;
		this.startPosition=startPosition;
		this.endPosition=endPosition;
		if(mlambda<0.0) throw new IllegalArgumentException("Lambda of a Poisson distribution must be larger than zero");
		this.mlambda=mlambda;
		if(mlambda<0.0) throw new IllegalArgumentException("Lambda of a Poisson distribution must be larger than zero");
		this.flambda=flambda;
		if(mlambda<0.0) throw new IllegalArgumentException("Lambda of a Poisson distribution must be larger than zero");
		this.hlambda=hlambda;

	}
	


	
	/**
	 * A random number generator decides whether a recombination event takes place within the given window
	 * @return
	 */
	public int getRecombinationEvents(Sex sex, Random random)
	{
		if(sex==Sex.Female) return Poisson.getPoisson(this.flambda,random);
		else if(sex==Sex.Male) return Poisson.getPoisson(this.mlambda,random);
		else if(sex==Sex.Hermaphrodite) return Poisson.getPoisson(this.hlambda,random);
		else throw new IllegalArgumentException("Invalid sex "+sex);
	}


	/**
	 * Obtain a random postion within the range of the RecombinationWindow 
	 * @return a random position
	 */
	public GenomicPosition getRandomPosition(Random random)
	{
				// eg.:  100		  			1 	=           100-1+1 =100 
		int length=(this.endPosition - this.startPosition + 1);
		
		// basically values between 0-99 (random numbers between 0 and 0.999)
		int randAdd= (int)(random.nextDouble()*length);
		
		// a random GenomicPosition within the window
		return new GenomicPosition(chromosome, this.startPosition+randAdd);
		
	}


	public Chromosome getChromosome(){return this.chromosome;}
	public int getStartPosition(){return this.startPosition;}
	public int getEndPosition(){return this.endPosition;}
	public double getMaleLambda(){return this.mlambda;}
	public double getFemaleLambda(){return this.flambda;}
	public double getHermaphroditeLambda(){return this.hlambda;}
}
