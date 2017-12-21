package mimcore.io.fasta;

import mimcore.data.fasta.FastaRecord;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class FastaWriter {
	private BufferedWriter bf;
	private final String outputFile;
	private Logger logger;
	private final int width;
	public FastaWriter(String outputFile, int widthofEntry, Logger logger)
	{

		this.logger=logger;
		try
		{
			bf=new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outputFile))));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		this.outputFile=outputFile;
		this.width=widthofEntry;
	}
	
	
	
	public void writeEntry(FastaRecord fastaRecord) {


		String sequence=fastaRecord.getSequence();
		int currentPos=0;



			try {

				bf.write(">"+fastaRecord.getHeader() + "\n");

				while(currentPos<sequence.length()) {
					// 012345678
					// AAAATTTTC  length=9
					int endpos=currentPos+width;
					if(endpos> sequence.length()) endpos=sequence.length();
					String subseq = sequence.substring(currentPos,endpos);
					currentPos += endpos;
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
	}




	public void close()
		{
		try{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	


	
	
	
	
}
