package mimcore.io;

import mimcore.data.Chromosome;
import mimcore.data.recombination.*;

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
	
	
	private RecombinationWindow parseLine(String line)
	{
		// 2L:0..100000            0.00            0.00            0.00      
		// 2L:100000..200000       0.00            0.00            0.00      
		// 2L:200000..300000       0.00            0.00            1.89      
		// 2L:300000..400000       1.89            1.92            1.95      
		
		String[] a=line.split("\t");
		String[] tmp1=a[0].split(":");
		String[] tmp2=tmp1[1].split("\\.\\.");
		
		Chromosome chr=Chromosome.getChromosome(tmp1[0]);
		int start=Integer.parseInt(tmp2[0].trim())+1;
		int end=Integer.parseInt(tmp2[1].trim());
		double recrate=Double.parseDouble(a[2]);

		// MALES are not recombining and the published recombination rate is for females
		// deactivated male halfing - because mim2 is intented for general audience
		//recrate=recrate * 0.5;
		return new RecombinationWindow(chr,start,end,recrate);
		
	}
	

	
	
	public CrossoverGenerator getRecombinationRate()
	{
		
		String line;
		ArrayList<RecombinationWindow> entries=new ArrayList<RecombinationWindow>();
		this.logger.info("Start reading recombinaton rate from file "+this.recombinationFile);
		// deactivated male halfing as mim2 is intended for general audience
		//this.logger.info("Males in Drosophila do not recombine; Will thus multiply recombination rate by 1/2");
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
	
	

}
