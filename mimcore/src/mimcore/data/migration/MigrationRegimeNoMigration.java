package mimcore.data.migration;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 6/4/14.
 */
public class MigrationRegimeNoMigration implements IMigrationRegime {



	public MigrationRegimeNoMigration()
		{

		}


	/**
	 * Return the migration rate (eg.: replace 50% of the evolved population by the base population
	 * Last migration rate will be used for all generations larger than the last entry
	 * @param generation
	 * @return
	 */
	public double getMigrationRate(int generation, int replicate)
	{
		return 0.0D;
	}
}

