package qmimcore.data.recombination;

import qmimcore.data.*;

import java.util.Random;


/**
 * Summarizes the recombination rate and the random assortment of chromosomes
 * @author robertkofler
 */
public class RecombinationGenerator {
	private final CrossoverGenerator recRate;
	private final RandomAssortmentGenerator assortGen;
	
	public RecombinationGenerator(CrossoverGenerator recRate, RandomAssortmentGenerator assortGen)
	{
		this.recRate=recRate;
		this.assortGen=assortGen;
	}	
	
	/**
	 * Obtain a new randomly generated recombination event, including crossovers and random assortments
	 * This method will return a new random recombination event upon each call
	 * @return
	 */
	public RecombinationEvent getRecombinationEvent(Random random)
	{
		CrossoverEvents crossovers=recRate.generateCrossoverEvents(random);
		RandomAssortment randas=assortGen.getRandomAssortment(crossovers,random);
		return new RecombinationEvent(randas,crossovers);
	}
	
	

}
