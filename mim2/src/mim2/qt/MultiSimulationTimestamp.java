package qtmimicree.simulate;

import qmimcore.data.fitness.*;
import qmimcore.data.recombination.RecombinationGenerator;
import qmimcore.data.statistic.PACReducer;
import qmimcore.data.statistic.PopulationAlleleCount;
import qmimcore.data.DiploidGenome;
import qmimcore.data.Population;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

public class MultiSimulationTimestamp {
	private final ArrayList<DiploidGenome> dipGenomes;
	private final GenotypeCalculator gc;
	private final PhenotypeCalculator pc;
	private final ISelectionRegime sr;
	private final RecombinationGenerator recGenerator;
	private final HashSet<Integer> outputGenerations;
	private final int maxGeneration;
	private final int replicateRuns;
	private Logger logger;

	public MultiSimulationTimestamp(ArrayList<DiploidGenome> dipGenomes, GenotypeCalculator gc, PhenotypeCalculator pc, ISelectionRegime sr, RecombinationGenerator recGenerator,
									ArrayList<Integer> outputGenerations, int replicateRuns, Logger logger)
	{

		this.dipGenomes=dipGenomes;
		this.pc=pc;
		this.gc=gc;
		this.sr=sr;
		
		int max=0;
		HashSet<Integer> toOutput=new HashSet<Integer>();
		for(Integer i : outputGenerations)
		{
			toOutput.add(i);
			if(i > max ) max=i;
		}
		this.maxGeneration=max;
		this.outputGenerations=toOutput;
		this.logger=logger;
		this.recGenerator=recGenerator;
		this.replicateRuns=replicateRuns;
		
	}

	
	public ArrayList<PopulationAlleleCount> run()
	{
		ArrayList<PopulationAlleleCount> pacs=new ArrayList<PopulationAlleleCount>();
		for(int k =0; k<this.replicateRuns; k++)
		{
			Population startingPopulation=Population.loadPopulation(dipGenomes,gc,pc,new Random());
			int startpopulationsize=startingPopulation.size();
			int simulationNumber=k+1;
			this.logger.info("Starting simulation replicate number " + simulationNumber);
			this.logger.info("qtMimicree will proceed with forward simulations until generation " + this.maxGeneration);
			this.logger.info("Average genotype of starting population "+startingPopulation.getAverageGenotype()+"; average phenotype of starting population "+startingPopulation.getAveragePhenotype());

			this.logger.info("Recording base population at generation of replicate " + simulationNumber);
			pacs.add(new PACReducer(startingPopulation).reduce());
			Population nextPopulation =startingPopulation;
			// For the number of requested simulations get the next generation, and write it to file if requested
			for(int i=1; i<=this.maxGeneration; i++)
			{
				this.logger.info("Processing generation "+i+ " of replicate run "+simulationNumber);
				Population phenTail=nextPopulation.getPenotypicTail(sr.getSelectionIntensity(i,simulationNumber));
				this.logger.info("Selection intensity " +sr.getSelectionIntensity(i,simulationNumber) +"; Selected "+phenTail.size()+ " for next generation; average genotype "+phenTail.getAverageGenotype() +"; average phenotype "+phenTail.getAveragePhenotype());
				nextPopulation=phenTail.getNextGeneration(gc,pc,new MatingFunctionLargeNe(),this.recGenerator,startpopulationsize);
				this.logger.info("Average genotype of offspring "+nextPopulation.getAverageGenotype()+"; average phenotype of offspring "+nextPopulation.getAveragePhenotype());
				if(outputGenerations.contains(i))
				{
					this.logger.info("Recording population at generation "+i+" of replicate "+simulationNumber);
					pacs.add(new PACReducer(nextPopulation).reduce());
				}
			}
		}
		return pacs;
	}
}
