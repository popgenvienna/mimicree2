package qmimcore.data.fitness;

import qmimcore.data.*;
import qmimcore.data.haplotypes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents a summary of the additive fitness effects of SNPs
 * Immutable; Also allows to calculate the additive fitness effect of SNPs
 */
public class GenotypeCalculator implements IGenotypeCalculator{
	private final ArrayList<AdditiveSNPeffect> additiveSNPs;
	private final HashMap<GenomicPosition,AdditiveSNPeffect> pos2add;
	

	public GenotypeCalculator(ArrayList<AdditiveSNPeffect> addSnps)
	{
		this.pos2add=new HashMap<GenomicPosition,AdditiveSNPeffect>();
		for(AdditiveSNPeffect as: addSnps)
		{
			pos2add.put(as.getPosition(), as);
		}
		this.additiveSNPs=new ArrayList<AdditiveSNPeffect>(addSnps);
	}

	

	public double getGenotype(DiploidGenome dipGenome)
	{
		double toret=0.0;
		for(AdditiveSNPeffect snp: additiveSNPs)
		{
			char[] genotype=dipGenome.getSNPGenotype(snp.getPosition());
			double effectSize=snp.getEffectSizeOfGenotype(genotype);
			toret+=effectSize;
		}
		return toret;
	}

	
	
	/**
	 * Get the additive SNP for a given position
	 * @param position
	 * @return
	 */
	public AdditiveSNPeffect getAdditiveforPosition(GenomicPosition position)
	{
		if(!this.pos2add.containsKey(position)) return null;
		return this.pos2add.get(position);
	}
	
	public ArrayList<GenomicPosition> getSelectedPositions()
	{
		return new ArrayList<GenomicPosition>(new HashSet<GenomicPosition>(this.pos2add.keySet()));
	}


	public ArrayList<AdditiveSNPeffect> getAdditiveSNPeffects()
	{
		return new ArrayList<AdditiveSNPeffect>(this.additiveSNPs);
	}




	
	
}
