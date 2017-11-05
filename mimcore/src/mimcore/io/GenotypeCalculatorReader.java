package mimcore.io;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.quantitative.AdditiveSNPeffect;
import mimcore.data.gpf.quantitative.GenotypeCalculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GenotypeCalculatorReader {

	private BufferedReader bf;
	private String additiveFile;
	private Logger logger;
	public GenotypeCalculatorReader(String additiveFile, Logger logger)
	{
		this.additiveFile=additiveFile;
		try{
			bf=new BufferedReader(new FileReader(additiveFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}
	
	/**
	 * Retrieve the additive gpf effects of SNPs from a file
	 * @return
	 */
	public GenotypeCalculator readAdditiveFitness()
	{
		ArrayList<AdditiveSNPeffect> addSNPs=new ArrayList<AdditiveSNPeffect>();
		this.logger.info("Start reading additive genotypic effects from file "+this.additiveFile);
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				addSNPs.add(parseLine(line));
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
		
		
		this.logger.info("Finished reading " + addSNPs.size() + " additive gpf effects of SNPs");
		return new GenotypeCalculator(addSNPs);
	}
	
	
	
	private AdditiveSNPeffect parseLine(String line)
	{ 
		//  0		1				2		3		4
		//  X       3929069 		C       a    d
		//  3R      23302904        A       a    d

		//(GenomicPosition position, char achar, double a, double d)
		String[] a=line.split("\t");
		GenomicPosition gp=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		double aeffect=Double.parseDouble(a[3]);
		if(aeffect<0) throw new IllegalArgumentException("Fitness effect must not be smaller than zero");
		return new AdditiveSNPeffect(gp,a[2].charAt(0),aeffect,Double.parseDouble(a[4]));
		
	}
	

}
