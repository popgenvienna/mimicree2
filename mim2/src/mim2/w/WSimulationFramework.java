package mim2.w;

import mim2.qs.MultiSimulationQS;
import mim2.shared.GPFHelper;
import mim2.shared.SimulationMode;
import mimcore.data.DiploidGenome;
import mimcore.data.gpf.fitness.*;
import mimcore.data.gpf.quantitative.*;
import mimcore.data.gpf.survival.SurvivalRegimeAllSurvive;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.RecombinationRateReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.io.w.SNPFitnessReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WSimulationFramework {
	private final String haplotypeFile;
	private final String recombinationFile;
	private final String chromosomeDefinition;
	private final String fitnessFile;
	private final String epistasisFile;
	private final String migrationRegimeFile;
	private final String outputSync;
	private final String outputGPF;
	private final String outputDir;
	private final SimulationMode simMode;
	private final int replicateRuns;



	private final java.util.logging.Logger logger;
	//(haplotypeFile,recombinationFile,chromosomeDefinition,fitnessFile,epistasisFile,migrationRegimeFile,outputSync,outputGPF,outputDir,simMode,replicateRuns,logger);

	public WSimulationFramework(String haplotypeFile, String recombinationFile, String chromosomeDefinition,
                                String fitnessFile, String epistasisFile, String migrationRegimeFile, String outputSync, String outputGPF, String outputDir, SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);



		if((fitnessFile != null) && (!new File(fitnessFile).exists())) throw new IllegalArgumentException("Fitness file does not exist; "+fitnessFile);
		if((epistasisFile != null) && (!new File(epistasisFile).exists())) throw new IllegalArgumentException("Epistasis file does not exist; "+epistasisFile);
		if(fitnessFile==null) logger.info("No fitness file found; commencing simulations without selected SNPs");
		if(epistasisFile==null) logger.info("No epistasis file found; commencing simulations without pairs of interacting SNPs");

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




		if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);

		this.outputGPF=outputGPF;
		this.outputSync=outputSync;
		this.outputDir=outputDir;
		this.fitnessFile=fitnessFile;
		this.haplotypeFile=haplotypeFile;
		this.recombinationFile=recombinationFile;
		this.chromosomeDefinition=chromosomeDefinition;
		this.epistasisFile=epistasisFile;
		this.migrationRegimeFile=migrationRegimeFile;
		this.simMode=simMode;
		this.replicateRuns=replicateRuns;
		this.logger=logger;
	}


	public void run()
	{
		this.logger.info("Starting simulations with directly provided fitness effects of SNPs (w)");


		// Load the data
		RecombinationGenerator recGenerator = new RecombinationGenerator(new RecombinationRateReader(this.recombinationFile,this.logger).getRecombinationRate(),
				new ChromosomeDefinitionReader(this.chromosomeDefinition).getRandomAssortmentGenerator());

		ArrayList<DiploidGenome> dipGenomes=new DiploidGenomeReader(this.haplotypeFile,"",this.logger).readGenomes();

		// genotype and phenotype are set to 1.0 (this is ignored)
		IGenotypeCalculator genotypeCalculator=new GenotypeCalculatorAllEqual();
		IPhenotypeCalculator phenotypeCalculator=new PhenotypeCalculatorAllEqual();

		// Load fitness computers (snps and
		IFitnessCalculator snpFitness=new FitnessCalculatorAllEqual();
		if(this.fitnessFile!=null) snpFitness=new SNPFitnessReader(this.fitnessFile,logger).readSNPFitness();

		IFitnessCalculator epistasisFitness =new FitnessCalculatorAllEqual();
		if(this.epistasisFile!=null)  epistasisFitness=new FitnessCalculator_Epistasis(null, null);

		IFitnessCalculator snpAndEpistasisFitness=new FitnessCalculator_SNPandEpistasis(snpFitness,epistasisFitness);




		// Survival function; no selective deaths; all surviveISurvivalFunction survivalFunction= new SurvivalRegimeAllSurvive();
		ISurvivalFunction survivalFunction=new SurvivalRegimeAllSurvive();


		// Migration regime; If none specified no migration
		IMigrationRegime migrationRegime=new MigrationRegimeNoMigration();
		if(migrationRegimeFile != null) migrationRegime=new MigrationRegimeReader(this.migrationRegimeFile,this.logger,dipGenomes).readMigrationRegime();

		MultiSimulationW ws=new MultiSimulationW(dipGenomes,genotypeCalculator,phenotypeCalculator,snpAndEpistasisFitness,survivalFunction, migrationRegime, this.outputSync, this.outputGPF,this.outputDir,
			recGenerator,simMode.getTimestamps(),this.replicateRuns,this.logger);
		ws.run();

		this.logger.info("Finished simulations");
	}



}
