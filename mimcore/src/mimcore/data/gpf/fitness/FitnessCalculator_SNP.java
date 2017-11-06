package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;

import java.util.ArrayList;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculator_SNP implements IFitnessCalculator {
	private final ArrayList<FitnessOfSNP> snps;

	public FitnessCalculator_SNP(ArrayList<FitnessOfSNP> snps)
	{
		this.snps=new ArrayList<FitnessOfSNP>(snps);

	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype)
	{
		double fitness=1.0;

		// multiply fitness of all SNPs
		for(FitnessOfSNP snp:snps)
		{
			char[] genotypeAtSNP=dipGenome.getSNPGenotype(snp.getPosition()); // genotype at the SNP
			double fitnessAtSNP=snp.getEffectSizeOfGenotype(genotypeAtSNP);		//fitness at the SNP
			fitness*=fitnessAtSNP;
		}
		return fitness;
	}

	/**
	 * mostly for debugging
	 * @return
	 */
	public ArrayList<FitnessOfSNP> getSNPs()
	{
		return new ArrayList<FitnessOfSNP>(this.snps);
	}
}
