package mimcore.data.gpf.quantitative;

import mimcore.data.*;
import mimcore.data.sex.Sex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents a summary of the additive gpf effects of SNPs
 * Immutable; Also allows to calculate the additive gpf effect of SNPs
 */
public class GenotypeCalculator implements IGenotypeCalculator{
	private final ArrayList<IAdditiveSNPeffect> additiveSNPs;
	private final HashMap<GenomicPosition,IAdditiveSNPeffect> pos2add;
	

	public GenotypeCalculator(ArrayList<IAdditiveSNPeffect> addSnps)
	{
		this.pos2add=new HashMap<GenomicPosition,IAdditiveSNPeffect>();
		for(IAdditiveSNPeffect as: addSnps)
		{
			pos2add.put(as.getPosition(), as);
		}
		this.additiveSNPs=new ArrayList<IAdditiveSNPeffect>(addSnps);
	}

	

	public double getGenotype(DiploidGenome dipGenome, Sex sex)
	{
		double toret=0.0;
		for(IAdditiveSNPeffect snp: additiveSNPs)
		{
			char[] genotype=dipGenome.getSNPGenotype(snp.getPosition());
			double effectSize=snp.getEffectSizeOfGenotype(genotype,sex);
			toret+=effectSize;
		}
		return toret;
	}

	
	
	/**
	 * Get the additive SNP for a given position
	 * @param position
	 * @return
	 */
	public IAdditiveSNPeffect getAdditiveforPosition(GenomicPosition position)
	{
		if(!this.pos2add.containsKey(position)) return null;
		return this.pos2add.get(position);
	}
	
	public ArrayList<GenomicPosition> getSelectedPositions()
	{
		return new ArrayList<GenomicPosition>(new HashSet<GenomicPosition>(this.pos2add.keySet()));
	}


	public ArrayList<IAdditiveSNPeffect> getAdditiveSNPeffects()
	{
		return new ArrayList<IAdditiveSNPeffect>(this.additiveSNPs);
	}




	
	
}
