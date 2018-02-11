package mimcore.io.fasta;

import mimcore.data.SexedDiploids;
import mimcore.data.fasta.FastaRecord;

import java.util.ArrayList;

public interface IFastaMultiWriter {
	
	public abstract void writeHaploidGenomeRecords(ArrayList<FastaRecord> haploidGenomeRecords);

	public abstract void close();


}
