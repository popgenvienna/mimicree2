package qmimcore.data.fitness;

import qmimcore.data.MatePair;
import qmimcore.data.Population;
import qmimcore.data.Specimen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Mating function generates couples for mating for a given population of specimen.
 * Mating success is directly proportional to fitness
 * @author robertkofler
 *
 */
public class MatingFunctionSmallNe implements IMatingFunction {



	  private final ArrayList<Specimen> spec;
		private final int leng;



	public MatingFunctionSmallNe(Population pop)
	{
		
		this.spec=new ArrayList<Specimen>(pop.getSpecimen());
		this.leng=this.spec.size();
		if(leng<=2)throw new IllegalArgumentException("Population size must be larger than two; Selection strength to high?");
	}

	/**
	 * Default constructor
	 */
	public MatingFunctionSmallNe()
	{
		this.spec=new ArrayList<Specimen>();
		this.leng=0;
	}


	public  IMatingFunction factory(Population population)
	{
		return new MatingFunctionSmallNe(population);
	}

	

	
	/**
	 * Choose a couple for mating
	 * @return
	 */
	public MatePair getCouple(Random random)
	{
		if(this.leng==0) throw new IllegalStateException("Can not obain matepairs for empty mating functions");
		// super simple algorithm, random sort an array...
		Collections.shuffle(this.spec,random);
		MatePair mp=new MatePair(this.spec.get(0),this.spec.get(1));
		return mp;

	}

}
