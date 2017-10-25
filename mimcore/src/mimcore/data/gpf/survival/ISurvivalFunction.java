package mimcore.data.gpf.survival;

import mimcore.data.Population;

/**
 * Created by robertkofler on 11/20/16.
 */
public interface ISurvivalFunction {
	public abstract Population getSurvivors(Population population, int generation, int replicate);

	public double getSurvivorFraction(int generation, int replicate);
}
