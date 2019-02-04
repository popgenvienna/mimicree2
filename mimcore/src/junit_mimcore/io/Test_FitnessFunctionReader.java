package junit_mimcore.io;

import junit_mimcore.factories.GenomicDataFactory;
import junit_mimcore.factories.SharedFactory;
import mimcore.data.gpf.fitness.*;
import mimcore.data.haplotypes.SNP;
import mimcore.data.migration.MigrationEntry;
import mimcore.data.migration.MigrationRegime;
import mimcore.data.sex.Sex;
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

	public static FitnessFunctionReader getGaussSexSpecific()
	{
		String input=
						"[stabilizing]\n"+
						"1\t0.5\t1.2\t10\t3\t2.0\t2.3\t20\t22\t3.0\t3.3\t30\t33\n"+
						"10\t0.5\t1.2\t20\t3\t2.0\t2.4\t40\t55\t3.0\t3.3\t60\t66\n"+
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

	public static FitnessFunctionReader getDisruptiveSexSpecific()
	{
		String input=
				"[disruptive]\n"+
						"1\t0.5\t1.2\t10\t3\t2.0\t2.3\t20\t22\t3.0\t3.3\t30\t33\n"+
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

	public static FitnessFunctionReader getDirselSexSpecific()
	{
		String input=
				"[dirsel]\n"+
						"1\t0.5\t1.2\t10\t-0.4\t5\t0.6\t1.3\t11\t-0.5\t6\t0.7\t1.4\t12\t-0.6\t7\n"+
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
	public void gauss_sex_specific()
	{
		FitnessFunctionContainer ffc=getGaussSexSpecific().readFitnessFunction();
		//if (!(o instanceof SNP)) {
		//"1\t0.5\t1.2\t10\t3     2.0\t2.3\t20\t22\t    3.0\t3.3\t30\t33\n"+
		FitnessCalculatorSexSpecific fcss=(FitnessCalculatorSexSpecific)ffc.getFitnessCalculator(1,1);

		FitnessFunctionQuantitativeGauss m=(FitnessFunctionQuantitativeGauss)fcss.getMale();
		assertEquals(m.getMinFitness(),0.5,0.000001);
		assertEquals(m.getMaxFitness(),1.2,0.000001);
		assertEquals(m.getMean(),10,0.000001);
		assertEquals(m.getStdev(),3,0.000001);


		FitnessFunctionQuantitativeGauss f=(FitnessFunctionQuantitativeGauss)fcss.getFemale();
		assertEquals(f.getMinFitness(),2.0,0.000001);
		assertEquals(f.getMaxFitness(),2.3,0.000001);
		assertEquals(f.getMean(),20,0.000001);
		assertEquals(f.getStdev(),22,0.000001);

		FitnessFunctionQuantitativeGauss h=(FitnessFunctionQuantitativeGauss)fcss.getHermaphrodite();
		assertEquals(h.getMinFitness(),3.0,0.000001);
		assertEquals(h.getMaxFitness(),3.3,0.000001);
		assertEquals(h.getMean(),30,0.000001);
		assertEquals(h.getStdev(),33,0.000001);

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
		Sex m= Sex.Male;
		FitnessFunctionArbitraryLandscape g=(FitnessFunctionArbitraryLandscape)ffc.getFitnessCalculator(1,1);
		assertEquals(g.getFitness(null,0,m),0.0,0.00001);
		assertEquals(g.getFitness(null,0.5,m),1.0,0.00001);
		assertEquals(g.getFitness(null,1.7,m),3.4,0.00001);

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
	public void directional_selection_sex_specific() {
		FitnessFunctionContainer ffc = getDirselSexSpecific().readFitnessFunction();

		//"1\t0.5\t1.2\t10\t-0.4\t5\t0.6\t1.3\t11\t-0.5\t6\t0.7\t1.4\t12\t-0.6\t7\n"+
		FitnessCalculatorSexSpecific ff = (FitnessCalculatorSexSpecific) ffc.getFitnessCalculator(1, 1);


		FitnessFunctionQuantitativeDirectionalSelection m = (FitnessFunctionQuantitativeDirectionalSelection) ff.getMale();
		FitnessFunctionQuantitativeDirectionalSelection f = (FitnessFunctionQuantitativeDirectionalSelection) ff.getFemale();
		FitnessFunctionQuantitativeDirectionalSelection h = (FitnessFunctionQuantitativeDirectionalSelection) ff.getHermaphrodite();

		assertEquals(m.getMinFitness(), 0.5, 0.000001);
		assertEquals(m.getMaxFitness(), 1.2, 0.000001);
		assertEquals(m.getS(), 10, 0.000001);
		assertEquals(m.getR(), -0.4, 0.000001);
		assertEquals(m.getBeta(), 5, 0.000001);


		assertEquals(f.getMinFitness(), 0.6, 0.000001);
		assertEquals(f.getMaxFitness(), 1.3, 0.000001);
		assertEquals(f.getS(), 11, 0.000001);
		assertEquals(f.getR(), -0.5, 0.000001);
		assertEquals(f.getBeta(), 6, 0.000001);

		assertEquals(h.getMinFitness(),0.7,0.000001);
		assertEquals(h.getMaxFitness(),1.4,0.000001);
		assertEquals(h.getS(),12,0.000001);
		assertEquals(h.getR(),-0.6,0.000001);
		assertEquals(h.getBeta(),7,0.000001);
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

	@Test
	public void disruptive_sex_specific()
	{
		FitnessFunctionContainer ffc=getDisruptiveSexSpecific().readFitnessFunction();
		//if (!(o instanceof SNP)) {
		//SNP tc = (SNP) o;
		FitnessCalculatorSexSpecific ff=(FitnessCalculatorSexSpecific)ffc.getFitnessCalculator(1,1);
		FitnessFunctionQuantitativeDisruptive m=(FitnessFunctionQuantitativeDisruptive)ff.getMale();
		FitnessFunctionQuantitativeDisruptive f=(FitnessFunctionQuantitativeDisruptive)ff.getFemale();
		FitnessFunctionQuantitativeDisruptive h=(FitnessFunctionQuantitativeDisruptive)ff.getHermaphrodite();


// "1\t0.5\t1.2\t10\t3\   t2.0\t2.3\t20\t22\    t3.0\t3.3\t30\t33\n"+
		assertEquals(m.getMinFitness(),0.5,0.000001);
		assertEquals(m.getMaxFitness(),1.2,0.000001);
		assertEquals(m.getMean(),10,0.000001);
		assertEquals(m.getStdev(),3,0.000001);

		assertEquals(f.getMinFitness(),2.0,0.000001);
		assertEquals(f.getMaxFitness(),2.3,0.000001);
		assertEquals(f.getMean(),20,0.000001);
		assertEquals(f.getStdev(),22,0.000001);

		assertEquals(h.getMinFitness(),3.0,0.000001);
		assertEquals(h.getMaxFitness(),3.3,0.000001);
		assertEquals(h.getMean(),30,0.000001);
		assertEquals(h.getStdev(),33,0.000001);

	}


	

}
