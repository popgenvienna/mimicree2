package mimcore.io.recombination;

import mimcore.data.Chromosome;
import mimcore.data.recombination.CrossoverGenerator;
import mimcore.data.recombination.IRecombinationWindow;
import mimcore.data.recombination.RecombinationWindow;
import mimcore.data.recombination.RecombinationWindowSexSpecific;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Loads the recombination rate from a file
 * @author robertkofler
 * hello this is a new comment
 */
public class RRRcMpMb {
	private BufferedReader bf;
	/**
	 * Contains a single entry of a file
	 * @author robertkofler
	 *
	 */



	public RRRcMpMb(BufferedReader br)
	{

		this.bf=br;
	}
	
	
	private IRecombinationWindow parseLine(String line)
	{
		// 2L:0..100000            2.1
		// 2L:100000..200000       2.2
		// 2L:200000..300000       0.1
		// 2L:300000..400000       3.1
		
		String[] a=line.split("\\s+");
		String[] tmp1=a[0].split(":");
		String[] tmp2=tmp1[1].split("\\.\\.");
		
		Chromosome chr=Chromosome.getChromosome(tmp1[0]);
		int start=Integer.parseInt(tmp2[0].trim())+1;
		int end=Integer.parseInt(tmp2[1].trim());
		double length=end-start+1;

		if(a.length==2) {
			double cmpmb = Double.parseDouble(a[1]);
			if (cmpmb >= 49.9) throw new IllegalArgumentException("Genetic distance must not be larger than 49.9cM/Mb");
			double lambda = haldanetransform(cmpmb, length);
			return new RecombinationWindow(chr, start, end, lambda);
		}
		else if(a.length==4)
		{
			// male - female - hermaphrodite
			double mcmpmb = Double.parseDouble(a[1]);
			double fcmpmb = Double.parseDouble(a[2]);
			double hcmpmb = Double.parseDouble(a[3]);
			if (mcmpmb >= 49.9|| fcmpmb >= 49.9|| hcmpmb >= 49.9) throw new IllegalArgumentException("Genetic distance must not be larger than 49.9cM/Mb");

			double flambda = haldanetransform(fcmpmb, length);
			double mlambda = haldanetransform(mcmpmb, length);
			double hlambda = haldanetransform(hcmpmb, length);
			return new RecombinationWindowSexSpecific(chr, start, end, mlambda,flambda,hlambda);
		}
		else throw new IllegalArgumentException("Input must have two or four columns; the genomic position (chr:start..end) followed by the recombination fraction(s)");
		
	}
	

	
	
	public CrossoverGenerator getRecombinationRate()
	{
		
		String line;
		ArrayList<IRecombinationWindow> entries=new ArrayList<IRecombinationWindow>();


		try
		{
			while((line=bf.readLine())!=null)
			{

				entries.add(parseLine(line));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return new CrossoverGenerator(entries);
	}


	public static double haldanetransform(double cmpmb, double windowsize)
	{

		// Recombination rate is in cM
		double recFraction=cmpmb/100;

		// return 0 for small recombination rates

		double cWin=1000000.0/windowsize;
		double distance=haldane1919mapFunction(recFraction);
		double lambdawin=distance/cWin;
		return lambdawin;
	}





	public static double haldane1919mapFunction(double rf)
	{
		double lambda = -0.5 * Math.log(1.0-2.0*rf);
		return lambda;
	}
	

}
