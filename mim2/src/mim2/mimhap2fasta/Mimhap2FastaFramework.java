package mim2.mimhap2fasta;

import mimcore.data.fasta.FastaCollectionBuilder;
import mimcore.data.fasta.FastaRecord;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.io.fasta.*;
import mimcore.io.haplotypes.HaplotypeReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Mimhap2FastaFramework {
	private final String referenceFile;
	private final String mimhapFile;
	private final String outputFasta;
	private final String outputDir;
	private final boolean extremeSplit;
	private final boolean stringent;
	private final boolean haploid;




	private final java.util.logging.Logger logger;
	//(haplotypeFile,recombinationFile,chromosomeDefinition,fitnessFile,epistasisFile,migrationRegimeFile,outputSync,outputGPF,outputDir,simMode,replicateRuns,logger);

	public Mimhap2FastaFramework(String referenceFile, String mimhapFile, String outputFasta, String outputDir, boolean extremeSplit, boolean haploid, boolean stringent, Logger logger)
                        	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(referenceFile).exists()) throw new IllegalArgumentException("Reference file does not exist "+referenceFile);
		if(! new File(mimhapFile).exists()) throw new IllegalArgumentException("MimicrEE2 haploytpe file does not exist " + mimhapFile);

		if((outputDir == null) && (outputFasta==null)) throw new IllegalArgumentException("No output was provided; Provide either an output directory or an output fasta file");
		if((outputDir != null) && (outputFasta!=null)) throw new IllegalArgumentException("Too many output parameters; Provide either an output directory or an output fasta file, NOT BOTH");
		if((outputDir == null) && extremeSplit) throw new IllegalArgumentException("Invalid parameter combination; Splitting by chromosome only supported for output directories");

		if(outputFasta!=null) try {new File(outputFasta).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create output fasta file "+outputFasta);}
		if(outputDir!= null && (!new File(outputDir).exists())) throw new IllegalArgumentException("The provided output directory does not exist "+outputDir);


		this.referenceFile=referenceFile;
		this.mimhapFile=mimhapFile;
		this.outputFasta=outputFasta;
		this.outputDir=outputDir;
		this.extremeSplit=extremeSplit;
		this.stringent=stringent;
		this.haploid=haploid;
		this.logger=logger;
	}


	public void run()
	{
		this.logger.info("Starting converting MimicrEE2 haplotype file to fasta file");


		ArrayList<FastaRecord> refGenome= FastaReader.readAll(this.referenceFile,this.logger);

		ArrayList<HaploidGenome> genomes= new HaplotypeReader(this.mimhapFile,haploid,this.logger).getHaplotypes();

		IFastaMultiWriter writer=null;
		if(outputFasta!=null) {
			writer = new FastaMultiWriterSingleFile(this.outputFasta, 60, logger);
		}
		else if(outputDir!=null)
		{
			// boolean only two options possible
			if(extremeSplit) writer =new FastaMultiWriterDirectoryExtremeSplit(outputDir,60,logger);
			else writer =new FastaMultiWriterDirectory(outputDir,60,logger);
		}
		else throw new IllegalArgumentException("Invalid output options");


		// now do the actual work; introduce SNPs into the sequences and save the output
		for(int i=0; i<genomes.size();i++)
		{
			if(haploid && i%2==1)continue; // for haploids ignore every second genome
			HaploidGenome haploidGenome= genomes.get(i);
			FastaCollectionBuilder fcb=new FastaCollectionBuilder(refGenome,this.stringent);
			fcb.introduceChanges(haploidGenome);
			ArrayList<FastaRecord> records=fcb.getRecords();
			writer.writeHaploidGenomeRecords(records);

		}
		writer.close();



		this.logger.info("Finished conversions");
	}



}
