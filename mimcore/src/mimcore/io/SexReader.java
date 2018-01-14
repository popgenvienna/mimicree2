package mimcore.io;

import mimcore.data.PopulationSizeContainer;
import mimcore.data.sex.SexAssigner;
import mimcore.data.sex.SexInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class SexReader {

	private BufferedReader bf;
	private String sexFile;
	private Logger logger;
	private double selfingRate=0.0;
	private SexAssigner sexAssigner=null;

	public SexReader(String sexFile, Logger logger)
	{
		this.sexFile=sexFile;
		try{
			bf=new BufferedReader(new FileReader(sexFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.logger=logger;

	}

	public SexReader(String sexFile, BufferedReader br, Logger logger)
	{
		this.sexFile=sexFile;
		this.bf=br;
		this.logger=logger;

	}

	

	public SexInfo readSexInfo()
	{

		this.logger.info("Start reading sex definition file "+this.sexFile);
		double maleFraction=0.0;
		double femaleFraction=0.0;
		double hermaphroditeFraction=0.0;
		double selfingRate=0.0;
		int minCount=1;
		try
		{
			String line=null;
			while((line=bf.readLine())!=null)
			{

				line=line.replaceAll("#.*",""); // ignore comments
				line=line.trim();
				if(line.equals("")) continue; // ignore empty lines

				if(!line.contains("=")) throw new IllegalArgumentException("Invalid sex definition entry; Line does not contain assignment (=) "+line);
				String[] a=line.split("="); // split at equals;
				if(a.length>2) throw new IllegalArgumentException("Invalid sex entry "+line+" Only one assignment (=) per line;");
				String id=a[0].trim().toLowerCase();
				double value=Double.parseDouble(a[1].trim());

				if(id.equals("m"))maleFraction=value;
				else if(id.equals("f"))femaleFraction=value;
				else if(id.equals("h"))hermaphroditeFraction=value;
				else if(id.equals("mc"))minCount=(int)value;
				else if(id.equals("sr"))selfingRate=value;
				else throw new IllegalArgumentException("Unknown ID "+id);
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

		this.logger.info("Finished reading the sex definition file");
		// validity of parameters is checked within SexInfo or SexAssigner
		return new SexInfo(new SexAssigner(maleFraction,femaleFraction,hermaphroditeFraction,minCount),selfingRate);

	}


}
