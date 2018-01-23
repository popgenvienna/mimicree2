package mim2.inbredBasePop.simulate;

import mimcore.data.*;
import mimcore.data.recombination.*;
import mimcore.data.sex.Sex;
import mimcore.io.*;
import mimcore.io.haplotypes.DiploidGenomeWriter;
import mimcore.io.recombination.RecombinationRateReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InbredBasePopFramework {

	private final String haplotypeFile;
	private final String recombinationFile;
	private final int isofemaleLines;
	private final int sizeBasePop;
	private final int sizeisofemaleLine;
	private final int geninbreeding;
	private final String outputFile;


	private final java.util.logging.Logger logger;


	public InbredBasePopFramework(String haplotypeFile, String recombinationFile, String outputFile,
								  int isofemaleLines, int sizeBasePop, int sizeisofemaleLine,int geninbreeding, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);

		try
		{
			// Check if the output file can be created
			new File(outputFile).createNewFile();

		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		this.outputFile=outputFile;

		this.haplotypeFile=haplotypeFile;
		this.recombinationFile=recombinationFile;
	    if(isofemaleLines==0) throw new IllegalArgumentException("isofemale lines must be more than zero");
		if(sizeBasePop<2) throw new IllegalArgumentException("size of base population must be larger or equal to 2");
		if(geninbreeding==0) throw new IllegalArgumentException("must be larger than zero");
		if(sizeisofemaleLine<2) throw new IllegalArgumentException("isofemale line must be larger or equal to 2");

		this.isofemaleLines=isofemaleLines;
		this.sizeBasePop=sizeBasePop;
		this.sizeisofemaleLine=sizeisofemaleLine;
		this.geninbreeding=geninbreeding;
		this.logger=logger;
	}
	
	/**
	public void run()
	{
		this.logger.info("Starting inbredbasepop");


		// Load the data
		RecombinationGenerator recGenerator = new RecombinationGenerator(new RecombinationRateReader(this.recombinationFile,this.logger).getRecombinationRate(),
				new ChromosomeDefinitionReader("").getRandomAssortmentGenerator());

		ArrayList<DiploidGenome> dipGenomes=new mimcore.io.DiploidGenomeReader(this.haplotypeFile,this.logger).readGenomes();
		if(dipGenomes.size()!=2) throw new IllegalArgumentException("Only two diploid genomes are allowed");


	 	MatePair matepair=new MatePair(new Specimen(Sex.Female,1.0,1.0,1.0,dipGenomes.get(0)),new Specimen(Sex.Male,1.0,1.0,1.0,dipGenomes.get(1)));

		ArrayList<DiploidGenome> basepopulation=new SimulationInbreeding(matepair, isofemaleLines, sizeBasePop, sizeisofemaleLine, geninbreeding, recGenerator, this.logger).run();
		this.logger.info("Writing final base population of size "+basepopulation.size());
		DiploidGenomeWriter dgw = new DiploidGenomeWriter(this.outputFile,this.logger);
		dgw.write(basepopulation);
		this.logger.info("Finished simulations");
	}
	 */



}
