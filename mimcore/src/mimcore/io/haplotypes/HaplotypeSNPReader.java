package qmimcore.io.haplotypes;

import qmimcore.data.Chromosome;
import qmimcore.data.GenomicPosition;
import qmimcore.data.haplotypes.*;

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
	
	public HaplotypeSNPReader(BufferedReader bf )
	{
		this.bf=bf;
	}
	
	/**
	 * Obtain all SNPs from a haplotype file.
	 * @return a unsorted collection of SNPs
	 */
	public SNPCollection getSNPcollection()
	{
		
		ArrayList<SNP> snpcol=new ArrayList<SNP>();
		SNP s;
		while((s=next())!=null)
		{
			snpcol.add(s);
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
	
	/**
	 * Read one SNP at the time from a haplotype file;
	 * @return
	 */
	private SNP next()
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
		return parseSNP(line);
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
