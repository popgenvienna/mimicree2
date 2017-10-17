package mimcore.data.migration;

import mimcore.data.fitness.survival.ISelectionRegime;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 6/4/14.
 */
public class MigrationRegimeDefault implements IMigrationRegime {
	private final HashMap<Integer,Double> sr;
	private final double def;


	public MigrationRegimeDefault(HashMap<Integer, Double> input)
		{
			LinkedList<Integer> keys = new LinkedList<Integer>(input.keySet());
			Collections.sort(keys);
			int lastGenerations=keys.peekLast();
			Double defMigration=null;

			HashMap<Integer,Double> store=new HashMap<Integer, Double>();
			for(int i=1;i<=lastGenerations; i++)
			{
				if(input.containsKey(i))defMigration=input.get(i);
				if(defMigration==null) throw new IllegalArgumentException("You did not provide a migration rate for the first generations");
				store.put(i,defMigration);

			}
			def=defMigration;
			sr=store;
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
			return def;
		}

	}
}

