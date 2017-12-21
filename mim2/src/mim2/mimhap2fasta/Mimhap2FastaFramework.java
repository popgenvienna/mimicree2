package mim2.mimhap2fasta;

import mim2.shared.SimulationMode;
import mim2.w.MultiSimulationW;
import mimcore.data.DiploidGenome;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Mutator.MutatorGenomeWideRate;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.fasta.FastaCollectionBuilder;
import mimcore.data.fasta.FastaRecord;
import mimcore.data.gpf.fitness.FitnessCalculatorAllEqual;
import mimcore.data.gpf.fitness.FitnessCalculator_SNPandEpistasis;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.gpf.quantitative.GenotypeCalculatorAllEqual;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.IPhenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculatorAllEqual;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.gpf.survival.SurvivalRegimeAllSurvive;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.PopulationSizeReader;
import mimcore.io.fasta.FastaReader;
import mimcore.io.fasta.FastaWriter;
import mimcore.io.haplotypes.HaplotypeReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.io.recombination.RecombinationRateReader;
import mimcore.io.w.EpistasisFitnessReader;
import mimcore.io.w.SNPFitnessReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Mimhap2FastaFramework {
	private final String referenceFile;
	private final String mimhapFile;
	private final String outputFasta;
	private final boolean stringent;





	private final java.util.logging.Logger logger;
	//(haplotypeFile,recombinationFile,chromosomeDefinition,fitnessFile,epistasisFile,migrationRegimeFile,outputSync,outputGPF,outputDir,simMode,replicateRuns,logger);

	public Mimhap2FastaFramework(String referenceFile, String mimhapFile, String outputFasta, boolean stringent, Logger logger)
                        	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(referenceFile).exists()) throw new IllegalArgumentException("Reference file does not exist "+referenceFile);
		if(! new File(mimhapFile).exists()) throw new IllegalArgumentException("MimicrEE2 haploytpe file does not exist " + mimhapFile);
		try {new File(outputFasta).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create output sync file "+outputFasta);}




		this.referenceFile=referenceFile;
		this.mimhapFile=mimhapFile;
		this.outputFasta=outputFasta;
		this.stringent=stringent;
		this.logger=logger;
	}


	public void run()
	{
		this.logger.info("Starting converting MimicrEE2 haplotype file to fasta file");


		ArrayList<FastaRecord> refGenome= FastaReader.readAll(this.referenceFile,this.logger);

		ArrayList<HaploidGenome> genomes= new HaplotypeReader(this.mimhapFile,this.logger).getHaplotypes();

		FastaWriter writer=new FastaWriter(this.outputFasta,60,logger);
		int counter=1;
		for(HaploidGenome haploidGenome: genomes)
		{
			FastaCollectionBuilder fcb=new FastaCollectionBuilder(refGenome,this.stringent);
			fcb.introduceChanges(haploidGenome);
			ArrayList<FastaRecord> records=fcb.getRecords("_mimhap"+counter);
			for(FastaRecord fr : records) {
				writer.writeEntry(fr);
			}
			counter++;
		}
		writer.close();



		this.logger.info("Finished conversions");
	}



}
