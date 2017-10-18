package mimcore.io.migrationRegime;

import mimcore.data.fitness.survival.ISelectionRegime;
import mimcore.data.migration.IMigrationRegime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class MigrationRegimeReader {

	private BufferedReader bf;
	private String migrationRegimeFile;
	private Logger logger;
	public MigrationRegimeReader(String migrationRegimeFile, Logger logger)
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
	public IMigrationRegime readMigrationRegime()
	{

		this.logger.info("Testing migration regime file "+this.migrationRegimeFile);
		ISelectionRegime toret;
		String line="";
		try
		{
			line=bf.readLine();
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


		String[] a=line.split("\t");
		if(a.length==2)
		{
			this.logger.info("Migration regime file has 2 columns; Reading default migration regime (migration applies to all replicates equally)");
			return new MigrationRegimeDefaultReader(this.migrationRegimeFile,this.logger).readMigrationRegime();
		}
		else if(a.length == 3)
		{
			throw new IllegalArgumentException("Migration regime file has three columns; Replicate specific migration is currently not supported");

		}
		else throw new IllegalArgumentException("Migration regime file must either have 2 or 3 columns; Found "+a.length+ " columns");
	}

}
