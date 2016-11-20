package qmimcore.io;

import qmimcore.data.*;
import qmimcore.data.haplotypes.*;
import qmimcore.io.haplotypes.HaplotypeReader;
import java.io.File;
import java.util.ArrayList;

public class HaploidGenomeReader {
	
	private final String haplotypeFile;
	private final String inversionFile;
	private java.util.logging.Logger logger;
	
	public HaploidGenomeReader(String haplotypeFile, String inversionFile, java.util.logging.Logger logger)
	{
		this.haplotypeFile=haplotypeFile;
		this.inversionFile=inversionFile;
		this.logger=logger;
	}
	
	
	public ArrayList<HaploidGenome> readGenomes()
	{
		ArrayList<HaploidGenome> haps= new HaplotypeReader(this.haplotypeFile,this.logger).getHaplotypes();
		return haps;
		
	}

}
