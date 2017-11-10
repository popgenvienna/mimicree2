package mimcore.io;

import mimcore.data.Chromosome;
import mimcore.data.recombination.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.logging.Logger;

/**
 * Loads the recombination rate from a file
 * @author robertkofler
 * hello this is a new comment
 */
public class RecombinationRateReader {
	private BufferedReader bf;
	private String recombinationFile;
	private Logger logger;
	
	/**
	 * Contains a single entry of a file
	 * @author robertkofler
	 *
	 */
	
	
	public RecombinationRateReader(String recombinationFile,Logger logger)
	{
		this.recombinationFile=recombinationFile;
		try
		{
			bf=new BufferedReader(new FileReader(recombinationFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}

	public RecombinationRateReader(String recombinationFile,BufferedReader br, Logger logger)
	{
		this.recombinationFile=recombinationFile;
		this.bf=br;
		this.logger=logger;
	}
	
	
	private RecombinationWindow parseLine(String line,RecombinationMode recombinationMode)
	{
		// 2L:0..100000            2.1
		// 2L:100000..200000       2.2
		// 2L:200000..300000       0.1
		// 2L:300000..400000       3.1
		
		String[] a=line.split("\t");
		if(a.length>2) throw new IllegalArgumentException("Input must have two columns; the genomic position (chr:start..end) followed by the recombination fraction");
		String[] tmp1=a[0].split(":");
		String[] tmp2=tmp1[1].split("\\.\\.");
		
		Chromosome chr=Chromosome.getChromosome(tmp1[0]);
		int start=Integer.parseInt(tmp2[0].trim())+1;
		int end=Integer.parseInt(tmp2[1].trim());

		double rec= Double.parseDouble(a[1]);
		if(recombinationMode==RecombinationMode.RF)
		{
			if(rec>0.5) throw new IllegalArgumentException("Recombination fraction between two loci can not be larger than 0.5; Ideal would be a value smaller than 0.4");
			rec=haldane1919mapFunction(rec);
		}
		else if(recombinationMode==RecombinationMode.LAMBDA){int nix=0;}
		else throw new IllegalArgumentException("Do not support recombination mode "+recombinationMode);


		// MALES are not recombining and the published recombination rate is for females
		// deactivated male halfing - because mim2 is intented for general audience
		//recrate=recrate * 0.5;
		return new RecombinationWindow(chr,start,end,rec);
		
	}
	

	
	
	public CrossoverGenerator getRecombinationRate()
	{
		
		String line;
		ArrayList<RecombinationWindow> entries=new ArrayList<RecombinationWindow>();
		this.logger.info("Start reading recombinaton rate from file "+this.recombinationFile);
		// deactivated male halfing as mim2 is intended for general audience
		//this.logger.info("Males in Drosophila do not recombine; Will thus multiply recombination rate by 1/2");
		RecombinationMode recmode=null;

		try
		{
			while((line=bf.readLine())!=null)
			{
				if(recmode==null)
				{
					recmode=getRecMode(line);
				}
				else entries.add(parseLine(line,recmode));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		
		try
		{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.logger.info("Finished reading recombination rate; Read "+entries.size()+ " entries");
		return new CrossoverGenerator(entries);
		
		
		
		
	}

	public static double haldane1919mapFunction(double rf)
	{
		double lambda = -0.5 * Math.log(1.0-2.0*rf);
		return lambda;
	}

	private RecombinationMode getRecMode(String firstLine)
	{
		if(firstLine.toLowerCase().equals("[lambda]")) {
			this.logger.info("Recombination mode: [lambda]; recombination rates are provided as lambda values of a Poisson distribution");
			return RecombinationMode.LAMBDA;
		}
		else if(firstLine.toLowerCase().equals("[rf]")) {
			this.logger.info("Recombination mode: [rf]; recombination rates are provided as recombination fractions; Will compute lambda values of a Poisson distribution using Haldanes map function (1919)");
			return RecombinationMode.RF;
		}

		else throw new IllegalArgumentException("Do not recognize recombination encoding; allowed values [lambda], [rf]; got: "+firstLine);
	}

	private enum RecombinationMode {RF,LAMBDA};
	

}
