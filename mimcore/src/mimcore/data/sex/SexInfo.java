package mimcore.data.sex;

import com.sun.javaws.exceptions.InvalidArgumentException;
import mimcore.data.Chromosome;
import mimcore.data.haplotypes.SNPCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 14/01/2018.
 */
public class SexInfo {
    private final double selfingRate;
    private final ISexAssigner sexAssignerDefault;
    private final HashMap<Sex,HashSet<Chromosome>> hemizygousChromosomes;
    private HashMap<Sex,ArrayList<Integer>> sexchrsites;
    public static SexInfo getDefaultSexInfo()
    {
        // Default:
        // 100% hermaphrodites;
        // min count=1;
        // selfing rate =0.0
        return new SexInfo(new SexAssignerFraction(0.0,0.0,1.0,1),0.0,new HashMap<Sex,HashSet<Chromosome>>());
    }

    public SexInfo(ISexAssigner sexAssigner, double selfingRate, HashMap<Sex,HashSet<Chromosome>> hemizygousChromosomes)
    {

        this.sexAssignerDefault=sexAssigner;
        this.selfingRate=selfingRate;
        if(selfingRate<0.0) throw new IllegalArgumentException("Selfing rate must not be smaller than zero");
        if(selfingRate>1.0) throw new IllegalArgumentException("Selfing rate can not exceed 1.0");
        this.hemizygousChromosomes=hemizygousChromosomes;
        sexchrsites=null;
    }


    public ISexAssigner getSexAssigner(){return this.sexAssignerDefault;}
    public double getSelfingRate(){return this.selfingRate;}

    public HashMap<Sex,HashSet<Chromosome>> getHemizygousChromosomes(){return this.hemizygousChromosomes;}

    public void setHemizygousSite(SNPCollection snpcoll)
    {
        HashMap<Sex,ArrayList<Integer>> tostore=new HashMap<Sex,ArrayList<Integer>>();
        Sex[] sexes={Sex.Female,Sex.Male,Sex.Hermaphrodite};
        for(Sex s:sexes)
        {
            if(hemizygousChromosomes.containsKey(s))
            {
                HashSet<Chromosome> chrs=hemizygousChromosomes.get(s);
                ArrayList<Integer> sites=snpcoll.getIndexListForChromosomes(chrs);
                tostore.put(s,sites);
            }
            else tostore.put(s,new ArrayList<Integer>());
        }
        this.sexchrsites=tostore;
    }


    public ArrayList<Integer> getHemizygousSites(Sex sex)
    {
        if(this.sexchrsites==null) throw new IllegalArgumentException("Sex chromosome sites have not been set; set before usage");
        return new ArrayList<Integer>(this.sexchrsites.get(sex));
    }

}
