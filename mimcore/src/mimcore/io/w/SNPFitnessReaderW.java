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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SNPFitnessReaderW {

	private BufferedReader bf;
	private Logger logger;
	private final double minEffect=0.00000001;

	/*
	mostly for debugging
	 */
	public SNPFitnessReaderW(BufferedReader br)
	{

		this.bf=br;
	}
	
	/**
	 * Retrieve the additive gpf effects of SNPs from a file
	 * @return
	 */
	public FitnessCalculator_SNP readSNPFitness()
	{
		ArrayList<FitnessOfSNP> snps=new ArrayList<FitnessOfSNP>();
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
		if(waa<0) throw new InvalidParameterException("Fitness of waa must be larger than zero");
		if(waA<0) throw new InvalidParameterException("Fitness of waA must be larger than zero");
		if(wAA<0) throw new InvalidParameterException("Fitness of wAA must be larger than zero");
		if(waa<minEffect && waA <minEffect && wAA< minEffect) throw new InvalidParameterException("Fitness of at least one genotype must be larger than zero");
		String[] tmp=alleles.split("/");
		char achar=tmp[0].charAt(0);
		char Achar=tmp[1].charAt(0);

		return new FitnessOfSNP(gp,achar,Achar,waa,waA,wAA);
		
	}
	

}
