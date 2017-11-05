package mimcore.io.w;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessCalculator_Epistasis;
import mimcore.data.gpf.fitness.FitnessCalculator_SNP;
import mimcore.data.gpf.fitness.FitnessOfEpistasisPair;
import mimcore.data.gpf.fitness.FitnessOfSNP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class EpistasisFitnessReader {

	private BufferedReader bf;
	private String epistasisFile;
	private Logger logger;
	public EpistasisFitnessReader(String fitnessFile, Logger logger)
	{
		this.epistasisFile=fitnessFile;
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
	public FitnessCalculator_Epistasis readEpistaticPairs()
	{
		ArrayList<FitnessOfEpistasisPair> epis=new ArrayList<FitnessOfEpistasisPair>();
		this.logger.info("Start reading epistatic fitness effects of pairs of SNPs from file "+this.epistasisFile);
		String line;
		try
		{
			int linecounter=0;
			String[] threeLines=new String[3];
			while((line=bf.readLine())!=null)
			{
				// skip empty lines
				if(line=="") continue;

				int index=linecounter%3;
				threeLines[index]=line;
				if(linecounter>0 && index==0) epis.add(parseThreeLines(threeLines));

				// last increment counter
				linecounter++;
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
		return new FitnessCalculator_Epistasis(snps);
	}
	
	
	
	private FitnessOfEpistasisPair parseThreeLines(String[] lines)
	{
		// chr1    pos1    a/A
		// chr2    pos2    b/B
		// waabb   waabB    waaBB    waAbb    waAbB    waABB    wAAbb    wAAbB    wAABB

		String[] a1=lines[0].split("\t");
		String[] a2=lines[1].split("\t");
		String[] a3=lines[2].split("\t");
		if(a1.length!=3) throw new IllegalArgumentException("Invalid entry for first SNP of epistatic pair; must have 3 columns "+ lines[0]);
		if(a2.length!=3) throw new IllegalArgumentException("Invalid entry for second SNP of epistatic pair; must have 3 columns "+ lines[1]);
		if(a3.length!=9) throw new IllegalArgumentException("Invalid entry for fitness effects of epistatic pair; must have 9 columns "+ lines[2]);


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
