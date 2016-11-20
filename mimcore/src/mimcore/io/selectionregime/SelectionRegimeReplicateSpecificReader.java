package mimcore.io.selectionregime;

import mimcore.data.fitness.survival.SelectionRegimeReplicateSpecific;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class SelectionRegimeReplicateSpecificReader {

	private BufferedReader bf;
	private String selectionRegimeFile;
	private Logger logger;

	private class SelEntry
	{
		int generation;
		double selectionIntensity;
		int replicate;
	}

	public SelectionRegimeReplicateSpecificReader(String selectionRegimeFile, Logger logger)
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
	public SelectionRegimeReplicateSpecific readSelectionRegime()
	{

		this.logger.info("Start reading the selection regime file "+this.selectionRegimeFile);

		ArrayList<SelEntry> entries=new ArrayList<SelEntry>();
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\t");
				assert(a.length==3);
				SelEntry se=new SelEntry();
				se.generation=Integer.parseInt(a[0]);
				se.selectionIntensity=Double.parseDouble(a[1]);
				se.replicate=Integer.parseInt(a[2]);

				if(!(se.selectionIntensity>0.0)) throw new IllegalArgumentException("Selection intensity must be larger than 0.0");
				if(se.selectionIntensity>1.0) throw new IllegalArgumentException("Selection intensity must be smaller than 1.0");
				entries.add(se);
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


		SelectionRegimeReplicateSpecific sr=getSelectionRegime(entries);
		this.logger.info("Finished reading the selection regime");
		return sr;
	}

	private SelectionRegimeReplicateSpecific getSelectionRegime(ArrayList<SelEntry> entries)
	{
		int maxReplicate=0;
		for(SelEntry se:entries)
		{
			if(se.replicate>maxReplicate) maxReplicate=se.replicate;
		}
		if(maxReplicate<1) throw new IllegalArgumentException("Number of replicates must at least be one");
		ArrayList<HashMap<Integer,Double>> res =new ArrayList<HashMap<Integer, Double>>();
		for(int i=0; i<maxReplicate;i++)
		{
			HashMap<Integer,Double> tmp=new HashMap<Integer, Double>();
			res.add(tmp);
		}

		for(SelEntry se: entries)
		{
			res.get(se.replicate-1).put(se.generation,se.selectionIntensity);
		}
		return new SelectionRegimeReplicateSpecific(res);

	}


}
