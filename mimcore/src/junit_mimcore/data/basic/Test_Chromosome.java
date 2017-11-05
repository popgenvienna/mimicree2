package junit_mimcore.data.basic;

import mimcore.data.Chromosome;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;


public class Test_Chromosome {

	@Test
	public void one_chromosome_found() {
		Chromosome.resetChromosomes();
		Chromosome c= Chromosome.getChromosome("2L");
		assertEquals(c.toString(),"2L");
	}
	
	@Test
	public void two_chromosomes_found() {
		Chromosome.resetChromosomes();
		Chromosome c1= Chromosome.getChromosome("2L");
		Chromosome c2= Chromosome.getChromosome("2R");
		assertEquals(c2.toString(),"2R");
	}
	
	@Test
	public void chromosome_collection_complete() {
		Chromosome.resetChromosomes();
		Chromosome.getChromosome("2L");
		Chromosome.getChromosome("2R");
		Chromosome.getChromosome("3L");
		
		ArrayList<Chromosome> chrs= Chromosome.getChromosome();
		HashSet<Chromosome> h=new HashSet<Chromosome>(chrs);
		assertEquals(chrs.size(),3);
		assertTrue(h.contains(Chromosome.getChromosome("2L")));
		assertTrue(h.contains(Chromosome.getChromosome("2R")));
		assertTrue(h.contains(Chromosome.getChromosome("3L")));
	}
	
	@Test
	public void no_influence_of_default_chromosome() {
		Chromosome.resetChromosomes();
		Chromosome.getChromosome("2L");
		Chromosome.getChromosome("2R");
		Chromosome.getChromosome("3L");
		Chromosome.getDefaultChromosome();
		
		ArrayList<Chromosome> chrs= Chromosome.getChromosome();
		HashSet<Chromosome> h=new HashSet<Chromosome>(chrs);
		assertEquals(chrs.size(),3);
		assertTrue(h.contains(Chromosome.getChromosome("2L")));
		assertTrue(h.contains(Chromosome.getChromosome("2R")));
		assertTrue(h.contains(Chromosome.getChromosome("3L")));
	}

}
