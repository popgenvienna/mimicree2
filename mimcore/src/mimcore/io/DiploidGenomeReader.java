package mimcore.io;

import mimcore.data.*;
import mimcore.data.haplotypes.*;
import mimcore.io.haplotypes.HaplotypeReader;

import java.util.ArrayList;
import java.util.logging.Logger;

public class DiploidGenomeReader implements IDiploidGenomeReader {
	private final String haplotypeFile;
	private Logger logger;
	public DiploidGenomeReader(String haplotypeFile, Logger logger)
	{
		this.haplotypeFile=haplotypeFile;
		this.logger=logger;
	}
	
	/**
	 * Read the diploid genomes as specified in the haplotypeFile and inversionFile
	 * @return
	 */
	public ArrayList<DiploidGenome> readGenomes()
	{
		ArrayList<HaploidGenome> hapGenomes=new HaplotypeReader(this.haplotypeFile,this.logger).getHaplotypes();
		ArrayList<DiploidGenome> dipGenomes=new ArrayList<DiploidGenome>();
		for(int i=0; i<hapGenomes.size(); i+=2)
		{
			dipGenomes.add(new DiploidGenome(hapGenomes.get(i),hapGenomes.get(i+1)));
		}
		this.logger.info("Finished creating diploid genomes");
		return dipGenomes;
	}
	
	

}
