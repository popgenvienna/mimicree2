package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;

import java.util.ArrayList;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessCalculator_SNP implements IFitnessCalculator {
	private final ArrayList<IFitnessOfSNP> snps;

	public FitnessCalculator_SNP(ArrayList<IFitnessOfSNP> snps)
	{
		this.snps=new ArrayList<IFitnessOfSNP>(snps);

	}

	public double getFitness(DiploidGenome dipGenome, double phenotype, Sex sex)
	{
		double fitness=1.0;

		// multiply fitness of all SNPs
		for(IFitnessOfSNP snp:snps)
		{
			char[] genotypeAtSNP=dipGenome.getSNPGenotype(snp.getPosition()); // genotype at the SNP
			double fitnessAtSNP=snp.getEffectSizeOfGenotype(genotypeAtSNP,sex);		//fitness at the SNP
			fitness*=fitnessAtSNP;
		}
		return fitness;
	}

	/**
	 * mostly for debugging
	 * @return
	 */
	public ArrayList<IFitnessOfSNP> getSNPs()
	{
		return new ArrayList<IFitnessOfSNP>(this.snps);
	}


	public int size()
	{
		return this.snps.size();
	}
}
