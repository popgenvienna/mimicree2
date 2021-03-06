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
	public EpistasisFitnessReader(String epistasisFile, Logger logger)
	{
		this.epistasisFile=epistasisFile;
		try{
			bf=new BufferedReader(new FileReader(epistasisFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}

	public EpistasisFitnessReader(String epistasisFile, BufferedReader br, Logger logger)
	{
		this.epistasisFile=epistasisFile;
		this.bf=br;
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
				// snp 1   mod 0
				// snp 2   mod 1
				// epista  mod 2
				if(index==2) epis.add(parseThreeLines(threeLines));

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
		
		
		this.logger.info("Finished reading " + epis.size() + " fitness effects for epistatic pairs of SNPs");
		return new FitnessCalculator_Epistasis(epis);
	}
	
	
	
	private FitnessOfEpistasisPair parseThreeLines(String[] lines)
	{
		// chr1    pos1    a/A
		// chr2    pos2    b/B
		// waabb   waabB    waaBB    waAbb    waAbB    waABB    wAAbb    wAAbB    wAABB

		String[] a1=lines[0].split("\\s+");
		String[] a2=lines[1].split("\\s+");
		String[] a3=lines[2].split("\\s+");
		if(a1.length!=3) throw new IllegalArgumentException("Invalid entry for first SNP of epistatic pair; must have 3 columns "+ lines[0]);
		if(a2.length!=3) throw new IllegalArgumentException("Invalid entry for second SNP of epistatic pair; must have 3 columns "+ lines[1]);
		if(a3.length!=9) throw new IllegalArgumentException("Invalid entry for fitness effects of epistatic pair; must have 9 columns "+ lines[2]);


		GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome(a1[0]),Integer.parseInt(a1[1]));
		GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome(a2[0]),Integer.parseInt(a2[1]));
		String[] tmp1=a1[2].split("/");
		String[] tmp2=a2[2].split("/");
		char achar=tmp1[0].charAt(0);
		char Achar=tmp1[1].charAt(0);
		char bchar=tmp2[0].charAt(0);
		char Bchar=tmp2[1].charAt(0);

		double[] epiFit=new double[9];
		for(int i=0; i<9;i++)
		{
			double ep=Double.parseDouble(a3[i]);
			epiFit[i]=ep;
		}

		return new FitnessOfEpistasisPair(pos1,pos2,achar,Achar,bchar,Bchar,epiFit);
		
	}
	

}
