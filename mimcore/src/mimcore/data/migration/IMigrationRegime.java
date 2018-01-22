package mimcore.data.migration;

import mimcore.data.DiploidGenome;
import mimcore.data.SexedDiploids;

import java.util.ArrayList;

/**
 * Created by robertkofler on 7/16/14.
 */
public interface IMigrationRegime {

	public abstract SexedDiploids getMigrants(int generation, int replicate);
}
