package mimcore.data;



import mimcore.data.fitness.mating.IMatingFunction;
import mimcore.data.fitness.quantitative.IGenotypeCalculator;
import mimcore.data.fitness.quantitative.IPhenotypeCalculator;
import mimcore.data.recombination.*;
import mimcore.misc.MimicreeThreadPool;


import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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





	public static Population loadPopulation(ArrayList<DiploidGenome> genomes, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, Random random)
	{
		ArrayList<Specimen> specimens=new ArrayList<Specimen>();
		ArrayList<Double> genotypes=new ArrayList<Double>();
		for(DiploidGenome genome: genomes)
		{
			double genotype=gc.getGenotype(genome);
			double phenotype=pc.getPhenotype(genotype,random);
			Specimen s=new Specimen(genotype,phenotype,genome);
			specimens.add(s);

		}
		
		// Create a new population
		return new Population(specimens);
	}

	/**
	 * Get the fraction of individuals having the most extreme phenotypes
	 * @param selectedFraction
	 * @return
	 */
	public Population getPenotypicTail(double selectedFraction)
	{
		ArrayList<Specimen>  specs=new ArrayList<Specimen>(this.specimen);
		Collections.sort(specs,new Comparator<Specimen>() {
			@Override
			public int compare(Specimen s1, Specimen s2) {
				if(s1.phenotype() < s2.phenotype()) return 1;
				if(s1.phenotype() > s2.phenotype()) return -1;
				return 0;
			}
		});

		ArrayList<Specimen> toret=new ArrayList<Specimen>();
		int toselect=(int)((double)specs.size()*selectedFraction);
		for(int i=0; i<toselect; i++)
		{
			   toret.add(specs.get(i));
		}
		return new Population(toret);
	}


	
	/**
	 * Obtain the next generation of the population;
	 * @return
	 */
	public Population getNextGeneration( IGenotypeCalculator gc, IPhenotypeCalculator pc, IMatingFunction matingfunction,  RecombinationGenerator recGenerator, int targetN)
	{
		// the selected ones: go and mate ..populate the earth
		IMatingFunction mf=matingfunction.factory(this);
		SpecimenGenerator specGen=new SpecimenGenerator(mf,gc,pc,recGenerator,targetN);

		return new Population(specGen.getSpecimen());
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
	private final IGenotypeCalculator gc;
	private final IPhenotypeCalculator pc;
	private final IMatingFunction mf;
	private final int populationSize;
	private final RecombinationGenerator recGen;

	
	public SpecimenGenerator(IMatingFunction mf, IGenotypeCalculator gc, IPhenotypeCalculator pc, RecombinationGenerator recGen, int populationSize )
	{
		//	 * (mf,gc,pc,recGenerator,this.size());

		this.mf=mf;
		this.populationSize=populationSize;
		this.gc=gc;
		this.pc=pc;
		this.recGen=recGen;


	}

	public ArrayList<Specimen> getSpecimen()
	{
		SpecimenCollector col=new SpecimenCollector();
		ExecutorService executor= MimicreeThreadPool.getExector();
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();

		for(int i=0; i<this.populationSize; i++)
		{
			call.add(Executors.callable(new SingleSpecimenGenerator(this.mf,this.gc,this.pc,this.recGen,col,MimicreeThreadPool.getRandomForThread(i))));
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
		assert(specs.size()==populationSize);
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
	private final IMatingFunction mf;
	private final SpecimenCollector collector;
	private final IGenotypeCalculator gc;
	private final IPhenotypeCalculator pc;
	private final Random random;
	private final RecombinationGenerator recGenerator;
	public SingleSpecimenGenerator(IMatingFunction mf, IGenotypeCalculator gc, IPhenotypeCalculator pc, RecombinationGenerator recGen, SpecimenCollector collector, Random random)
	{
		this.mf=mf;
		this.pc=pc;
		this.gc=gc;
		this.collector=collector;
		this.recGenerator=recGen;
		this.random = random;
	}
	
	public void run()
	{
		MatePair mp=mf.getCouple(this.random);
		DiploidGenome fertilizedEgg=mp.getChild(recGenerator,this.random);
		double genotype=gc.getGenotype(fertilizedEgg);
		double phenotype=pc.getPhenotype(genotype,random);
		Specimen spec=   new Specimen(genotype,phenotype,fertilizedEgg);
		collector.addSpecimen(spec);
	}
	
}

