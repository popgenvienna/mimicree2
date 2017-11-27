package junit_mimcore.io;

import junit_mimcore.factories.GenomicDataFactory;
import junit_mimcore.factories.SharedFactory;
import mimcore.data.gpf.fitness.*;
import mimcore.data.haplotypes.SNP;
import mimcore.data.migration.MigrationEntry;
import mimcore.data.migration.MigrationRegime;
import mimcore.io.fitnessfunction.FitnessFunctionReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Test_FitnessFunctionReader {


	

	public static FitnessFunctionReader getGauss()
	{
		String input=
								"[stabilizing]\n"+
								"1\t0.5\t1.2\t10\t3\n"+
								"10\t0.5\t1.2\t20\t3\n"+
								"20\t0.5\t1.2\t30\t3\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new FitnessFunctionReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static FitnessFunctionReader getDisruptive()
	{
		String input=
				"[disruptive]\n"+
						"1\t0.5\t1.2\t10\t3\n"+
						"10\t0.5\t1.2\t20\t3\n"+
						"20\t0.5\t1.2\t30\t3\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new FitnessFunctionReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static FitnessFunctionReader getDimret()
	{
		String input=
						"[dimret]\n"+
						"1\t0.5\t1.2\t10\t3\n"+
						"10\t0.5\t1.2\t20\t3\n"+
						"20\t0.5\t1.2\t30\t3\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new FitnessFunctionReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static FitnessFunctionReader getDirsel()
	{
		String input=
				"[dirsel]\n"+
						"1\t0.5\t1.2\t10\t-0.4\t5\n"+
						"10\t0.5\t1.2\t20\t3\t2\n"+
						"20\t0.5\t1.2\t30\t3\t10\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new FitnessFunctionReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static FitnessFunctionReader getInterpolate()
	{
		String input=
								"[interpolate]\n"+
								"1\t0.0\t0.0\n" +
								"1\t0.5\t1.0\n" +
								"1\t1.0\t2.0\n" +
								"1\t1.5\t3.0\n" +
								"1\t2.0\t4.0\n" +
								"5\t0.0\t1.2\n" +
								"5\t0.5\t1.1\n" +
								"5\t1.0\t0.8\n" +
								"5\t1.5\t1.2\n" +
								"5\t2.0\t1.3\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new FitnessFunctionReader("fakefile",br, SharedFactory.getNullLogger());

	}
	
	@Test
	public void gauss_correctly_identified()
	{
		FitnessFunctionContainer ffc=getGauss().readFitnessFunction();


		assertTrue(ffc.getFitnessCalculator(1,1) instanceof FitnessFunctionQuantitativeGauss);
		assertTrue(ffc.getFitnessCalculator(10,1) instanceof FitnessFunctionQuantitativeGauss);
		assertTrue(ffc.getFitnessCalculator(1,10) instanceof FitnessFunctionQuantitativeGauss);
		assertTrue(ffc.getFitnessCalculator(1,20) instanceof FitnessFunctionQuantitativeGauss);

	}

	@Test
	public void gauss_correctly_read()
	{
		FitnessFunctionContainer ffc=getGauss().readFitnessFunction();
		//if (!(o instanceof SNP)) {
		//SNP tc = (SNP) o;
		FitnessFunctionQuantitativeGauss g=(FitnessFunctionQuantitativeGauss)ffc.getFitnessCalculator(1,1);

		assertEquals(g.getMinFitness(),0.5,0.000001);
		assertEquals(g.getMaxFitness(),1.2,0.000001);
		assertEquals(g.getMean(),10,0.000001);
		assertEquals(g.getStdev(),3,0.000001);
	}

	@Test
	public void diminishing_returns_correctly_identified()
	{
		FitnessFunctionContainer ffc=getDimret().readFitnessFunction();


		assertTrue(ffc.getFitnessCalculator(1,1) instanceof FitnessFunctionQuantitativeDimRet);
		assertTrue(ffc.getFitnessCalculator(10,1) instanceof FitnessFunctionQuantitativeDimRet);
		assertTrue(ffc.getFitnessCalculator(1,10) instanceof FitnessFunctionQuantitativeDimRet);
		assertTrue(ffc.getFitnessCalculator(1,20) instanceof FitnessFunctionQuantitativeDimRet);

	}

	@Test
	public void diminishing_returns_correctly_read()
	{
		FitnessFunctionContainer ffc=getDimret().readFitnessFunction();
		//if (!(o instanceof SNP)) {
		//SNP tc = (SNP) o;
		FitnessFunctionQuantitativeDimRet g=(FitnessFunctionQuantitativeDimRet)ffc.getFitnessCalculator(1,1);

		assertEquals(g.getMinFitness(),0.5,0.000001);
		assertEquals(g.getMaxFitness(),1.2,0.000001);
		assertEquals(g.getAlpha(),10,0.000001);
		assertEquals(g.getBeta(),3,0.000001);
	}


	@Test
	public void interpolate_correctly_identified()
	{
		FitnessFunctionContainer ffc=getInterpolate().readFitnessFunction();


		assertTrue(ffc.getFitnessCalculator(1,1) instanceof FitnessFunctionArbitraryLandscape);
		assertTrue(ffc.getFitnessCalculator(10,1) instanceof FitnessFunctionArbitraryLandscape);
		assertTrue(ffc.getFitnessCalculator(1,10) instanceof FitnessFunctionArbitraryLandscape);
		assertTrue(ffc.getFitnessCalculator(1,20) instanceof FitnessFunctionArbitraryLandscape);

	}


	@Test
	public void interpolate_correctly_read()
	{
		FitnessFunctionContainer ffc=getInterpolate().readFitnessFunction();

		FitnessFunctionArbitraryLandscape g=(FitnessFunctionArbitraryLandscape)ffc.getFitnessCalculator(1,1);
		assertEquals(g.getFitness(null,0),0.0,0.00001);
		assertEquals(g.getFitness(null,0.5),1.0,0.00001);
		assertEquals(g.getFitness(null,1.7),3.4,0.00001);

	}



	@Test
	public void directional_selection_correctly_identified()
	{
		FitnessFunctionContainer ffc=getDirsel().readFitnessFunction();


		assertTrue(ffc.getFitnessCalculator(1,1) instanceof FitnessFunctionQuantitativeDirectionalSelection);
		assertTrue(ffc.getFitnessCalculator(10,1) instanceof FitnessFunctionQuantitativeDirectionalSelection);
		assertTrue(ffc.getFitnessCalculator(1,10) instanceof FitnessFunctionQuantitativeDirectionalSelection);
		assertTrue(ffc.getFitnessCalculator(1,20) instanceof FitnessFunctionQuantitativeDirectionalSelection);

	}

	@Test
	public void directional_selection_correctly_read()
	{
		FitnessFunctionContainer ffc=getDirsel().readFitnessFunction();
		//if (!(o instanceof SNP)) {
		//"1\t0.5\t1.2\t10\t3\t5\n"+
		FitnessFunctionQuantitativeDirectionalSelection g=(FitnessFunctionQuantitativeDirectionalSelection)ffc.getFitnessCalculator(1,1);

		assertEquals(g.getMinFitness(),0.5,0.000001);
		assertEquals(g.getMaxFitness(),1.2,0.000001);
		assertEquals(g.getS(),10,0.000001);
		assertEquals(g.getR(),-0.4,0.000001);
		assertEquals(g.getBeta(),5,0.000001);
	}




	@Test
	public void disruptive_correctly_identified()
	{
		FitnessFunctionContainer ffc=getDisruptive().readFitnessFunction();


		assertTrue(ffc.getFitnessCalculator(1,1) instanceof FitnessFunctionQuantitativeDisruptive);
		assertTrue(ffc.getFitnessCalculator(10,1) instanceof FitnessFunctionQuantitativeDisruptive);
		assertTrue(ffc.getFitnessCalculator(1,10) instanceof FitnessFunctionQuantitativeDisruptive);
		assertTrue(ffc.getFitnessCalculator(1,20) instanceof FitnessFunctionQuantitativeDisruptive);

	}

	@Test
	public void disruptive_correctly_read()
	{
		FitnessFunctionContainer ffc=getDisruptive().readFitnessFunction();
		//if (!(o instanceof SNP)) {
		//SNP tc = (SNP) o;
		FitnessFunctionQuantitativeDisruptive g=(FitnessFunctionQuantitativeDisruptive)ffc.getFitnessCalculator(1,1);

		assertEquals(g.getMinFitness(),0.5,0.000001);
		assertEquals(g.getMaxFitness(),1.2,0.000001);
		assertEquals(g.getMean(),10,0.000001);
		assertEquals(g.getStdev(),3,0.000001);
	}


	

}
