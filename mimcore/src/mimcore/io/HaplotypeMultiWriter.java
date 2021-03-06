package mimcore.io;

import mimcore.data.Population;
import mimcore.data.Specimen;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexInfo;
import mimcore.io.haplotypes.HaplotypeWriter;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


public class HaplotypeMultiWriter {
	private final Population population;
	private final String outputDir;
	private final int generation;
	private final int simulationNumber;
	private final Logger logger;
	private final boolean haploid;
	private final SexInfo sexInfo;

	public HaplotypeMultiWriter(Population population, String outputDir, int  generation, int simulationNumber, boolean haploid, SexInfo sexInfo, Logger logger)
	{
		this.population=population;
		this.outputDir=outputDir;
		this.generation=generation;
		this.simulationNumber=simulationNumber;
		this.logger=logger;
		this.sexInfo=sexInfo;
		this.haploid=haploid;
	}
	
	
	public void write()
	{
		ArrayList<HaploidGenome> haplotypes=new ArrayList<HaploidGenome>();
		ArrayList<Specimen> specimens=population.getSpecimen();
		ArrayList<Sex> sexes=new ArrayList<Sex>();
		for(Specimen spec: specimens){
			haplotypes.add(spec.getGenome().getHaplotypeA());
			haplotypes.add(spec.getGenome().getHaplotypeB());
			sexes.add(spec.getSex());
		}
		
		String haplotypeOFile="";
		
		try
		{
			 haplotypeOFile = new File(this.outputDir,"haplotypes.r" + this.simulationNumber + ".g"+this.generation+".mimhap.gz").getCanonicalPath();
			 
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		/* In case I ever need gpf output
		try{
			fitnessOFile = new File(this.outputDir,"gpf.r" + this.simulationNumber + ".g"+this.generation).getCanonicalPath();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		*/
		new HaplotypeWriter(haplotypeOFile,haploid,sexInfo,this.logger).write(haplotypes,sexes);

	}
	

}
