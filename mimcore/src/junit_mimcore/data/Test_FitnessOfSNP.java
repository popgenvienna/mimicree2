package junit_mimcore.data;

import junit_mimcore.factories.SharedFactory;
import junit_mimcore.factories.WDataFactory;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.sex.Sex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by robertkofler on 30/10/2017.
 */
public class Test_FitnessOfSNP {
    @Test
    public void fitness_of_additiveSNP() {
        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        FitnessOfSNP f=new FitnessOfSNP(pos,'A','C',0.9,1.0,1.1);
        Sex m= Sex.Male;

        char[] geno={'A','A'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),0.9,0.000001);

       char[] geno1={'A','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),1.0,0.000001);

        char[] geno2={'C','A'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),1.0,0.000001);


        char[] geno3={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),1.1,0.000001);

    }


    @Test
    public void fitness_of_dominantSNP() {


        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        FitnessOfSNP f=new FitnessOfSNP(pos,'C','T',0.8,1.1,1.1);
        Sex m= Sex.Male;

        char[] geno={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),0.8,0.000001);

        char[] geno1={'C','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),1.1,0.000001);

        char[] geno2={'T','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),1.1,0.000001);


        char[] geno3={'T','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),1.1,0.000001);

    }

    @Test
    public void fitness_of_recessiveSNP() {


        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        FitnessOfSNP f=new FitnessOfSNP(pos,'C','T',0.8,0.8,1.1);
        Sex m= Sex.Male;

        char[] geno={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),0.8,0.000001);

        char[] geno1={'C','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),0.8,0.000001);

        char[] geno2={'T','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),0.8,0.000001);


        char[] geno3={'T','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),1.1,0.000001);

    }

    @Test
    public void fitness_of_overdominantSNP() {


        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        FitnessOfSNP f=new FitnessOfSNP(pos,'C','T',0.8,1.2,0.8);
        Sex m= Sex.Male;

        char[] geno={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),0.8,0.000001);

        char[] geno1={'C','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),1.2,0.000001);

        char[] geno2={'T','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),1.2,0.000001);


        char[] geno3={'T','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),0.8,0.000001);

    }

    @Test
    public void fitness_of_underdominantSNP() {


        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        FitnessOfSNP f=new FitnessOfSNP(pos,'C','T',1.2,0.8,1.2);
        Sex m= Sex.Male;

        char[] geno={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),1.2,0.000001);

        char[] geno1={'C','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),0.8,0.000001);

        char[] geno2={'T','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),0.8,0.000001);


        char[] geno3={'T','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),1.2,0.000001);

    }




}
