package mimcore.data;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerDirect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by robertkofler on 22/01/2018.
 */
public class SexedDiploids {

    public static SexedDiploids getEmptySet()
    {
        SexedDiploids sd=new SexedDiploids(new ArrayList<DiploidGenome>(),new SexAssignerDirect(new ArrayList<Sex>()));
        return sd;
    }
    private final ArrayList<DiploidGenome> diploids;
    private final ISexAssigner sexAssigner;

    public SexedDiploids(ArrayList<DiploidGenome> diploidGenomes, ISexAssigner sexAssigner)
    {
        this.diploids=new ArrayList<DiploidGenome>(diploidGenomes);
        this.sexAssigner=sexAssigner;
    }

    public ArrayList<DiploidGenome> getDiploids(){return new ArrayList<DiploidGenome>(this.diploids);}
    public ISexAssigner getSexAssigner(){return sexAssigner;}

    public int size(){return this.diploids.size();}


    public SexedDiploids getRandomSubset(int targetSize)
    {
        if(targetSize>diploids.size()) throw new IllegalArgumentException("Can not get subset of population; Requested count larger than the size of the source population");
        Random r=ThreadLocalRandom.current();
        LinkedList<DiploidGenome> potentialMigrants=new LinkedList<DiploidGenome>(diploids);
        LinkedList<Sex> potentialSexes=new LinkedList<Sex>(sexAssigner.getSexes(diploids.size(),r));


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
        return new SexedDiploids(new ArrayList<DiploidGenome>(toretGens),new SexAssignerDirect(toretSexes));
    }
}
