package mimcore.data.sex;

import mimcore.data.*;
import mimcore.data.Specimen;

import java.util.Random;


import java.util.ArrayList;

/**
 * Mating function generates couples for sex for a given population of specimen.
 * Mating success is directly proportional to gpf
 * @author robertkofler
 *
 */
public class MatingFunctionRandomMating implements IMatingFunction {
	
	

	private final ArrayList<Specimen> fmh;
	private final ArrayList<Specimen> fh;
	private final ArrayList<Specimen> mh;
	private final double selfingRate;


	public MatingFunctionRandomMating(Population pop, double selfingRate)
	{
		if(selfingRate<0.0 || selfingRate>1.0) throw new IllegalArgumentException("Selfing rate must be between 0.0 and 1.0");
		this.selfingRate=selfingRate;

		this.fmh=new ArrayList<Specimen>(pop.getSpecimen());
		if(this.fmh.size()<=2)throw new IllegalArgumentException("Population size must be larger than two; Selection strength to high?");

		ArrayList<Specimen> afh = new ArrayList<Specimen>();
		ArrayList<Specimen> amh = new ArrayList<Specimen>();
		for(Specimen s: this.fmh)
		{
			if(s.getSex()==Sex.Female) afh.add(s);
			else if(s.getSex()==Sex.Male)amh.add(s);
			else if(s.getSex()==Sex.Hermaphrodite)
			{
				afh.add(s);
				amh.add(s);
			}
			else throw new IllegalArgumentException("Unknown sex "+s.getSex());
		}
		this.fh=afh;
		this.mh=amh;
	}
	public MatingFunctionRandomMating(double selfingRate)
	{
		this.fmh=new ArrayList<Specimen>();
		this.fh=new ArrayList<Specimen>();
		this.mh=new ArrayList<Specimen>();
		this.selfingRate=selfingRate;

	}


	public  IMatingFunction factory(Population population)
	{
		return new MatingFunctionRandomMating(population,this.selfingRate);
	}

	

	
	/**
	 * Choose a couple for sex; Allow selfing
	 * Note that most simulations tools allow selfing, so for the sake of comparability we do this too.
	 * (Although it would be simple to forbid)
	 * @return
	 */
	public MatePair getCouple(Random random)
	{

		if(fmh.size()==0) throw new IllegalStateException("Can not obtain mate pairs for empty sex functions");
		Specimen s1=this.fmh.get(random.nextInt(this.fmh.size()));
		Specimen s2=null;

		if(s1.getSex()==Sex.Male)
		{
			s2=this.fh.get(random.nextInt(fh.size()));
		}
		else if(s1.getSex()==Sex.Female)
		{
			s2=this.mh.get(random.nextInt(this.mh.size()));
		}
		else if(s1.getSex()==Sex.Hermaphrodite)
		{
			// hermaphrodite mates everything
			// test for selfing
			if(random.nextDouble()<this.selfingRate) s2=s1;
			else
			{
				// no selfing; ensure no selfing, not even randomly. thus s1==s2
				s2=this.fmh.get(random.nextInt(this.fmh.size()));
				while(s1==s2) s2=this.fmh.get(random.nextInt(this.fmh.size()));
			}
		}
		else throw new IllegalArgumentException("unknown sex "+s1.getSex());




		MatePair mp=new MatePair(s1,s2);
		return mp;

	}

}
