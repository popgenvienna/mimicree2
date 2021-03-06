package mimcore.data;



import mimcore.data.Mutator.IMutator;
import mimcore.data.sex.IMatingFunction;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.IPhenotypeCalculator;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.recombination.*;
import mimcore.data.sex.Sex;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.SexInfo;
import mimcore.misc.MimicreeThreadPool;


import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

	public boolean isFertile()
	{
		int males=0;
		int females=0;
		int herma=0;
		for(Specimen spec: this.specimen)
		{
			Sex s=spec.getSex();
			if(s==Sex.Female)females++;
			else if(s==Sex.Male)males++;
			else if(s==Sex.Hermaphrodite)herma++;
			else throw new IllegalArgumentException("Unknown sex "+s);
		}
		if(females==0 &&herma==0) return false;
		if(males==0 &&herma==0) return false;
		return true;
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
		Population toret=new Population(newPopulation);
		if(!toret.isFertile()) throw new IllegalArgumentException("Population is not fertile; either only males or only females");
		return toret;
	}


	public static Population loadPopulation(SexedDiploids sexed, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, Random random,boolean checkValid)
	{
		ArrayList<Specimen> specimens=new ArrayList<Specimen>();

		ArrayList<DiploidGenome> genomes=sexed.getDiploids();
		ArrayList<Sex> sexes=sexed.getSexAssigner();
		for(int i=0; i<sexed.size(); i++)
		{
			DiploidGenome genome=genomes.get(i);
			Sex mysex=sexes.get(i);
			double genotype=gc.getGenotype(genome,mysex);
			double phenotype=pc.getPhenotype(mysex,genotype,random);
			double fitness=fc.getFitness(genome,phenotype,mysex);
			Specimen s=new Specimen(mysex, genotype,phenotype,fitness,genome);
			specimens.add(s);

		}
		// Create a new population
		Population toret=new Population(specimens);
		if(checkValid && (!toret.isFertile())) throw new IllegalArgumentException("Population is not fertile; either only males or only females");
		return toret;
	}




	
	/**
	 * Obtain the next generation of the population;
	 * @return
	 */
	public Population getNextGeneration(SexInfo si, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, IMatingFunction matingfunction,
										RecombinationGenerator recGenerator, IMutator mutator, int targetN, boolean haploid, boolean clonal)
	{
		// the selected ones: go and mate ..populate the earth
		IMatingFunction mf=matingfunction.factory(this);
		SpecimenGenerator specGen=new SpecimenGenerator(si, mf,gc,pc,fc,recGenerator,mutator, targetN,haploid,clonal);
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
	private final ISexAssigner sa;
	private final IGenotypeCalculator gc;
	private final IPhenotypeCalculator pc;
	private final IFitnessCalculator fc;
	private final IMatingFunction mf;
	private final int populationSize;
	private final RecombinationGenerator recGen;
	private final IMutator mutator;
	private final SexInfo si;
	private final boolean haploid;
	private final boolean clonal;

	
	public SpecimenGenerator(SexInfo si, IMatingFunction mf, IGenotypeCalculator gc, IPhenotypeCalculator pc, IFitnessCalculator fc, RecombinationGenerator recGen, IMutator mutator, int populationSize,boolean haploid, boolean clonal )
	{
		//	 * (mf,gc,pc,recGenerator,this.size());

		this.mf=mf;
		this.populationSize=populationSize;
		this.gc=gc;
		this.pc=pc;
		this.recGen=recGen;
		this.fc=fc;
		this.mutator=mutator;
		this.sa=si.getSexAssigner();
		this.si=si;
		this.haploid=haploid;
		this.clonal=clonal;


	}

	public ArrayList<Specimen> getSpecimen()
	{
		SpecimenCollector col=new SpecimenCollector();
		ExecutorService executor= MimicreeThreadPool.getExector();
		ArrayList<Callable<Object>> call=new ArrayList<Callable<Object>>();
		LinkedList<Sex> sexes=new LinkedList<Sex>(sa.getSexes(this.populationSize,new Random()));
		List<Future<Object>> future=null;

		for(int i=0; i<this.populationSize; i++)
		{
			call.add(Executors.callable(new SingleSpecimenGenerator(sexes.remove(0),this.si,this.mf,this.gc,this.pc,this.fc,this.recGen,col,mutator,MimicreeThreadPool.getRandomForThread(i),haploid,clonal)));
		}

		try
		{
			// Run them all!
			future=executor.invokeAll(call);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		ArrayList<Specimen> specs=col.getSpecimen();


		if(specs.size()!=populationSize) {
			for(Future<Object>f: future)
			{
				try{
				f.get();}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}
			}
			throw new IllegalArgumentException("Fatal error during mating: obtained population size smaller than the target size; Please contact author");

		}
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
	private final SexInfo si;
	private final boolean haploid;
	private final boolean clonal;
	public SingleSpecimenGenerator(Sex mysex, SexInfo si, IMatingFunction mf, IGenotypeCalculator gc, IPhenotypeCalculator pc,
								   IFitnessCalculator fc, RecombinationGenerator recGen, SpecimenCollector collector, IMutator mutator, Random random, boolean haploid,boolean clonal)
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
		this.si=si;
		this.haploid=haploid;
		this.clonal=clonal;
	}
	
	public void run()
	{
		MatePair mp=mf.getCouple(this.random);

		DiploidGenome fertilizedEgg=null;

		// haploids and diploids mate and recombine differently
		if(clonal) fertilizedEgg=mp.getChildClonal(recGenerator,mutator,haploid,this.random);
		else if(haploid) fertilizedEgg=mp.getChildHaploid(recGenerator,mysex, mutator,si,this.random);
		else fertilizedEgg=mp.getChild(recGenerator,mysex, mutator,si,this.random);

		double genotype=gc.getGenotype(fertilizedEgg,mysex);
		double phenotype=pc.getPhenotype(mysex,genotype,random);
		double fitness=fc.getFitness(fertilizedEgg,phenotype,mysex);
		Specimen spec=   new Specimen(mysex,genotype,phenotype,fitness,fertilizedEgg);
		collector.addSpecimen(spec);
	}
	
}

