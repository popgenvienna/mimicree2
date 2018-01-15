package mimcore.data.recombination;

import mimcore.data.*;
import mimcore.data.sex.Sex;
import mimcore.data.statistic.Poisson;

import java.util.Random;

public class RecombinationWindow implements IRecombinationWindow {
	private final Chromosome chromosome;
	private final int startPosition;
	private final int endPosition;
	private final double lambda;


	
	public RecombinationWindow(Chromosome chromosome, int startPosition, int endPosition, double lambda)
	{
		this.chromosome=chromosome;
		this.startPosition=startPosition;
		this.endPosition=endPosition;
		if(lambda<0.0) throw new IllegalArgumentException("Lambda of a Poisson distribution must be larger than zero");
		this.lambda=lambda;

	}
	


	
	/**
	 * A random number generator decides whether a recombination event takes place within the given window
	 * @return
	 */
	public int getRecombinationEvents(Sex sex, Random random)
	{
		return Poisson.getPoisson(this.lambda,random);
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
	public double getLambda(){return this.lambda;}

	
}
