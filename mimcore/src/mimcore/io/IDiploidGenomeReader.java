package mimcore.io;

import mimcore.data.DiploidGenome;

import java.util.ArrayList;

public interface IDiploidGenomeReader {
	
	public abstract ArrayList<DiploidGenome> readGenomes();

}
