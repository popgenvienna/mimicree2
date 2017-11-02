package junit_mimcore.data.basic;

import junit_mimcore.factories.GenomicDataFactory;
import mimcore.data.Population;
import mimcore.data.Specimen;
import mimcore.data.gpf.mating.MatingFunctionFecundity;



/*
public class Test_MatingFunctionFecundity {

	@Test
	public void test_twoEqualFitnessSpecimen()
	{
		ArrayList<Specimen> specs=new ArrayList<Specimen>();
		specs.add(GenomicDataFactory.getSpecimen(2.0, 1.0));
		specs.add(SharedDataFactory.getSpecimen(2.0, 2.0));
		MatingFunction m=MatingFunction.getMatingFunction(new Population(specs));
		assertEquals(m.getSpecimen(0.0).additiveFitness(),		1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.499999).additiveFitness(),	1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.5).additiveFitness(),		2.0,	0.00000000001);	
		assertEquals(m.getSpecimen(0.99999999999).additiveFitness(), 	2.0,	0.00000000001);
		//assertEquals(m.getSpecimen(1.0).additiveFitness(),1.0,0.001111);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_throws()
	{
		ArrayList<Specimen> specs=new ArrayList<Specimen>();
		specs.add(SharedDataFactory.getSpecimen(2.0, 1.0));
		specs.add(SharedDataFactory.getSpecimen(2.0, 2.0));
		MatingFunction m=MatingFunction.getMatingFunction(new Population(specs));
		assertEquals(m.getSpecimen(1.0).additiveFitness(), 	2.0,	0.00000000001);
	}
	
	@Test
	public void test_twoUnequalFitnessSpecimen()
	{
		ArrayList<Specimen> specs=new ArrayList<Specimen>();
		specs.add(SharedDataFactory.getSpecimen(1.0, 1.0));
		specs.add(SharedDataFactory.getSpecimen(2.0, 2.0));
		MatingFunction m=MatingFunction.getMatingFunction(new Population(specs));
		assertEquals(m.getSpecimen(0.0).additiveFitness(),		1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.333333332).additiveFitness(),	1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.333333334).additiveFitness(),		2.0,	0.00000000001);	
		assertEquals(m.getSpecimen(0.99999999999).additiveFitness(), 	2.0,	0.00000000001);
		//assertEquals(m.getSpecimen(1.0).additiveFitness(),1.0,0.001111);
	}
	
	
	@Test
	public void test_threeEqualFitnessSpecimen()
	{
		ArrayList<Specimen> specs=new ArrayList<Specimen>();
		specs.add(SharedDataFactory.getSpecimen(2.0, 1.0));
		specs.add(SharedDataFactory.getSpecimen(2.0, 2.0));
		specs.add(SharedDataFactory.getSpecimen(2.0, 3.0));
		
		MatingFunction m=MatingFunction.getMatingFunction(new Population(specs));
		assertEquals(m.getSpecimen(0.0).additiveFitness(),			1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.333333332).additiveFitness(),	1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.333333334).additiveFitness(),	2.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.666666665).additiveFitness(),	2.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.666666667).additiveFitness(),	3.0,	0.00000000001);		
		assertEquals(m.getSpecimen(0.99999999999).additiveFitness(), 	3.0,	0.00000000001);
		//assertEquals(m.getSpecimen(1.0).additiveFitness(),1.0,0.001111);
	}
	
	@Test
	public void test_threeUnEqualFitnessSpecimen()
	{
		ArrayList<Specimen> specs=new ArrayList<Specimen>();
		specs.add(SharedDataFactory.getSpecimen(2.0, 1.0));
		specs.add(SharedDataFactory.getSpecimen(3.0, 2.0));
		specs.add(SharedDataFactory.getSpecimen(5.0, 3.0));
		
		MatingFunction m=MatingFunction.getMatingFunction(new Population(specs));
		assertEquals(m.getSpecimen(0.0).additiveFitness(),			1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.199999999).additiveFitness(),	1.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.2).additiveFitness(),	2.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.499999999).additiveFitness(),	2.0,	0.00000000001);
		assertEquals(m.getSpecimen(0.5).additiveFitness(),	3.0,	0.00000000001);		
		assertEquals(m.getSpecimen(0.99999999999).additiveFitness(), 	3.0,	0.00000000001);
		//assertEquals(m.getSpecimen(1.0).additiveFitness(),1.0,0.001111);
	}
	
	
	


}

*/
