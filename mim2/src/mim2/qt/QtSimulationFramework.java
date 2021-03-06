package mim2.qt;

import mim2.shared.GPFHelper;
import mim2.shared.GlobalResourceManager;
import mimcore.data.gpf.fitness.FitnessCalculatorAllEqual;
import mimcore.data.gpf.fitness.IFitnessCalculator;
import mimcore.data.gpf.quantitative.GenotypeCalculatorAllEqual;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.gpf.survival.*;
import mimcore.io.*;
import mimcore.io.selectionregime.*;

import java.io.File;

public class QtSimulationFramework {
	private final String effectSizeFile;
	private final String selectionRegimeFile;
	private final Double ve;
	private final Double heritability;


	private final java.util.logging.Logger logger;

	public QtSimulationFramework( String effectSizeFile, Double ve, Double heritability, String selectionRegimeFile)
	{
		this.logger= GlobalResourceManager.getLogger();
		if(! new File(effectSizeFile).exists()) logger.info("No effect size file found; Commencing neutral simulations");
		// selection regime
		if(selectionRegimeFile == null)logger.info("No selection regime file found; Commencing neutral simulations");
		else if (! new File(selectionRegimeFile).exists()) throw new IllegalArgumentException("Selection regime file does not exist; "+selectionRegimeFile);
		if((heritability==null) && (ve == null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided");
		if((heritability!=null) && (ve != null)) throw new IllegalArgumentException("Either ve or the heritability needs to be provided, NOT BOTH");


		this.effectSizeFile=effectSizeFile;
		this.selectionRegimeFile=selectionRegimeFile;
		this.ve=ve;
		this.heritability=heritability;

	}
	
	
	public void run()
	{




		// Compute GPF
		IGenotypeCalculator genotypeCalculator= new GenotypeCalculatorAllEqual();
		if(this.effectSizeFile!=null) genotypeCalculator=new SNPQuantitativeEffectSizeReader(this.effectSizeFile,this.logger).readAdditiveFitness();
		PhenotypeCalculator phenotypeCalculator= GPFHelper.getPhenotypeCalculator(genotypeCalculator,this.ve,this.heritability);
		IFitnessCalculator fitnessCalculator=new FitnessCalculatorAllEqual();

		// Survival function (truncating selection); If none specified all survive
		ISurvivalFunction survivalFunction= new SurvivalRegimeAllSurvive();
		if(selectionRegimeFile != null)
		{
			ISelectionRegime selectionRegime=new SelectionRegimeReader(this.selectionRegimeFile,this.logger).readSelectionRegime();
			survivalFunction=new SurvivalRegimeTruncatingSelection(selectionRegime);
		}



		MultiSimulationQT mst=new MultiSimulationQT(genotypeCalculator,phenotypeCalculator,fitnessCalculator,survivalFunction);
		mst.run();

		this.logger.info("Finished simulations");
	}


}
