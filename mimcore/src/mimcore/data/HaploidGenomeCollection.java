package mimcore.data;

import mimcore.data.haplotypes.*;

import java.util.ArrayList;
import java.util.HashMap;


public class HaploidGenomeCollection {
	private final ArrayList<HaploidGenome> haploidGenomes;
	private final SNPCollection snpCollection;
	private final HashMap<GenomicPosition,Double> pos2freq;
	private final double accuracyFixed=0.000001;
	private final double accuracyRsquared=1000000.0;
	
	public HaploidGenomeCollection(ArrayList<HaploidGenome> haploidGenomes)
	{
		// At least a single haploid genome has to be present
		if(haploidGenomes.size()<1) throw new IllegalArgumentException("At least one genome needs to be provided");
		
		SNPCollection snpCol=haploidGenomes.get(0).getSNPCollection();
		
		pos2freq=new HashMap<GenomicPosition,Double>();
		for(int i=0; i<snpCol.size(); i++)
		{
			SNP s=snpCol.getSNPforIndex(i);
			pos2freq.put(s.genomicPosition(),calculatepX(s.genomicPosition(),snpCol,haploidGenomes));
		}
		
		this.haploidGenomes=new ArrayList<HaploidGenome>(haploidGenomes);
		this.snpCollection=snpCol;
		
		
	}
	
	private double calculatepX(GenomicPosition position,SNPCollection snpCol, ArrayList<HaploidGenome> genomes)
	{
		int countMaj=0;
		int index=snpCol.getIndexforPosition(position);
		
		double total = ((double)genomes.size());
		for(HaploidGenome g: genomes)
		{
			if(g.hasAncestral(index)) countMaj++;
		}
		return ((double)countMaj)/total;
	}
	
	/**
	 * Calculate the population frequency of the major allele for the given SNP
	 * @param position
	 * @return
	 */
	public double pX(GenomicPosition position)
	{
		if(!this.pos2freq.containsKey(position)) throw new IllegalArgumentException("No haplotypes found for given SNPs");
		return this.pos2freq.get(position);
	}
	
	/**
	 * Calculate the joint population frequency of the two ancestral alleles of the given SNPs
	 * @param positionA
	 * @param positionB
	 * @return
	 */
	public double pAB(GenomicPosition positionA, GenomicPosition positionB)
	{
		int countAnc=0;
		double total = ((double)this.haploidGenomes.size());
		int indexA=this.snpCollection.getIndexforPosition(positionA);
		int indexB=this.snpCollection.getIndexforPosition(positionB);
		
		for(HaploidGenome g: this.haploidGenomes)
		{
			if(g.hasAncestral(indexA) && g.hasAncestral(indexB)) countAnc++;
		}
		return ((double)countAnc)/total;
	}
	
	/**
	 * Calculate R^2 for the given pair of genomic positions
	 * @param positionA
	 * @param positionB
	 * @return
	 */
	public double calculateRsquared(GenomicPosition positionA, GenomicPosition positionB)
	{
		double pA=this.pX(positionA);
		double pB=this.pX(positionB);
		double pAB=this.pAB(positionA,positionB);
		double DAB=pAB-pA*pB;
		double divisor=pA*(1.0-pA)*pB*(1.0-pB);
		double res=Math.pow(DAB, 2)/divisor;
		res=Math.round(res*this.accuracyRsquared);
		res=res/this.accuracyRsquared;
		return res;
	}
	
	public SNPCollection getNotFixedSNPs()
	{
		ArrayList<SNP> notFixed=new ArrayList<SNP>();
		for(int i=0; i<this.snpCollection.size(); i++)
		{
			SNP s=this.snpCollection.getSNPforIndex(i);
			double pf=this.pX(s.genomicPosition());
			// Discard fixed; major allele frequency either 0.0 or 1.0
			if(pf < this.accuracyFixed) continue;
			if(1.0-pf < this.accuracyFixed)continue;
			
			notFixed.add(s);
		}
		return new SNPCollection(notFixed);
	}

}
