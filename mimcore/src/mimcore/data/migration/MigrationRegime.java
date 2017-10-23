package mimcore.data.migration;

import mimcore.data.fitness.survival.ISelectionRegime;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 6/4/14.
 */
public class MigrationRegime implements IMigrationRegime {
	private final HashMap<Integer,Double> sr;
	private final double defMigration;


	public MigrationRegime(HashMap<Integer, Double> input) {
		sr = new HashMap<Integer,Double>(input);
		this.defMigration=0.0;

	}


	/**
	 * Return the migration rate (eg.: replace 50% of the evolved population by the base population
	 * Last migration rate will be used for all generations larger than the last entry
	 * @param generation
	 * @return
	 */
	public double getMigrationRate(int generation, int replicate)
	{
		if(generation<1)throw new IllegalArgumentException("No generations can be smaller 1");
		if(sr.containsKey(generation))
		{
			return sr.get(generation);
		}
		else
		{
			return defMigration;
		}

	}
}

