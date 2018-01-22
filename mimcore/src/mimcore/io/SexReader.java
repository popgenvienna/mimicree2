package mimcore.io;

import mimcore.data.Chromosome;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerFraction;
import mimcore.data.sex.SexInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class SexReader {

	private BufferedReader bf;
	private String sexFile;
	private Logger logger;
	private double selfingRate=0.0;
	private ISexAssigner sexAssigner=null;

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
		HashMap<Sex,HashSet<Chromosome>> hemiChr=new HashMap<Sex,HashSet<Chromosome>>();
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
				String value=a[1].trim();

				if(id.equals("m"))maleFraction=Double.parseDouble(value);
				else if(id.equals("f"))femaleFraction=Double.parseDouble(value);
				else if(id.equals("h"))hermaphroditeFraction=Double.parseDouble(value);
				else if(id.equals("mc"))minCount=Integer.parseInt(value);
				else if(id.equals("sr"))selfingRate=Double.parseDouble(value);
				else if(id.equals("hz"))hemiChr=parseHemizygousChromosomes(value);
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
		return new SexInfo(new SexAssignerFraction(maleFraction,femaleFraction,hermaphroditeFraction,minCount),selfingRate,hemiChr);

	}


	private HashMap<Sex,HashSet<Chromosome>> parseHemizygousChromosomes(String val)
	{
		HashMap<Sex,HashSet<Chromosome>> toret=new HashMap<Sex,HashSet<Chromosome>>();
		String[] a={val};
		if(val.contains(" "))a=val.split(" ");
		for(String t:a)
		{
			String[] b=t.split(":");
			Sex sex=parseSex(b[0]);
			HashSet<Chromosome> chrs=parseChromosomes(b[1]);
			toret.put(sex,chrs);
		}
		return toret;
	}


	private Sex parseSex(String a)
	{
		a=a.toLowerCase();
		if(a=="m") return Sex.Male;
		else if(a=="f") return Sex.Female;
		else if(a=="h") return Sex.Hermaphrodite;
		else throw new IllegalArgumentException("Invalid sex "+a);
	}

	private HashSet<Chromosome> parseChromosomes(String b)
	{
		HashSet<Chromosome> chrs=new HashSet<Chromosome>();
		String[] t={b};
		if(b.contains(","))t=b.split(",");
		for(String s: t) chrs.add(Chromosome.getChromosome(s));
		return chrs;
	}


}
