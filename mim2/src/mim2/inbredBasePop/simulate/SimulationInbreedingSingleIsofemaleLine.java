package inbredbasepop.simulate;

import qmimcore.data.DiploidGenome;
import qmimcore.data.MatePair;
import qmimcore.data.Population;
import qmimcore.data.Specimen;
import qmimcore.data.fitness.*;
import qmimcore.data.recombination.RecombinationGenerator;
import qmimcore.data.statistic.PACReducer;

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
		this.gc=new GenotypeCalculatorDefault();
		this.pc=new PhenotypeCalculatorDefault();
		this.runnumber=runnumber;
		
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
				nextPopulation=nextPopulation.getNextGeneration(this.gc,this.pc,new MatingFunctionSmallNe(),recGenerator,this.sizeisofemaleLine);
			}

		this.logger.info("Propagating final isofemale line to a size of "+ targetCensus);
		Population propagatedPopulation= nextPopulation.getNextGeneration(this.gc,this.pc,new MatingFunctionSmallNe(),recGenerator,this.targetCensus);
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
			DiploidGenome f1child=matepair.getChild(this.recGenerator,random);
			double genotype =this.gc.getGenotype(f1child);
			double phenotype=this.pc.getPhenotype(genotype,random);
			Specimen s=new Specimen(genotype,phenotype,f1child);
		specs.add(s);
		}
		return new Population(specs);
	}

}
