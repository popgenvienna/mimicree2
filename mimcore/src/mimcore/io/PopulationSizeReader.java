package mimcore.io;

import mimcore.data.PopulationSizeContainer;
import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.migration.MigrationEntry;
import mimcore.io.fitnessfunction.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class PopulationSizeReader {

	private BufferedReader bf;
	private String popsizeFile;
	private Logger logger;
	public PopulationSizeReader(String popsizeFile, Logger logger)
	{
		this.popsizeFile=popsizeFile;
		try{
			bf=new BufferedReader(new FileReader(popsizeFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}

	public PopulationSizeReader(String popsizeFile, BufferedReader br, Logger logger)
	{
		this.popsizeFile=popsizeFile;
		this.bf=br;
		this.logger=logger;
	}

	
	/**
	 * read the selection regime
	 * @return
	 */
	public PopulationSizeContainer readPopulationSizes()
	{

		this.logger.info("Start reading population size file "+this.popsizeFile);
		HashMap<Integer,Integer> popsizes=new HashMap<Integer,Integer>();
		try
		{
			String line=null;
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\\s+");
				if(a.length!=2) throw new IllegalArgumentException("Invalid population size entry, must have 2 columns "+line);

				int generation=Integer.parseInt(a[0]);
				int populationSize=Integer.parseInt(a[1]);

				popsizes.put(generation,populationSize);
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

		this.logger.info("Finished reading the population sizes");
		return new PopulationSizeContainer(popsizes);
	}

}
