package mimcore.data.sex;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robertkofler on 14/01/2018.
 */
public class SexAssignerDirect implements ISexAssigner {
        private final ArrayList<Sex> sexes;

    public SexAssignerDirect(ArrayList<Sex> sexes)
    {

        this.sexes=new ArrayList<Sex>(sexes);
    }


    /**
     * Test if the population is dying out because of lack of males or females
     * @return
     */
    public boolean isValid()
    {
        int males=0;
        int females=0;
        int herma=0;
        for(Sex s: sexes)
        {
            if(s==Sex.Female)females++;
            else if(s==Sex.Male)males++;
            else if(s==Sex.Hermaphrodite)herma++;
            else throw new IllegalArgumentException("Unknown sex "+s);
        }
        if(females==0 &&herma==0) return false;
        if(males==0 &&herma==0) return false;
        return true;
    }



    public ArrayList<Sex> getSexes(int popSize, Random random)
    {
        if(popSize!=this.sexes.size()) throw new IllegalArgumentException("Can not generate sex list for the requested population size "+popSize);
        return new ArrayList<Sex>(this.sexes);
    }

    public int size()
    {
        return this.sexes.size();
    }



}
