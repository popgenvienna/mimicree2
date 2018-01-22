package mimcore.data.sex;

import mimcore.data.Chromosome;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 14/01/2018.
 */
public class SexInfo {
    private final double selfingRate;
    private final ISexAssigner sexAssignerDefault;
    private final HashMap<Sex,HashSet<Chromosome>> hemizygousChromosomes;
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
    }


    public ISexAssigner getSexAssigner(){return this.sexAssignerDefault;}
    public double getSelfingRate(){return this.selfingRate;}

    public HashMap<Sex,HashSet<Chromosome>> getHemizygousChromosomes(){return this.hemizygousChromosomes;}

}
