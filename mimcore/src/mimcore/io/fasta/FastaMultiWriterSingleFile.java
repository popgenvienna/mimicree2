package mimcore.io.fasta;

import mimcore.data.fasta.FastaCollectionBuilder;
import mimcore.data.fasta.FastaRecord;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.io.haplotypes.HaplotypeReader;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 11/02/2018.
 */
public class FastaMultiWriterSingleFile implements IFastaMultiWriter {
    private final FastaWriter fastaWriter;

    private int counter;

    public FastaMultiWriterSingleFile(String outputFile, int width,Logger logger)
    {
        this.fastaWriter=new FastaWriter(outputFile,width,logger);
        this.counter=1;
    }


    public void writeHaploidGenomeRecords(ArrayList<FastaRecord> haploidGenomeRecords)
    {


            for(FastaRecord fr : haploidGenomeRecords) {
                this.fastaWriter.writeEntry(fr,"_mimhap"+counter);
            }
            counter++;

    }


    public void close()
    {
        this.fastaWriter.close();
    }

}
