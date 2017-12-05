package mimcore.io.w;

import mimcore.data.gpf.fitness.FitnessCalculator_SNP;
import mimcore.data.recombination.CrossoverGenerator;
import mimcore.io.recombination.RRRLambda;
import mimcore.io.recombination.RRRRecFraction;
import mimcore.io.recombination.RRRcMpMb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Loads the recombination rate from a file
 * @author robertkofler
 * hello this is a new comment
 */
public class SNPFitnessReader {
	private BufferedReader bf;
	private String snpfitnessfile;
	private Logger logger;

	/**
	 * Contains a single entry of a file
	 * @author robertkofler
	 *
	 */


	public SNPFitnessReader(String snpfitnessfile, Logger logger)
	{
		this.snpfitnessfile=snpfitnessfile;
		try
		{
			bf=new BufferedReader(new FileReader(snpfitnessfile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}

	public SNPFitnessReader(String snpfitnessfile, BufferedReader br, Logger logger)
	{
		this.snpfitnessfile=snpfitnessfile;
		this.bf=br;
		this.logger=logger;
	}
	

	

	
	
	public FitnessCalculator_SNP getSNPFitness()
	{


		this.logger.info("Start reading fitness effects of SNPs from file "+this.snpfitnessfile);
		// deactivated male halfing as mim2 is intended for general audience
		//this.logger.info("Males in Drosophila do not recombine; Will thus multiply recombination rate by 1/2");
		FitnessCalculator_SNP snpfitness=null;

		try
		{
			String firstline=bf.readLine();
			if(firstline.toLowerCase().equals("[w]")) {
				this.logger.info("SNP fitness mode: [w]; effect sizes of SNPs provided as absolute fitness values");

				snpfitness= new SNPFitnessReaderW(this.bf).readSNPFitness();
			}
			else if(firstline.toLowerCase().equals("[s]")) {
				this.logger.info("SNP fitness mode: [s]; selection coefficients of SNPs were provided");

				snpfitness= new SNPFitnessReaderS(bf).readSNPFitness();
			}
			else throw new IllegalArgumentException("Do not recognize SNP format: "+firstline);


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
		
		this.logger.info("Finished reading effect sizes of SNPs; Read "+snpfitness.size()+ " entries");
		return snpfitness;
		
		
		
		
	}



	

}
