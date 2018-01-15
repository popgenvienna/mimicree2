package mimcore.data.sex;

/**
 * Created by robertkofler on 14/01/2018.
 */
public class SexInfo {
    private final double selfingRate;
    private final SexAssigner sexAssigner;
    public static SexInfo getDefaultSexInfo()
    {
        // Default:
        // 100% hermaphrodites;
        // min count=1;
        // selfing rate =0.0
        return new SexInfo(new SexAssigner(0.0,0.0,1.0,1),0.0);
    }

    public SexInfo(SexAssigner sexAssigner, double selfingRate)
    {
        this.sexAssigner=sexAssigner;
        this.selfingRate=selfingRate;
        if(selfingRate<0.0) throw new IllegalArgumentException("Selfing rate must not be smaller than zero");
        if(selfingRate>1.0) throw new IllegalArgumentException("Selfing rate can not exceed 1.0");
    }

    public SexAssigner getSexAssigner(){return this.sexAssigner;}
    public double getSelfingRate(){return this.selfingRate;}


}
