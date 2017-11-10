package junit_mimcore.data.basic;

import junit_mimcore.factories.GenomicDataFactory;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class Test_SNPCollection {
	
	

	

	@Test
	public void test_index() {
		ArrayList<SNP> snps= GenomicDataFactory.getSNPCollection();
		SNPCollection s=new SNPCollection(snps);
		assertEquals(s.size(),12);
		int index;
		
		index=s.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2R"),13));
		assertEquals(index,11);
		index=s.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2L"),2));
		assertEquals(index,0);
		index=s.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2R"),11));
		assertEquals(index,5);		
	}
	
	@Test
	public void test_get_SNP_for_position() {
		ArrayList<SNP> snps=GenomicDataFactory.getSNPCollection();
		SNPCollection c=new SNPCollection(snps);
		assertEquals(c.size(),12);
		
		SNP s;
		s=c.getSNPforPosition(new GenomicPosition(Chromosome.getChromosome("2R"),13));
		assertEquals(s.genomicPosition().position(), 13);
		assertEquals(s.referenceCharacter(),'T');
	
		s=c.getSNPforPosition(new GenomicPosition(Chromosome.getChromosome("2L"),2));
		assertEquals(s.genomicPosition().position(), 2);
		assertEquals(s.referenceCharacter(),'G');
		
		s=c.getSNPforPosition(new GenomicPosition(Chromosome.getChromosome("3R"),1112));
		assertEquals(s.genomicPosition().position(), 1112);
		assertEquals(s.referenceCharacter(),'C');
	}
	
	
	@Test
	public void get_SNP_for_index() {
		ArrayList<SNP> snps=GenomicDataFactory.getSNPCollection();
		SNPCollection c=new SNPCollection(snps);
		assertEquals(c.size(),12);
		
		SNP s;
		s=c.getSNPforIndex(0);
		assertEquals(s.genomicPosition().position(), 2);
		assertEquals(s.referenceCharacter(),'G');
		
		s=c.getSNPforIndex(11);
		assertEquals(s.genomicPosition().position(), 13);
		assertEquals(s.referenceCharacter(),'T');
		
		s=c.getSNPforIndex(2);
		assertEquals(s.genomicPosition().position(), 1112);
		assertEquals(s.referenceCharacter(),'C');
	}
	
	@Test
	public void SNP() {
		ArrayList<SNP> snps=GenomicDataFactory.getSNPCollection();
		SNPCollection c=new SNPCollection(snps);
		assertEquals(c.size(),12);
		
		SNP s;
		//	public static SNP s2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'G','G','C');
		s=c.getSNPforIndex(0);
		assertEquals(s.genomicPosition().position(), 2);
		assertEquals(s.genomicPosition().chromosome().toString(),"2L");
		assertEquals(s.referenceCharacter(),'G');
		assertEquals(s.ancestralAllele(),'G');
		assertEquals(s.derivedAllele(),'C');
		
		
		//public static SNP s11=new SNP(new GenomicPosition(Chromosome.getChromosome("3R"),1112),'C','G','C');
		s=c.getSNPforIndex(2);
		assertEquals(s.genomicPosition().position(), 1112);
		assertEquals(s.genomicPosition().chromosome().toString(),"3R");
		assertEquals(s.referenceCharacter(),'C');
		assertEquals(s.ancestralAllele(),'G');
		assertEquals(s.derivedAllele(),'C');
	}
	
	@Test
	public void upstream_immutable() {
		ArrayList<SNP> snps=GenomicDataFactory.getSNPCollection();
		SNPCollection c=new SNPCollection(snps);
		Collections.sort(snps);
		SNPCollection c1=new SNPCollection(snps);
		
		int index;
		index=c.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2R"),13));
		assertEquals(index,11);
		index=c1.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2R"),13));
		assertEquals(index,5);
		
		index=c.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2L"),2));
		assertEquals(index,0);
		index=c1.getIndexforPosition(new GenomicPosition(Chromosome.getChromosome("2L"),2));
		assertEquals(index,1);
	}
	
	@Test
	public void isSorted() {
		ArrayList<SNP> snps=GenomicDataFactory.getSNPCollection();
		SNPCollection c=new SNPCollection(snps);
		Collections.sort(snps);
		SNPCollection c1=new SNPCollection(snps);
		
		assertTrue(c1.isSorted());
		assertFalse(c.isSorted());
	}
	
	
	

	
	
	

}
