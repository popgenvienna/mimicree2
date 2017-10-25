package mim2.qs;

import mim2.qt.MultiSimulationTimestamp;
import mim2.shared.GPFHelper;
import mim2.shared.SimulationMode;
import mimcore.data.DiploidGenome;
import mimcore.data.gpf.FitnessCalculatorDefault;
import mimcore.data.gpf.IFitnessCalculator;
import mimcore.data.gpf.quantitative.GenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.gpf.survival.ISelectionRegime;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.gpf.survival.SurvivalRegimeAllSurvive;
import mimcore.data.gpf.survival.SurvivalRegimeTruncatingSelection;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.GenotypeCalculatorReader;
import mimcore.io.RecombinationRateReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.io.selectionregime.SelectionRegimeReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QsSimulationFramework {
	private final String haplotypeFile;
	private final String recombinationFile;
	private final String chromosomeDefinition;
	private final String effectSizeFile;
	private final String gaussianFitnessFunctionFile;
	private final String fitnessFunctionFile;
	private final String migrationRegimeFile;
	private final String outputSync;
	private final String outputGPF;
	private final String outputDir;
	private final Double ve;
	private final Double heritability;
	private final SimulationMode simMode;
	private final int replicateRuns;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QsSimulationFramework(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String effectSizeFile, Double ve, Double heritability,
                                 String gaussianFitnessFunctionFile, String fitnessFunctionFile, String migrationRegimeFile, String outputSync, String outputGPF, String outputDir, SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations\n");
		// gpf function
		if((gaussianFitnessFunctionFile==null) && (fitnessFunctionFile == null)) throw new IllegalArgumentException("Either a gaussian gpf function or a gpf function must be provided");
		if((gaussianFitnessFunctionFile!=null) && (fitnessFunctionFile != null)) throw new IllegalArgumentException("Either a gaussian gpf function or a gpf function must be provided; NOT BOTH");

		if((gaussianFitnessFunctionFile != null) && (!new File(gaussianFitnessFunctionFile).exists())) throw new IllegalArgumentException("Gaussian gpf function file does not exist; "+gaussianFitnessFunctionFile);
		if((fitnessFunctionFile != null) && (!new File(fitnessFunctionFile).exists())) throw new IllegalArgumentException("Fitness function file does not exist; "+fitnessFunctionFile);

		// migration regime
		if(migrationRegimeFile == null)logger.info("No migration regime file found; Proceeding without migration\n");
		else if (! new File(migrationRegimeFile).exists()) throw new IllegalArgumentException("Migration regime file does not exist; "+ migrationRegimeFile);

		if(outputGPF == null) logger.info("No output genotype/phenotype/gpf file provided; will not record GPF\n");
		else try {new File(outputGPF).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create GPF output file "+outputGPF);}

		if((outputDir == null) && (outputSync==null)) throw new IllegalArgumentException("No output was provided; Provide either an output directory or an output sync file or both");

		if(outputDir== null) logger.info("No output director found; Will not record haplotypes\n");
		else if(! new File(outputDir).exists()) throw new IllegalArgumentException("The provided output directory does not exist "+outputDir);

		if(outputSync == null) logger.info("No output sync file was provided; Will not record allele frequencies\n");
		else try {new File(outputSync).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create output sync file "+outputSync);}



		if((heritability==null) && (ve == null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided");
		if((heritability!=null) && (ve != null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided, NOT BOTH");
		if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);

		this.outputGPF=outputGPF;
		this.outputSync=outputSync;
		this.outputDir=outputDir;
		this.effectSizeFile=effectSizeFile;
		this.haplotypeFile=haplotypeFile;
		this.recombinationFile=recombinationFile;
		this.chromosomeDefinition=chromosomeDefinition;
		this.gaussianFitnessFunctionFile=gaussianFitnessFunctionFile;
		this.fitnessFunctionFile=fitnessFunctionFile;
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

		ArrayList<DiploidGenome> dipGenomes=new DiploidGenomeReader(this.haplotypeFile,"",this.logger).readGenomes();

		// Compute GPF
		GenotypeCalculator genotypeCalculator=new GenotypeCalculatorReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator= GPFHelper.getPhenotypeCalculator(dipGenomes,genotypeCalculator,this.ve,this.heritability,this.logger)(dipGenomes,genotypeCalculator);
		IFitnessCalculator fitnessCalculator=new FitnessCalculatorDefault();

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

		MultiSimulationTimestamp mst=new MultiSimulationTimestamp(dipGenomes,genotypeCalculator,phenotypeCalculator,fitnessCalculator,survivalFunction, migrationRegime, this.outputSync, this.outputGPF,this.outputDir,
			recGenerator,simMode.getTimestamps(),this.replicateRuns,this.logger);
		mst.run();

		this.logger.info("Finished simulations");
	}



}
