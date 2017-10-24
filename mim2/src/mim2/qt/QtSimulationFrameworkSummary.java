package mim2.qt;

import mimcore.data.*;
import mimcore.data.fitness.*;
import mimcore.data.fitness.FitnessCalculatorDefault;
import mimcore.data.fitness.quantitative.PhenotypeCalculator;
import mimcore.data.fitness.quantitative.GenotypeCalculator;
import mimcore.data.fitness.survival.*;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.*;
import mimcore.io.*;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.io.migrationRegime.MigrationRegimeReader_Deprecated;
import mimcore.io.selectionregime.*;

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
	private final String outputGPF;
	private final String outputDir;
	private final Double ve;
	private final Double heritability;
	private final SimulationMode simMode;
	private final int replicateRuns;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QtSimulationFrameworkSummary(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String effectSizeFile, Double ve, Double heritability,
										String selectionRegimeFile, String migrationRegimeFile, String outputSync,String outputGPF, String outputDir,  SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations\n");
		// selection regime
		if(selectionRegimeFile == null)logger.info("No selection regime file found; Commencing neutral simulations\n");
		else if (! new File(selectionRegimeFile).exists()) throw new IllegalArgumentException("Selection regime file does not exist; "+selectionRegimeFile);

		// migration regime
		if(migrationRegimeFile == null)logger.info("No migration regime file found; Proceeding without migration\n");
		else if (! new File(migrationRegimeFile).exists()) throw new IllegalArgumentException("Migration regime file does not exist; "+ migrationRegimeFile);

		if(outputGPF == null) logger.info("No output genotype/phenotype/fitness file provided; will not record GPF\n");
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

		// Compute GPF
		GenotypeCalculator genotypeCalculator=new GenotypeCalculatorReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator=getPhenotypeCalculator(dipGenomes,genotypeCalculator);
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

	public PhenotypeCalculator getPhenotypeCalculator(ArrayList<DiploidGenome> dipGenomes, GenotypeCalculator genotypeCalculator)
	{
		// if environmental variance exists than use it directly
		if(this.ve!=null) {
			this.logger.info("Enviromental variance was provided; Will proceed with VE="+this.ve);
			return new PhenotypeCalculator(this.ve);
		}

		this.logger.info("No environmental variance was provided; Will compute VE from the heritability and the genotypic variance in the base population");

		// if not compute the environmental variance from the heritability
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


		double vecompute=PhenotypeCalculator.computeVEfromVGandH2(variance,this.heritability);

		this.logger.info("Estimated genotypic variance VG="+variance+" ; Using a heritability h2="+heritability+" ; Will proceed with VE="+vecompute);
		return new PhenotypeCalculator(vecompute);
	}

}
