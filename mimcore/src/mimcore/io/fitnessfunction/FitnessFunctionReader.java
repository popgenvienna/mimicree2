package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import mimcore.data.gpf.fitness.IFitnessCalculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class FitnessFunctionReader {

	private BufferedReader bf;
	private String fitnessFunctionFile;
	private Logger logger;
	public FitnessFunctionReader(String fitnessFunctionFile, Logger logger)
	{
		this.fitnessFunctionFile=fitnessFunctionFile;
		try{
			bf=new BufferedReader(new FileReader(fitnessFunctionFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}

	public FitnessFunctionReader(String fitnessFunctionFile, BufferedReader br, Logger logger)
	{
		this.fitnessFunctionFile=fitnessFunctionFile;
		this.bf=br;
		this.logger=logger;
	}

	
	/**
	 * read the selection regime
	 * @return
	 */
	public FitnessFunctionContainer readFitnessFunction()
	{

		this.logger.info("Start reading fitness functions from  file "+this.fitnessFunctionFile);
		FitnessFunctionContainer container=null;
		try
		{
			String firstline=bf.readLine();
			if(firstline.toLowerCase().equals("[interpolate]"))
			{
				this.logger.info("Fitness function provided as dense points for an arbitrary function");
				container=new FFRArbitraryFunction(this.bf).readFitnessFunction();
			}
			else if(firstline.toLowerCase().equals("[stabilizing]"))
			{
				this.logger.info("A gaussian fitness function was provided");
				container =new FFRGaussian(this.bf).readFitnessFunction();

			}
			else if(firstline.toLowerCase().equals("[dimret]"))
			{
				this.logger.info("Diminishing returns epistasis was provided");
				container=new FFRDiminishingReturns(this.bf).readFitnessFunction();

			}
			else if(firstline.toLowerCase().equals("[dirsel]"))
			{
				this.logger.info("Directional selection was provided");
				container=new FFRDirectionalSelection(this.bf).readFitnessFunction();

			}
			else throw new IllegalArgumentException("Do not recognize fitness function "+firstline);

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

		this.logger.info("Finished reading the fitness functions");
		return container;
	}

}
