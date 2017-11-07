package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.gpf.fitness.FitnessOfEpistasisPair;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.io.w.EpistasisFitnessReader;
import mimcore.io.w.SNPFitnessReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Test_EpistasisFitnessReader {


	

	public static EpistasisFitnessReader getReader()
	{
		String input=
								"2L\t1\tA/T\n"+
								"2R\t2\tC/G\n"+
								"0.1\t0.2\t0.3\t0.4\t0.5\t0.6\t0.7\t0.8\t0.9\n"+
								"3L\t3\tA/C\n"+
								"3R\t4\tC/G\n"+
								"1\t2\t3\t4\t5\t6\t7\t8\t9\n"+
								"4L\t5\tG/T\n"+
								"4R\t6\tC/G\n"+
								"0.9\t0.8\t0.7\t0.6\t0.5\t0.4\t0.3\t0.2\t0.1\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new EpistasisFitnessReader("fakefile",br, SharedFactory.getNullLogger());

	}
	
	@Test
	public void position_of_first_epistatic_pair() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		assertEquals(e.get(0).getPosition_1().chromosome().toString(), "2L");
		assertEquals(e.get(0).getPosition_2().chromosome().toString(), "2R");
		assertEquals(e.get(0).getPosition_1().position(), 1);
		assertEquals(e.get(0).getPosition_2().position(), 2);
	}


	@Test
	public void position_of_second_epistatic_pair() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		assertEquals(e.get(1).getPosition_1().chromosome().toString(), "3L");
		assertEquals(e.get(1).getPosition_2().chromosome().toString(), "3R");
		assertEquals(e.get(1).getPosition_1().position(), 3);
		assertEquals(e.get(1).getPosition_2().position(), 4);
	}

	@Test
	public void position_of_third_epistatic_pair() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		assertEquals(e.get(2).getPosition_1().chromosome().toString(), "4L");
		assertEquals(e.get(2).getPosition_2().chromosome().toString(), "4R");
		assertEquals(e.get(2).getPosition_1().position(), 5);
		assertEquals(e.get(2).getPosition_2().position(), 6);
	}

	@Test
	public void alleles_of_first_epistatic_pair() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		assertEquals(e.get(0).getachar(),'A');
		assertEquals(e.get(0).getAchar(),'T');
		assertEquals(e.get(0).getbchar(),'C');
		assertEquals(e.get(0).getBchar(),'G');
	}

	@Test
	public void alleles_of_third_epistatic_pair() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		assertEquals(e.get(2).getachar(),'G');
		assertEquals(e.get(2).getAchar(),'T');
		assertEquals(e.get(2).getbchar(),'C');
		assertEquals(e.get(2).getBchar(),'G');
	}

	@Test
	public void fitness_of_first_epistatic_pair_homoa() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		char[] geno1={'A','A'};
		char[] geno2={'C','C'};

		// AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
		// {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.1,0.00000001);

		geno2[0]='C'; geno2[1]='G';
		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.2,0.00000001);
		geno2[0]='G'; geno2[1]='G';
		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.3,0.00000001);
	}

	@Test
	public void fitness_of_first_epistatic_pair_heteroa() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		char[] geno1={'A','T'};
		char[] geno2={'C','C'};

		// AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
		// {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.4,0.00000001);

		geno2[0]='C'; geno2[1]='G';
		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.5,0.00000001);
		geno2[0]='G'; geno2[1]='G';
		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.6,0.00000001);
	}

	@Test
	public void fitness_of_first_epistatic_pair_homoA() {

		ArrayList<FitnessOfEpistasisPair> e = getReader().readEpistaticPairs().getEpistaticPairs();


		char[] geno1={'T','T'};
		char[] geno2={'C','C'};

		// AACC  AACG  AAGG   ATCC  ATCG  ATGG  TTCC  TTCG  TTGG
		// {0.1,  0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9};

		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.7,0.00000001);

		geno2[0]='C'; geno2[1]='G';
		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.8,0.00000001);
		geno2[0]='G'; geno2[1]='G';
		assertEquals(e.get(0).getEffectSizeOfGenotype(geno1,geno2),0.9,0.00000001);
	}


	

	

}
