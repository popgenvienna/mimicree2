package mimcore.data.sex;

import mimcore.data.MatePair;
import mimcore.data.Population;
import mimcore.data.Specimen;

import java.util.ArrayList;
import java.util.Random;

/**
 * Mating function generates couples for sex for a given population of specimen.
 * Mating success is directly proportional to gpf
 * @author robertkofler
 *
 */
public class MatingFunctionRandomMating_DeprOld implements IMatingFunction {



	  private final ArrayList<Specimen> spec;
		private final int leng;


	public MatingFunctionRandomMating_DeprOld(Population pop)
	{

		this.spec=new ArrayList<Specimen>(pop.getSpecimen());
		this.leng=this.spec.size();
		if(leng<=2)throw new IllegalArgumentException("Population size must be larger than two; Selection strength to high?");
	}
	public MatingFunctionRandomMating_DeprOld()
	{
		this.spec=new ArrayList<Specimen>();
		this.leng=0;
	}


	public  IMatingFunction factory(Population population)
	{
		return new MatingFunctionRandomMating_DeprOld(population);
	}

	

	
	/**
	 * Choose a couple for sex; Allow selfing
	 * Note that most simulations tools allow selfing, so for the sake of comparability we do this too.
	 * (Although it would be simple to forbid)
	 * @return
	 */
	public MatePair getCouple(Random random)
	{
		if(leng==0) throw new IllegalStateException("Can not obtain mate pairs for empty sex functions");
		int i1   =   random.nextInt(this.leng);
		int i2   = 	random.nextInt(this.leng);
		MatePair mp=new MatePair(this.spec.get(i1),this.spec.get(i2));
		return mp;

	}

}
