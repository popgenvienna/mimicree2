package qmimcore.io.haplotypes;

import qmimcore.data.haplotypes.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class HaplotypeWriter {
	private BufferedWriter bf; 
	private final String outputFile;
	private Logger logger;
	public HaplotypeWriter(String outputFile, Logger logger)
	{
		// The extension will be decided at the level which output encoding should be used.
		// Output encoding will be decided at this level, thus extension also here.
		String gzipOutput=outputFile+".gz";
		this.logger=logger;
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
	
	
	
	public void write(ArrayList<HaploidGenome> haplotypes)
	{
		this.logger.info("Writing haplotypes to file " + this.outputFile);
		if(!(haplotypes.size()>0)) throw new IllegalArgumentException("Invalid number of haplotypes for output, must be larger than zero");
	
		SNPCollection scol=haplotypes.get(0).getSNPCollection();
		for(int i=0; i<scol.size(); i++)
		{
			SNP activeSNP=scol.getSNPforIndex(i);
			ArrayList<Character> chars=new ArrayList<Character>();
			for(int k=0; k < haplotypes.size(); k++)
			{
				HaploidGenome activeHap=haplotypes.get(k);
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
		
		this.logger.info("Finished writing "+haplotypes.size() +" haplotypes");
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
			sb.append(chars.get(i+1));
		}
		return sb.toString();
		
	}
	
	
	
	
}
