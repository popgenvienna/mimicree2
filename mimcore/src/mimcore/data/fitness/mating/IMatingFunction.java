package mimcore.data.fitness.mating;

import mimcore.data.MatePair;
import mimcore.data.Population;

import java.util.Random;

/**
 * Created by robertkofler on 8/29/14.
 */
public interface IMatingFunction {

	public abstract MatePair getCouple(Random random);
	public abstract IMatingFunction factory(Population population);
}
