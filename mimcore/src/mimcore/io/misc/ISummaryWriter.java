package mimcore.io.misc;

import mimcore.data.statistic.PopulationAlleleCount;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: robertkofler
 * Date: 3/6/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISummaryWriter {

	public abstract void write(ArrayList<PopulationAlleleCount> pacs);

}