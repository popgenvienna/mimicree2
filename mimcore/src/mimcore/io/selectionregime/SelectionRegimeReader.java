package mimcore.io.selectionregime;

import mimcore.data.gpf.survival.ISelectionRegime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class SelectionRegimeReader {

	private BufferedReader bf;
	private String selectionRegimeFile;
	private Logger logger;
	public SelectionRegimeReader(String selectionRegimeFile, Logger logger)
	{
		this.selectionRegimeFile=selectionRegimeFile;
		try{
			bf=new BufferedReader(new FileReader(selectionRegimeFile));
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
	public ISelectionRegime readSelectionRegime()
	{

		this.logger.info("Testing selection regime file "+this.selectionRegimeFile);
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
			this.logger.info("Selection regime file has 2 columns; Reading default selection regime (selection regimes applies to all replicates)");
			return new SelectionRegimeDefaultReader(this.selectionRegimeFile,this.logger).readSelectionRegime();
		}
		else if(a.length==3)
		{
			this.logger.info("Selection regime file has 3 columns; Reading replicate specific selection regime file");
			return new SelectionRegimeReplicateSpecificReader(this.selectionRegimeFile, this.logger).readSelectionRegime();
		}
		else throw new IllegalArgumentException("Selection regime file must either have 2 or 3 columns; Found "+a.length+ " columns");

		//return null;
	}

}
