package mimcore.io.haplotypes;

import mimcore.data.BitArray.BitArray;
import mimcore.data.haplotypes.*;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.SexAssignerDirect;


import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

/**
 * Read the haplotypes.
 * Proper haplotypes actually contain SNP and inversion information
 * @author robertkofler
 *
 */
public class HaplotypeReader {
	private String input;
	private java.util.logging.Logger logger;
	private boolean inputIsStringStream;
	private SNPCollection snpCollection;
	private SexAssignerDirect sexAssigner;
	private boolean haploid;

	public HaplotypeReader(String haplotypeFile, boolean haploid, java.util.logging.Logger logger)
	{
		this.logger=logger;
		this.input=haplotypeFile;
		this.inputIsStringStream=false;
		this.haploid=haploid;
		readBasicInfo();

	}

	public HaplotypeReader(String inputStringStream, boolean haploid, java.util.logging.Logger logger, boolean inputIsStringStream)
	{
		this.input=inputStringStream;
		this.logger=logger;
		this.inputIsStringStream=true;
		this.haploid=haploid;
		readBasicInfo();
	}

	public void readBasicInfo()
	{
		this.logger.info("Starting reading haplotypes from file "+this.input);
		this.logger.fine("Start reading the SNPs");
		HaplotypeSNPReader hr=new HaplotypeSNPReader(getBufferedReader());
		snpCollection= hr.getSNPCollection();
		sexAssigner=hr.getSexAssigner();
		this.logger.fine("Finished reading SNPs; SNPs read "+ snpCollection.size());
	}

	public SexAssignerDirect getSexAssigner()
	{
		return this.sexAssigner;
	}


	public ArrayList<HaploidGenome> getHaplotypes()
	{

		this.logger.fine("Start reading haplotype information");
		ArrayList<BitArray> haps=new HaplotypeHaplotypeReader(getBufferedReader(),snpCollection,haploid).getHaplotypes();
		this.logger.fine("Finished reading haplotype information; Haplotypes read " + haps.size());

		ArrayList<HaploidGenome> haplotypes=new ArrayList<HaploidGenome>();
		for (BitArray ba : haps)
		{
			haplotypes.add(new HaploidGenome(ba,snpCollection));
		}
		this.logger.info("Finished reading haplotypes; Read "+snpCollection.size() + " SNPs and " + haplotypes.size() + " haplotypes");

		return haplotypes;
	}

	/**
	 * Factory method for obtaining a new BufferedReader to the input
	 * BufferedReader either to a File or to a String (for debugging)
	 * @return
	 */
	private BufferedReader getBufferedReader()
	{
		BufferedReader bf;
		if(this.inputIsStringStream)
		{
			bf=new BufferedReader(new StringReader(this.input));
		}
		else
		{
			bf=getBufferedFileReader(this.input);
		}
		return bf;

	}


	/**
	 * Get a BufferedReader for a input file;
	 * Decides from the file extension whether the file is zipped ".gz"
	 * @param inputFile
	 * @return
	 */
	private BufferedReader getBufferedFileReader(String inputFile)
	{
		BufferedReader br=null;
		if(inputFile.endsWith(".gz"))
		{
			try{
				br=new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile))));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		else{

			try{
				br= new BufferedReader(new FileReader(inputFile));
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
				System.exit(1);
			}

		}
		return br;
	}


}