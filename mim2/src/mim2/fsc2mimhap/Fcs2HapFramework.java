package mim2.fsc2mimhap;

import mimcore.data.haplotypes.Haplotype;
import mimcore.io.haplotypes.HaplotypeWriter;
import mimcore.io.misc.FscReader;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: robertkofler
 * Date: 1/18/13
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class Fcs2HapFramework {
    private final String inputFile;
    private final String outputFile;
    private final String chromosomeName;

    private Logger logger;

    public Fcs2HapFramework(String inputFile, String outputFile,String chromosomeName,Logger logger)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.chromosomeName=chromosomeName;
        this.logger = logger;
    }

    public void run()
    {
        ArrayList<Haplotype>  haplotypes=new FscReader(this.inputFile,this.chromosomeName,this.logger).getHaplotypes();
		if(!(haplotypes.size()>0)) throw new IllegalArgumentException("Failed to read haplotypes, zero haplotypes red");
        new HaplotypeWriter(this.outputFile,this.logger).write(haplotypes);
    }


}
