package mim2.qs;


import mim2.shared.GlobalResourceManager;
import mim2.shared.ResultRecorder;
import mimcore.data.DiploidGenome;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Population;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.SexedDiploids;
import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.sex.IMatingFunction;
import mimcore.data.sex.MatingFunctionFecundity;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.sex.SexInfo;
import java.util.Random;
import java.util.logging.Logger;

public class MultiSimulationQS {
	private final SexedDiploids basePopulation;
	private final IGenotypeCalculator gc;
	private final PhenotypeCalculator pc;
	private final FitnessFunctionContainer ffc;
	private final ISurvivalFunction sf; //not used; but may be used in the future
	private final IMigrationRegime migrationRegime;
	private final PopulationSizeContainer popcont;
	private final RecombinationGenerator recGenerator;
	private final IMutator mutator;
	private final SexInfo si;

	private final int maxGeneration;
	private final int replicateRuns;
	private Logger logger;


	public MultiSimulationQS(IGenotypeCalculator gc, PhenotypeCalculator pc, FitnessFunctionContainer ffc, ISurvivalFunction sf)
	{

		this.basePopulation= GlobalResourceManager.getBasePopulation();
		this.pc=pc;
		this.gc=gc;
		this.sf=sf;
		this.ffc=ffc;
		this.popcont=GlobalResourceManager.getPopulationSizeContainer();
		this.si=GlobalResourceManager.getSexInfo();


		this.maxGeneration=GlobalResourceManager.getSimulationMode().getMaximumGenerations();
		this.migrationRegime=GlobalResourceManager.getMigrationRegime();
		this.logger=GlobalResourceManager.getLogger();
		this.recGenerator=GlobalResourceManager.getRecombinationGenerator();
		this.mutator=GlobalResourceManager.getMutator();
		this.replicateRuns=GlobalResourceManager.getReplicateRuns();
	}

	
	public void run()
	{
		IFitnessCalculator fc=ffc.getFitnessCalculator(1,1);
		IMatingFunction mf=new MatingFunctionFecundity(si.getSelfingRate());
		ResultRecorder rr=GlobalResourceManager.getResultRecorder();


		for(int k =0; k < this.replicateRuns; k++)
		{
			// Base population generated always anew, because new env. variance for each individual
			// for different replicates you dont use the same individuals (phenotypes) but only the same genotypes (with different phenotypes)
			Population basePopulation=Population.loadPopulation(this.basePopulation,gc,pc,fc,new Random(),true);

			int simulationNumber=k+1;
			this.logger.info("Starting simulation replicate number " + simulationNumber);
			this.logger.info("MimicrEE2 will proceed with forward simulations until generation " + this.maxGeneration);
			this.logger.info("Average genotype of starting population "+basePopulation.getAverageGenotype()+"; average phenotype of starting population "+basePopulation.getAveragePhenotype()+"; average fitness of starting population "+basePopulation.getAverageFitness());

			// record stuff
			rr.record(0,simulationNumber,basePopulation);

			Population nextPopulation =basePopulation;
			// For the number of requested simulations get the next generation, and write it to file if requested
			for(int i=1; i<=this.maxGeneration; i++)
			{
				// Load the new fitness function genertor; the new population will already be treated with this fitness function
				fc=ffc.getFitnessCalculator(simulationNumber,i);
				int popsize=popcont.getPopulationSize(i,simulationNumber);
				this.logger.info("Processing generation "+i+ " of replicate run "+simulationNumber+ " with N="+popsize);

				// Survival would go here if considered....(no survival needed for stabilizing selection);

				nextPopulation=nextPopulation.getNextGeneration(si,gc,pc,fc,mf,this.recGenerator,mutator,popsize);
				this.logger.info("Average genotype of offspring "+nextPopulation.getAverageGenotype()+"; average phenotype of offspring "+nextPopulation.getAveragePhenotype()+"; average fitness of offspring "+nextPopulation.getAverageFitness());

				// Use migration, if wanted ; replace with an ArrayList<DiploidGenomes>
				SexedDiploids migrants=this.migrationRegime.getMigrants(i,simulationNumber);
				if(migrants.size()>0) {
					this.logger.info("Updating sex chromosomes of migrants");
					migrants=migrants.updateSexChromosome(si);
					this.logger.info("Adding "+migrants.size()+ " migrants to the evolved population (randomly removing an equivalent number of evolved individuals)");
					Population migrantPop=Population.loadPopulation(migrants,gc,pc,fc, new Random(),false);
					this.logger.info("Average genotype of migrants "+migrantPop.getAverageGenotype()+"; average phenotype of migrants "+migrantPop.getAveragePhenotype()+"; average fitness of migrants "+migrantPop.getAverageFitness());
					nextPopulation = Population.loadMigration(migrantPop, nextPopulation, new Random(),this.logger);

					this.logger.info("Average genotype of new population "+nextPopulation.getAverageGenotype()+"; average phenotype of new population "+nextPopulation.getAveragePhenotype()+"; average fitness of new population "+nextPopulation.getAverageFitness());
				}
				// record stuff only in the requested generations
				rr.record(i,simulationNumber,nextPopulation);
			}
		}

		rr.finishWriting();

	}


}
