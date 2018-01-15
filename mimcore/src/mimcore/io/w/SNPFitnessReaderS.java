package mimcore.io.w;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessCalculator_SNP;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.gpf.fitness.FitnessOfSNPSexSpecific;
import mimcore.data.gpf.fitness.IFitnessOfSNP;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SNPFitnessReaderS {

	private BufferedReader bf;

	/*
	mostly for debugging
	 */
	public SNPFitnessReaderS(BufferedReader br)
	{

		this.bf=br;
	}
	
	/**
	 * Retrieve the additive gpf effects of SNPs from a file
	 * @return
	 */
	public FitnessCalculator_SNP readSNPFitness()
	{
		ArrayList<IFitnessOfSNP> snps=new ArrayList<IFitnessOfSNP>();
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
	
	
	
	private IFitnessOfSNP parseLine(String line)
	{ 
		// X        3929069    C/A       0.8    1.0    1.2
		// 3R      23302904      G/C      1.0    0.8     0.9

		//(GenomicPosition position, char achar, double a, double d)
		String[] a=line.split("\t");
		GenomicPosition gp=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		String alleles=a[2];
		String[] tmp = alleles.split("/");
		char achar = tmp[0].charAt(0);
		char Achar = tmp[1].charAt(0);


		if(a.length==5) {
			double s = Double.parseDouble(a[3]);
			double h = Double.parseDouble(a[4]);
			double waa = 1.0;
			double waA = 1.0 + s * h;
			double wAA = 1.0 + s;
			if (waa < 0 || waA < 0 || wAA < 0)
				throw new IllegalArgumentException("Invalid parameters for SNP; fitness of genotypes must be larger than zero; " + line);
			return new FitnessOfSNP(gp, achar, Achar, waa, waA, wAA);
		}
		else if(a.length==9)
		{
			// male
			double ms = Double.parseDouble(a[3]);
			double mh = Double.parseDouble(a[4]);
			double mwaa = 1.0;
			double mwaA = 1.0 + ms * mh;
			double mwAA = 1.0 + ms;
			if (mwaa < 0 || mwaA < 0 || mwAA < 0)
				throw new IllegalArgumentException("Invalid parameters for SNP; fitness of genotypes must be larger than zero; " + line);
			// female
			double fs = Double.parseDouble(a[5]);
			double fh = Double.parseDouble(a[6]);
			double fwaa = 1.0;
			double fwaA = 1.0 + fs * fh;
			double fwAA = 1.0 + fs;
			if (fwaa < 0 || fwaA < 0 || fwAA < 0)
				throw new IllegalArgumentException("Invalid parameters for SNP; fitness of genotypes must be larger than zero; " + line);
			// hermaphrodite
			double hs = Double.parseDouble(a[7]);
			double hh = Double.parseDouble(a[8]);
			double hwaa = 1.0;
			double hwaA = 1.0 + hs * hh;
			double hwAA = 1.0 + hs;
			if (hwaa < 0 || hwaA < 0 || hwAA < 0)
				throw new IllegalArgumentException("Invalid parameters for SNP; fitness of genotypes must be larger than zero; " + line);


			return new  FitnessOfSNPSexSpecific(new FitnessOfSNP(gp, achar, Achar, mwaa, mwaA, mwAA),
					new FitnessOfSNP(gp, achar, Achar, fwaa, fwaA, fwAA),
					new FitnessOfSNP(gp, achar, Achar, hwaa, hwaA, hwAA));

		}
		else throw new IllegalArgumentException("Invalid entry of SNP effect size, must have either 5 or 9 columns");
		
	}
	

}
