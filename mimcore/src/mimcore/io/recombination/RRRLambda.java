package mimcore.io.recombination;

import mimcore.data.Chromosome;
import mimcore.data.recombination.CrossoverGenerator;
import mimcore.data.recombination.IRecombinationWindow;
import mimcore.data.recombination.RecombinationWindow;
import mimcore.data.recombination.RecombinationWindowSexSpecific;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Loads the recombination rate from a file
 * @author robertkofler
 * hello this is a new comment
 */
public class RRRLambda {
	private BufferedReader bf;
	/**
	 * Contains a single entry of a file
	 * @author robertkofler
	 *
	 */



	public RRRLambda( BufferedReader br)
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

		if(a.length==2) {
			double lambda = Double.parseDouble(a[1]);
			return new RecombinationWindow(chr,start,end,lambda);
		}
		else if(a.length==4)
		{
			// male female hermaphrodite
			double mlambda = Double.parseDouble(a[1]);
			double flambda = Double.parseDouble(a[2]);
			double hlambda = Double.parseDouble(a[3]);
			return new RecombinationWindowSexSpecific(chr,start,end,mlambda,flambda,hlambda);
		}
		else throw new IllegalArgumentException("Input must have two or four columns; the genomic position (chr:start..end) followed by the recombination fraction(s)");



		// MALES are not recombining and the published recombination rate is for females
		// deactivated male halfing - because mim2 is intented for general audience
		//recrate=recrate * 0.5;

		
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
	

}
