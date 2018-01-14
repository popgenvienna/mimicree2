package mim2.qt;


import mimcore.data.Mutator.IMutator;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.sex.MatingFunctionRandomMating;
import mimcore.data.gpf.quantitative.*;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.statistic.PACReducer;
import mimcore.data.statistic.GPFCollection;
import mimcore.data.statistic.PopulationAlleleCount;
import mimcore.data.DiploidGenome;
import mimcore.data.Population;
import mimcore.io.HaplotypeMultiWriter;
import mimcore.io.misc.GPFWriter;
import mimcore.io.misc.ISummaryWriter;
import mimcore.io.misc.SyncWriter;
import mimcore.data.statistic.GPFReducer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

public class MultiSimulationQT {
	private final ArrayList<DiploidGenome> dipGenomes;
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

	private final int maxGeneration;
	private final int replicateRuns;
	private Logger logger;


	// internal variables
	private final HashSet<Integer> outputGenerations;
	private ArrayList<PopulationAlleleCount> pacs;
	private ArrayList<GPFCollection> gpfs;

	public MultiSimulationQT(ArrayList<DiploidGenome> dipGenomes, PopulationSizeContainer popcont, IGenotypeCalculator gc, PhenotypeCalculator pc, IFitnessCalculator fc, ISurvivalFunction sf,
                             IMigrationRegime migrationRegime, IMutator mutator, String outputSync, String outputGPF, String outputDir, RecombinationGenerator recGenerator,
                             ArrayList<Integer> outputGenerations, int replicateRuns, Logger logger)
	{

		this.dipGenomes=dipGenomes;
		this.pc=pc;
		this.gc=gc;
		this.sf=sf;
		this.fc=fc;
		
		int max=0;
		HashSet<Integer> toOutput=new HashSet<Integer>();
		for(Integer i : outputGenerations)
		{
			toOutput.add(i);
			if(i > max ) max=i;
		}
		this.maxGeneration=max;
		this.migrationRegime=migrationRegime;
		this.outputSync=outputSync;
		this.outputGPF=outputGPF;
		this.outputDir=outputDir;
		this.outputGenerations=toOutput;
		this.logger=logger;
		this.recGenerator=recGenerator;
		this.replicateRuns=replicateRuns;
		this.popcont=popcont;
		this.mutator=mutator;


		// internal variables
		this.pacs=new ArrayList<PopulationAlleleCount>();
		this.gpfs=new ArrayList<GPFCollection>();
		
	}

	
	public void run()
	{



		for(int k =0; k < this.replicateRuns; k++)
		{
			// Base population generated always anew, because new env. variance for each individual
			// for different replicates you dont use the same individuals (phenotypes) but only the same genotypes (with different phenotypes)
			Population basePopulation=Population.loadPopulation(dipGenomes,gc,pc,fc,new Random());


			int simulationNumber=k+1;
			this.logger.info("Starting simulation replicate number " + simulationNumber);
			this.logger.info("MimicrEE2 will proceed with forward simulations until generation " + this.maxGeneration);
			this.logger.info("Average genotype of starting population "+basePopulation.getAverageGenotype()+"; average phenotype of starting population "+basePopulation.getAveragePhenotype());

			// record stuff
			recordPAC(basePopulation,0,simulationNumber);
			recordGPF(basePopulation, 0, simulationNumber);
			recordHap(basePopulation, 0, simulationNumber);

			Population nextPopulation =basePopulation;
			// For the number of requested simulations get the next generation, and write it to file if requested
			for(int i=1; i<=this.maxGeneration; i++)
			{
				int popsize=popcont.getPopulationSize(i,simulationNumber);
				this.logger.info("Processing generation "+i+ " of replicate run "+simulationNumber+ " with N="+popsize);
				Population phenTail=sf.getSurvivors(nextPopulation, i, simulationNumber);
				this.logger.info("Selection intensity " +sf.getSurvivorFraction(i, simulationNumber) +"; Selected "+phenTail.size()+ " for next generation; average genotype "+phenTail.getAverageGenotype() +"; average phenotype "+phenTail.getAveragePhenotype());
				nextPopulation=phenTail.getNextGeneration(gc,pc,fc,new MatingFunctionRandomMating(),this.recGenerator,mutator, popsize);
				this.logger.info("Average genotype of offspring "+nextPopulation.getAverageGenotype()+"; average phenotype of offspring "+nextPopulation.getAveragePhenotype());

				// Use migration, if wanted ; replace with an ArrayList<DiploidGenomes>
				ArrayList<DiploidGenome> migrants=this.migrationRegime.getMigrants(i,simulationNumber);
				if(migrants.size()>0) {
					this.logger.info("Adding "+migrants.size()+ " migrants to the evolved population (randomly removing an equivalent number of evolved individuals)");
					Population migrantPop=Population.loadPopulation(migrants,gc,pc,fc, new Random());
					this.logger.info("Average genotype of migrants "+migrantPop.getAverageGenotype()+"; average phenotype of migrants "+migrantPop.getAveragePhenotype());
					nextPopulation = Population.loadMigration(migrantPop, nextPopulation, new Random(),this.logger);

					this.logger.info("Average genotype of new population "+nextPopulation.getAverageGenotype()+"; average phenotype of new population "+nextPopulation.getAveragePhenotype()+ "; N="+nextPopulation.size());
				}

				// record stuff only in the requested generations
				if(outputGenerations.contains(i))
				{
					recordGPF(nextPopulation, i, simulationNumber);
					recordPAC(nextPopulation, i, simulationNumber);
					recordHap(nextPopulation, i, simulationNumber);
				}
			}
		}

		// Finally write as yet unwritten results
		if(this.outputSync!=null) {
			ISummaryWriter sw = new SyncWriter(this.outputSync, this.logger);
			sw.write(this.pacs);
		}
		if(this.outputGPF!=null){
			GPFWriter gw=new GPFWriter(this.outputGPF,this.logger);
			gw.write(gpfs);
		}

	}


	private void recordPAC(Population toRecord,int generation, int replicate)
	{
		// No output file no action
		if(this.outputSync==null) return;
		this.logger.info("Recording allele frequences at generation "+generation+" of replicate "+replicate);
		pacs.add(new PACReducer(toRecord).reduce());
	}

	private void recordGPF(Population toRecord, int generation, int replicate)
	{
		// No output file no action
		if(this.outputGPF==null) return;
		this.logger.info("Recording genotype/phenotype/fitness at generation "+generation+" of replicate "+replicate);
		gpfs.add(new GPFReducer(toRecord,replicate,generation).reduce());
	}


	private void recordHap(Population toRecord, int generation, int replicate)
	{
		// No output file no action
		if(this.outputDir==null) return;
		this.logger.info("Recording haplotypes at generation "+generation+" of replicate "+replicate);
		new HaplotypeMultiWriter(toRecord, this.outputDir,generation, replicate, this.logger).write();
	}


}
