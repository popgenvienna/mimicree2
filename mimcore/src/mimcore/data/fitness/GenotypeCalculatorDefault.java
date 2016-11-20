package qmimcore.data.fitness;

import qmimcore.data.DiploidGenome;
import qmimcore.data.GenomicPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents a summary of the additive fitness effects of SNPs
 * Immutable; Also allows to calculate the additive fitness effect of SNPs
 */
public class GenotypeCalculatorDefault implements IGenotypeCalculator{



	public GenotypeCalculatorDefault()
	{
	}

	

	public double getGenotype(DiploidGenome dipGenome)
	{
		return 1.0;
	}
}
