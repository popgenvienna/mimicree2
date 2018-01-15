package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.recombination.IRecombinationWindow;
import mimcore.data.recombination.RecombinationWindow;
import mimcore.io.recombination.RecombinationRateReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Test_RecombinationRateReader {


	public static RecombinationRateReader getReaderLambda()
	{
		String input=
						"[lambda]\n"+
						"2L:4500..4600\t0.1\n"+
						"2L:4700..4800\t0.2\n"+
						"2L:4800..4900\t0.3\n"+
						"3L:4200..5400\t0.4\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new RecombinationRateReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static RecombinationRateReader getReaderRecFraction()
	{
		String input=
								"[rf]\n"+
								"2L:4500..4600\t0.1\n"+
								"2L:4700..4800\t0.2\n"+
								"2L:4800..4900\t0.3\n"+
								"3L:4200..5400\t0.4\n";
		// 0.1115718 0.2554128 0.4581454 0.8047190



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new RecombinationRateReader("fakefile",br, SharedFactory.getNullLogger());
		/** r-function translating the mean
			 rf2m<-function(rf)
			 {
				 m<- 0.5*log(1-2*rf)*-1
				 return(m)
			 }
		 */

	}
	
	@Test
	public void position_of_first_window_lambda()
	{
		ArrayList<IRecombinationWindow> s= getReaderLambda().getRecombinationRate().getWindows();
		RecombinationWindow rw=(RecombinationWindow)s.get(0);

		assertEquals(rw.getChromosome().toString(),"2L");
		assertEquals(rw.getStartPosition(),4501);
		assertEquals(rw.getEndPosition(),4600);

	}

	@Test
	public void position_of_first_window_rf()
	{
		ArrayList<IRecombinationWindow> s= getReaderRecFraction().getRecombinationRate().getWindows();
		RecombinationWindow rw=(RecombinationWindow)s.get(0);

		assertEquals(rw.getChromosome().toString(),"2L");
		assertEquals(rw.getStartPosition(),4501);
		assertEquals(rw.getEndPosition(),4600);

	}



	@Test
	public void position_of_second_window_rf()
	{
		ArrayList<IRecombinationWindow> s= getReaderRecFraction().getRecombinationRate().getWindows();
		RecombinationWindow rw=(RecombinationWindow)s.get(1);

		assertEquals(rw.getChromosome().toString(),"2L");
		assertEquals(rw.getStartPosition(),4701);
		assertEquals(rw.getEndPosition(),4800);

	}

	@Test
	public void position_of_second_window_lambda()
	{
		ArrayList<IRecombinationWindow> s= getReaderLambda().getRecombinationRate().getWindows();

		assertEquals(s.get(1).getChromosome().toString(),"2L");
		assertEquals(s.get(1).getStartPosition(),4701);
		assertEquals(s.get(1).getEndPosition(),4800);

	}

	@Test
	public void position_of_last_window_rf()
	{
		ArrayList<IRecombinationWindow> s= getReaderRecFraction().getRecombinationRate().getWindows();

		assertEquals(s.get(3).getChromosome().toString(),"3L");
		assertEquals(s.get(3).getStartPosition(),4201);
		assertEquals(s.get(3).getEndPosition(),5400);

	}

	@Test
	public void position_of_last_window_lambda()
	{
		ArrayList<IRecombinationWindow> s= getReaderLambda().getRecombinationRate().getWindows();

		assertEquals(s.get(3).getChromosome().toString(),"3L");
		assertEquals(s.get(3).getStartPosition(),4201);
		assertEquals(s.get(3).getEndPosition(),5400);

	}

	@Test
	public void lambda_correct_recognized()
	{
		ArrayList<IRecombinationWindow> s2= getReaderLambda().getRecombinationRate().getWindows();
		ArrayList<RecombinationWindow> s=new ArrayList<RecombinationWindow>();
		for(IRecombinationWindow rw: s2)s.add((RecombinationWindow)rw);

		assertEquals(s.get(0).getLambda(),0.1,0.0000001);
		assertEquals(s.get(1).getLambda(),0.2,0.0000001);
		assertEquals(s.get(2).getLambda(),0.3,0.0000001);
		assertEquals(s.get(3).getLambda(),0.4,0.0000001);
	}

	@Test
	public void rf_correct_recognized()
	{
		ArrayList<IRecombinationWindow> s2= getReaderRecFraction().getRecombinationRate().getWindows();
		ArrayList<RecombinationWindow> s=new ArrayList<RecombinationWindow>();
		for(IRecombinationWindow rw: s2)s.add((RecombinationWindow)rw);

		assertNotEquals(s.get(0).getLambda(),0.1,0.00001);
		assertNotEquals(s.get(1).getLambda(),0.2,0.00001);
		assertNotEquals(s.get(2).getLambda(),0.3,0.00001);
		assertNotEquals(s.get(3).getLambda(),0.4,0.00001);
	}

	@Test
	public void rf_haldane_1919()
	{
		ArrayList<IRecombinationWindow> s2= getReaderRecFraction().getRecombinationRate().getWindows();
		ArrayList<RecombinationWindow> s=new ArrayList<RecombinationWindow>();
		for(IRecombinationWindow rw: s2)s.add((RecombinationWindow)rw);
// 0.1115718 0.2554128 0.4581454 0.8047190

		assertEquals(s.get(0).getLambda(),0.1115718,0.00001);
		assertEquals(s.get(1).getLambda(),0.2554128,0.00001);
		assertEquals(s.get(2).getLambda(),0.4581454,0.00001);
		assertEquals(s.get(3).getLambda(),0.8047190,0.00001);
	}

}
