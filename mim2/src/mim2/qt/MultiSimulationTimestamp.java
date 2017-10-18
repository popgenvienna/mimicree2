package mim2.qt;


import mimcore.data.fitness.IFitnessCalculator;
import mimcore.data.fitness.mating.MatingFunctionRandomMating;
import mimcore.data.fitness.quantitative.*;
import mimcore.data.fitness.survival.ISurvivalFunction;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.statistic.PACReducer;
import mimcore.data.statistic.PopulationAlleleCount;
import mimcore.data.DiploidGenome;
import mimcore.data.Population;
import mimcore.io.misc.ISummaryWriter;
import mimcore.io.misc.SyncWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

public class MultiSimulationTimestamp {
	private final ArrayList<DiploidGenome> dipGenomes;
	private final GenotypeCalculator gc;
	private final PhenotypeCalculator pc;
	private final IFitnessCalculator fc;
	private final ISurvivalFunction sf;
	private final IMigrationRegime migrationRegime;
	private final String outputSync;
	private final String outputGPF;
	private final String outputDir;
	private final RecombinationGenerator recGenerator;

	private final int maxGeneration;
	private final int replicateRuns;
	private Logger logger;


	// internal variables
	private final HashSet<Integer> outputGenerations;
	private ArrayList<PopulationAlleleCount> pacs;

	public MultiSimulationTimestamp(ArrayList<DiploidGenome> dipGenomes, GenotypeCalculator gc, PhenotypeCalculator pc, IFitnessCalculator fc, ISurvivalFunction sf,
									IMigrationRegime migrationRegime, String outputSync, String outputGPF, String outputDir, RecombinationGenerator recGenerator,
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


		// internal variables
		this.pacs=new ArrayList<PopulationAlleleCount>();
		
	}

	
	public void run()
	{

		for(int k =0; k<this.replicateRuns; k++)
		{
			Population startingPopulation=Population.loadPopulation(dipGenomes,gc,pc,fc,new Random());
			int startpopulationsize=startingPopulation.size();
			int simulationNumber=k+1;
			this.logger.info("Starting simulation replicate number " + simulationNumber);
			this.logger.info("MimicrEE2 will proceed with forward simulations until generation " + this.maxGeneration);
			this.logger.info("Average genotype of starting population "+startingPopulation.getAverageGenotype()+"; average phenotype of starting population "+startingPopulation.getAveragePhenotype());

			this.logger.info("Recording base population of replicate " + simulationNumber);


			pacs.add(new PACReducer(startingPopulation).reduce());
			Population nextPopulation =startingPopulation;
			// For the number of requested simulations get the next generation, and write it to file if requested
			for(int i=1; i<=this.maxGeneration; i++)
			{
				this.logger.info("Processing generation "+i+ " of replicate run "+simulationNumber);
				Population phenTail=sf.getSurvivors(nextPopulation, i, simulationNumber);
				this.logger.info("Selection intensity " +sf.getSurvivorFraction(i, simulationNumber) +"; Selected "+phenTail.size()+ " for next generation; average genotype "+phenTail.getAverageGenotype() +"; average phenotype "+phenTail.getAveragePhenotype());
				nextPopulation=phenTail.getNextGeneration(gc,pc,fc,new MatingFunctionRandomMating(),this.recGenerator,startpopulationsize);
				this.logger.info("Average genotype of offspring "+nextPopulation.getAverageGenotype()+"; average phenotype of offspring "+nextPopulation.getAveragePhenotype());


				if(outputGenerations.contains(i))
				{
					this.logger.info("Recording population at generation "+i+" of replicate "+simulationNumber);
					pacs.add(new PACReducer(nextPopulation).reduce());
				}
			}
		}

		// todo
		ISummaryWriter sw = new SyncWriter(this.outputSync,this.logger);
		sw.write(this.pacs);

	}


	private void recordPAC(Population toRecord)
	{

	}
}
