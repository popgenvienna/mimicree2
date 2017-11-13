package mimcore.data.migration;

import mimcore.data.DiploidGenome;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom; // no idea if this is a good idea
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/4/14.
 * NOTE used ThreadLocalRandom to generate random numbers
 */
public class MigrationRegime implements IMigrationRegime {
	private final HashMap<Integer,MigrationEntry> sr;
	private final ArrayList<DiploidGenome> defaultSourcePopulation;
	private final SNPCollection controlCollection; // all SNPs of the migration file must match this collection!
	private final Logger logger;



	public MigrationRegime(HashMap<Integer, MigrationEntry> input, ArrayList<DiploidGenome> defaultSourcePopulation, Logger logger) {
		sr = new HashMap<Integer,MigrationEntry>(input);
		this.logger=logger;
		this.defaultSourcePopulation=new ArrayList<DiploidGenome>(defaultSourcePopulation);
		if(defaultSourcePopulation.size()<1) throw new IllegalArgumentException("Size of default source population must be larger than zero");
		this.controlCollection=defaultSourcePopulation.get(0).getHaplotypeA().getSNPCollection();
	}

	public MigrationRegime(HashMap<Integer, MigrationEntry> input, SNPCollection controlCollection, Logger logger) {
		sr = new HashMap<Integer,MigrationEntry>(input);
		this.logger=logger;
		this.defaultSourcePopulation=new ArrayList<DiploidGenome>();
		this.controlCollection=controlCollection;
	}


	/**
	 * Return the migration rate (eg.: replace 50% of the evolved population by the base population
	 * Last migration rate will be used for all generations larger than the last entry
	 * @param generation
	 * @return
	 */
	public ArrayList<DiploidGenome> getMigrants(int generation, int replicate)
	{
		if(generation<1)throw new IllegalArgumentException("No generations can be smaller 1");
		if(sr.containsKey(generation))
		{
			MigrationEntry me=this.sr.get(generation);
			if(me.useDefault())
			{
				return getMigrants(this.defaultSourcePopulation,me.getMigrantCount());
			}
			else
			{
				ArrayList<DiploidGenome> fileContent=readDiploidGenomes(me.getPathToSourcePopulation());
				return getMigrants(fileContent,me.getMigrantCount());
			}

		}
		else
		{
			return new ArrayList<DiploidGenome>();
		}

	}


	private ArrayList<DiploidGenome> getMigrants(ArrayList<DiploidGenome> genomes, int migrantCount)
	{
		if(migrantCount>genomes.size()) throw new IllegalArgumentException("Can not get migrants; Requested migrant count larger than the size of the source population");
		LinkedList<DiploidGenome> potentialMigrants=new LinkedList<DiploidGenome>(genomes);


		ArrayList<DiploidGenome> toret=new ArrayList<DiploidGenome>();

		for(int i=0; i<migrantCount; i++)
		{
			// get random index using ThreadLocalRandom
			int targetindex=ThreadLocalRandom.current().nextInt(potentialMigrants.size());

			DiploidGenome genome=potentialMigrants.remove(targetindex);
			toret.add(genome);
		}
		return toret;


	}

	private ArrayList<DiploidGenome> readDiploidGenomes(String path)
	{
		this.logger.info("Loading potential migrants from file "+path);
		if(! new File(path).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+path);

		ArrayList<DiploidGenome> dipGenomes=new mimcore.io.DiploidGenomeReader(path,this.logger).readGenomes();
		if(dipGenomes.size()<1) throw new IllegalArgumentException("Invalid input of migrant population; Number of diploid genomes must be larger than zero; file="+path);
		this.logger.info("Successfully loaded "+dipGenomes.size()+ " potential migrants");
		validateDiploidGenomes(dipGenomes);
		return dipGenomes;
	}

	/**
	 * Check if migrant populatin is valid
	 * ie. has the same SNPs as the base population
	 * @param genomes
	 * @return
	 */
	private boolean validateDiploidGenomes(ArrayList<DiploidGenome> genomes)
	{
		this.logger.info("Testing validity of migrant population (ie. if migrant population has the same SNPs as the base popualtion)");

		SNPCollection tocompare=genomes.get(0).getHaplotypeA().getSNPCollection();

		if(tocompare.size()!=this.controlCollection.size())throw new IllegalArgumentException("Invalid migrant population; Migrant population has different SNPs than the base population; size "+tocompare.size()+" vs "+controlCollection.size());
		for(int i=0; i<tocompare.size(); i++)
		{
			SNP valid=this.controlCollection.getSNPforIndex(i);
			SNP check=tocompare.getSNPforIndex(i);
			if(!valid.equals(check)) throw new IllegalArgumentException("Invalid migrant population; Migrant population has different SNPs than the base population "+valid.toString()+ " vs "+check.toString());

		}
		this.logger.info("Finished; Migrant population is valid");
		return true;
	}

	public HashMap<Integer,MigrationEntry> getMigrationEntries()
	{
		return new HashMap<Integer,MigrationEntry>(this.sr);
	}


}

