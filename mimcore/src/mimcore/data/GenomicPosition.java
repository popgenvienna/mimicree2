package mimcore.data;


/**
 * Represents a position within a genome.
 * Maybe used for sorting or for hashing to support fast access to data associated with genomic positions
 * @author robertkofler
 *
 */
public class GenomicPosition implements Comparable<GenomicPosition> {
	private final Chromosome chromosome;
	private final int position;
	
	public GenomicPosition(Chromosome chromosome, int position)
	{
		this.chromosome=chromosome;
		this.position=position;
	}
	
	public Chromosome chromosome()
	{
		return this.chromosome;
	}
	
	public int position()
	{
		return this.position;
	}
	
	 /**
     * Sort the SNPs by chromosome and than by position
     * @param b the Snp to compare this SNP to
     * @return the sort order
     */
    @Override
    public int compareTo(GenomicPosition b)
    {
        int chrcomp=this.chromosome().compareTo(b.chromosome());
        if(chrcomp!=0) return chrcomp;
        if(this.position() < b.position()) return -1;
        if(this.position() > b.position()) return 1;
        return 0;   
    }
    
    @Override
    public int hashCode()
    {
        int hc=17;
        hc=hc*31+chromosome.hashCode();
        hc=hc*31+position;
        return hc;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof GenomicPosition)){return false;}
        GenomicPosition tc=(GenomicPosition)o; 
        if(tc.position() == this.position() && tc.chromosome().equals(this.chromosome())){return true;}
        return false;
    }
    
    @Override
    public String toString()
    {
    	return String.format("%s:%d", this.chromosome,this.position);
    }

}
