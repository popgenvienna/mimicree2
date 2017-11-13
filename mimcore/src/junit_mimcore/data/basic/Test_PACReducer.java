package junit_mimcore.data.basic;

import junit_mimcore.factories.GenomicDataFactory;
import mimcore.data.statistic.PACReducer;
import mimcore.data.statistic.PopulationAlleleCount;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: robertkofler
 * Date: 3/22/13
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class Test_PACReducer {


	@Test
	public void minor_frequency_08()
	{
		PACReducer pr=new PACReducer(GenomicDataFactory.getDiploidGenomesMf08());
		PopulationAlleleCount pac= pr.reduce();
		assertEquals(pac.ancestralFrequency(0),0.8,0.0000001);
		assertEquals(pac.derivedFrequency(0),0.2,0.0000001);
		assertEquals(pac.ancestralCount(0),8);
		assertEquals(pac.derivedCount(0),2);
		assertEquals(pac.ancestralFrequency(19),0.8,0.0000001);
		assertEquals(pac.derivedFrequency(19),0.2,0.0000001);
		assertEquals(pac.ancestralCount(19),8);
		assertEquals(pac.derivedCount(19),2);

	}


	@Test
	public void minor_frequency_02()
	{
		PACReducer pr=new PACReducer(GenomicDataFactory.getDiploidGenomesMf02());
		PopulationAlleleCount pac= pr.reduce();
		assertEquals(pac.ancestralFrequency(0),0.2,0.0000001);
		assertEquals(pac.derivedFrequency(0),0.8,0.0000001);
		assertEquals(pac.ancestralCount(0),2);
		assertEquals(pac.derivedCount(0),8);
		assertEquals(pac.ancestralFrequency(19),0.2,0.0000001);
		assertEquals(pac.derivedFrequency(19),0.8,0.0000001);
		assertEquals(pac.ancestralCount(19),2);
		assertEquals(pac.derivedCount(19),8);

	}
}
