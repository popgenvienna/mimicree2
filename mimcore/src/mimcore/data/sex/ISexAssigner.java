package mimcore.data.sex;

import mimcore.data.MatePair;
import mimcore.data.Population;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robertkofler on 8/29/14.
 */
public interface ISexAssigner {

	public abstract ArrayList<Sex> getSexes(int popSize, Random random);

	public abstract boolean isValid();

}
