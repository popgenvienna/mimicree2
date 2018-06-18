package mim2.fsc2mimhap.io;

import mimcore.data.BitArray.BitArray;
import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNPCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Helper class of HaplotypeReader; Reads the haplotypes and stores them
 */
class ArlequinHaplotypeReader {

	private BufferedReader bf;
	private SNPCollection snpcol;
	private boolean haploid;
	
	public ArlequinHaplotypeReader(BufferedReader bf, SNPCollection snpcol, boolean haploid)
	{
		this.bf=bf;
		this.snpcol=snpcol;
		this.haploid=haploid;
	}
	
	public ArrayList<BitArray> getHaplotypes()
	{
		ArrayList<BitArrayBuilder> haplotypeCollection=new ArrayList<BitArrayBuilder>();
		String line=null;
		int step=0;
		try
		{

			while((line=bf.readLine())!=null)
			{
				if(line.contains("polymorphic positions on chromosome"))
				{
					step=1;
				}
				else if(step==1 && line.contains("SampleData="))
				{
					step=2;
				}
				else if(step==2)
				{
					if(line.trim().equals(""))continue;
					if(line.contains("}")){step=3; break;}
					String[] tmp=line.split("\\s+");
					char[] alleles=tmp[2].toCharArray();
					if(snpcol.size()!=alleles.length) throw new IllegalArgumentException("Invalid size of arp haplotypes "+tmp[2]);
					BitArrayBuilder bab=new BitArrayBuilder(snpcol.size());
					for(int i=0; i<alleles.length;i++)
					{
						if(alleles[i]==snpcol.getSNPforIndex(i).ancestralAllele()) bab.setBit(i);
					}
					haplotypeCollection.add(bab);
					if(haploid) haplotypeCollection.add(bab);  // nice trick if haploid just add twice

				}


			}
		}
		catch(IOException fe)
		{
			fe.printStackTrace();
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
		
		// Convert the BitArrayBuilder into a BitArray
		ArrayList<BitArray> toret=new ArrayList<BitArray>();
		for(BitArrayBuilder build: haplotypeCollection)
		{
			toret.add(build.getBitArray());
		}
		return toret;
	}
	
	
	/**
	 * Initialize, create an empty collection of BitArrayBuilder
	 * @param snpcount number of SNPs will correspond to size of the BitArray
	 * @param haplotypeCount will correspond to size of ArrayList 
	 * @return
	 */
	private ArrayList<BitArrayBuilder> initializeHaplotypeCollection(int snpcount, int haplotypeCount)
	{
		ArrayList<BitArrayBuilder> ba=new ArrayList<BitArrayBuilder>();
		for(int i=0; i<haplotypeCount; i++)
		{
			ba.add(new BitArrayBuilder(snpcount));
		}
		return ba;
		
	}
	
	

	


	
	
	
	

}
