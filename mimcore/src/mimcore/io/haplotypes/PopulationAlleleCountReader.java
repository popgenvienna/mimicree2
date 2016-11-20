package qmimcore.io.haplotypes;

import qmimcore.data.haplotypes.*;
import qmimcore.data.statistic.*;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class PopulationAlleleCountReader {
	
	private final ArrayList<String> haplotypeFiles;
	private Logger logger;
	
	public PopulationAlleleCountReader(ArrayList<String> inputFiles, Logger logger)
	{
		this.haplotypeFiles=inputFiles;
		this.logger=logger;
	}
	
	
	public ArrayList<PopulationAlleleCount> readPopulations()
	{
		assert(this.haplotypeFiles.size()>0);
		String firstFile=this.haplotypeFiles.get(0);
		this.logger.info("Reading SNP information from file "+firstFile);
		SNPCollection snpCol= new HaplotypeSNPReader(getBufferedReader(firstFile)).getSNPcollection();
		
		
		ArrayList<PopulationAlleleCount> pac=new ArrayList<PopulationAlleleCount>();
		for(String file: this.haplotypeFiles)
		{
			this.logger.info("Reading allele frequencies for file " +file);
			PopulationAlleleCount p=new SinglePopulationAlleleCountReader(getBufferedReader(file)).getAlleleCount(snpCol);
			pac.add(p);
		}
		
		this.logger.info("Finished reading allele frequencies");
		return pac;
		
		
	}


	private static BufferedReader getBufferedReader(String file)
	{
		BufferedReader br=null;
		if(file.endsWith(".gz"))
		{
			try{
				br=new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		else{

			try{
				br= new BufferedReader(new FileReader(file));
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
				System.exit(1);
			}

		}
		return br;
	}


}
