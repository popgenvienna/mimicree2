package mimcore.data.migration;

import mimcore.data.DiploidGenome;
import mimcore.data.SexedDiploids;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexInfo;

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
	private final SexedDiploids defaultSourcePopulation;
	private final ISexAssigner defaultSexAssigner;
	private final SNPCollection controlCollection; // all SNPs of the migration file must match this collection!
	private final Logger logger;
	private final boolean haploid;
	private final boolean clonal;



	public MigrationRegime(HashMap<Integer, MigrationEntry> input, SexedDiploids defaultSourcePopulation, ISexAssigner defaultSexAssigner, boolean haploid, boolean clonal, Logger logger) {
		sr = new HashMap<Integer,MigrationEntry>(input);
		this.logger=logger;
		this.defaultSourcePopulation=defaultSourcePopulation;
		if(defaultSourcePopulation.size()<1) throw new IllegalArgumentException("Size of default source population must be larger than zero");
		this.controlCollection=defaultSourcePopulation.getDiploids().get(0).getHaplotypeA().getSNPCollection();
		this.haploid=haploid;
		this.clonal=clonal;
		this.defaultSexAssigner=defaultSexAssigner;
	}

	public MigrationRegime(HashMap<Integer, MigrationEntry> input, SNPCollection controlCollection,  boolean haploid, boolean clonal,  Logger logger) {
		sr = new HashMap<Integer,MigrationEntry>(input);
		this.logger=logger;
		defaultSexAssigner= SexInfo.getDefaultSexInfo().getSexAssigner();
		this.haploid=haploid;
		this.clonal=clonal;
		this.defaultSourcePopulation=new SexedDiploids(new ArrayList<DiploidGenome>(),new ArrayList<Sex>());
		this.controlCollection=controlCollection;

	}


	/**
	 * Return the migration rate (eg.: replace 50% of the evolved population by the base population
	 * Last migration rate will be used for all generations larger than the last entry
	 * @param generation
	 * @return
	 */
	public SexedDiploids getMigrants(int generation, int replicate)
	{
		if(generation<1)throw new IllegalArgumentException("No generations can be smaller 1");
		if(sr.containsKey(generation))
		{
			MigrationEntry me=this.sr.get(generation);
			if(me.getMigrantCount()==0) return SexedDiploids.getEmptySet();
			if(me.useDefault())
			{
				return this.defaultSourcePopulation.getRandomSubset(me.getMigrantCount());

			}
			else
			{
				SexedDiploids fileContent=readDiploidGenomes(me.getPathToSourcePopulation());
				return fileContent.getRandomSubset(me.getMigrantCount());
			}

		}
		else
		{
			return SexedDiploids.getEmptySet();
		}

	}


	private SexedDiploids readDiploidGenomes(String path)
	{
		this.logger.info("Loading potential migrants from file "+path);
		if(! new File(path).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+path);

		SexedDiploids dipGenomes=new mimcore.io.DiploidGenomeReader(path,this.defaultSexAssigner,haploid,this.logger).readGenomes();
		if(dipGenomes.size()<1) throw new IllegalArgumentException("Invalid input of migrant population; Number of diploid genomes must be larger than zero; file="+path);
		this.logger.info("Successfully loaded "+dipGenomes.size()+ " potential migrants");

		// validate
		validateDiploidGenomes(dipGenomes.getDiploids());
		if(clonal && dipGenomes.countFemales()>0) throw new IllegalArgumentException("No females allowed for simulations of clonal evolution; solely hermaphrodites or no-sex");
		if(clonal && dipGenomes.countMales()>0) throw new IllegalArgumentException("No males allowed for simulations of clonal evolution; solely hermaphrodites or no-sex");
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

