package qmimcore.io.misc;

import qmimcore.data.fitness.*;
import qmimcore.data.haplotypes.SNP;
import qmimcore.data.haplotypes.SNPCollection;
import qmimcore.data.statistic.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SumWriter implements ISummaryWriter{
	public final String outputFile;
	public BufferedWriter bf;
	public Logger logger;
	public SumWriter(String outputFile,Logger logger)
	{
		this.outputFile=outputFile;
		this.logger=logger;
		try
		{
			bf=new BufferedWriter(new FileWriter(outputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void write(ArrayList<PopulationAlleleCount> pacs)
	{
		
		assert(pacs.size()>0);
		this.logger.info("Writing haplotypes into summary file "+this.outputFile);
		SNPCollection snpcol=pacs.get(0).getSNPCollection();
		
		for(int i=0; i<snpcol.size(); i++)
		{
			StringBuilder sb=new StringBuilder();
			SNP s=snpcol.getSNPforIndex(i);
			
			sb.append(s.genomicPosition().chromosome().toString()+"\t");
			sb.append(s.genomicPosition().position()); sb.append("\t");
			sb.append(s.referenceCharacter()); sb.append("\t");
			sb.append(s.ancestralAllele()); sb.append('/'); sb.append(s.derivedAllele());

			for(PopulationAlleleCount p : pacs)
			{
				sb.append("\t");
				sb.append(formatSinglePop(p,i));
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
		
		try{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.logger.info("Finished writing summary file");
	}
	

	/*
	private String getComment(FitnessFunction ff, GenomicPosition pos)
	{
		AdditiveSNPFitness af=ff.getAdditiveSNPFitness();
		AdditiveSNP as=af.getAdditiveforPosition(pos);
		ArrayList<EpistaticSNP> episnps=ff.getEpistaticSNPFitness().getEpistaticSNP(pos);
		
		if(as==null && episnps.size()==0) return ".";
		
		ArrayList<String> effects=new ArrayList<String>();
		if(as!=null)
		{
			StringBuilder sb=new StringBuilder();
			sb.append("A=");
			sb.append(as.w11Char()); 	sb.append(':');
			sb.append(as.s()); 			sb.append(':');
			sb.append(as.h());
			effects.add(sb.toString());
		}
		
		for(EpistaticSNP e: episnps)
		{
			effects.add(formatEpistaticEffect(e,pos));
		}
		
		String toret=effects.get(0);
		if(effects.size()>1)
		{
			for(int i=1; i<effects.size(); i++)
			{
				toret=toret+";"+effects.get(i);
			}
		}
		return toret;
		
	}

	*/

	/*
	private String formatEpistaticEffect(EpistaticSNP e, GenomicPosition pos)
	{
		EpistaticSubeffectSNP esub=e.getEpistaticSubeffectSNP(pos);
		if(esub==null) throw new IllegalArgumentException("Invalid state during writing of epistatic effects; no epistatic effect found for valid position");
		StringBuilder sb=new StringBuilder();
		sb.append("E=");
		sb.append(esub.epistaticChar()); sb.append(':');
		sb.append(e.name()); sb.append(':');
		sb.append(e.s());
		
		return sb.toString();
	}
	*/
	
	private String formatSinglePop(PopulationAlleleCount p, int index){
		StringBuilder sb=new StringBuilder();
		sb.append(p.ancestralCount(index));
		sb.append(":");
		sb.append(p.derivedCount(index));
		return sb.toString();
	}
	

}
