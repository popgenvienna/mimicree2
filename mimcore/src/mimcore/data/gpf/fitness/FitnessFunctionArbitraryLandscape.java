package mimcore.data.gpf.fitness;

import mimcore.data.DiploidGenome;
import mimcore.data.sex.Sex;
import mimcore.data.statistic.Gaussian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by robertkofler on 11/20/16.
 */
public class FitnessFunctionArbitraryLandscape implements IFitnessCalculator {

	private final ArrayList<ArbitraryLandscapeEntry> entries;
	private final ArbitraryLandscapeEntry lowest;
	private final ArbitraryLandscapeEntry highest;

	public FitnessFunctionArbitraryLandscape(ArrayList<ArbitraryLandscapeEntry> entries)
	{
		ArrayList<ArbitraryLandscapeEntry> tostore=new ArrayList<ArbitraryLandscapeEntry>(entries);
		//sort ascending
		Collections.sort(tostore,new Comparator<ArbitraryLandscapeEntry>() {
			@Override
			public int compare(ArbitraryLandscapeEntry i1, ArbitraryLandscapeEntry i2) {
				if(i1.getPhenotypicValue() < i2.getPhenotypicValue())return -1;
				if(i1.getPhenotypicValue() > i2.getPhenotypicValue() )return 1;
				return 0;
			}
		});

		// from sorted entries get the lowest and the largest phenotypic values
		this.entries=tostore;
		this.lowest=this.entries.get(0);
		this.highest=this.entries.get(this.entries.size()-1);


	}

	public  double getFitness(DiploidGenome dipGenome, double phenotype, Sex sex)
	{
		// Exclude the boundary conditions; if lower than lowest or higher than highest; return the boundary condition
		if(phenotype <= this.lowest.getPhenotypicValue()) return this.lowest.getFitness();
		if(phenotype >= this.highest.getPhenotypicValue()) return this.highest.getFitness();

		double fitness=binarySearchAndInterpolateFitness(phenotype);
		return fitness;

	}

	private  double binarySearchAndInterpolateFitness(double phenotypicValue)
	{
		// binary search to speed it up with many entries...
		// lowest and highest index
		int low = 0;
		int high = entries.size() - 1;

		while(high > low) {

			int middle = (low + high) / 2;


			if(entries.get(middle).getPhenotypicValue() == phenotypicValue) {
				// Jackpot: we found the value; just return the fitness; not expected to happen often...
				return entries.get(middle).getFitness();
			}
			else if(entries.get(middle).getPhenotypicValue() < phenotypicValue) {
				low = middle + 1;
			}
			else if(entries.get(middle).getPhenotypicValue() > phenotypicValue) {
				high = middle - 1;
			}
			else throw new IllegalArgumentException("OK something went terribly wrong during binary search; 1");
		}

		if(low!=high) throw new IllegalArgumentException("Algorithm assumption violated; contact programmer; 2");

		// interpolate between the two values
		if(entries.get(low).getPhenotypicValue()==phenotypicValue) return entries.get(low).getFitness(); // first check if identical
		else if(entries.get(low).getPhenotypicValue()<phenotypicValue) return interpolateFitness(low, low+1, phenotypicValue);
		else if(entries.get(low).getPhenotypicValue()>phenotypicValue) return interpolateFitness(low-1, low, phenotypicValue);
		else throw new IllegalArgumentException("Algorithm assumption violated; contact programmer; 3");

	}

	private double interpolateFitness(int lowIndex, int highIndex,double phenotype)
	{
		ArbitraryLandscapeEntry lowEntry=this.entries.get(lowIndex);
		ArbitraryLandscapeEntry highEntry=this.entries.get(highIndex);
		if(phenotype < lowEntry.getPhenotypicValue()) throw new IllegalArgumentException("Algorithm assumption was violated; contact programmer... 4");
		if(phenotype > highEntry.getPhenotypicValue()) throw new IllegalArgumentException("Algorithm assumption was violated; contact programmer... 5");

		// k steigung slope
		// delta y (fitness) / delta x (phenotypic value)
		double k=(highEntry.getFitness()-lowEntry.getFitness())/(highEntry.getPhenotypicValue()-lowEntry.getPhenotypicValue());

		double deltaPheno=phenotype-lowEntry.getPhenotypicValue();
		double deltaFitness=k*deltaPheno;
		double interpolationResult=lowEntry.getFitness()+deltaFitness;
		return interpolationResult;
	}
}
