package mimcore.io.migrationRegime;

import mimcore.data.DiploidGenome;
import mimcore.data.SexedDiploids;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationEntry;
import mimcore.data.migration.MigrationRegime;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerDirect;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class MigrationRegimeReader {

	private BufferedReader bf;
	private String migrationRegimeFile;
	private Logger logger;
	private SexedDiploids defaultSourcePopulation;
	private ISexAssigner defaultSexAssigner;



	public MigrationRegimeReader(String migrationRegimeFile, BufferedReader br, Logger logger)
	{
		this.migrationRegimeFile=migrationRegimeFile;
		this.bf=br;
		this.logger=logger;
		this.defaultSourcePopulation=SexedDiploids.getEmptySet();
		this.defaultSexAssigner=new SexAssignerDirect(new ArrayList<Sex>());
	}


	public MigrationRegimeReader(String migrationRegimeFile, Logger logger, SexedDiploids defaultSourcePopulation, ISexAssigner defaultSexAssigner) {
		this.migrationRegimeFile = migrationRegimeFile;
		try {
			bf = new BufferedReader(new FileReader(migrationRegimeFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		this.logger = logger;
		this.defaultSourcePopulation = defaultSourcePopulation;
		this.defaultSexAssigner=defaultSexAssigner;
	}
	
	/**
	 * read the selection regime
	 * @return
	 */
	public MigrationRegime readMigrationRegime()
	{

		this.logger.info("Start reading the migration regime file "+this.migrationRegimeFile);
		HashMap<Integer,MigrationEntry> res=new HashMap<Integer, MigrationEntry>();
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\t");
				if(a.length>3 || a.length<2) throw new IllegalArgumentException("Invalid migration entry, must have 2 or 3 columns "+line);

				int generation=Integer.parseInt(a[0]);
				int migrantCount=Integer.parseInt(a[1]);
				if(migrantCount<0.0) throw new IllegalArgumentException("Migrant count must be larger than or equal to zero");

				String pathToMigrationFile=null; // DEFAULT value is NULL
				// lets make it a bit more userfriendly and ignore empty entries (invalid file path anyway, and it would crash later on anyway)
				if(a.length==3 && a[2]!="") pathToMigrationFile=a[2];

				MigrationEntry me=new MigrationEntry(generation,migrantCount,pathToMigrationFile);
				res.put(generation,me);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		try
		{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		this.logger.info("Finished reading the migration regime");
		return new MigrationRegime(res,this.defaultSourcePopulation,this.defaultSexAssigner,this.logger);
	}

}
