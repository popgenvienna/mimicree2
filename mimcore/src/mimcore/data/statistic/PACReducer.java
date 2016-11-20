package mimcore.data.statistic;

import mimcore.data.*;
import mimcore.data.haplotypes.*;

import java.util.ArrayList;

public class PACReducer {

	private final ArrayList<HaploidGenome> haplotypes;

	public PACReducer(ArrayList<DiploidGenome> genomes)
	{
		this.haplotypes =new ArrayList<HaploidGenome>();
		for(DiploidGenome dg : genomes)
		{
			haplotypes.add(dg.getHaplotypeA());
			haplotypes.add(dg.getHaplotypeB());
		}
	}


	public PACReducer(Population population)
	{
		ArrayList<Specimen> specs=population.getSpecimen();
		this.haplotypes=new ArrayList<HaploidGenome>();
		for(Specimen s :specs)
		{
			haplotypes.add(s.getGenome().getHaplotypeA());
			haplotypes.add(s.getGenome().getHaplotypeB());
		}
	}




	public PopulationAlleleCount reduce()
	{
			return getPAC(haplotypes);
	}

	private PopulationAlleleCount getPAC(ArrayList<HaploidGenome> haplotypes)
	{
			SNPCollection snpcol=haplotypes.get(0).getSNPCollection();

			int[] ancestralCount=new int[snpcol.size()];
			int[] derivedCount=new int[snpcol.size()];
			for(int i=0; i<snpcol.size(); i++)
			{
				int ancCount=0;
				int derCount=0;
				for(HaploidGenome h: haplotypes)
				{
					if(h.hasAncestral(i))
					{
						ancCount++;
					}
					else
					{
						derCount++;
					}
				}

				ancestralCount[i]=ancCount;
				derivedCount[i]=derCount;
			}
		return new PopulationAlleleCount(snpcol,ancestralCount,derivedCount);
	}

}
