package mimcore.data.statistic;

import java.util.ArrayList;

/**
 * Represents summary statistics for a single population
 */
public class GPFCollection {
	private final ArrayList<GPF> gpfcollection;
	private final int generation;
	private final int replicate;

	public GPFCollection(ArrayList<GPF> gpfcollection, int replicate, int generation)
	{
		this.gpfcollection=new ArrayList<GPF>(gpfcollection);
		this.replicate=replicate;
		this.generation=generation;
	}


	public int getGeneration()
	{
		return this.generation;
	}

	public int getReplicate()
	{
		return this.replicate;
	}

	public ArrayList<GPF> getPGFs()
	{
		return new ArrayList<GPF>(this.gpfcollection);
	}

	


	
}
