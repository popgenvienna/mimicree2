package mimcore.io.fasta;

import mimcore.data.fasta.FastaRecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 11/02/2018.
 */
public class FastaMultiWriterDirectoryExtremeSplit implements IFastaMultiWriter {

    private final String outputDirectory;
    private final int width;
    private final Logger logger;
    private int counter;
    public FastaMultiWriterDirectoryExtremeSplit(String outputDirectory, int width, Logger logger)
    {
        this.outputDirectory=outputDirectory;
        this.width=width;
        this.logger=logger;
        this.counter=1;
    }



    public void writeHaploidGenomeRecords(ArrayList<FastaRecord> haploidGenomeRecords)
    {
        for(FastaRecord r: haploidGenomeRecords) {
            String header=r.getHeader();
            String outputFile = "";
            try {
                outputFile = new File(this.outputDirectory, header+"_mimhap_" + this.counter + ".fasta").getCanonicalPath();

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
            FastaWriter fw = new FastaWriter(outputFile, width, logger);
            fw.writeEntry(r);
            fw.close();
        }
        counter++;
    }


    public void close()
    {

    }

}
