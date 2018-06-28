/**
package mimcore.io.deprecated;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.statistic.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class SinglePopulationAlleleCountReader {
	private final BufferedReader bf;
	
	
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
	
	
	public SinglePopulationAlleleCountReader(BufferedReader bf)
	{

		this.bf=bf;
	}
	
	public PopulationAlleleCount getAlleleCount(SNPCollection snpcol)
	{
		int[] majorCount=new int[snpcol.size()];
		int[] minorCount=new int[snpcol.size()];
		HapFileContainer hc;
		while((hc=next())!=null)
		{
			int index=snpcol.getIndexforPosition(hc.pos);
			SNP s=snpcol.getSNPforIndex(index);
			int mac=getCount(hc.hapList,s.ancestralAllele());
			int mic=getCount(hc.hapList,s.derivedAllele());
			majorCount[index]=mac;
			minorCount[index]=mic;
		}
		return new PopulationAlleleCount(snpcol,majorCount,minorCount);
		
	}
	
	private int getCount(ArrayList<Character> chars, char toCount)
	{
		int toret=0;
		for(char c : chars)
		{
			if(c==toCount)toret++;
		}
		return toret;
	}
	
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
 **/
