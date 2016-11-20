package qmimcore.io.misc;

import qmimcore.data.haplotypes.SNP;
import qmimcore.data.haplotypes.SNPCollection;
import qmimcore.data.statistic.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

public class SyncWriter implements ISummaryWriter{
	public final String outputFile;
	public BufferedWriter bf;
	public Logger logger;
	public SyncWriter(String outputFile,Logger logger)
	{
		this.outputFile=outputFile;
		this.logger=logger;
		try
		{
			bf=new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outputFile))));
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
		this.logger.info("Writing haplotypes into synchronized file "+this.outputFile);
		SNPCollection snpcol=pacs.get(0).getSNPCollection();
		
		for(int i=0; i<snpcol.size(); i++)
		{
			StringBuilder sb=new StringBuilder();
			SNP s=snpcol.getSNPforIndex(i);
			
			sb.append(s.genomicPosition().chromosome().toString());
			sb.append("\t");
			sb.append(s.genomicPosition().position());
			sb.append("\t");
			sb.append(s.referenceCharacter());
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
		
		this.logger.info("Finished writing synchronized file");
	}
	
	private String formatSinglePop(PopulationAlleleCount p, int index){
		StringBuilder sb=new StringBuilder();
		sb.append(p.getCount('A', index));
		sb.append(":");
		sb.append(p.getCount('T',index));
		sb.append(":");
		sb.append(p.getCount('C',index));
		sb.append(":");
		sb.append(p.getCount('G', index));
		sb.append(":0:0");
		return sb.toString();
	}
	

}
