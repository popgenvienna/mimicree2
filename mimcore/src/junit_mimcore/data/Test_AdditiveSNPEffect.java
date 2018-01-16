package junit_mimcore.data;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.gpf.quantitative.AdditiveSNPeffect;
import mimcore.data.sex.Sex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by robertkofler on 30/10/2017.
 */
public class Test_AdditiveSNPEffect {
    @Test
    public void effect_of_overdominant_SNP() {
        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        AdditiveSNPeffect f=new AdditiveSNPeffect(pos,'A','C',0.9,1.0);
        Sex m=Sex.Male;

        char[] geno={'A','A'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),0.9,0.000001);

       char[] geno1={'A','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),1.0,0.000001);

        char[] geno2={'C','A'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),1.0,0.000001);


        char[] geno3={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),-0.9,0.000001);

    }


    @Test
    public void effect_of_additive_SNP() {

        Sex m=Sex.Male;
        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        AdditiveSNPeffect f=new AdditiveSNPeffect(pos,'C','T',2.0,0);

        char[] geno={'C','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno,m),2.0,0.000001);

        char[] geno1={'C','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno1,m),0,0.000001);

        char[] geno2={'T','C'};
        assertEquals(f.getEffectSizeOfGenotype(geno2,m),0,0.000001);


        char[] geno3={'T','T'};
        assertEquals(f.getEffectSizeOfGenotype(geno3,m),-2.0,0.000001);

    }

    @Test
    public void position_of_SNP() {


        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        AdditiveSNPeffect f=new AdditiveSNPeffect(pos,'C','T',2.0,0);


        assertEquals(f.getPosition().chromosome().toString(),"2R");
        assertEquals(f.getPosition().position(),1);


    }

    @Test
    public void polymorphism_of_SNP() {


        GenomicPosition pos=new GenomicPosition(Chromosome.getChromosome("2R"),1);
        AdditiveSNPeffect f=new AdditiveSNPeffect(pos,'C','T',2.0,0);


        assertEquals(f.achar(),'C');
        assertEquals(f.altchar(),'T');


    }





}
