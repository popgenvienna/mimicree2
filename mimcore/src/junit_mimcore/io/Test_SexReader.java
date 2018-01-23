package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.SexAssignerFraction;
import mimcore.data.sex.SexInfo;
import mimcore.io.PopulationSizeReader;
import mimcore.io.SexReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class Test_SexReader {


	

	public static SexInfo getSexinfo()
	{
		String input=
						"M=0.5"+
								"   # comment bla\n"+
						"F=0.4\n"+
						"H=0.1  # comment\n"+
						"SR=0.6\n"+
						"MC=2\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SexReader("fakefile",br, SharedFactory.getNullLogger()).readSexInfo();

	}

	@Test
	public void male_female_herma_ratio() {
		SexInfo si = getSexinfo();
		SexAssignerFraction sa=(SexAssignerFraction)si.getSexAssigner();
		assertEquals(sa.getMaleFraction(),0.5,0.0000001);
		assertEquals(sa.getFemaleFraction(),0.4,0.0000001);
		assertEquals(sa.getHermaphroditeFraction(),0.1,0.0000001);
		assertEquals(sa.getMinCount(),2);

	}

	@Test
	public void selfing_rate() {
		SexInfo si = getSexinfo();
		assertEquals(si.getSelfingRate(),0.6, 0.0000001);
	}





}
