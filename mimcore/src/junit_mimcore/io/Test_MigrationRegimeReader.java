package junit_mimcore.io;

import junit_mimcore.factories.GenomicDataFactory;
import junit_mimcore.factories.SharedFactory;
import mimcore.data.migration.MigrationEntry;
import mimcore.data.migration.MigrationRegime;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Test_MigrationRegimeReader {


	

	public static MigrationRegimeReader getReader()
	{
		String input=
						"1\t100\n"+
						"10\t200\t\n"+
						"20\t300\t/path/to/random/file.txt\n"+
						"40\t50\t\n";



		BufferedReader br=new BufferedReader(new StringReader(input));
		return new MigrationRegimeReader("fakefile",br, SharedFactory.getNullLogger(), GenomicDataFactory.getMinimalGenomes());

	}
	
	@Test
	public void simple_entry()
	{
		MigrationRegimeReader r=getReader();
		MigrationRegime mr=r.readMigrationRegime();
		HashMap<Integer,MigrationEntry> s= mr.getMigrationEntries();

		assertEquals(s.get(1).getGeneration(),1);
		assertEquals(s.get(1).getMigrantCount(),100);
		assertEquals(s.get(1).useDefault(),true);

	}


	@Test
	public void simple_entry_two()
	{
		MigrationRegimeReader r=getReader();
		HashMap<Integer,MigrationEntry> s= r.readMigrationRegime().getMigrationEntries();

		assertEquals(s.get(10).getGeneration(),10);
		assertEquals(s.get(10).getMigrantCount(),200);
		assertEquals(s.get(10).useDefault(),true);

	}

	@Test
	public void entry_with_source_file()
	{
		MigrationRegimeReader r=getReader();
		HashMap<Integer,MigrationEntry> s= r.readMigrationRegime().getMigrationEntries();

		assertEquals(s.get(20).getGeneration(),20);
		assertEquals(s.get(20).getMigrantCount(),300);
		assertEquals(s.get(20).useDefault(),false);
		assertEquals(s.get(20).getPathToSourcePopulation(),"/path/to/random/file.txt");
	}



	@Test
	public void final_entry()
	{
		MigrationRegimeReader r=getReader();
		HashMap<Integer,MigrationEntry> s= r.readMigrationRegime().getMigrationEntries();

		assertEquals(s.get(40).getGeneration(),40);
		assertEquals(s.get(40).getMigrantCount(),50);
		assertEquals(s.get(40).useDefault(),true);
	}


	@Test
	public void does_not_contain_unprovided_entries()
	{
		MigrationRegimeReader r=getReader();
		HashMap<Integer,MigrationEntry> s= r.readMigrationRegime().getMigrationEntries();

		assertFalse(s.containsKey(0));
		assertFalse(s.containsKey(2));
		assertFalse(s.containsKey(9));
		assertFalse(s.containsKey(11));

		assertFalse(s.containsKey(39));
		assertFalse(s.containsKey(45));

	}


	

}
