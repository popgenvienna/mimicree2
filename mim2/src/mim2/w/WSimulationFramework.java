package mim2.w;

import mim2.shared.GlobalResourceManager;
import mim2.shared.SimulationMode;
import mimcore.data.DiploidGenome;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Mutator.MutatorGenomeWideRate;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.gpf.fitness.*;
import mimcore.data.gpf.quantitative.*;
import mimcore.data.gpf.survival.SurvivalRegimeAllSurvive;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.CrossoverGenerator;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.sex.SexInfo;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.PopulationSizeReader;
import mimcore.io.SexReader;
import mimcore.io.recombination.RecombinationRateReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.io.w.EpistasisFitnessReader;
import mimcore.io.w.SNPFitnessReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WSimulationFramework {

	private final String fitnessFile;
	private final String epistasisFile;

	private final java.util.logging.Logger logger;
	//(haplotypeFile,recombinationFile,chromosomeDefinition,fitnessFile,epistasisFile,migrationRegimeFile,outputSync,outputGPF,outputDir,simMode,replicateRuns,logger);

	public WSimulationFramework(String fitnessFile, String epistasisFile)
	{

		logger= GlobalResourceManager.getLogger();


		if((fitnessFile != null) && (!new File(fitnessFile).exists())) throw new IllegalArgumentException("Fitness file does not exist; "+fitnessFile);
		if((epistasisFile != null) && (!new File(epistasisFile).exists())) throw new IllegalArgumentException("Epistasis file does not exist; "+epistasisFile);

		if(fitnessFile==null) logger.info("No fitness file found; commencing simulations without selected SNPs");
		if(epistasisFile==null) logger.info("No epistasis file found; commencing simulations without pairs of interacting SNPs");

		this.fitnessFile=fitnessFile;
		this.epistasisFile=epistasisFile;
	}


	public void run()
	{




		// genotype and phenotype are set to 1.0 (this is ignored)
		IGenotypeCalculator genotypeCalculator=new GenotypeCalculatorAllEqual();
		IPhenotypeCalculator phenotypeCalculator=new PhenotypeCalculatorAllEqual();

		// Load fitness computers (snps and
		IFitnessCalculator snpFitness=new FitnessCalculatorAllEqual();
		if(this.fitnessFile!=null) snpFitness=new SNPFitnessReader(this.fitnessFile,logger).getSNPFitness();
		IFitnessCalculator epistasisFitness =new FitnessCalculatorAllEqual();
		if(this.epistasisFile!=null)  epistasisFitness=new EpistasisFitnessReader(this.epistasisFile,this.logger).readEpistaticPairs();
		IFitnessCalculator snpAndEpistasisFitness=new FitnessCalculator_SNPandEpistasis(snpFitness,epistasisFitness);

		// Survival function; no selective deaths; all surviveISurvivalFunction survivalFunction= new SurvivalRegimeAllSurvive();
		ISurvivalFunction survivalFunction=new SurvivalRegimeAllSurvive();


		MultiSimulationW ws=new MultiSimulationW(genotypeCalculator,phenotypeCalculator,snpAndEpistasisFitness,survivalFunction);
		ws.run();

		this.logger.info("Finished simulations");
	}



}
