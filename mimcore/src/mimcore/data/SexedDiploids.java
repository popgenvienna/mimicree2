package mimcore.data;

import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.DiploidGenome;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerDirect;
import mimcore.data.sex.SexInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 22/01/2018.
 */
public class SexedDiploids {

    public static SexedDiploids getEmptySet()
    {
        SexedDiploids sd=new SexedDiploids(new ArrayList<DiploidGenome>(),new ArrayList<Sex>());
        return sd;
    }
    private final ArrayList<DiploidGenome> diploids;
    private final ArrayList<Sex> sexes;

    public SexedDiploids(ArrayList<DiploidGenome> diploidGenomes, ArrayList<Sex> sexes)
    {
        if(diploidGenomes.size()!=sexes.size()) throw new IllegalArgumentException("Size of sexes and genomes must agree");
        this.diploids=new ArrayList<DiploidGenome>(diploidGenomes);
        this.sexes=new ArrayList<Sex>(sexes);
    }

    public ArrayList<DiploidGenome> getDiploids(){return new ArrayList<DiploidGenome>(this.diploids);}
    public ArrayList<Sex> getSexAssigner(){return new ArrayList<Sex>(sexes);}

    public int size(){return this.diploids.size();}


    public SexedDiploids getRandomSubset(int targetSize)
    {
        if(targetSize>diploids.size()) throw new IllegalArgumentException("Can not get subset of population; Requested count larger than the size of the source population");

        LinkedList<DiploidGenome> potentialMigrants=new LinkedList<DiploidGenome>(diploids);
        LinkedList<Sex> potentialSexes=new LinkedList<Sex>(this.sexes);

        Random r=ThreadLocalRandom.current();
        ArrayList<DiploidGenome> toretGens=new ArrayList<DiploidGenome>();
        ArrayList<Sex> toretSexes=new ArrayList<Sex>();

        for(int i=0; i<targetSize; i++)
        {
            // get random index using ThreadLocalRandom
            int targetindex= r.nextInt(potentialMigrants.size());

            DiploidGenome genome=potentialMigrants.remove(targetindex);
            Sex sex=potentialSexes.remove(targetindex);
            toretGens.add(genome);
            toretSexes.add(sex);
        }
        return new SexedDiploids(new ArrayList<DiploidGenome>(toretGens),toretSexes);
    }

    public SexedDiploids updateSexChromosome(SexInfo si, Logger logger)
    {
        ArrayList<DiploidGenome> toret=new ArrayList<DiploidGenome>();
        logger.info("Updating sex chromosomes");
        for(int i=0; i<this.diploids.size(); i++)
        {
            DiploidGenome dg=this.diploids.get(i);
            Sex sex=this.sexes.get(i);
            HaploidGenome hg=dg.getHaplotypeA();
            BitArrayBuilder raw=dg.getHaplotypeB().getRawGenome().getBitArrayBuilder();
            ArrayList<Integer> sites=si.getHemizygousSites(sex);

            for(int k: sites)
            {
                boolean bab1hasit=hg.hasAncestral(k);
                boolean bab2hasit= raw.hasBit(k);
                if(bab1hasit!=bab2hasit) raw.flipBit(k);
            }
            HaploidGenome sexupdated=new HaploidGenome(raw.getBitArray(),hg.getSNPCollection());
            toret.add(new DiploidGenome(hg,sexupdated));
        }
        return new SexedDiploids(toret,sexes);
    }
}
