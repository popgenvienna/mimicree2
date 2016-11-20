package qmimcore.data.haplotypes;

import qmimcore.data.GenomicPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Immutable representation of a collection of SNPs.
 * Provides functionality for fast element access via GenomicPosition
 * @author robertkofler
 */
public class SNPCollection {
	private final ArrayList<SNP> mysnps;
	private final HashMap<GenomicPosition,Integer> pos2snp;
	private final boolean isSorted;
	
	public SNPCollection(ArrayList<SNP> snps)
	{
		// First set the internals 
		this.mysnps=new ArrayList<SNP>(snps);
		
		HashMap<GenomicPosition,Integer> hm=new HashMap<GenomicPosition,Integer>();
		for(int i=0; i<mysnps.size(); i++)
		{
			GenomicPosition gp=mysnps.get(i).genomicPosition();
			hm.put(gp,i);
		}
		this.pos2snp=hm;
		this.isSorted=isSortedList(snps);
	}
	
	
	/**
	 * Get the index of a given GenomicPosition within the SNP collection
	 * @param gp
	 * @return
	 */
	public int getIndexforPosition(GenomicPosition gp)
	{
		if(!this.pos2snp.containsKey(gp)) throw new IllegalArgumentException("Map does not contain key " +gp.toString());
		int toret= this.pos2snp.get(gp);
		return toret;
	}
	
	/**
	 * Get the SNP for a given GenomicPosition;
	 * @param gp
	 * @return
	 */
	public SNP getSNPforPosition(GenomicPosition gp)
	{
		return getSNPforIndex(getIndexforPosition(gp));
	}
	
	
	/**
	 * Get the SNP for a given index within the SNP collection
	 * @param i the index of the SNP within the SNPCollection
	 * @return a SNP
	 */
	public SNP getSNPforIndex(int i)
	{
		return this.mysnps.get(i);
	}
	
	/**
	 * Get the size of the SNP collection
	 * @return
	 */
	public int size()
	{
		return this.mysnps.size();
	}
	
	
	/**
	 * Test whether the given SNP collection is sorted; 
	 * Sorting is a precondition for several algorithm operating on SNPCollections
	 * @return
	 */
	public boolean isSorted()
	{
		return isSorted;
	}
	
	
	
	private boolean isSortedList(ArrayList<SNP> unsorted)
	{
		ArrayList<SNP> sorted= new ArrayList<SNP>(unsorted);
		Collections.sort(sorted);
		assert(sorted.size() == unsorted.size()); 
		
		
		for(int i=0; i<sorted.size(); i++)
		{
			SNP sort=sorted.get(i);
			SNP usort=unsorted.get(i);
			if((sort.genomicPosition().position() != usort.genomicPosition().position()) ||  (!sort.genomicPosition().chromosome().equals(usort.genomicPosition().chromosome())) ) return false;
		}
		return true;
	}
	
	

}
