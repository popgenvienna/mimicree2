package mim2.qt;

import mimcore.data.*;
import mimcore.data.fitness.*;
import mimcore.data.fitness.FitnessCalculatorDefault;
import mimcore.data.fitness.quantitative.PhenotypeCalculator;
import mimcore.data.fitness.quantitative.GenotypeCalculator;
import mimcore.data.fitness.survival.*;
import mimcore.data.recombination.*;
import mimcore.data.statistic.PopulationAlleleCount;
import mimcore.io.*;
import mimcore.io.selectionregime.*;
import mimcore.io.misc.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QtSimulationFrameworkSummary {
	private final String haplotypeFile;
	private final String recombinationFile;
	private final String chromosomeDefinition;
	private final String effectSizeFile;
	private final String selectionRegimeFile;
	private final String migrationRegimeFile;
	private final String outputSync;
	private final String outputDir;
	private final Double ve;
	private final Double heritability;
	private final SimulationMode simMode;
	private final int replicateRuns;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QtSimulationFrameworkSummary(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String effectSizeFile, Double ve, Double heritability,
										String selectionRegimeFile, String migrationRegimeFile, String outputSync, String outputDir,  SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations\n");
		if(! new File(selectionRegimeFile).exists()) logger.info("No selection regime file found; Commencing neutral simulations\n");
		if(! new File(migrationRegimeFile).exists()) logger.info("No migration regime file found; Proceeding without migration\n");

		// Check output; either directory or sync file or both
		boolean validOutput=false;
		if(! new File(outputDir).exists()) logger.info("No output director found; Will not write haplotypes to file\n");
		else  validOutput=true;
		try
		{
			// Check if the output file can be created
			new File(outputSync).createNewFile();
			validOutput=true;
		}
		catch(IOException e)
		{
			int bla=0;
		}
		if(!validOutput) throw new IllegalArgumentException("No output was provided; Provide either an output directory or an output sync file or both");


		if(heritability.equals(null) && ve.equals(null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided");
		if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);

		this.outputSync=outputSync;
		this.outputDir=outputDir;
		this.effectSizeFile=effectSizeFile;
		this.haplotypeFile=haplotypeFile;
		this.recombinationFile=recombinationFile;
		this.chromosomeDefinition=chromosomeDefinition;
		this.selectionRegimeFile=selectionRegimeFile;
		this.migrationRegimeFile=migrationRegimeFile;
		this.ve=ve;
		this.heritability=heritability;
		this.simMode=simMode;
		this.replicateRuns=replicateRuns;
		this.logger=logger;
	}
	
	
	public void run()
	{
		this.logger.info("Starting qt");


		// Load the data
		RecombinationGenerator recGenerator = new RecombinationGenerator(new RecombinationRateReader(this.recombinationFile,this.logger).getRecombinationRate(),
				new ChromosomeDefinitionReader(this.chromosomeDefinition).getRandomAssortmentGenerator());

		ArrayList<DiploidGenome> dipGenomes=new mimcore.io.DiploidGenomeReader(this.haplotypeFile,"",this.logger).readGenomes();

		GenotypeCalculator genotypeCalculator=new GenotypeCalculatorReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator=getPhenotypeCalculator(dipGenomes,genotypeCalculator);
		IFitnessCalculator fitnessCalculator=new FitnessCalculatorDefault();

		ISelectionRegime selectionRegime=new SelectionRegimeReader(this.selectionRegimeFile,this.logger).readSelectionRegime();
		ISurvivalFunction survivalFunction=new SurvivalRegimeTruncatingSelection(selectionRegime);

		ArrayList<PopulationAlleleCount> pacs=new MultiSimulationTimestamp(dipGenomes,genotypeCalculator,phenotypeCalculator,fitnessCalculator,survivalFunction,
			recGenerator,simMode.getTimestamps(),this.replicateRuns,this.logger).run();

		ISummaryWriter sw = new SyncWriter(this.outputSync,this.logger);


		sw.write(pacs);
		this.logger.info("Finished simulations");
	}

	public PhenotypeCalculator getPhenotypeCalculator(ArrayList<DiploidGenome> dipGenomes, GenotypeCalculator genotypeCalculator)
	{
		if(!this.ve.equals(null)) return new PhenotypeCalculator(this.ve);

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
		// now we have the mean and the variance of the genotypes;
		// h2 = vg/vg+ve
		//ve= (1-h2)vg/h2


		return new PhenotypeCalculator(ve,variance);
	}

}
