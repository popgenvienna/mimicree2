package mimcore.io.recombination;

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
	

	

	
	
	public CrossoverGenerator getRecombinationRate()
	{


		this.logger.info("Start reading recombinaton rate from file "+this.recombinationFile);
		// deactivated male halfing as mim2 is intended for general audience
		//this.logger.info("Males in Drosophila do not recombine; Will thus multiply recombination rate by 1/2");
		CrossoverGenerator crgen=null;

		try
		{
			String firstline=bf.readLine();
			if(firstline.toLowerCase().equals("[lambda]")) {
				this.logger.info("Recombination mode: [lambda]; recombination rates are provided as lambda values of a Poisson distribution");

				crgen= new RRRLambda(bf).getRecombinationRate();
			}
			else if(firstline.toLowerCase().equals("[rf]")) {
				this.logger.info("Recombination mode: [rf]; recombination rates are provided as recombination fractions; Will compute lambda values of a Poisson distribution using Haldanes map function (1919)");


				crgen= new RRRRecFraction(bf).getRecombinationRate();
			}
			else if(firstline.toLowerCase().equals("[cm/mb]"))
			{
				this.logger.info("Recombination mode: [cM/Mb]; recombination rates are provided as centi Morgan per mega base pair; Will compute lambda values of a Poisson distribution using Haldanes map function (1919)");
				crgen=new RRRcMpMb(bf).getRecombinationRate();
			}
			else throw new IllegalArgumentException("Do not recognize recombination encoding: "+firstline);


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
		
		this.logger.info("Finished reading recombination rate; Read "+crgen.size()+ " entries");
		return crgen;
		
		
		
		
	}



	

}
