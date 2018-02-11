package mim2.qs;

import mim2.shared.GPFHelper;
import mim2.shared.GlobalResourceManager;
import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.gpf.quantitative.GenotypeCalculatorAllEqual;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.gpf.survival.ISurvivalFunction;
import mimcore.data.gpf.survival.SurvivalRegimeAllSurvive;
import mimcore.io.*;
import mimcore.io.fitnessfunction.FitnessFunctionReader;

import java.io.File;

public class QsSimulationFramework {

	private final String effectSizeFile;
	private final String fitnessFunctionFile;
	private final Double ve;
	private final Double heritability;



	private final java.util.logging.Logger logger;
	//chromosomeDefinition,   	effectSizeFile,heritability,selectionRegimFile,outputFile,simMode,
	public QsSimulationFramework(String effectSizeFile, Double ve, Double heritability, String fitnessFunctionFile)
	{
		this.logger= GlobalResourceManager.getLogger();

		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations");
		// fitness function
		if(!new File(fitnessFunctionFile).exists()) throw new IllegalArgumentException("Fitness function file does not exist; "+fitnessFunctionFile);

		if((heritability==null) && (ve == null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided");
		if((heritability!=null) && (ve != null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided, NOT BOTH");

		this.effectSizeFile=effectSizeFile;

		this.fitnessFunctionFile=fitnessFunctionFile;
		this.ve=ve;
		this.heritability=heritability;
	}


	public void run()
	{

		// Compute GPF
		IGenotypeCalculator genotypeCalculator = new GenotypeCalculatorAllEqual();
		if(this.effectSizeFile!=null) genotypeCalculator=new SNPQuantitativeEffectSizeReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator= GPFHelper.getPhenotypeCalculator(genotypeCalculator,this.ve,this.heritability);
		FitnessFunctionContainer ffc=new FitnessFunctionReader(this.fitnessFunctionFile,this.logger).readFitnessFunction();

		ISurvivalFunction survivalFunction= new SurvivalRegimeAllSurvive();


		MultiSimulationQS ms=new MultiSimulationQS(genotypeCalculator,phenotypeCalculator,ffc,survivalFunction);
		ms.run();

		this.logger.info("Finished simulations");
	}



}
