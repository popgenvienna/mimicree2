package mimcore.data.recombination;

import mimcore.data.*;
import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;

import java.util.ArrayList;
import java.util.LinkedList;

public class RecombinationEvent {
	private final CrossoverEvents crossovers;
	private final RandomAssortment randAssort;
	public RecombinationEvent(RandomAssortment randAssort, CrossoverEvents crossovers)
	{
		this.crossovers=crossovers;
		this.randAssort=randAssort;
	}

	public BitArrayBuilder getGamete(DiploidGenome genome)
	{
		HaploidGenome haplotypeA = genome.getHaplotypeA();
		HaploidGenome haplotypeB = genome.getHaplotypeB();

		SNPCollection scol=haplotypeA.getSNPCollection();
		
		// the new haplotype
		BitArrayBuilder newHap=new BitArrayBuilder(scol.size());
		
		Chromosome activeChr=Chromosome.getDefaultChromosome();
		LinkedList<GenomicPosition> activeCrossovers=new LinkedList<GenomicPosition>();
		boolean isHaplotypeA=false;
		
		for(int i=0; i<scol.size(); i++)
		{
			// Get the SNP at the given index
			SNP s=scol.getSNPforIndex(i);
			Chromosome chr=s.genomicPosition().chromosome();
			int position=s.genomicPosition().position();

			if(!(chr.equals(activeChr))){
				// the SNP is in a new chromosome -> switch chromosomes
				activeChr=chr;
				isHaplotypeA=randAssort.startWithFirstHaplotype(chr);
				activeCrossovers =crossovers.getCrossovers(chr);
			}
			// SNPs at 30 and 31; Crossover at 30
			// Start haplotype=A     => 30=A 31=B => thus crossover at 30 occurs directly after base
			while((!activeCrossovers.isEmpty()) && activeCrossovers.peekFirst().position() < position)
			{
				// a crossover event occured before the SNPs -> switch haplotype
				activeCrossovers.pollFirst();
				isHaplotypeA= !isHaplotypeA;
			}
			
			boolean ancestralHaplotype=false;
			if(isHaplotypeA)
			{
				ancestralHaplotype=haplotypeA.hasAncestral(i);
			}
			else
			{
				ancestralHaplotype=haplotypeB.hasAncestral(i);
			}
			
			// Set the major haplotype bit if positive
			if(ancestralHaplotype) newHap.setBit(i);
			// else leave the default => no haplotype
		}
		return newHap;
	}
	


	
	
	

}
