package mimcore.io.haplotypes;

import mimcore.data.BitArray.*;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Helper class of HaplotypeReader; Reads the haplotypes and stores them
 */
class HaplotypeHaplotypeReader {
	
	private static class HapFileContainer
	{
		public GenomicPosition pos;
		public ArrayList<Character> hapList;
		
		public HapFileContainer(GenomicPosition pos, ArrayList<Character> hapList)
		{
			this.pos=pos;
			this.hapList=hapList;
		}
	}
	
	private BufferedReader bf;
	private SNPCollection snpcol;
	
	public HaplotypeHaplotypeReader(BufferedReader bf,SNPCollection snpcol)
	{
		this.bf=bf;
		this.snpcol=snpcol;
	}
	
	public ArrayList<BitArray> getHaplotypes()
	{
		ArrayList<BitArrayBuilder> haplotypeCollection=null;
		HapFileContainer hc =null;
		while((hc=next())!=null)
		{
			if(haplotypeCollection==null) haplotypeCollection= initializeHaplotypeCollection(snpcol.size(),hc.hapList.size());
			
			// Get the index of the SNP in the haplotype collection
			int index=snpcol.getIndexforPosition(hc.pos);
			// SNP only stores characters in uppercase as well as the haplotype collection
			char maj=snpcol.getSNPforPosition(hc.pos).ancestralAllele();
			
			// Check for every haplotype SNP state whether it agrees with the major allele, if so set the bit to 1 else (minor allele) leave the default (0)
			for(int i=0; i<hc.hapList.size(); i++)
			{
				if(hc.hapList.get(i) == maj) haplotypeCollection.get(i).setBit(index);
			}
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
	
	
	/**
	 * Read the next line of the file
	 * @return
	 */
	private HapFileContainer next()
	{
		String line=null;
		try
		{
			line=bf.readLine();
		}
		catch(IOException fe)
		{
			fe.printStackTrace();
			System.exit(0);
		}
		if(line==null) return null;
		return parseLine(line);
	}
	
	/**
	 * Parse the content of a haplotype line
	 * @param line
	 * @return
	 */
	private HapFileContainer parseLine(String line)
	{
		//3L	13283707	T	G/T	GT GG GG GG
		String[] a = line.split("\t");
		GenomicPosition gp=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		String[] temp=a[4].split(" ");
		
		ArrayList<Character> snplist=new ArrayList<Character>(2*temp.length);
		for(String s: temp)
		{
			snplist.add(Character.toUpperCase(s.charAt(0)));
			snplist.add(Character.toUpperCase(s.charAt(1)));
		}
		assert(gp!=null);
		return new HapFileContainer(gp,snplist);
	}

	
	
	
	

}
