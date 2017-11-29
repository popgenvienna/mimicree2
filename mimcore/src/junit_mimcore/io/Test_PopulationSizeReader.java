package junit_mimcore.io;

import junit_mimcore.factories.GenomicDataFactory;
import junit_mimcore.factories.SharedFactory;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.migration.MigrationEntry;
import mimcore.data.migration.MigrationRegime;
import mimcore.io.PopulationSizeReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Test_PopulationSizeReader {


	

	public static PopulationSizeContainer getReader()
	{
		String input=
						"1\t100\n"+
						"10\t200\n"+
						"20\t300\n"+
						"40\t50\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new PopulationSizeReader("fakefile",br, SharedFactory.getNullLogger()).readPopulationSizes();

	}

	@Test
	public void advanced_generations() {
		PopulationSizeContainer r = getReader();
		assertEquals(r.getPopulationSize(10, 1), 200);
		assertEquals(r.getPopulationSize(20, 1), 300);
		assertEquals(r.getPopulationSize(40, 1), 50);
	}

	@Test
	public void very_advanced_generations() {
		PopulationSizeContainer r = getReader();
		assertEquals(r.getPopulationSize(100, 1), 50);
		assertEquals(r.getPopulationSize(1000, 1), 50);
		assertEquals(r.getPopulationSize(1000, 100), 50);
	}

	@Test
	public void first_generations()
	{
		PopulationSizeContainer r=getReader();
		assertEquals(r.getPopulationSize(1,1),100);
		assertEquals(r.getPopulationSize(2,1),100);
		assertEquals(r.getPopulationSize(3,1),100);
		assertEquals(r.getPopulationSize(5,1),100);
		assertEquals(r.getPopulationSize(6,1),100);
		assertEquals(r.getPopulationSize(7,1),100);
		assertEquals(r.getPopulationSize(9,1),100);

	}

	@Test
	public void replicates() {
		PopulationSizeContainer r = getReader();
		assertEquals(r.getPopulationSize(1, 1), 100);
		assertEquals(r.getPopulationSize(1, 2), 100);
		assertEquals(r.getPopulationSize(1, 20), 100);
		assertEquals(r.getPopulationSize(1, 200), 100);
	}











}
