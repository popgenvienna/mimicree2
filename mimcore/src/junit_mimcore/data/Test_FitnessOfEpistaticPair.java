package junit_mimcore.data;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.gpf.fitness.FitnessOfEpistasisPair;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by robertkofler on 30/10/2017.
 */
public class Test_FitnessOfEpistaticPair {
    @Test
    public void correct_positions() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        assertEquals(f.getPosition_1().chromosome().toString(),"2L");
        assertEquals(f.getPosition_1().position(),1);
        assertEquals(f.getPosition_2().chromosome().toString(),"2R");
        assertEquals(f.getPosition_2().position(),2);
    }

    @Test
    public void correct_fitness_homozygous_locusa() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        char[] geno1={'A','A'};
        char[] geno2={'C','C'};

        // AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
        // {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.1,0.00000001);

        geno2[0]='C'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.2,0.00000001);
        geno2[0]='G'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.3,0.00000001);
    }

    @Test
    public void correct_fitness_heterozygous_locusa() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        char[] geno1={'A','T'};
        char[] geno2={'C','C'};

        // aabb  aabB  aaBB   aAbb  aAbB  aABB  AAbb  AAbB  AABB
        // AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
        // {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.4,0.00000001);

        geno2[0]='C'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.5,0.00000001);
        geno2[0]='G'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.6,0.00000001);
    }

    @Test
    public void correct_fitness_homozygous_locusA() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        char[] geno1={'T','T'};
        char[] geno2={'C','C'};

        // AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
        // {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.7,0.00000001);

        geno2[0]='C'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.8,0.00000001);
        geno2[0]='G'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.9,0.00000001);
    }

    @Test
    public void correct_fitness_heterozygous2_locusa() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        char[] geno1={'T','A'};
        char[] geno2={'C','C'};

        // aabb  aabB  aaBB   aAbb  aAbB  aABB  AAbb  AAbB  AABB
        // AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
        // {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.4,0.00000001);

        geno2[0]='C'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.5,0.00000001);
        geno2[0]='G'; geno2[1]='G';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.6,0.00000001);
    }

    @Test
    public void correct_fitness_heterozygous_locusb() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        char[] geno1={'A','A'};
        char[] geno2={'G','C'};

        // aabb  aabB  aaBB   aAbb  aAbB  aABB  AAbb  AAbB  AABB
        // AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
        // {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.2,0.00000001);

        geno1[0]='A'; geno1[1]='T';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.5,0.00000001);
        geno1[0]='T'; geno1[1]='T';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.8,0.00000001);
    }

    @Test
    public void correct_fitness_heterozygous2_locusb() {
        GenomicPosition pos1=new GenomicPosition(Chromosome.getChromosome("2L"),1);
        GenomicPosition pos2=new GenomicPosition(Chromosome.getChromosome("2R"),2);

        double[] epifit={0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        FitnessOfEpistasisPair f=new FitnessOfEpistasisPair(pos1,pos2,'A','T','C','G',epifit);

        char[] geno1={'A','A'};
        char[] geno2={'C','G'};

        // aabb  aabB  aaBB   aAbb  aAbB  aABB  AAbb  AAbB  AABB
        // AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
        // {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.2,0.00000001);

        geno1[0]='A'; geno1[1]='T';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.5,0.00000001);
        geno1[0]='T'; geno1[1]='T';
        assertEquals(f.getEffectSizeOfGenotype(geno1,geno2),0.8,0.00000001);
    }







}
