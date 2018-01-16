package mimcore.data;



import mimcore.data.Mutator.IMutator;
import mimcore.data.sex.IMatingFunction;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.IPhenotypeCalculator;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.recombination.*;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssigner;
import mimcore.misc.MimicreeThreadPool;


import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


/**
 * A population
 * is a collection of specimen (individuals)
 * @author robertkofler
 *
 */
public class Population {
	private final ArrayList<Specimen> specimen;
	// The matings that lead to the certain population
	public Population(ArrayList<Specimen> specimen)
	{

		this.specimen=new ArrayList<Specimen>(specimen);

	}

	public double getAveragePhenotype()
	{
		double toret=0.0;
		for(Specimen s: this.specimen)
		{
			toret+=s.quantPhenotype();
		}
		return toret/this.size();
	}

	public double getAverageGenotype()
	{
		double toret=0.0;
		for(Specimen s: this.specimen)
		{
			toret+=s.quantGenotype();
		}
		return toret/this.size();
	}

	public double getAverageFitness()
	{
		double toret=0.0;
		for(Specimen s: this.specimen)
		{
			toret+=s.fitness();
		}
		return toret/this.size();
	}




	public static Population loadMigration(Population migrants, Population targetPopulation, Random random, Logger logger)
	{
		// no migration no action
		if(migrants.size()==0) return targetPopulation;

		//ok ok we have some migration
		// Convert migrants into specimens
		ArrayList<Specimen> newPopulation=migrants.getSpecimen();

		int popsize=targetPopulation.size();


		// second fill in the remaining individuals randomly from the target population
		LinkedList<Specimen> tps=new LinkedList<Specimen>(targetPopulation.getSpecimen());
		int countAdded=0;
		while(newPopulation.size()<popsize)
		{
			int targetindex=random.nextInt(tps.size());
			Specimen spec=tps.remove(targetindex);
			newPopulation.add(spec);
			countAdded++;
		}
		logger.fine("Added "+countAdded+" individuals from evolved population");
		// Create a new population
		return new Population(newPopulation);
	}


	public static Population loadPopulation(ArrayList<DiploidGenome> genomes, SexAssigner sa, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, Random random)
	{
		ArrayList<Specimen> specimens=new ArrayList<Specimen>();
		ArrayList<Double> genotypes=new ArrayList<Double>();
		LinkedList<Sex> sexes=new LinkedList<Sex>(sa.getSexes(genomes.size(),random));
		for(DiploidGenome genome: genomes)
		{
			Sex mysex=sexes.remove(0);
			double genotype=gc.getGenotype(genome,mysex);
			double phenotype=pc.getPhenotype(mysex,genotype,random);
			double fitness=fc.getFitness(genome,phenotype,mysex);
			Specimen s=new Specimen(mysex, genotype,phenotype,fitness,genome);
			specimens.add(s);

		}
		
		// Create a new population
		return new Population(specimens);
	}




	
	/**
	 * Obtain the next generation of the population;
	 * @return
	 */
	public Population getNextGeneration( SexAssigner sa, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, IMatingFunction matingfunction,
										 RecombinationGenerator recGenerator, IMutator mutator, int targetN)
	{
		// the selected ones: go and mate ..populate the earth
		IMatingFunction mf=matingfunction.factory(this);
		SpecimenGenerator specGen=new SpecimenGenerator(sa, mf,gc,pc,fc,recGenerator,mutator, targetN);
		ArrayList<Specimen> spec=specGen.getSpecimen();
		return new Population(spec);
	}



	
	/**
	 * Retrieve the specimen constituting a population
	 * @return
	 */
	public ArrayList<Specimen> getSpecimen()
	{
		return new ArrayList<Specimen>(this.specimen);
	}
	

	
	public int size()
	{
		return this.specimen.size();
	}
	
	public boolean isFixed(GenomicPosition position)
	{
		int countAncestral=0;
		int hapCount=this.specimen.size()*2;
		for(Specimen spec: this.specimen)
		{
			if(spec.getGenome().getHaplotypeA().hasAncestral(position)) countAncestral++;
			if(spec.getGenome().getHaplotypeB().hasAncestral(position)) countAncestral++;
		}
		if(countAncestral==0 || countAncestral==hapCount) return true;
		return false;
	}

	/**
	 * Determine whether the ancestral allele fixed
	 * @param position
	 * @return
	 */
	public boolean isAncestralFixed(GenomicPosition position)
	{
		int countAncestral=0;
		int hapCount=this.specimen.size()*2;
		for(Specimen spec: this.specimen)
		{
			if(spec.getGenome().getHaplotypeA().hasAncestral(position)) countAncestral++;
			if(spec.getGenome().getHaplotypeB().hasAncestral(position)) countAncestral++;
		}
		if(! (countAncestral==0 || countAncestral==hapCount)) throw new IllegalArgumentException("Population is not yet fixed");
		if(countAncestral==hapCount)
		{
			return true;
		}
		return false;
	}
	
}

// --------------------------------------------------------------------------- //
// ----------------------------------- HELPER -------------------------------- //
// -------------------------- GET SPECIMEN IN MULTITHREADING ----------------- //
// --------------------------------------------------------------------------- //


class SpecimenGenerator
{
	private final SexAssigner sa;
	private final IGenotypeCalculator gc;
	private final IPhenotypeCalculator pc;
	private final IFitnessCalculator fc;
	private final IMatingFunction mf;
	private final int populationSize;
	private final RecombinationGenerator recGen;
	private final IMutator mutator;

	
	public SpecimenGenerator(SexAssigner sa, IMatingFunction mf, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, RecombinationGenerator recGen, IMutator mutator, int populationSize )
	{
		//	 * (mf,gc,pc,recGenerator,this.size());

		this.mf=mf;
		this.populationSize=populationSize;
		this.gc=gc;
		this.pc=pc;
		this.recGen=recGen;
		this.fc=fc;
		this.mutator=mutator;
		this.sa=sa;


	}

	public ArrayList<Specimen> getSpecimen()
	{
		SpecimenCollector col=new SpecimenCollector();
		ExecutorService executor= MimicreeThreadPool.getExector();
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		LinkedList<Sex> sexes=new LinkedList<Sex>(sa.getSexes(this.populationSize,new Random()));

		for(int i=0; i<this.populationSize; i++)
		{
			call.add(Executors.callable(new SingleSpecimenGenerator(sexes.remove(0),this.mf,this.gc,this.pc,this.fc,this.recGen,col,mutator,MimicreeThreadPool.getRandomForThread(i))));
		}

		try
		{
			// Run them all!
			executor.invokeAll(call);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		ArrayList<Specimen> specs=col.getSpecimen();

		if(specs.size()!=populationSize) throw new IllegalArgumentException("Fatal error; obtained population size smaller than the target size; Please contact author");
		return specs;
	}


	
	private void  run()
	{



	}
	

}

/**
 * Only a synchronized wrapper for a ArrayList
 * @author robertkofler
 *
 */
class SpecimenCollector
{
	private final ArrayList<Specimen> specs =new ArrayList<Specimen>();


	public SpecimenCollector(){}
	
	public synchronized void addSpecimen(Specimen specimen)
			{
		this.specs.add(specimen);
	}

	
	public synchronized ArrayList<Specimen> getSpecimen()
	{
		return new ArrayList<Specimen>(this.specs);
	}
}

/**
 * Sinlge generator for a specimen
 * @author robertkofler
 *
 */
class SingleSpecimenGenerator implements Runnable
{
	//this.mf,this.gc,this.pc,this.recGen,col,MimicreeThreadPool.getRandomForThread(i))
	private final Sex mysex;
	private final IMatingFunction mf;
	private final SpecimenCollector collector;
	private final IGenotypeCalculator gc;
	private final IPhenotypeCalculator pc;
	private final IFitnessCalculator fc;
	private final IMutator mutator;
	private final Random random;
	private final RecombinationGenerator recGenerator;
	public SingleSpecimenGenerator(Sex mysex, IMatingFunction mf, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, RecombinationGenerator recGen, SpecimenCollector collector, IMutator mutator, Random random)
	{
		this.mf=mf;
		this.pc=pc;
		this.gc=gc;
		this.fc=fc;
		this.collector=collector;
		this.recGenerator=recGen;
		this.random = random;
		this.mutator=mutator;
		this.mysex=mysex;
	}
	
	public void run()
	{
		MatePair mp=mf.getCouple(this.random);
		DiploidGenome fertilizedEgg=mp.getChild(recGenerator,mutator,this.random);
		double genotype=gc.getGenotype(fertilizedEgg,mysex);
		double phenotype=pc.getPhenotype(mysex,genotype,random);
		double fitness=fc.getFitness(fertilizedEgg,phenotype,mysex);
		Specimen spec=   new Specimen(mysex,genotype,phenotype,fitness,fertilizedEgg);
		collector.addSpecimen(spec);
	}
	
}

