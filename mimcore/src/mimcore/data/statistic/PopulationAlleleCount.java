package mimcore.data.statistic;

import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;

/**
 * Represents summary statistics for a single populatio
 */
public class PopulationAlleleCount {
	private final SNPCollection snpcol;
	private final int[] ancestralCount;
	private final int[] derivedCount;
	public PopulationAlleleCount(SNPCollection snpcol, int[] ancestralCount, int[] derivedCount)
	{
		this.snpcol=snpcol;
		this.ancestralCount=ancestralCount;
		this.derivedCount=derivedCount;
	}

	
	public SNPCollection getSNPCollection()
	{
		return this.snpcol;
	}
	
	public int ancestralCount(int index)
	{
		return this.ancestralCount[index];
	}
	
	public int derivedCount(int index)
	{
		return this.derivedCount[index];
	}

	public double ancestralFrequency(int index)
	{
		int ac=this.ancestralCount(index);
		int dc=this.derivedCount(index);
		double af=((double)ac)/((double)(ac+dc));
		return af;
	}

	public double derivedFrequency(int index)
	{
		int ac=this.ancestralCount(index);
		int dc=this.derivedCount(index);
		double df=((double)dc)/((double)(ac+dc));
		return df;
	}

	
	
	public int getCount(char toCount, int index)
	{
		int toret=0;
		SNP s=snpcol.getSNPforIndex(index);
		if(s.ancestralAllele()==toCount)
		{
			toret=ancestralCount[index];
		}
		else if(s.derivedAllele()==toCount)
		{
			toret=derivedCount[index];
		}
		return toret;
	}
	
	
	
	
}
