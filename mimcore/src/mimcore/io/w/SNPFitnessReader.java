package mimcore.io.w;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessCalculator_SNP;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.gpf.quantitative.AdditiveSNPeffect;
import mimcore.data.gpf.quantitative.GenotypeCalculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SNPFitnessReader {

	private BufferedReader bf;
	private String fitnessFile;
	private Logger logger;
	public SNPFitnessReader(String fitnessFile, Logger logger)
	{
		this.fitnessFile=fitnessFile;
		try{
			bf=new BufferedReader(new FileReader(fitnessFile));
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
	public FitnessCalculator_SNP readSNPFitness()
	{
		ArrayList<FitnessOfSNP> snps=new ArrayList<FitnessOfSNP>();
		this.logger.info("Start reading fitness of SNPs from file "+this.fitnessFile);
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				snps.add(parseLine(line));
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
		
		
		this.logger.info("Finished reading " + snps.size() + " fitness effects of SNPs");
		return new FitnessCalculator_SNP(snps);
	}
	
	
	
	private FitnessOfSNP parseLine(String line)
	{ 
		// X        3929069    C/A       0.8    1.0    1.2
		// 3R      23302904      G/C      1.0    0.8     0.9

		//(GenomicPosition position, char achar, double a, double d)
		String[] a=line.split("\t");
		GenomicPosition gp=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		String alleles=a[2];
		double waa=Double.parseDouble(a[3]);
		double waA=Double.parseDouble(a[4]);
		double wAA=Double.parseDouble(a[5]);
		String[] tmp=alleles.split("/");
		char achar=tmp[0].charAt(0);
		char Achar=tmp[1].charAt(0);

		return new FitnessOfSNP(gp,achar,Achar,waa,waA,wAA);
		
	}
	

}
