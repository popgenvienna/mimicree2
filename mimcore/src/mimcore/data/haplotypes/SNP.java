package mimcore.data.haplotypes;

import mimcore.data.GenomicPosition;


/**
 * Immutable representation of a SNP;
 * Only the information about IGenomicPositon is relevant (for sorting as well as for hashing
 * 
 * Only two allelic SNPs are permited. Representation is immutable thus may be hashed (only chromosome and position is used for hashing)
 * 
 * @author robertkofler
 *
 */
public class SNP implements Comparable<SNP>{
	private final GenomicPosition genpos;
	private final char referenceCharacter;
	private final char ancestralAllele;
	private final char derivedAllele;
	
	public SNP(GenomicPosition genpos, char referenceCharacter, char ancestralAllele,char derivedAllele)
	{
		this.genpos=genpos;
		this.referenceCharacter=Character.toUpperCase(referenceCharacter);
		this.ancestralAllele=Character.toUpperCase(ancestralAllele);
		this.derivedAllele=Character.toUpperCase(derivedAllele);
	}

	/**
	 * return the position of the SNP in the genome
	 * @return
	 */
	public GenomicPosition genomicPosition()
	{
		return this.genpos;
	}
	
	/**
	 * return the character in the reference sequence
	 * @return
	 */
	public char referenceCharacter()
	{
		return this.referenceCharacter;
	}
	
	/**
	 * return the ancestral allele of the SNP
	 * @return
	 */
	public char ancestralAllele()
	{
		return this.ancestralAllele;
	}
	
	/**
	 * return the derived allele of the SNP
	 * @return
	 */
	public char derivedAllele()
	{
		return this.derivedAllele;
	}
	
	@Override 
	public int compareTo(SNP a){ 
		return this.genomicPosition().compareTo(a.genomicPosition());
	}
	
	@Override
	public String toString()
	{
		return this.genpos.toString()+" "+this.ancestralAllele+"/"+this.derivedAllele;
	}
	
	
   
	
	

}
