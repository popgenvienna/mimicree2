package qmimcore.io;

import qmimcore.data.fitness.SelectionRegimeDefault;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class SelectionRegimeDefaultReader {

	private BufferedReader bf;
	private String selectionRegimeFile;
	private Logger logger;
	public SelectionRegimeDefaultReader(String selectionRegimeFile, Logger logger)
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
	public SelectionRegimeDefault readSelectionRegime()
	{

		this.logger.info("Start reading the selection regime file "+this.selectionRegimeFile);
		HashMap<Integer,Double> res=new HashMap<Integer, Double>();
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\t");
				assert(a.length==2);
				int generation=Integer.parseInt(a[0]);
				double selectionIntensity=Double.parseDouble(a[1]);
				if(!(selectionIntensity>0.0)) throw new IllegalArgumentException("Selection intensity must be larger than 0.0");
				if(selectionIntensity>1.0) throw new IllegalArgumentException("Selection intensity must be smaller than 1.0");
				res.put(generation,selectionIntensity);
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

		this.logger.info("Finished reading the selection regime");
		return new SelectionRegimeDefault(res);
	}

}
