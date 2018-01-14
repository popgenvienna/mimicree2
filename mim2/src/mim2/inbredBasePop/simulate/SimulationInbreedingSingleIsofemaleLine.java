package mim2.inbredBasePop.simulate;

import mimcore.data.DiploidGenome;
import mimcore.data.MatePair;
import mimcore.data.Population;
import mimcore.data.Specimen;
import mimcore.data.gpf.fitness.FitnessCalculatorAllEqual;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.mating.MatingFunctionRandomMating;
import mimcore.data.gpf.quantitative.*;
import mimcore.data.recombination.RecombinationGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class SimulationInbreedingSingleIsofemaleLine {
	private final MatePair matepair;
	private final int sizeisofemaleLine;
	private final int geninbreeding;
	private final int targetCensus;
	private final RecombinationGenerator recGenerator;
	private Logger logger;
	private IGenotypeCalculator gc;
	private IPhenotypeCalculator pc;
	private IFitnessCalculator fc;
	private final int runnumber;    //// TODO

	//new SimulationInbreedingSingleIsofemaleLine(this.matepair,this.sizeisofemaleLine,this.geninbreeding,targets.get(k),this.recGenerator,this.logger)
	public SimulationInbreedingSingleIsofemaleLine(MatePair mp, int sizeisofemaleLine, int geninbreeding, int targetCensus, RecombinationGenerator recgen, Logger logger,int runnumber)
	{
		this.matepair=mp;
		this.sizeisofemaleLine=sizeisofemaleLine;
		this.geninbreeding=geninbreeding;
		this.recGenerator=recgen;
		this.targetCensus=targetCensus;
		this.logger=logger;
		this.gc=new GenotypeCalculatorAllEqual();
		this.pc=new PhenotypeCalculatorAllEqual();
		this.runnumber=runnumber;
		this.fc=new FitnessCalculatorAllEqual();
		
	}

	
	public ArrayList<DiploidGenome> run()
	{


			Population startingPopulation=getStartingPopulation(new Random());
			int startpopulationsize=startingPopulation.size();
			this.logger.info("Starting simulation of isofemale line number " + runnumber);
			this.logger.info("will proceed with inbreeding using an inbred population size of "+this.sizeisofemaleLine+ " until generation " + this.geninbreeding);


			// For the number of requested simulations get the next generation, and write it to file if requested
			Population nextPopulation=startingPopulation;
			for(int i=1; i<=this.geninbreeding; i++)
			{
				this.logger.info("Processing generation "+i+ " of isofemale line "+runnumber);
				nextPopulation=nextPopulation.getNextGeneration(this.gc,this.pc,fc,new MatingFunctionRandomMating(),recGenerator,null, this.sizeisofemaleLine);
			}

		this.logger.info("Propagating final isofemale line to a size of "+ targetCensus);
		Population propagatedPopulation= nextPopulation.getNextGeneration(this.gc,this.pc,fc,new MatingFunctionRandomMating(),recGenerator,null, this.targetCensus);
		ArrayList<DiploidGenome> genomes=new ArrayList<DiploidGenome>();


		for(Specimen s: propagatedPopulation.getSpecimen())
		{
			genomes.add(s.getGenome());
		}
		return genomes;
	}

	private Population getStartingPopulation(Random random)
	{
		ArrayList<Specimen> specs= new ArrayList<Specimen>();
		for(int i=0; i<this.sizeisofemaleLine; i++)
		{
			DiploidGenome f1child=matepair.getChild(this.recGenerator, null,random);
			double genotype =this.gc.getGenotype(f1child);
			double phenotype=this.pc.getPhenotype(genotype,random);
			double fitness=this.fc.getFitness(f1child,phenotype);
			Specimen s=new Specimen(genotype,phenotype,fitness,f1child);
		specs.add(s);
		}
		return new Population(specs);
	}

}
