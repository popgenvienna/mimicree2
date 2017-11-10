package junit_mimcore.data.basic;

import mimcore.data.BitArray.BitArray;
import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNP;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class Test_SNP {

	@Test
	public void equals() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		assertTrue(a.equals(b));
	}


	@Test
	public void not_equals_chromosome() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','C','T');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		assertFalse(a.equals(b));
	}

	@Test
	public void not_equals_position() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),2),'A','C','T');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		assertFalse(a.equals(b));
	}

	@Test
	public void not_equals_refchar() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'T','C','T');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		assertFalse(a.equals(b));
	}

	@Test
	public void not_equals_ancestral() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','T','T');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		assertFalse(a.equals(b));
	}

	@Test
	public void not_equals_derived() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','G');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),1),'A','C','T');
		assertFalse(a.equals(b));
	}

	@Test
	public void not_equals_everything() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'C','C','A');
		SNP b=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),3),'A','G','T');
		assertFalse(a.equals(b));
	}


	@Test
	public void position_and_characters() {
		SNP a=new SNP(new GenomicPosition(Chromosome.getChromosome("X"),13434),'A','C','T');

		assertEquals(a.genomicPosition().chromosome().toString(),"X");
		assertEquals(a.genomicPosition().position(),13434);
		assertEquals(a.referenceCharacter(),'A');
		assertEquals(a.ancestralAllele(),'C');
		assertEquals(a.derivedAllele(),'T');
	}



}
