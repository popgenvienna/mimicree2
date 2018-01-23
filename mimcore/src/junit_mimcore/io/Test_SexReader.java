package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.Chromosome;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerFraction;
import mimcore.data.sex.SexInfo;
import mimcore.io.PopulationSizeReader;
import mimcore.io.SexReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Test_SexReader {


	

	public static SexInfo getSexinfo()
	{
		String input=
						"M=0.5"+
								"   # comment bla\n"+
						"F=0.4\n"+
						"H=0.1  # comment\n"+
						"SR=0.6\n"+
						"MC=2\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SexReader("fakefile",br, SharedFactory.getNullLogger()).readSexInfo();

	}

	public static SexInfo getSexinfoHemizygous()
	{
		String input=
						"M=0.5"+
						"   # comment bla\n"+
						"F=0.4\n"+
						"H=0.1  # comment\n"+
						"SR=0.6\n"+
						"MC=2\n"+
				"HZ=M:2L,2R F:X H:3,4,5";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SexReader("fakefile",br, SharedFactory.getNullLogger()).readSexInfo();

	}

	@Test
	public void male_female_herma_ratio() {
		SexInfo si = getSexinfo();
		SexAssignerFraction sa=(SexAssignerFraction)si.getSexAssigner();
		assertEquals(sa.getMaleFraction(),0.5,0.0000001);
		assertEquals(sa.getFemaleFraction(),0.4,0.0000001);
		assertEquals(sa.getHermaphroditeFraction(),0.1,0.0000001);
		assertEquals(sa.getMinCount(),2);

	}

	@Test
	public void selfing_rate() {
		SexInfo si = getSexinfo();
		assertEquals(si.getSelfingRate(),0.6, 0.0000001);
	}

	@Test
	public void hemizygous_male() {
		//				"HZ=M:2L,2R F:X H:3,4,5";
		SexInfo si = getSexinfoHemizygous();
		HashMap<Sex,HashSet<Chromosome>> h=si.getHemizygousChromosomes();
		HashSet<Chromosome> chrs=h.get(Sex.Male);
		assertTrue(chrs.contains(Chromosome.getChromosome("2L")));
		assertTrue(chrs.contains(Chromosome.getChromosome("2R")));
		assertFalse(chrs.contains(Chromosome.getChromosome("X")));
		assertFalse(chrs.contains(Chromosome.getChromosome("3")));
		assertFalse(chrs.contains(Chromosome.getChromosome("4")));
		assertFalse(chrs.contains(Chromosome.getChromosome("5")));
	}


	@Test
	public void hemizygous_female() {
		//				"HZ=M:2L,2R F:X H:3,4,5";
		SexInfo si = getSexinfoHemizygous();
		HashMap<Sex,HashSet<Chromosome>> h=si.getHemizygousChromosomes();
		HashSet<Chromosome> chrs=h.get(Sex.Female);
		assertFalse(chrs.contains(Chromosome.getChromosome("2L")));
		assertFalse(chrs.contains(Chromosome.getChromosome("2R")));
		assertTrue(chrs.contains(Chromosome.getChromosome("X")));
		assertFalse(chrs.contains(Chromosome.getChromosome("3")));
		assertFalse(chrs.contains(Chromosome.getChromosome("4")));
		assertFalse(chrs.contains(Chromosome.getChromosome("5")));
	}


	@Test
	public void hemizygous_hermaphrodite() {
		//				"HZ=M:2L,2R F:X H:3,4,5";
		SexInfo si = getSexinfoHemizygous();
		HashMap<Sex,HashSet<Chromosome>> h=si.getHemizygousChromosomes();
		HashSet<Chromosome> chrs=h.get(Sex.Hermaphrodite);
		assertFalse(chrs.contains(Chromosome.getChromosome("2L")));
		assertFalse(chrs.contains(Chromosome.getChromosome("2R")));
		assertFalse(chrs.contains(Chromosome.getChromosome("X")));
		assertTrue(chrs.contains(Chromosome.getChromosome("3")));
		assertTrue(chrs.contains(Chromosome.getChromosome("4")));
		assertTrue(chrs.contains(Chromosome.getChromosome("5")));
	}





}
