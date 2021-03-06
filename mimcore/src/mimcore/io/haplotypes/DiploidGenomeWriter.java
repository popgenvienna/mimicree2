package mimcore.io.haplotypes;

import mimcore.data.*;
import mimcore.data.haplotypes.*;
import mimcore.data.sex.Sex;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class DiploidGenomeWriter {
	private BufferedWriter bf;
	private final String outputFile;
	private Logger logger;
	private boolean haploid;
	public DiploidGenomeWriter(String outputFile, boolean haploid, Logger logger)
	{
		// The extension will be decided at the level which output encoding should be used.
		// Output encoding will be decided at this level, thus extension also here.
		String gzipOutput=outputFile+".gz";
		this.logger=logger;
		this.haploid=haploid;
		try
		{
			bf=new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(gzipOutput))));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.outputFile=gzipOutput;
	}

	public void write(ArrayList<DiploidGenome> genomes, ArrayList<Sex> sexes)
	{
		this.logger.info("Writing genomes to file " + this.outputFile);
		if(!(genomes.size()>0)) throw new IllegalArgumentException("Invalid number of genomes for output, must be larger than zero");
		writeSex(sexes);
		writeHaplotypes(genomes);
		this.logger.info("Finished writing "+genomes.size() +" genomes");
	}

	public void write(ArrayList<DiploidGenome> genomes)
	{
		this.logger.info("Writing genomes to file " + this.outputFile);
		if(!(genomes.size()>0)) throw new IllegalArgumentException("Invalid number of genomes for output, must be larger than zero");
		writeHaplotypes(genomes);
		this.logger.info("Finished writing "+genomes.size() +" genomes");
	}
	

	
	private void writeHaplotypes(ArrayList<DiploidGenome> genomes)
	{

		ArrayList<HaploidGenome> haploids=this.getHaploids(genomes);

	
		SNPCollection scol=haploids.get(0).getSNPCollection();
		for(int i=0; i<scol.size(); i++)
		{
			SNP activeSNP=scol.getSNPforIndex(i);
			ArrayList<Character> chars=new ArrayList<Character>();
			for(int k=0; k < haploids.size(); k++)
			{
				HaploidGenome activeHap=haploids.get(k);
				boolean hasMajor=activeHap.hasAncestral(i);
				if(hasMajor)
				{
					chars.add(activeSNP.ancestralAllele());
				}
				else
				{
					chars.add(activeSNP.derivedAllele());
				}
			}
			
			String toWrite=formatOutput(activeSNP,chars);
			try
			{
				bf.write(toWrite+"\n");
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		
		try{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

	}

	private void writeSex(ArrayList<Sex> sexes)
	{
		if(sexes!=null)	return;
		StringBuilder sb = new StringBuilder();
		sb.append("#sex");
		for(Sex s: sexes)
		{
			sb.append(" ");
			if(s==Sex.Female)sb.append("F");
			else if(s==Sex.Male)sb.append("M");
			else if(s==Sex.Hermaphrodite)sb.append("H");
			else throw new IllegalArgumentException("Unknown sex");
		}
		try
		{
			bf.write(sb.toString()+"\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	private ArrayList<HaploidGenome> getHaploids(ArrayList<DiploidGenome> diploids)
	{
		ArrayList<HaploidGenome> toret=new ArrayList<HaploidGenome>();
		for(DiploidGenome g:diploids)
		{
			toret.add(g.getHaplotypeA());
			toret.add(g.getHaplotypeB());
		}
		return toret;
	}

	
	private String formatOutput(SNP snp,ArrayList<Character> chars)
	{
		StringBuilder sb=new StringBuilder();
		//2L      861026    T      A/T    TT AT AA AA TT
		
		sb.append(snp.genomicPosition().chromosome().toString()+"\t");
		sb.append(snp.genomicPosition().position());
		sb.append("\t");
		sb.append(snp.referenceCharacter());
		sb.append("\t");
		sb.append(snp.ancestralAllele());
		sb.append("/");
		sb.append(snp.derivedAllele());
		sb.append("\t");
		sb.append(formatHaplotypes(chars));
		return sb.toString();
		
	}
	
	private String formatHaplotypes(ArrayList<Character> chars)
	{
		StringBuilder sb=new StringBuilder();
		sb.append(chars.get(0));
		sb.append(chars.get(1));
		for(int i=2; i<chars.size(); i+=2)
		{
			sb.append(" ");
			sb.append(chars.get(i));
			if(!haploid)sb.append(chars.get(i+1));
		}
		return sb.toString();
		
	}
	
	
	
	
}
