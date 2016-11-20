package mimcore.data.fitness.survival;

import mimcore.data.Population;

/**
 * Created by robertkofler on 11/20/16.
 */
public class SurvivalRegimeTruncatingSelection implements ISurvivalFunction {
	private final ISelectionRegime sr;

	public SurvivalRegimeTruncatingSelection(ISelectionRegime sr)
	{
		   this.sr=sr;
	}
	public  Population selectSuitable(Population population, int generation, int replicate)
	{
		double selectionIntensity=sr.getSelectionIntensity(generation, replicate);
		return population;
	}
}
