package qmimcore.data.fitness;

import qmimcore.data.*;
import qmimcore.data.Specimen;
import java.util.Random;


import java.util.ArrayList;

/**
 * Mating function generates couples for mating for a given population of specimen.
 * Mating success is directly proportional to fitness
 * @author robertkofler
 *
 */
public class MatingFunctionLargeNe implements IMatingFunction {
	
	

	  private final ArrayList<Specimen> spec;
		private final int leng;


	public MatingFunctionLargeNe(Population pop)
	{
		
		this.spec=new ArrayList<Specimen>(pop.getSpecimen());
		this.leng=this.spec.size();
		if(leng<=2)throw new IllegalArgumentException("Population size must be larger than two; Selection strength to high?");
	}
	public MatingFunctionLargeNe()
	{
		this.spec=new ArrayList<Specimen>();
		this.leng=0;
	}


	public  IMatingFunction factory(Population population)
	{
		return new MatingFunctionLargeNe(population);
	}

	

	
	/**
	 * Choose a couple for mating
	 * @return
	 */
	public MatePair getCouple(Random random)
	{
		if(leng==0) throw new IllegalStateException("Can not obtain mate pairs for empty mating functions");
		int i1   =   random.nextInt(this.leng);
		int i2   = 	random.nextInt(this.leng);

		int breakcounter=0;
		while(i1==i2)
		{
			if(breakcounter>1000) throw new IllegalArgumentException("Something is fucked up; is the population shrinking");
			i2=random.nextInt(this.leng);
		breakcounter++;
		}
		MatePair mp=new MatePair(this.spec.get(i1),this.spec.get(i2));
		return mp;

	}

}
