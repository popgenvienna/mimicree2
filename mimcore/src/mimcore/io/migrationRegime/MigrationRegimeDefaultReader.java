package mimcore.io.migrationRegime;

import mimcore.data.migration.MigrationRegimeDefault;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class MigrationRegimeDefaultReader {

	private BufferedReader bf;
	private String migrationRegimeFile;
	private Logger logger;
	public MigrationRegimeDefaultReader(String migrationRegimeFile, Logger logger)
	{
		this.migrationRegimeFile=migrationRegimeFile;
		try{
			bf=new BufferedReader(new FileReader(migrationRegimeFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;
	}
	
	/**
	 * read the selection regime
	 * @return
	 */
	public MigrationRegimeDefault readMigrationRegime()
	{

		this.logger.info("Start reading the migration regime file "+this.migrationRegimeFile);
		HashMap<Integer,Double> res=new HashMap<Integer, Double>();
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\t");
				assert(a.length==2);
				int generation=Integer.parseInt(a[0]);
				double migrationRate=Double.parseDouble(a[1]);
				if(migrationRate<0.0) throw new IllegalArgumentException("Migration rate must be larger than or equal to zero");
				if(migrationRate>1.0) throw new IllegalArgumentException("Migration rate must be smaller than or equal to 1.0");
				res.put(generation,migrationRate);
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
		return new MigrationRegimeDefault(res);
	}

}
