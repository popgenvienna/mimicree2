package mim2.fsc2mimhap.io;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerDirect;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Read only the SNPs from a haplotype file.
 * @author robertkofler
 *
 */
class ArlequinSNPReader {

	private BufferedReader bf;
	private final Chromosome chrom;
	
	public ArlequinSNPReader(BufferedReader bf, Chromosome chrom )
	{
		this.bf=bf;
		this.chrom=chrom;

	}


	public SNPCollection getSNPCollection()
	{

		ArrayList<Integer> positions=new ArrayList<Integer>();
		char[] firstChar=null;
		char[] secondChar=null;

		int step=0;
		try
		{
			String line=null;
			while((line=bf.readLine())!=null)
			{
				if(line.contains("polymorphic positions on chromosome"))
				{
					step=1;
				}
				else if(step==1)
				{
					positions=parsePositions(line);
					step=2;
				}
				else if(step==2 && line.contains("SampleData="))
				{
					step=3;
				}
				else if(step==3)
				{
					if(line.trim().equals(""))continue;
					if(line.contains("}")){step=4; break;}
					String[] tmp=line.split("\\s+");
					char[] alleles=tmp[2].toCharArray();

					if(firstChar==null) {firstChar=alleles; secondChar=alleles.clone();}
					else
					{
						for(int i=0;i<firstChar.length;i++)
						{
							if(firstChar[i]!=alleles[i])secondChar[i]=alleles[i];
						}
					}


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
		if(positions.size()!=firstChar.length)throw new IllegalArgumentException("Invalid input file; number of sites not identical to number of character states");

		ArrayList<SNP> toret=new ArrayList<SNP>();
		for(int i=0; i<firstChar.length;i++)
		{
			int pos=positions.get(i);
			char fchar=firstChar[i];
			char schcar=secondChar[i];
			if(fchar==schcar) throw new IllegalArgumentException("Invalid input file; monomorphic SNP found at position "+pos+ " with allele "+fchar);
			GenomicPosition gp=new GenomicPosition(chrom,pos);
			SNP s= new SNP(gp,fchar,fchar,schcar);
			toret.add(s);
		}

		// Collections.sort(toret); Sorting not valid for Arlequin data; check if sorted
		validateSNPs(toret);
		return new SNPCollection(toret);
	}

	private ArrayList<Integer> parsePositions(String line)
	{
		if(!line.contains(",")) throw new IllegalArgumentException("Invalid line with SNP positions; Must contain , "+line);
		String[] putpos=line.split(",");
		putpos[0]=putpos[0].replace("#","");
		ArrayList<Integer> toret=new ArrayList<Integer>();
		for(String s: putpos)
		{
			toret.add(Integer.parseInt(s.trim()));
		}
		return toret;

	}
	
	/**
	 * Check if every SNP is only provided once; Considering only chromosome and position
	 * @param snps
	 */
	private void validateSNPs(ArrayList<SNP> snps)
	{
		HashSet<GenomicPosition> posset=new HashSet<GenomicPosition>();
		int lastPos=0;
		for(SNP s: snps)
		{
			int position=s.genomicPosition().position();
			if(position<=lastPos) throw new IllegalArgumentException("Invalid Arlequin file; not sorted by position");
			lastPos=position;

			if(posset.contains(s.genomicPosition()))  throw new IllegalArgumentException("Invalid SNPs, a SNP was provided several times "+ s.genomicPosition().toString());
			posset.add(s.genomicPosition());
		}
	}
	


	/**
	 * Parse a line of the file to a SNP
	 * @param line
	 * @return
	 */
	private SNP parseSNP(String line)
	{
		//3L	13283707	T	G/T	GT GG GG GG
		String[] a =line.split("\\s+");
		char ref=a[2].charAt(0);
		char anc=a[3].charAt(0);    // ancestral allele
		char der=a[3].charAt(2);    // derived allele
		GenomicPosition genpos=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		return new SNP(genpos,ref,anc,der);
	}
	
	

}
