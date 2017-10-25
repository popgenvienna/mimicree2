package mimcore.data.gpf.survival;

import mimcore.data.Population;

/**
 * Created by robertkofler on 11/20/16.
 */
public class SurvivalRegimeAllSurvive implements ISurvivalFunction {

	public SurvivalRegimeAllSurvive()
	{

	}
	public  Population getSurvivors(Population population, int generation, int replicate)
	{
		return population;
	}

	public double getSurvivorFraction(int generation, int replicate){return 1.0;}
}
