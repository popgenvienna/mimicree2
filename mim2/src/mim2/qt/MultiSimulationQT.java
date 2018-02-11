package mim2.qt;


import mim2.shared.GlobalResourceManager;
import mim2.shared.ResultRecorder;
import mimcore.data.Mutator.IMutator;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.SexedDiploids;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.sex.IMatingFunction;
import mimcore.data.sex.MatingFunctionRandomMating;
import mimcore.data.gpf.quantitative.*;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.sex.SexInfo;
import mimcore.data.Population;

import java.util.Random;
import java.util.logging.Logger;

public class MultiSimulationQT {
	private final SexedDiploids basePopulation;
	private final IGenotypeCalculator gc;
	private final PhenotypeCalculator pc;
	private final IFitnessCalculator fc;
	private final ISurvivalFunction sf;
	private final PopulationSizeContainer popcont;
	private final IMigrationRegime migrationRegime;
	private final String outputSync;
	private final String outputGPF;
	private final String outputDir;
	private final RecombinationGenerator recGenerator;
	private final IMutator mutator;
	private final SexInfo si;

	private final int maxGeneration;
	private final int replicateRuns;
	private Logger logger;



	public MultiSimulationQT( IGenotypeCalculator gc, PhenotypeCalculator pc, IFitnessCalculator fc, ISurvivalFunction sf)
	{

		this.basePopulation= GlobalResourceManager.getBasePopulation();
		this.pc=pc;
		this.gc=gc;
		this.sf=sf;
		this.fc=fc;
		this.si=GlobalResourceManager.getSexInfo();


		this.maxGeneration=GlobalResourceManager.getSnapshotManager().getMaximumGeneration();
		this.migrationRegime=GlobalResourceManager.getMigrationRegime();
		this.logger=GlobalResourceManager.getLogger();
		this.recGenerator=GlobalResourceManager.getRecombinationGenerator();
		this.replicateRuns=GlobalResourceManager.getReplicateRuns();
		this.popcont=GlobalResourceManager.getPopulationSizeContainer();
		this.mutator=GlobalResourceManager.getMutator();
		this.outputDir=GlobalResourceManager.getOutputDir();
		this.outputGPF=GlobalResourceManager.getOutputGPF();
		this.outputSync=GlobalResourceManager.getOutputSync();
		
	}

	
	public void run()
	{

		IMatingFunction mf= new MatingFunctionRandomMating(si.getSelfingRate());
		ResultRecorder rr=GlobalResourceManager.getResultRecorder();

		for(int k =0; k < this.replicateRuns; k++)
		{
			// Base population generated always anew, because new env. variance for each individual
			// for different replicates you dont use the same individuals (phenotypes) but only the same genotypes (with different phenotypes)
			Population basePopulation=Population.loadPopulation(this.basePopulation,gc,pc,fc,new Random(),true);


			int simulationNumber=k+1;
			this.logger.info("Starting simulation replicate number " + simulationNumber);
			this.logger.info("MimicrEE2 will proceed with forward simulations until generation " + this.maxGeneration);
			this.logger.info("Average genotype of starting population "+basePopulation.getAverageGenotype()+"; average phenotype of starting population "+basePopulation.getAveragePhenotype());

			// record stuff
			rr.record(0,simulationNumber,basePopulation);

			Population nextPopulation =basePopulation;
			// For the number of requested simulations get the next generation, and write it to file if requested
			for(int i=1; i<=this.maxGeneration; i++)
			{
				int popsize=popcont.getPopulationSize(i,simulationNumber);
				this.logger.info("Processing generation "+i+ " of replicate run "+simulationNumber+ " with N="+popsize);
				Population phenTail=sf.getSurvivors(nextPopulation, i, simulationNumber);
				this.logger.info("Selection intensity " +sf.getSurvivorFraction(i, simulationNumber) +"; Selected "+phenTail.size()+ " for next generation; average genotype "+phenTail.getAverageGenotype() +"; average phenotype "+phenTail.getAveragePhenotype());
				nextPopulation=phenTail.getNextGeneration(si,gc,pc,fc,mf,this.recGenerator,mutator, popsize);
				this.logger.info("Average genotype of offspring "+nextPopulation.getAverageGenotype()+"; average phenotype of offspring "+nextPopulation.getAveragePhenotype());

				// Use migration, if wanted ; replace with an ArrayList<DiploidGenomes>
				SexedDiploids migrants=this.migrationRegime.getMigrants(i,simulationNumber);
				if(migrants.size()>0) {
					this.logger.info("Updating sex chromosomes of migrants");
					migrants=migrants.updateSexChromosome(si);
					this.logger.info("Adding "+migrants.size()+ " migrants to the evolved population (randomly removing an equivalent number of evolved individuals)");
					Population migrantPop=Population.loadPopulation(migrants,gc,pc,fc, new Random(),false);
					this.logger.info("Average genotype of migrants "+migrantPop.getAverageGenotype()+"; average phenotype of migrants "+migrantPop.getAveragePhenotype());
					nextPopulation = Population.loadMigration(migrantPop, nextPopulation, new Random(),this.logger);
					this.logger.info("Average genotype of new population "+nextPopulation.getAverageGenotype()+"; average phenotype of new population "+nextPopulation.getAveragePhenotype()+ "; N="+nextPopulation.size());
				}

				// record stuff
				rr.record(i,simulationNumber,nextPopulation);
			}
		}

		rr.finishWriting();

	}





}
