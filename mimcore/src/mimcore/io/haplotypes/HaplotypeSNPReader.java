package mimcore.io.haplotypes;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.*;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssignerDirect;
import mimcore.data.sex.ISexAssigner;

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
class HaplotypeSNPReader {

	private BufferedReader bf;
	private SexAssignerDirect sa;
	private SNPCollection snpCollection;
	
	public HaplotypeSNPReader(BufferedReader bf )
	{
		this.bf=bf;
		sa=null;
		snpCollection=process();
	}


	public SNPCollection getSNPCollection(){return this.snpCollection;}
	public SexAssignerDirect getSexAssigner(){return this.sa;}
	/**
	 * Obtain all SNPs from a haplotype file.
	 * @return a unsorted collection of SNPs
	 */
	private SNPCollection process()
	{
		
		ArrayList<SNP> snpcol=new ArrayList<SNP>();

		String line=null;
		try
		{
			while((line=bf.readLine())!=null)
			{
				if(line.startsWith("#sex"))
				{
					this.sa=parseSexAssigner(line);
				}
				else if(line.startsWith("#")){}
				else
				{
					SNP s=parseSNP(line);
					snpcol.add(s);
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
		
		// Check if every SNP is only provided once;
		validateSNPs(snpcol);
		 
		Collections.sort(snpcol);
		return new SNPCollection(snpcol);
	}
	
	/**
	 * Check if every SNP is only provided once; Considering only chromosome and position
	 * @param snps
	 */
	private void validateSNPs(ArrayList<SNP> snps)
	{
		HashSet<GenomicPosition> posset=new HashSet<GenomicPosition>();
		for(SNP s: snps)
		{
			if(posset.contains(s.genomicPosition()))  throw new IllegalArgumentException("Invalid SNPs, a SNP was provided several times "+ s.genomicPosition().toString());
			posset.add(s.genomicPosition());
		}
	}
	

	private SexAssignerDirect parseSexAssigner(String line)
	{

		ArrayList<Sex> sexes=new ArrayList<Sex>();
		String[] a =line.split("\\s+");

		for(String s:a)
		{
			if(s.equals("#sex")){}
			else if(s.toLowerCase().equals("m"))sexes.add(Sex.Male);
			else if(s.toLowerCase().equals("f"))sexes.add(Sex.Female);
			else if(s.toLowerCase().equals("h"))sexes.add(Sex.Hermaphrodite);
			else throw new IllegalArgumentException("Invalid sex "+s);
		}
		SexAssignerDirect sa=new SexAssignerDirect(sexes);
		return sa;
	}
	/**
	 * Parse a line of the file to a SNP
	 * @param line
	 * @return
	 */
	private SNP parseSNP(String line)
	{
		//3L	13283707	T	G/T	GT GG GG GG
		String[] a =line.split("\t");
		char ref=a[2].charAt(0);
		char anc=a[3].charAt(0);    // ancestral allele
		char der=a[3].charAt(2);    // derived allele
		GenomicPosition genpos=new GenomicPosition(Chromosome.getChromosome(a[0]),Integer.parseInt(a[1]));
		return new SNP(genpos,ref,anc,der);
	}
	
	

}
