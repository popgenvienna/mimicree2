package mimcore.io;

import mimcore.data.DiploidGenome;
import mimcore.data.SexedDiploids;
import mimcore.data.sex.ISexAssigner;

import java.util.ArrayList;

public interface IDiploidGenomeReader {
	
	public abstract SexedDiploids readGenomes();


}
