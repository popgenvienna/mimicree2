package mimcore.io;

import mimcore.data.*;
import mimcore.data.recombination.*;

import java.util.ArrayList;
import java.util.HashSet;

/** 
 * Reads a given chromosome definition and creates a RandomAssortmentGenerator
 * @author robertkofler
 *
 */
public class ChromosomeDefinitionReader {
	
	private String chromosomeDefinition;
	
	public ChromosomeDefinitionReader(String chromosomeDefinition)
	{
	
		this.chromosomeDefinition=chromosomeDefinition;
	}
	
	
	public RandomAssortmentGenerator getRandomAssortmentGenerator()
	{
		ArrayList<ArrayList<Chromosome>> chroms=initializeChromosomeSet();	
		HashSet<Chromosome> alreadyDescribed = alreadyDescribed(chroms); 
		
		for(Chromosome chr: Chromosome.getChromosome())
		{
			if(!alreadyDescribed.contains(chr))
			{
				ArrayList<Chromosome> sar=new ArrayList<Chromosome>();
				sar.add(chr);
				chroms.add(sar);
			}
		}
		
		return new RandomAssortmentGenerator(chroms);
	}
	
	
	private HashSet<Chromosome> alreadyDescribed(ArrayList<ArrayList<Chromosome>> chroms)
	{
		HashSet<Chromosome> toret=new HashSet<Chromosome>();
		for(ArrayList<Chromosome> list:chroms)
		{
			for(Chromosome chr: list)
			{
				toret.add(chr);
			}
		}
		return toret;
	}
	
	/**
	 * Get a ArrayList of linked chromosomes ((2L,2R),(3L,3R))
	 * @return
	 */
	private ArrayList<ArrayList<Chromosome>> initializeChromosomeSet()
	{
		ArrayList<ArrayList<Chromosome>> chroms=new ArrayList<ArrayList<Chromosome>>();
		if(this.chromosomeDefinition.contains("="))
		{
			// if not containing a '=' return an empty list
			
			//Split into individual linkage definitions '2=2L+2R'
			String[] indiDef=getIndividualDefinition(this.chromosomeDefinition);
			for(String idef:indiDef)
			{
				ArrayList<Chromosome> linkedChrs=parseSingleEntry(idef);
				chroms.add(linkedChrs);
			}
		}
		return chroms;
	}
	
	
	/**
	 * Parset the following '2=2L+2R into  a ArrayList of linked chromosomes eg.: (2L,2R)
	 * @param s
	 * @return
	 */
	private ArrayList<Chromosome> parseSingleEntry(String s)
	{
		String[] t=s.split("=");
		String[] chrs=t[1].split("\\+");
		ArrayList<Chromosome> toret=new ArrayList<Chromosome>();
		for(String tmp: chrs)
		{
			toret.add(Chromosome.getChromosome(tmp));
		}
		return toret;
	}
	
	/**
	 * Get a collection of individual definitions eg: "2=2L+2R", "3=3L+3R"
	 * @param s
	 * @return
	 */
	private String[] getIndividualDefinition(String s)
	{
		String[] toret;
		if(s.contains(","))
		{
			toret=s.split(",");
		}
		else
		{
			toret = new String[1];
			toret[0]=s;
		}
		return toret;
	}
	

}
