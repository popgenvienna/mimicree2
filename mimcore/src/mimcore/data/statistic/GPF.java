package mimcore.data.statistic;

import mimcore.data.haplotypes.SNPCollection;

/**
 * Represents summary statistics for a single populatio
 */
public class GPF {
	private final double genotype;
	private final double phenotype;
	private final double fitness;


	public GPF(double genotype, double phenotype, double fitness)
	{
		this.genotype=genotype;
		this.phenotype=phenotype;
		this.fitness=fitness;
	}


	public double getGenotype() {
		return genotype;
	}

	public double getPhenotype() {
		return phenotype;
	}

	public double getFitness() {
		return fitness;
	}
}
