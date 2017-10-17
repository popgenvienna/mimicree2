package mimcore.data.fitness.quantitative;

import java.util.Random;

/**
 * Represents a summary of the additive fitness effects of SNPs
 * Immutable; Also allows to calculate the additive fitness effect of SNPs
 */
public class PhenotypeCalculator implements IPhenotypeCalculator {
	private final double environmentalVariance;
	private final double environmentalStdev;



	public PhenotypeCalculator(double environmentalVariance)
	{

		this.environmentalVariance=environmentalVariance;
		environmentalStdev = Math.sqrt(environmentalVariance);
	}

	

	public double getPhenotype(double genotype,Random random)
	{
		double gaus=random.nextGaussian();
		double environmenteffect=gaus*environmentalStdev;
		double phenotype=genotype+environmenteffect;
		return phenotype;
	}
	
	/**
	 * Check if all additive SNPs are fixed
	 * @param population
	 * @return

	public boolean areAdditiveFixed(Population population)
	{
		for(AdditiveSNPeffect as:this.additiveSNPs)
		{
			if(!population.isFixed(as.getPosition())) return false;
		}
		return true;
	}


	/**
	 * Count the number of additive SNPs that fixed in the correct direction
	 * Additive SNPs need to be fixed, it will throw an error if one of them are not fixed
	 * @param population
	 * @return

	public int countAdditiveFixedCorrectly(Population population)
	{
		SNPCollection snpCol=population.getSpecimen().get(0).getGenome().getHaplotypeA().getSNPHaplotype().getSNPCollection();

		int countCorrect=0;
		for(AdditiveSNPeffect as:this.additiveSNPs)
		{
			 SNP snp=snpCol.getSNPforPosition(as.getPosition());
			 boolean ancestralFixed=population.isAncestralFixed(as.getPosition());
			 boolean isAncestralSelected=as.isAncestralSelected(snp.ancestralAllele());
				if(ancestralFixed && isAncestralSelected)
				{
					countCorrect++;
				}
				else if( (!ancestralFixed) && (!isAncestralSelected))
				{
					countCorrect++;
				}
		}
		return countCorrect;
	}

	*/
	


	
	
}
