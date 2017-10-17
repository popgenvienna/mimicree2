package mimcore.data.migration;

/**
 * Created by robertkofler on 7/16/14.
 */
public interface IMigrationRegime {

	public abstract double getMigrationRate(int generation, int replicate);
}
