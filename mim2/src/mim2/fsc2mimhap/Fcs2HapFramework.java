package mim2.fsc2mimhap;

import mim2.fsc2mimhap.io.ArlequinReader;
import mimcore.data.Chromosome;
import mimcore.data.DiploidGenome;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.haplotypes.HaplotypeWriter;

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
    private final Chromosome chrom;
    private final boolean haploid;

    private Logger logger;

    public Fcs2HapFramework(String inputFile, String outputFile,String chromosomeName, boolean haploid, Logger logger)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.chrom=Chromosome.getChromosome(chromosomeName);
        this.haploid=haploid;
        this.logger = logger;
    }

    public void run()
    {
        ArrayList<HaploidGenome> haploidGenomes=new ArlequinReader(inputFile,haploid,chrom,logger).getHaplotypes();

    }


}
