package mimcore.io.misc;

import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.statistic.GPF;
import mimcore.data.statistic.GPFCollection;
import mimcore.data.statistic.PopulationAlleleCount;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GPFWriter{
	public final String outputFile;
	public BufferedWriter bf;
	public Logger logger;
	public GPFWriter(String outputFile, Logger logger)
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
	
	public void write(ArrayList<GPFCollection> gpfs)
	{
		
		assert(gpfs.size()>0);
		this.logger.info("Writing GPF entries into  file "+this.outputFile);


		for(GPFCollection gc: gpfs)
		{
			int generation=gc.getGeneration();
			int replicate=gc.getReplicate();
			for(GPF g:gc.getPGFs())
			{
				StringBuilder sb=new StringBuilder();
				sb.append(replicate); sb.append("\t");
				sb.append(generation); sb.append("\t");
				sb.append(g.getGenotype()); sb.append("\t");
				sb.append(g.getPhenotype()); sb.append("\t");
				sb.append(g.getFitness());
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
		}

		
		try{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.logger.info("Finished writing GPF file");
	}
	

	

}
