package mim2.depr_qt_hap;

import mim2.shared.SimulationMode;
import mimcore.data.DiploidGenome;
import mimcore.data.gpf.fitness.FitnessCalculatorAllEqual;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.gpf.quantitative.GenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.gpf.survival.ISelectionRegime;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.gpf.survival.SurvivalRegimeTruncatingSelection;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.SNPQuantitativeEffectSizeReader;
import mimcore.io.RecombinationRateReader;
import mimcore.io.selectionregime.SelectionRegimeReader;

import java.io.File;
import java.util.ArrayList;

public class QtSimulationFrameworkHaplotype {
	private final String haplotypeFile;
	private final String recombinationFile;
	private final String chromosomeDefinition;
	private final String effectSizeFile;
	private final String selectionRegimeFile;
	private final String outputDir;
	private final double ve;
	private final SimulationMode simMode;
	private final int replicateRuns;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QtSimulationFrameworkHaplotype(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String effectSizeFile, double ve,
										String selectionRegimeFile, String outputDir,  SimulationMode simMode, int replicateRuns, java.util.logging.Logger logger)
	{
		// 'File' represents files and directories
		// Test if input files exist
		if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
		if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations\n");
		if(! new File(selectionRegimeFile).exists()) logger.info("No selection regime file found; Commencing neutral simulations\n");
		if(! new File(outputDir).exists()) throw new IllegalArgumentException("Output directory does not exist\n");


		
		if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);

		this.outputDir=outputDir;
		this.effectSizeFile=effectSizeFile;
		this.haplotypeFile=haplotypeFile;
		this.recombinationFile=recombinationFile;
		this.chromosomeDefinition=chromosomeDefinition;
		this.selectionRegimeFile=selectionRegimeFile;
		this.ve=ve;
		this.simMode=simMode;
		this.replicateRuns=replicateRuns;
		this.logger=logger;
	}
	
	
	public void run()
	{
		this.logger.info("Starting qt-hap");


		// Load the data
		RecombinationGenerator recGenerator = new RecombinationGenerator(new RecombinationRateReader(this.recombinationFile,this.logger).getRecombinationRate(),
				new ChromosomeDefinitionReader(this.chromosomeDefinition).getRandomAssortmentGenerator());

		ArrayList<DiploidGenome> dipGenomes=new DiploidGenomeReader(this.haplotypeFile,"",this.logger).readGenomes();

		GenotypeCalculator genotypeCalculator=new SNPQuantitativeEffectSizeReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator=getPhenotypeCalculator(dipGenomes,genotypeCalculator,this.ve);
		IFitnessCalculator fitnessCalculator=new FitnessCalculatorAllEqual();

		ISelectionRegime selectionRegime=new SelectionRegimeReader(this.selectionRegimeFile,this.logger).readSelectionRegime();
		ISurvivalFunction survivalFunction=new SurvivalRegimeTruncatingSelection(selectionRegime);

		MultiSimulationTimestampHaplotype mshap=new MultiSimulationTimestampHaplotype(dipGenomes,genotypeCalculator,phenotypeCalculator,fitnessCalculator,survivalFunction,
			recGenerator,simMode.getTimestamps(),this.replicateRuns,this.outputDir,this.logger);
		mshap.run();

		this.logger.info("Finished simulations");
	}

	public PhenotypeCalculator getPhenotypeCalculator(ArrayList<DiploidGenome> dipGenomes, GenotypeCalculator genotypeCalculator,double ve)
	{
		ArrayList<Double> genotypes=new ArrayList<Double>();
		for(DiploidGenome dg: dipGenomes)
		{
			genotypes.add(genotypeCalculator.getGenotype(dg));
		}

		double mean=0.0;
		for(double d: genotypes)
		{
			mean+=d;
		}
		mean= mean/((double)genotypes.size());

		double variance=0.0;
		for(double d: genotypes)
		{
			double ss=Math.pow((d - mean), 2.0);
			variance+=ss;
		}
		variance= variance/((double)genotypes.size());
		//return new PhenotypeCalculator(ve,variance);
		return null;
	}

}
