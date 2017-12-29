package mim2.qt;

import mim2.shared.GPFHelper;
import mim2.shared.SimulationMode;
import mimcore.data.*;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Mutator.MutatorGenomeWideRate;
import mimcore.data.gpf.fitness.FitnessCalculatorAllEqual;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.gpf.quantitative.GenotypeCalculatorAllEqual;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.gpf.quantitative.GenotypeCalculator;
import mimcore.data.gpf.survival.*;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.*;
import mimcore.io.*;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.io.recombination.RecombinationRateReader;
import mimcore.io.selectionregime.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QtSimulationFramework {
	private final String haplotypeFile;
	private final String recombinationFile;
	private final String chromosomeDefinition;
	private final String effectSizeFile;
	private final String selectionRegimeFile;
	private final String migrationRegimeFile;
	private final String populationSizeFile;
	private final String outputSync;
	private final String outputGPF;
	private final String outputDir;
	private final Double ve;
	private final Double heritability;
	private final SimulationMode simMode;
	private final int replicateRuns;
	private final double mutationRate;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QtSimulationFramework(String haplotypeFile, String populationSizeFile, String recombinationFile, String chromosomeDefinition, String effectSizeFile, Double ve, Double heritability,
                                 String selectionRegimeFile, String migrationRegimeFile, double mutationRate, String outputSync, String outputGPF, String outputDir, SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations");
		// selection regime
		if(selectionRegimeFile == null)logger.info("No selection regime file found; Commencing neutral simulations");
		else if (! new File(selectionRegimeFile).exists()) throw new IllegalArgumentException("Selection regime file does not exist; "+selectionRegimeFile);

		// migration regime
		if(migrationRegimeFile == null)logger.info("No migration regime file found; Proceeding without migration");
		else if (! new File(migrationRegimeFile).exists()) throw new IllegalArgumentException("Migration regime file does not exist; "+ migrationRegimeFile);

		if(outputGPF == null) logger.info("No output genotype/phenotype/fitness file provided; will not record GPF");
		else try {new File(outputGPF).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create GPF output file "+outputGPF);}

		if((outputDir == null) && (outputSync==null)) throw new IllegalArgumentException("No output was provided; Provide either an output directory or an output sync file or both");

		if(outputDir== null) logger.info("No output director found; Will not record haplotypes");
		else if(! new File(outputDir).exists()) throw new IllegalArgumentException("The provided output directory does not exist "+outputDir);

		if(outputSync == null) logger.info("No output sync file was provided; Will not record allele frequencies");
		else try {new File(outputSync).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create output sync file "+outputSync);}

		if((populationSizeFile != null) && (!new File(populationSizeFile).exists())) throw new IllegalArgumentException("Population size file does not exist; "+populationSizeFile);

		if(mutationRate<0.0 || mutationRate>1.0)throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0");


		if((heritability==null) && (ve == null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided");
		if((heritability!=null) && (ve != null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided, NOT BOTH");
		if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);

		this.outputGPF=outputGPF;
		this.outputSync=outputSync;
		this.outputDir=outputDir;
		this.populationSizeFile=populationSizeFile;
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
		this.mutationRate=mutationRate;
	}
	
	
	public void run()
	{
		this.logger.info("Starting qt simulations: a quantitative trait with truncating selection");


		// Load the data
		RecombinationGenerator recGenerator = new RecombinationGenerator(new RecombinationRateReader(this.recombinationFile,this.logger).getRecombinationRate(),
				new ChromosomeDefinitionReader(this.chromosomeDefinition).getRandomAssortmentGenerator());

		ArrayList<DiploidGenome> dipGenomes=new mimcore.io.DiploidGenomeReader(this.haplotypeFile,this.logger).readGenomes();
		if(!recGenerator.isValid(dipGenomes)) throw new IllegalArgumentException("Recombination rate file is not valid, not all chromosomes are contained");

		// Compute GPF
		IGenotypeCalculator genotypeCalculator= new GenotypeCalculatorAllEqual();
		if(this.effectSizeFile!=null) genotypeCalculator=new SNPQuantitativeEffectSizeReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator= GPFHelper.getPhenotypeCalculator(dipGenomes,genotypeCalculator,this.ve,this.heritability,this.logger);
		IFitnessCalculator fitnessCalculator=new FitnessCalculatorAllEqual();

		// Load population size computer
		PopulationSizeContainer popcont=new PopulationSizeContainer(dipGenomes.size());
		if(this.populationSizeFile!=null) popcont=new PopulationSizeReader(this.populationSizeFile,this.logger).readPopulationSizes();

		// Survival function (truncating selection); If none specified all survive
		ISurvivalFunction survivalFunction= new SurvivalRegimeAllSurvive();
		if(selectionRegimeFile != null)
		{
			ISelectionRegime selectionRegime=new SelectionRegimeReader(this.selectionRegimeFile,this.logger).readSelectionRegime();
			survivalFunction=new SurvivalRegimeTruncatingSelection(selectionRegime);
		}

		// Migration regime; If none specified no migration
		IMigrationRegime migrationRegime=new MigrationRegimeNoMigration();
		if(migrationRegimeFile != null) migrationRegime=new MigrationRegimeReader(this.migrationRegimeFile,this.logger,dipGenomes).readMigrationRegime();

		IMutator mutator=new MutatorGenomeWideRate(this.mutationRate);

		MultiSimulationQT mst=new MultiSimulationQT(dipGenomes,popcont,genotypeCalculator,phenotypeCalculator,fitnessCalculator,survivalFunction, migrationRegime, mutator, this.outputSync, this.outputGPF,this.outputDir,
			recGenerator,simMode.getTimestamps(),this.replicateRuns,this.logger);
		mst.run();

		this.logger.info("Finished simulations");
	}


}
