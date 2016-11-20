package mimcore.data.recombination;

import mimcore.data.*;
import java.util.Random;

public class RecombinationWindow {
	private final Chromosome chromosome;
	private final int startPosition;
	private final int endPosition;
	private final double recRate;
	private final double p_recombination;
	
	public RecombinationWindow(Chromosome chromosome, int startPosition, int endPosition, double recRate)
	{
		this.chromosome=chromosome;
		this.startPosition=startPosition;
		this.endPosition=endPosition;
		if(recRate > 45.0)throw new IllegalArgumentException("Can not handle recombination rates larger than 45cM/Mb (Kosambi function fails)");
		this.recRate=recRate;
		int windowsize=endPosition - startPosition +  1;
		if(windowsize > 1000000.0) throw new IllegalArgumentException("Window size for recombination events needs to be smaller than 1Mbp");
		this.p_recombination=calculateP(recRate,windowsize);

	}
	
	
	private double calculateP(double recRate,int windowsize)
	{
		
		// Recombination rate is in cM
		double recFraction=recRate/100;
		
		// return 0 for small recombination rates
		if(Math.abs(recFraction)<0.000001) return 0.0;
		double cWin=1000000.0/windowsize;
		double dMb=inverseKosambiFunction(recFraction); // tested correct
		double dWin=dMb/cWin;
		double rWin=kosambiFunction(dWin);
		return rWin;
	}
	
	
	private double kosambiFunction(double mapDistance)
	{
		double po=Math.pow(Math.E, (4.0*mapDistance));
		double above= (po - 1.0);
		double below = (po + 1.0);
		double below2=2.0*below;
		double toRet=above/below2;
		return toRet;
	}
	
	private double inverseKosambiFunction(double recRate)
	{
		double div=(1.0 + 2.0 * recRate)/(1.0 - 2.0 * recRate);
		double toRet=0.25 * Math.log( div );
		return toRet;
		
	}
	

	
	
	
	/**
	 * A random number generator decides whether a recombination event takes place within the given window
	 * @return
	 */
	public boolean hasRecombinationEvent(Random random)
	{
		
		double rand=random.nextDouble();
		if(rand<this.p_recombination)return true;
		return false;
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
	
	
	/**
	 * Obtain the recombination rate in cM/Mb for the given window
	 * @return
	 */
	public double recombinationRate()
	{
		return this.recRate;
	}
	
}
