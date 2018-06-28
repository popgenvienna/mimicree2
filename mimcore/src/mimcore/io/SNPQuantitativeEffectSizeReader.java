package mimcore.io;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.quantitative.AdditiveSNPeffect;
import mimcore.data.gpf.quantitative.AdditiveSNPeffectSexSpecific;
import mimcore.data.gpf.quantitative.GenotypeCalculator;
import mimcore.data.gpf.quantitative.IAdditiveSNPeffect;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SNPQuantitativeEffectSizeReader {

	private BufferedReader bf;
	private String additiveFile;
	private Logger logger;
	public SNPQuantitativeEffectSizeReader(String additiveFile, Logger logger)
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
	public SNPQuantitativeEffectSizeReader(String additiveFile, BufferedReader br, Logger logger)
	{
		this.additiveFile=additiveFile;
		this.bf=br;
		this.logger=logger;
	}
	
	/**
	 * Retrieve the additive gpf effects of SNPs from a file
	 * @return
	 */
	public GenotypeCalculator readAdditiveFitness()
	{
		ArrayList<IAdditiveSNPeffect> addSNPs=new ArrayList<IAdditiveSNPeffect>();
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
	
	
	
	private IAdditiveSNPeffect parseLine(String line)
	{ 
		//  0		1				2		3		4
		//  X       3929069 		C       a    d
		//  3R      23302904        A       a    d

		//(GenomicPosition position, char achar, double a, double d)
		String[] a=line.split("\\s+");
		GenomicPosition gp=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		String[] tmp=a[2].split("/");
		char achar=tmp[0].charAt(0);
		char altchar=tmp[1].charAt(0);



		if(a.length==5) {
			double aeffect=Double.parseDouble(a[3]);
			//if(aeffect<0) throw new IllegalArgumentException("Genotype effect must not be smaller than zero");
			//if(aeffect<0.000000001) throw new IllegalArgumentException("Genotype effects must not be null (or approximately null): "+aeffect);
			double deffect=Double.parseDouble(a[4]);
			return new AdditiveSNPeffect(gp, achar, altchar, aeffect, deffect);
		}
		else if(a.length==9)
		{
			double maeffect=Double.parseDouble(a[3]);
			//if(maeffect<0) throw new IllegalArgumentException("Genotype effect must not be smaller than zero");
			//if(maeffect<0.000000001) throw new IllegalArgumentException("Genotype effects must not be null (or approximately null): "+maeffect);
			double mdeffect=Double.parseDouble(a[4]);

			double faeffect=Double.parseDouble(a[5]);
			//if(faeffect<0) throw new IllegalArgumentException("Genotype effect must not be smaller than zero");
			//if(faeffect<0.000000001) throw new IllegalArgumentException("Genotype effects must not be null (or approximately null): "+faeffect);
			double fdeffect=Double.parseDouble(a[6]);

			double haeffect=Double.parseDouble(a[7]);
			//if(haeffect<0.0) throw new IllegalArgumentException("Genotype effect must not be smaller than zero");
			//if(haeffect<0.000000001) throw new IllegalArgumentException("Genotype effects must not be null (or approximately null): "+haeffect);
			double hdeffect=Double.parseDouble(a[8]);

			return new AdditiveSNPeffectSexSpecific(new AdditiveSNPeffect(gp, achar, altchar, maeffect, mdeffect),
					new AdditiveSNPeffect(gp, achar, altchar, faeffect, fdeffect),
					new AdditiveSNPeffect(gp, achar, altchar, haeffect, hdeffect));

		}
		else throw new IllegalArgumentException("Invalid entry; must hae 6 or 10 columns");
		
	}
	

}
