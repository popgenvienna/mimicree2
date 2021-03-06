package mimcore.io;

import com.sun.javaws.exceptions.InvalidArgumentException;
import mimcore.data.*;
import mimcore.data.haplotypes.*;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.SexAssignerDirect;
import mimcore.io.haplotypes.HaplotypeReader;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class DiploidGenomeReader implements IDiploidGenomeReader {
	private final String haplotypeFile;
	private Logger logger;
	private final ISexAssigner defaultSexAssigner;
	private final boolean haploid;

	public DiploidGenomeReader(String haplotypeFile, ISexAssigner defaultSexAssigner, boolean haploid, Logger logger)
	{
		this.haplotypeFile=haplotypeFile;
		this.logger=logger;
		this.defaultSexAssigner=defaultSexAssigner;
		this.haploid=haploid;
	}
	
	/**
	 * Read the diploid genomes as specified in the haplotypeFile and inversionFile
	 * @return
	 */
	public SexedDiploids readGenomes()
	{

		HaplotypeReader hr=new HaplotypeReader(this.haplotypeFile,haploid,this.logger);
		ArrayList<HaploidGenome> hapGenomes=hr.getHaplotypes();
		SexAssignerDirect sa=hr.getSexAssigner();
		ArrayList<DiploidGenome> dipGenomes=new ArrayList<DiploidGenome>();
		for(int i=0; i<hapGenomes.size(); i+=2)
		{
			dipGenomes.add(new DiploidGenome(hapGenomes.get(i),hapGenomes.get(i+1)));
		}
		SexedDiploids sexedDips;

		int popsize=dipGenomes.size();
		Random r= ThreadLocalRandom.current();
		if(sa!=null)
		{
			if(sa.size()!=dipGenomes.size()) throw new IllegalArgumentException("Invalid haplotype file; Number of provided sexes does not fit number of diploid genomes "+sa.size()+" vs "+dipGenomes.size());
			sexedDips=new SexedDiploids(dipGenomes,sa.getSexes(popsize,r));
		}
		else sexedDips=new SexedDiploids(dipGenomes,defaultSexAssigner.getSexes(popsize,r));


		this.logger.info("Finished creating diploid genomes");
		return sexedDips;
	}

	
	

}
