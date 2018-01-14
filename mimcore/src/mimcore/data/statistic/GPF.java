package mimcore.data.statistic;

import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.sex.Sex;

/**
 * Represents summary statistics for a single populatio
 */
public class GPF {
	private final Sex sex;
	private final double genotype;
	private final double phenotype;
	private final double fitness;


	public GPF(Sex sex, double genotype, double phenotype, double fitness)
	{
		this.genotype=genotype;
		this.phenotype=phenotype;
		this.fitness=fitness;
		this.sex=sex;
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

	public Sex getSex(){return sex;}
}
