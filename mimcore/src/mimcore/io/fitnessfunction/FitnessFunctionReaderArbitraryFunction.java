package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FitnessFunctionReaderArbitraryFunction {

	private BufferedReader bf;
	private String fitnessFunctionFile;
	private Logger logger;
	public FitnessFunctionReaderArbitraryFunction(String fitnessFunctionFile, Logger logger)
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
	
	/**
	 * read the selection regime
	 * @return
	 */
	public FitnessFunctionContainer readFitnessFunction()
	{

		this.logger.info("Start reading user defined approximation of fitness functions from file "+this.fitnessFunctionFile);
		HashMap<Integer,ArrayList<ArbitraryLandscapeEntry>> data=new HashMap<Integer,ArrayList<ArbitraryLandscapeEntry>>();
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\t");
				if(a.length!=3) throw new IllegalArgumentException("Every entry in the fitness function file must have exactly three columns (tab separated)");


				int generation=Integer.parseInt(a[0]);
				double phenotypicValue=Double.parseDouble(a[1]);
				double fitness=Double.parseDouble(a[2]);
				if(fitness<0) throw new IllegalArgumentException("Fitness must be equal or larger than zero");
				ArbitraryLandscapeEntry ae=new ArbitraryLandscapeEntry(phenotypicValue,fitness);

				if(!data.containsKey(generation)) data.put(generation,new ArrayList<ArbitraryLandscapeEntry>());
				data.get(generation).add(ae);
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

		// map the entries to the Fitness functions
		HashMap<Integer,IFitnessCalculator> res=new HashMap<Integer, IFitnessCalculator>();
		for(Map.Entry<Integer,ArrayList<ArbitraryLandscapeEntry>> entry: data.entrySet())
		{
			Integer key= entry.getKey();
			ArrayList<ArbitraryLandscapeEntry> value=entry.getValue();
			res.put(key, new FitnessFunctionArbitraryLandscape(value));
		}

		this.logger.info("Finished reading the arbitrary fitness functions");
		return new FitnessFunctionContainer(res);
	}

}
