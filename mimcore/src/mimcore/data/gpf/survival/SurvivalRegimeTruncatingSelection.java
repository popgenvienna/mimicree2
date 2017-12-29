package mimcore.data.gpf.survival;

import mimcore.data.Population;
import mimcore.data.Specimen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by robertkofler on 11/20/16.
 */
public class SurvivalRegimeTruncatingSelection implements ISurvivalFunction {
	private final ISelectionRegime sr;

	public SurvivalRegimeTruncatingSelection(ISelectionRegime sr)
	{
		   this.sr=sr;
	}
	public  Population getSurvivors(Population population, int generation, int replicate)
	{
		double selectionIntensity=sr.getSelectionIntensity(generation, replicate);
		return getPenotypicTail(population,selectionIntensity);
	}

	/**
	 * Get the fraction of individuals having the most extreme phenotypes
	 * @param selectedFraction
	 * @return
	 */
	private Population getPenotypicTail(Population population, double selectedFraction)
	{
		ArrayList<Specimen> specs=population.getSpecimen();
		if(selectedFraction>=0) {
			Collections.sort(specs, new Comparator<Specimen>() {
				@Override
				public int compare(Specimen s1, Specimen s2) {
					if (s1.quantPhenotype() < s2.quantPhenotype()) return 1;
					if (s1.quantPhenotype() > s2.quantPhenotype()) return -1;
					return 0;
				}
			});
		}
		else
		{
			selectedFraction=-1.0*selectedFraction;
			Collections.sort(specs, new Comparator<Specimen>() {
				@Override
				public int compare(Specimen s1, Specimen s2) {
					if (s1.quantPhenotype() < s2.quantPhenotype()) return -1;
					if (s1.quantPhenotype() > s2.quantPhenotype()) return 1;
					return 0;
				}
			});

		}

		ArrayList<Specimen> toret=new ArrayList<Specimen>();
		int toselect=(int)((double)specs.size()*selectedFraction);
		for(int i=0; i<toselect; i++)
		{
			toret.add(specs.get(i));
		}
		return new Population(toret);
	}

	/** security
	 * 	private Population getPenotypicTail(Population population, double selectedFraction)
	 {
	 ArrayList<Specimen> specs=population.getSpecimen();
	 Collections.sort(specs, new Comparator<Specimen>() {
	@Override
	public int compare(Specimen s1, Specimen s2) {
	if (s1.quantPhenotype() < s2.quantPhenotype()) return 1;
	if (s1.quantPhenotype() > s2.quantPhenotype()) return -1;
	return 0;
	}
	});

	 ArrayList<Specimen> toret=new ArrayList<Specimen>();
	 int toselect=(int)((double)specs.size()*selectedFraction);
	 for(int i=0; i<toselect; i++)
	 {
	 toret.add(specs.get(i));
	 }
	 return new Population(toret);
	 }
	 * @param generation
	 * @param replicate
	 * @return
	 */

	public double getSurvivorFraction(int generation, int replicate) {
		return sr.getSelectionIntensity(generation, replicate);
	}
}
