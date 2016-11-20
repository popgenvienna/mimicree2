package mim2.qt;

import qmimcore.data.*;
import qmimcore.data.fitness.*;
import qmimcore.data.recombination.*;
import qmimcore.data.statistic.PopulationAlleleCount;
import qmimcore.io.*;
import qmimcore.io.misc.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QtSimulationFrameworkSummary {
	private final String haplotypeFile;
	private final String recombinationFile;
	private final String chromosomeDefinition;
	private final String effectSizeFile;
	private final String selectionRegimeFile;
	private final String outputFile;
	private final double heritability;
	private final SimulationMode simMode;
	private final int replicateRuns;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QtSimulationFrameworkSummary(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String effectSizeFile, double heritability,
										String selectionRegimeFile, String outputFile,  SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations\n");
		if(! new File(selectionRegimeFile).exists()) logger.info("No selection regime file found; Commencing neutral simulations\n");

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
		
		if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);

		this.outputFile=outputFile;
		this.effectSizeFile=effectSizeFile;
		this.haplotypeFile=haplotypeFile;
		this.recombinationFile=recombinationFile;
		this.chromosomeDefinition=chromosomeDefinition;
		this.selectionRegimeFile=selectionRegimeFile;
		this.heritability=heritability;
		this.simMode=simMode;
		this.replicateRuns=replicateRuns;
		this.logger=logger;
	}
	
	
	public void run()
	{
		this.logger.info("Starting qtMimicree");


		// Load the data
		RecombinationGenerator recGenerator = new RecombinationGenerator(new RecombinationRateReader(this.recombinationFile,this.logger).getRecombinationRate(),
				new ChromosomeDefinitionReader(this.chromosomeDefinition).getRandomAssortmentGenerator());

		ArrayList<DiploidGenome> dipGenomes=new qmimcore.io.DiploidGenomeReader(this.haplotypeFile,"",this.logger).readGenomes();

		GenotypeCalculator genotypeCalculator=new GenotypeCalculatorReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator=getPhenotypeCalculator(dipGenomes,genotypeCalculator,this.heritability);


		ISelectionRegime selectionRegime=new SelectionRegimeReader(this.selectionRegimeFile,this.logger).readSelectionRegime();

		ArrayList<PopulationAlleleCount> pacs=new MultiSimulationTimestamp(dipGenomes,genotypeCalculator,phenotypeCalculator,selectionRegime,
			recGenerator,simMode.getTimestamps(),this.replicateRuns,this.logger).run();

		ISummaryWriter sw = new SyncWriter(this.outputFile,this.logger);


		sw.write(pacs);
		this.logger.info("Finished simulations");
	}

	public PhenotypeCalculator getPhenotypeCalculator(ArrayList<DiploidGenome> dipGenomes, GenotypeCalculator genotypeCalculator,double heritability)
	{
		ArrayList<Double> genotypes=new ArrayList<Double>();
		for(DiploidGenome dg: dipGenomes)
		{
			genotypes.add(genotypeCalculator.getGenotype(dg));
		}

		double mean=0.0;
		for(double d: genotypes)
		{
			mean+=d;
		}
		mean= mean/((double)genotypes.size());

		double variance=0.0;
		for(double d: genotypes)
		{
			double ss=Math.pow((d - mean), 2.0);
			variance+=ss;
		}
		variance= variance/((double)genotypes.size());
		return new PhenotypeCalculator(heritability,variance);
	}

}
