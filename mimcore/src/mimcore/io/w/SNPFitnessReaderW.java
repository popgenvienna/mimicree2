package mimcore.io.w;

import com.sun.javaws.exceptions.InvalidArgumentException;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessCalculator_SNP;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.gpf.fitness.FitnessOfSNPSexSpecific;
import mimcore.data.gpf.fitness.IFitnessOfSNP;
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
		String[] a=line.split("\\s+");
		GenomicPosition gp=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		String alleles=a[2];
		String[] tmp=alleles.split("/");
		char achar=tmp[0].charAt(0);
		char Achar=tmp[1].charAt(0);
		if(a.length==6) {
			double waa = Double.parseDouble(a[3]);
			double waA = Double.parseDouble(a[4]);
			double wAA = Double.parseDouble(a[5]);
			if (waa < 0) throw new InvalidParameterException("Fitness of waa must be larger than zero");
			if (waA < 0) throw new InvalidParameterException("Fitness of waA must be larger than zero");
			if (wAA < 0) throw new InvalidParameterException("Fitness of wAA must be larger than zero");
			if (waa < minEffect && waA < minEffect && wAA < minEffect)
				throw new InvalidParameterException("Fitness of at least one genotype must be larger than zero");
			return new FitnessOfSNP(gp, achar, Achar, waa, waA, wAA);
		}
		if(a.length==12)
		{
			double mwaa = Double.parseDouble(a[3]);
			double mwaA = Double.parseDouble(a[4]);
			double mwAA = Double.parseDouble(a[5]);
			if (mwaa < 0) throw new InvalidParameterException("Fitness of waa must be larger than zero");
			if (mwaA < 0) throw new InvalidParameterException("Fitness of waA must be larger than zero");
			if (mwAA < 0) throw new InvalidParameterException("Fitness of wAA must be larger than zero");
			if (mwaa < minEffect && mwaA < minEffect && mwAA < minEffect)
				throw new InvalidParameterException("Fitness of at least one genotype must be larger than zero");

			double fwaa = Double.parseDouble(a[6]);
			double fwaA = Double.parseDouble(a[7]);
			double fwAA = Double.parseDouble(a[8]);
			if (fwaa < 0) throw new InvalidParameterException("Fitness of waa must be larger than zero");
			if (fwaA < 0) throw new InvalidParameterException("Fitness of waA must be larger than zero");
			if (fwAA < 0) throw new InvalidParameterException("Fitness of wAA must be larger than zero");
			if (fwaa < minEffect && fwaA < minEffect && fwAA < minEffect)
				throw new InvalidParameterException("Fitness of at least one genotype must be larger than zero");

			double hwaa = Double.parseDouble(a[9]);
			double hwaA = Double.parseDouble(a[10]);
			double hwAA = Double.parseDouble(a[11]);
			if (hwaa < 0) throw new InvalidParameterException("Fitness of waa must be larger than zero");
			if (hwaA < 0) throw new InvalidParameterException("Fitness of waA must be larger than zero");
			if (hwAA < 0) throw new InvalidParameterException("Fitness of wAA must be larger than zero");
			if (hwaa < minEffect && hwaA < minEffect && hwAA < minEffect)
				throw new InvalidParameterException("Fitness of at least one genotype must be larger than zero");

			return new FitnessOfSNPSexSpecific(
					new FitnessOfSNP(gp, achar, Achar, mwaa, mwaA, mwAA),
					new FitnessOfSNP(gp, achar, Achar, fwaa, fwaA, fwAA),
					new FitnessOfSNP(gp, achar, Achar, hwaa, hwaA, hwAA));
		}
		else throw new InvalidParameterException("Invalid entry of SNP fitness; must either have 6 or 12 columns");
		
	}
	

}
