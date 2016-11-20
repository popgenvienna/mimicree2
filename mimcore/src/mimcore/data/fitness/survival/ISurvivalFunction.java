package mimcore.data.fitness.survival;

import mimcore.data.Population;

/**
 * Created by robertkofler on 11/20/16.
 */
public interface ISurvivalFunction {
	public abstract Population selectSuitable(Population population, int generation, int replicate);
}
