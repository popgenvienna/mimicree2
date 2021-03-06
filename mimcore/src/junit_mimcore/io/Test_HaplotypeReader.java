package junit_mimcore.io;

import junit_mimcore.factories.GenomicDataFactory;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerDirect;
import mimcore.io.haplotypes.HaplotypeReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Test_HaplotypeReader {
	
	private static ArrayList<HaploidGenome> h;  // sorted

	
	@BeforeClass
	public static void setUp()
	{
		String input=
				"2R\t10\tG\tA/G\tAG AA AA AA GG\n"+
				"3L\t20\tG\tC/G\tGC CC GC CG GG\n"+
				"3R\t30\tA\tT/A\tTT TA AA AT TA\n"+
				"X\t40\tT\tG/T\tTG GG GG TT GG\n"+
				"X\t50\tT\tT/C\tCC TC CC TT CT\n";

		;
		h=new HaplotypeReader(input,  false,GenomicDataFactory.getNullLogger(),true).getHaplotypes();
	}

	public static HaplotypeReader getSexedHaplotypeReader()
	{
		String input=
				"# comment\n"+
						"#sex   	    F  M   F  M  H \n" +
						"2R\t10\tG\tA/G\tAG AA AA AA GG\n"+
						"3L\t20\tG\tC/G\tGC CC GC CG GG\n"+
						"3R\t30\tA\tT/A\tTT TA AA AT TA\n"+
						"X\t40\tT\tG/T\tTG GG GG TT GG\n"+
						"X\t50\tT\tT/C\tCC TC CC TT CT\n";

		;
		return new HaplotypeReader(input, false, GenomicDataFactory.getNullLogger(),true);
	}

	public static HaplotypeReader getHaploidHaplotypeReader()
	{
		String input=
				"# comment\n"+
						"#sex   	    F  M   F  M  H \n" +
						"2R\t10\tG\tA/G\tA A A A G\n"+
						"3L\t20\tG\tC/G\tG C G C G\n"+
						"3R\t30\tA\tT/A\tT T A A T\n"+
						"X\t40\tT\tG/T\tT G G T G\n"+
						"X\t50\tT\tT/C\tC T C T C\n";

		;
		return new HaplotypeReader(input, true, GenomicDataFactory.getNullLogger(),true);
	}
	
	@Test
	public void test_haplotypeNumber()
	{
		assertEquals(h.size(),10);
	}
	
	@Test
	public void chromosome_correct() {
		SNPCollection sc=h.get(0).getSNPCollection();
		assertEquals(sc.getSNPforIndex(0).genomicPosition().chromosome().toString(),"2R");
		assertEquals(sc.getSNPforIndex(1).genomicPosition().chromosome().toString(),"3L");
		assertEquals(sc.getSNPforIndex(2).genomicPosition().chromosome().toString(),"3R");
		assertEquals(sc.getSNPforIndex(3).genomicPosition().chromosome().toString(),"X");
		assertEquals(sc.getSNPforIndex(4).genomicPosition().chromosome().toString(),"X");
	}
	
	@Test
	public void snp_position_correct() {
		SNPCollection sc=h.get(0).getSNPCollection();
		assertEquals(sc.getSNPforIndex(0).genomicPosition().position(),10);
		assertEquals(sc.getSNPforIndex(1).genomicPosition().position(),20);
		assertEquals(sc.getSNPforIndex(2).genomicPosition().position(),30);
		assertEquals(sc.getSNPforIndex(3).genomicPosition().position(),40);
		assertEquals(sc.getSNPforIndex(4).genomicPosition().position(),50);
	}

	@Test
	public void reference_character_correct() {
		SNPCollection sc=h.get(0).getSNPCollection();
		assertEquals(sc.getSNPforIndex(0).referenceCharacter(),'G');
		assertEquals(sc.getSNPforIndex(1).referenceCharacter(),'G');
		assertEquals(sc.getSNPforIndex(2).referenceCharacter(),'A');
		assertEquals(sc.getSNPforIndex(3).referenceCharacter(),'T');
		assertEquals(sc.getSNPforIndex(4).referenceCharacter(),'T');
	}
	
	@Test
	public void ancestral_allele_correct() {
		SNPCollection sc=h.get(0).getSNPCollection();
		assertEquals(sc.getSNPforIndex(0).ancestralAllele(),'A');
		assertEquals(sc.getSNPforIndex(1).ancestralAllele(),'C');
		assertEquals(sc.getSNPforIndex(2).ancestralAllele(),'T');
		assertEquals(sc.getSNPforIndex(3).ancestralAllele(),'G');
		assertEquals(sc.getSNPforIndex(4).ancestralAllele(),'T');
	}

	@Test
	public void derived_allele_correct() {
		SNPCollection sc=h.get(0).getSNPCollection();
		assertEquals(sc.getSNPforIndex(0).derivedAllele(),'G');
		assertEquals(sc.getSNPforIndex(1).derivedAllele(),'G');
		assertEquals(sc.getSNPforIndex(2).derivedAllele(),'A');
		assertEquals(sc.getSNPforIndex(3).derivedAllele(),'T');
		assertEquals(sc.getSNPforIndex(4).derivedAllele(),'C');
	}
	
	@Test
	public void first_haplotype()
	{
		HaploidGenome ha=h.get(0);
		assertEquals(ha.getAllele(0),'A');
		assertEquals(ha.getAllele(1),'G');
		assertEquals(ha.getAllele(2),'T');
		assertEquals(ha.getAllele(3),'T');
		assertEquals(ha.getAllele(4),'C');
	}
	

	@Test
	public void second_haplotype()
	{
		HaploidGenome ha=h.get(1);
		assertEquals(ha.getAllele(0),'G');
		assertEquals(ha.getAllele(1),'C');
		assertEquals(ha.getAllele(2),'T');
		assertEquals(ha.getAllele(3),'G');
		assertEquals(ha.getAllele(4),'C');
	}
	
	@Test
	public void eigth_haplotype()
	{
		HaploidGenome ha=h.get(8);
		assertEquals(ha.getAllele(0),'G');
		assertEquals(ha.getAllele(1),'G');
		assertEquals(ha.getAllele(2),'T');
		assertEquals(ha.getAllele(3),'G');
		assertEquals(ha.getAllele(4),'C');
	}
	
	@Test
	public void ninth_haplotype()
	{
		HaploidGenome  ha=h.get(9);
		assertEquals(ha.getAllele(0),'G');
		assertEquals(ha.getAllele(1),'G');
		assertEquals(ha.getAllele(2),'A');
		assertEquals(ha.getAllele(3),'G');
		assertEquals(ha.getAllele(4),'T');
	}

	@Test
	public void sexlist()
	{
		HaplotypeReader hs=getSexedHaplotypeReader();
		ArrayList<Sex> sex=hs.getSexAssigner().getSexes(5, new Random());
		// F  M   F  M  H
		assertEquals(sex.get(0),Sex.Female);
		assertEquals(sex.get(1),Sex.Male);
		assertEquals(sex.get(2),Sex.Female);
		assertEquals(sex.get(3),Sex.Male);
		assertEquals(sex.get(4),Sex.Hermaphrodite);


	}

	@Test
	public void first_haplotype_with_comment()
	{
		HaploidGenome ha=getSexedHaplotypeReader().getHaplotypes().get(0);
		assertEquals(ha.getAllele(0),'A');
		assertEquals(ha.getAllele(1),'G');
		assertEquals(ha.getAllele(2),'T');
		assertEquals(ha.getAllele(3),'T');
		assertEquals(ha.getAllele(4),'C');
	}

	@Test
	public void ninth_haplotype_with_comment()
	{
		HaploidGenome  ha=getSexedHaplotypeReader().getHaplotypes().get(9);
		assertEquals(ha.getAllele(0),'G');
		assertEquals(ha.getAllele(1),'G');
		assertEquals(ha.getAllele(2),'A');
		assertEquals(ha.getAllele(3),'G');
		assertEquals(ha.getAllele(4),'T');
	}

	@Test
	public void haploidHaplotype_basic()
	{
		ArrayList<HaploidGenome> g=getHaploidHaplotypeReader().getHaplotypes();
		assertEquals(g.size(),10);
	}

	@Test
	public void haploidHaplotype_sex()
	{
		ArrayList<Sex> ss=getHaploidHaplotypeReader().getSexAssigner().getSexes(5,new Random());
		assertEquals(ss.get(0),Sex.Female);
		assertEquals(ss.get(1),Sex.Male);
		assertEquals(ss.get(2),Sex.Female);
		assertEquals(ss.get(3),Sex.Male);
		assertEquals(ss.get(4),Sex.Hermaphrodite);
	}

	@Test
	public void haploidHaplotype_alleles1()
	{
		ArrayList<HaploidGenome> g=getHaploidHaplotypeReader().getHaplotypes();
		assertEquals(g.get(0).getAllele(0),'A');
		assertEquals(g.get(1).getAllele(0),'A');
		assertEquals(g.get(8).getAllele(0),'G');
		assertEquals(g.get(9).getAllele(0),'G');
	}
	@Test
	public void haploidHaplotype_alleles2()
	{
		ArrayList<HaploidGenome> g=getHaploidHaplotypeReader().getHaplotypes();
		assertEquals(g.get(0).getAllele(1),'G');
		assertEquals(g.get(1).getAllele(1),'G');
		assertEquals(g.get(2).getAllele(1),'C');
		assertEquals(g.get(3).getAllele(1),'C');
		assertEquals(g.get(8).getAllele(1),'G');
		assertEquals(g.get(9).getAllele(1),'G');
	}

	/**
	 * 		String input=
	 "# comment\n"+
	 "#sex   	    F  M   F  M  H \n" +
	 "2R\t10\tG\tA/G\tA A A A G\n"+
	 "3L\t20\tG\tC/G\tG C G C G\n"+
	 "3R\t30\tA\tT/A\tT T A A T\n"+
	 "X\t40\tT\tG/T\tT G G T G\n"+
	 "X\t50\tT\tT/C\tC T C T C\n";
	 */

}
