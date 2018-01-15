package mimcore.data.recombination;

import mimcore.data.*;
import mimcore.data.sex.Sex;

import java.util.Random;
import java.util.ArrayList;

public class CrossoverGenerator {
	private final ArrayList<IRecombinationWindow> windows;


	
	public CrossoverGenerator(ArrayList<IRecombinationWindow> windows)
	{
		this.windows=new ArrayList<IRecombinationWindow>(windows);
	}

	
	/**
	 * Return a collection of randomly generated crossover events for the whole genome
	 * Note that this is only half of the recombination leading to a gamete
	 * the other half is the random assortment of chromosomes
	 * @return
	 */
	public CrossoverEvents generateCrossoverEvents(Random random, Sex sex)
	{
		ArrayList<GenomicPosition> recEvents=new ArrayList<GenomicPosition>();
		for(IRecombinationWindow window:windows)
		{
			int recs=window.getRecombinationEvents(sex,random);
			for(int i =0; i<recs; i++) recEvents.add(window.getRandomPosition(random));
			// if(window.hasRecombinationEvent(random)) recEvents.add(window.getRandomPosition(random));
		}
		return new CrossoverEvents(recEvents);
	}

	public int size()
	{
		return this.windows.size();
	}
	/**
	 * mostly for debugging
	 * @return
	 */
	public ArrayList<IRecombinationWindow> getWindows()
	{
		return new ArrayList<IRecombinationWindow>(this.windows);
	}

}



