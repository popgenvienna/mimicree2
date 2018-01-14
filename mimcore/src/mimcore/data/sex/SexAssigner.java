package mimcore.data.sex;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robertkofler on 14/01/2018.
 */
public class SexAssigner {
    private final double maleFraction;
    private final double femaleFraction;
    private final double hermaphroditeFraction;
    private final int minCount;


    public SexAssigner(double maleFraction, double femaleFraction, double hermaphroditeFraction, int minCount)
    {
        this.maleFraction=maleFraction;
        this.femaleFraction=femaleFraction;
        this.hermaphroditeFraction=hermaphroditeFraction;
        this.minCount=minCount;
        if(maleFraction<0.0 || maleFraction>1.0) throw new IllegalArgumentException("Fraction of males must be between 0.0 and 1.0");
        if(femaleFraction<0.0 || femaleFraction>1.0) throw new IllegalArgumentException("Fraction of females must be between 0.0 and 1.0");
        if(hermaphroditeFraction<0.0 || hermaphroditeFraction>1.0) throw new IllegalArgumentException("Fraction of hermaphroditeFraction must be between 0.0 and 1.0");


        double sum=maleFraction+femaleFraction+hermaphroditeFraction;
        if(Math.abs(sum-1.0)>0.000000001) throw new IllegalArgumentException("Sum of sex-fractions must be one");
        if(Math.abs(maleFraction-1.0)<0.000000001) throw new IllegalArgumentException("A population of just males, seriously?");
        if(Math.abs(femaleFraction-1.0)<0.000000001) throw new IllegalArgumentException("A population of just females, seriously?");
        if(minCount<1) throw new IllegalArgumentException("To prevent extinction of populations (e.g. just males); a minimum sex count must be larger than zero");

    }



    public ArrayList<Sex> getSexes(int popSize, Random random)
    {
        ArrayList<Sex> sexlist=initializewithMinCount();
        if(sexlist.size()>popSize) throw new IllegalArgumentException("The requested population size is too small; Can not guarantee minimum count of sexes; N="+popSize);


        double maleThreshold=maleFraction;
        double femaleThreshold=maleFraction+femaleFraction;
        double hermaphroditeThreshold=maleFraction+femaleFraction+hermaphroditeFraction;
        while(sexlist.size()<popSize)
        {
            double r=random.nextDouble();
            if(r<maleThreshold) sexlist.add(Sex.Male);
            else if(r<femaleThreshold) sexlist.add(Sex.Female);
            else if(r<hermaphroditeThreshold) sexlist.add(Sex.Hermaphrodite);
            else throw new IllegalArgumentException("Fatal error during sex assignment; random number higher than threshold "+r+" > "+hermaphroditeThreshold);
        }

        return sexlist;
    }

    private ArrayList<Sex> initializewithMinCount()
    {
        ArrayList<Sex> sexlist=new ArrayList<Sex>();
        if(maleFraction>0.0) for(int i=0; i<minCount; i++) sexlist.add(Sex.Male);
        if(femaleFraction>0.0) for(int i=0; i<minCount; i++) sexlist.add(Sex.Female);
        if(hermaphroditeFraction>0.0) for(int i=0; i<minCount; i++) sexlist.add(Sex.Hermaphrodite);
        return sexlist;
    }

}
