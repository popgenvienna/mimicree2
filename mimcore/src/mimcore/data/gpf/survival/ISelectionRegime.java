package mimcore.data.gpf.survival;

/**
 * Created by robertkofler on 7/16/14.
 */
public interface ISelectionRegime {

	public abstract double getSelectionIntensity(int generation, int replicate);
}
