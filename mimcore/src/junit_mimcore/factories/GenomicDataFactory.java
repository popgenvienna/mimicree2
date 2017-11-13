package junit_mimcore.factories;

import mimcore.data.BitArray.BitArray;
import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Chromosome;
import mimcore.data.DiploidGenome;
import mimcore.data.GenomicPosition;
import mimcore.data.Specimen;

import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.recombination.RandomAssortment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;


public class GenomicDataFactory {
	
	// Logger
	private static Logger logger;
	
	// SNPs
	public static SNP s2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'G','G','C');
	public static SNP s12=new SNP(new GenomicPosition(Chromosome.getChromosome("3R"),1113),'T','T','C');
	public static SNP s11=new SNP(new GenomicPosition(Chromosome.getChromosome("3R"),1112),'C','G','C');
	public static SNP s1=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','A','T');
	public static SNP s3=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),3),'T','T','C');
	public static SNP s4=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),11),'T','C','T');
	public static SNP s5=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),12),'G','G','A');
	public static SNP s10=new SNP(new GenomicPosition(Chromosome.getChromosome("3R"),1111),'A','A','G');
	public static SNP s9=new SNP(new GenomicPosition(Chromosome.getChromosome("3L"),113),'T','T','C');
	public static SNP s8=new SNP(new GenomicPosition(Chromosome.getChromosome("3L"),112),'C','G','C');
	public static SNP s7=new SNP(new GenomicPosition(Chromosome.getChromosome("3L"),111),'A','A','G');
	public static SNP s6=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),13),'T','G','C');
	
	
	public static SNP c1=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','A','T');
	public static SNP c2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'A','A','T');
	public static SNP c3=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),3),'A','A','T');
	public static SNP c4=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),4),'A','A','T');
	public static SNP c5=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),5),'A','A','T');
	public static SNP c6=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),6),'A','A','T');
	public static SNP c7=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),7),'A','A','T');
	public static SNP c8=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),8),'A','A','T');
	public static SNP c9=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),9),'A','A','T');
	public static SNP c10=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),10),'A','A','T');
	public static SNP c11=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),1),'A','C','G');
	public static SNP c12=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),2),'A','C','G');
	public static SNP c13=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),3),'A','C','G');
	public static SNP c14=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),4),'A','C','G');
	public static SNP c15=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),5),'A','C','G');
	public static SNP c16=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),6),'A','C','G');
	public static SNP c17=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),7),'A','C','G');
	public static SNP c18=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),8),'A','C','G');
	public static SNP c19=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),9),'A','C','G');
	public static SNP c20=new SNP(new GenomicPosition(Chromosome.getChromosome("2R"),10),'A','C','G');
	
	
	// Static constructor
	static 
	{
		// Create logger
		logger= Logger.getLogger("Test Logger");
		logger.setUseParentHandlers(false);
	}
	
	
	public static Logger getNullLogger()
	{
		return logger;
	}
	
	
	/**
	 * Get a collection of 12 SNPs on chromosomes 2L, 2R, 3L, 3R;
	 * 2L-2, 3R-1113, 3R-1112, 2L-1, 2L-3, 2R-11, 2R-12, 3R-1111, 3L-113, 3L-112, 3L-111, 2R-13
	 * @return
	 */
	public static ArrayList<SNP> getSNPCollection()
	{
		ArrayList<SNP> snps=new ArrayList<SNP>();
		snps.add(s2);
		snps.add(s12);
		snps.add(s11);
		snps.add(s1); 
		snps.add(s3); 
		snps.add(s4); 
		snps.add(s5);
		snps.add(s10);
		snps.add(s9);
		snps.add(s8);
		snps.add(s7);
		snps.add(s6);
		return snps;
	}
	
	/**
	 * A SNP collection
	 * 2L-1, 2L-2, 2L-3, 2R-11, 2R-12, 2R-13, 3L-111, 3L-112, 3L-113, 3R-1111, 3R-1112, 3R-1113 
	 * @return
	 */
	public static SNPCollection getSortedSNPCollection()
	{
		ArrayList<SNP> snps=getSNPCollection();
		Collections.sort(snps);
		return new SNPCollection(snps);
				
	}
	
	
	/**
	 * Get a SNP collection for testing crossover
	 * @return
	 */
	public static SNPCollection getCrossoverSNPCollection()
	{
		ArrayList<SNP> snps=new ArrayList<SNP>();
		snps.add(c1);
		snps.add(c2);
		snps.add(c3);
		snps.add(c4);
		snps.add(c5);
		snps.add(c6);
		snps.add(c7);
		snps.add(c8);
		snps.add(c9);
		snps.add(c10);
		snps.add(c11);
		snps.add(c12);
		snps.add(c13);
		snps.add(c14);
		snps.add(c15);
		snps.add(c16);
		snps.add(c17);
		snps.add(c18);
		snps.add(c19);
		snps.add(c20);
		Collections.sort(snps);
		return new SNPCollection(snps);
	}
	
	public static HaploidGenome getCrossoverHaplotypeMinor()
	{
		BitArray ba=new BitArrayBuilder(20).getBitArray();
		return new HaploidGenome(ba,getCrossoverSNPCollection());
	}
	
	public static HaploidGenome getCrossoverHaplotypeMajor()
	{
		BitArrayBuilder b=new BitArrayBuilder(20);
		b.setBit(0); b.setBit(1);b.setBit(2); b.setBit(3); b.setBit(4);b.setBit(5); b.setBit(6); b.setBit(7);b.setBit(8); b.setBit(9);
		b.setBit(10); b.setBit(11); b.setBit(12); b.setBit(13); b.setBit(14); b.setBit(15); b.setBit(16); b.setBit(17); b.setBit(18);
		b.setBit(19);
		BitArray ba=b.getBitArray();
		return new HaploidGenome(ba,getCrossoverSNPCollection());
	}

	public static ArrayList<DiploidGenome> getMinimalGenomes()
	{
		ArrayList<DiploidGenome> genomes=new ArrayList<DiploidGenome>();

		genomes.add(new DiploidGenome(getCrossoverHaplotypeMajor(),getCrossoverHaplotypeMajor()));
		return genomes;
	}
	
	public static RandomAssortment getRandomAssortment00()
	{
		HashMap<Chromosome,Boolean> tr=new HashMap<Chromosome,Boolean>();
		tr.put(Chromosome.getChromosome("2L"), false);
		tr.put(Chromosome.getChromosome("2R"), false);
		return new RandomAssortment(tr);
	}
	
	public static RandomAssortment getRandomAssortment01()
	{
		HashMap<Chromosome,Boolean> tr=new HashMap<Chromosome,Boolean>();
		tr.put(Chromosome.getChromosome("2L"), false);
		tr.put(Chromosome.getChromosome("2R"), true);
		return new RandomAssortment(tr);
	}
	
	public static RandomAssortment getRandomAssortment11()
	{
		HashMap<Chromosome,Boolean> tr=new HashMap<Chromosome,Boolean>();
		tr.put(Chromosome.getChromosome("2L"), true);
		tr.put(Chromosome.getChromosome("2R"), true);
		return new RandomAssortment(tr);
	}
	
	public static RandomAssortment getRandomAssortment10()
	{
		HashMap<Chromosome,Boolean> tr=new HashMap<Chromosome,Boolean>();
		tr.put(Chromosome.getChromosome("2L"), true);
		tr.put(Chromosome.getChromosome("2R"), false);
		return new RandomAssortment(tr);
	}


	/**
	 * Get an additive SNP for 2L-5 A/T; 
	 * w11=A s=-0.1 h=0.5	
	 * @return

	public static AdditiveSNP getAdditiveSNP_int1()
	{
		return new AdditiveSNP(new GenomicPosition(Chromosome.getChromosome("2L"),5),'A',0.1,0.5);

	}
	
	/**
	 * Get an additive SNP for 2R-5 C/G
	 * w11=C s=0.5 h=0.5
	 * @return

	public static AdditiveSNP  getAdditiveSNP_int2()
	{
		return new AdditiveSNP(new GenomicPosition(Chromosome.getChromosome("2R"),5),'C', 0.5, 0.5);
	}
	
	/**
	 * Get an additive SNP for 2L-6 A/T; 
	 * w11=A s=-0.1 h=0.0	
	 * @return

	public static AdditiveSNP getAdditiveSNP_domW11()
	{
		return new AdditiveSNP(new GenomicPosition(Chromosome.getChromosome("2L"),6),'A',0.1,0.0);
	}
	
	public static EpistaticSNP getEpistaticSNP_syn1()
	{
		ArrayList<EpistaticSubeffectSNP> es=new ArrayList<EpistaticSubeffectSNP>();
		es.add(new EpistaticSubeffectSNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A'));
		es.add(new EpistaticSubeffectSNP(new GenomicPosition(Chromosome.getChromosome("2R"),1),'C'));
		return new EpistaticSNP("pff",0.1,es);
	}
	
	public static EpistaticSNP getEpistaticSNP_syn2()
	{
		ArrayList<EpistaticSubeffectSNP> es=new ArrayList<EpistaticSubeffectSNP>();
		es.add(new EpistaticSubeffectSNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'T'));
		es.add(new EpistaticSubeffectSNP(new GenomicPosition(Chromosome.getChromosome("2R"),10),'G'));
		return new EpistaticSNP("pff",0.1,es);
	}
	
	public static EpistaticSNP getEpistaticSNP_syn3()
	{
		ArrayList<EpistaticSubeffectSNP> es=new ArrayList<EpistaticSubeffectSNP>();
		es.add(new EpistaticSubeffectSNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A'));
		es.add(new EpistaticSubeffectSNP(new GenomicPosition(Chromosome.getChromosome("2R"),10),'G'));
		return new EpistaticSNP("pff",0.1,es);
	}
	
	/**
	 * Get an additive SNP for 2L-7 A/T; 
	 * w11=A s=-0.1 h=1.0	
	 * @return

	public static AdditiveSNP getAdditiveSNP_domW22()
	{
		return new AdditiveSNP(new GenomicPosition(Chromosome.getChromosome("2L"),7),'A',0.1,1.0);
	}
	 */
	
	public static DiploidGenome getDiploidGenome_11()
	{
		HaploidGenome g1=getCrossoverHaplotypeMajor();
		HaploidGenome g2=getCrossoverHaplotypeMajor();
		return new DiploidGenome(g1,g2);
	}
	
	public static DiploidGenome getDiploidGenome_00()
	{
		HaploidGenome g1=getCrossoverHaplotypeMinor();
		HaploidGenome g2=getCrossoverHaplotypeMinor();
		return new DiploidGenome(g1,g2);
	}
	
	public static DiploidGenome getDiploidGenome_10()
	{
		HaploidGenome g1=getCrossoverHaplotypeMajor();
		HaploidGenome g2=getCrossoverHaplotypeMinor();
		return new DiploidGenome(g1,g2);
	}


	public static ArrayList<DiploidGenome> getDiploidGenomesMf08()
	{
		ArrayList<DiploidGenome> genomes= new ArrayList<DiploidGenome>();
		genomes.add(getDiploidGenome_11());
		genomes.add(getDiploidGenome_11());
		genomes.add(getDiploidGenome_11());
		genomes.add(getDiploidGenome_11());
		genomes.add(getDiploidGenome_00());
		return genomes;
	}

	public static ArrayList<DiploidGenome> getDiploidGenomesMf02()
	{
		ArrayList<DiploidGenome> genomes= new ArrayList<DiploidGenome>();
		genomes.add(getDiploidGenome_11());
		genomes.add(getDiploidGenome_00());
		genomes.add(getDiploidGenome_00());
		genomes.add(getDiploidGenome_00());
		genomes.add(getDiploidGenome_00());
		return genomes;
	}


	/**
	public static Specimen getSpecimen(double fitness, double additiveFitness)
	{
		DiploidGenome genome=getDiploidGenome_11();
		Specimen s=new Specimen(fitness,additiveFitness,1.0,genome,0,0);
		return s;
	}
	
	public static ArrayList<HaploidGenome> getHaploidGenomes_linked()
	{
		// public static SNP c1=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','A','T');
		// public static SNP c2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'A','A','T');
		// public static SNP c3=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),3),'A','A','T');
		ArrayList<SNP> snps=new ArrayList<SNP>();
		snps.add(c1); snps.add(c2); snps.add(c3);
		SNPCollection sc=new SNPCollection(snps);
		
		ArrayList<HaploidGenome> genomes=new ArrayList<HaploidGenome>();
		BitArrayBuilder b=new BitArrayBuilder(3);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(3);
		b.setBit(0); b.setBit(1);b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		return genomes;
	}
	
	public static ArrayList<HaploidGenome> getHaploidGenomes_allfixed()
	{
		// public static SNP c1=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','A','T');
		// public static SNP c2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'A','A','T');
		// public static SNP c3=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),3),'A','A','T');
		ArrayList<SNP> snps=new ArrayList<SNP>();
		snps.add(c1); snps.add(c2); snps.add(c3);
		SNPCollection sc=new SNPCollection(snps);
		
		ArrayList<HaploidGenome> genomes=new ArrayList<HaploidGenome>();
		BitArrayBuilder b=new BitArrayBuilder(3);
		b.setBit(0); b.setBit(1);b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(3);
		b.setBit(0);b.setBit(1);b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		return genomes;
	}
	
	
	public static ArrayList<HaploidGenome> getHaploidGenomes_unlinked()
	{
		// public static SNP c1=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','A','T');
		// public static SNP c2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'A','A','T');
		// public static SNP c3=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),3),'A','A','T');
		ArrayList<SNP> snps=new ArrayList<SNP>();
		snps.add(c1); snps.add(c2); snps.add(c3);
		SNPCollection sc=new SNPCollection(snps);
		
		// 0
		ArrayList<HaploidGenome> genomes=new ArrayList<HaploidGenome>();
		BitArrayBuilder b=new BitArrayBuilder(3);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		// 1
		b=new BitArrayBuilder(3);
		b.setBit(0);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(3);
		b.setBit(1);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(3);
		b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		//2
		b=new BitArrayBuilder(3);
		b.setBit(0); b.setBit(1);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(3);
		b.setBit(0); b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(3);
		b.setBit(1); b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		//3
		b=new BitArrayBuilder(3);
		b.setBit(0); b.setBit(1);b.setBit(2);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		return genomes;
	}
	
	public static ArrayList<HaploidGenome> getHaploidGenomes_chrBound()
	{
		// public static SNP c1=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),1),'A','A','T');
		// public static SNP c2=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),2),'A','A','T');
		// public static SNP c3=new SNP(new GenomicPosition(Chromosome.getChromosome("2L"),3),'A','A','T');
		ArrayList<SNP> snps=new ArrayList<SNP>();
		snps.add(c1); snps.add(c2); snps.add(c11); snps.add(c12);
		SNPCollection sc=new SNPCollection(snps);
		
		ArrayList<HaploidGenome> genomes=new ArrayList<HaploidGenome>();
		BitArrayBuilder b=new BitArrayBuilder(4);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		b=new BitArrayBuilder(4);
		b.setBit(0); b.setBit(1);b.setBit(2);b.setBit(3);
		genomes.add(new HaploidGenome(new Haplotype(b.getBitArray(),sc),new InversionHaplotype(new ArrayList<Inversion>())));
		
		return genomes;
	}
	
*/
}
