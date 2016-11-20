package qmimcore.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Immutable representation of a chromosome
 * Can only be instantiated by calling the static factory method
 * @author robertkofler
 *
 */
public class Chromosome implements Comparable<Chromosome> {
	private final String chrstr;
	
	/**
	 * does not allow public instance creation
	 * @param chromosome
	 */
	private Chromosome(String chromosome)
	{
		this.chrstr=chromosome;
	}
	
		
    @Override
    public int compareTo(Chromosome b)
    {
        return this.chrstr.compareTo(b.chrstr);
    }
	
	@Override
	public boolean equals(Object o)
	{
        if(!(o instanceof Chromosome)){return false;}
        Chromosome t= (Chromosome)o;
        return this.chrstr.equals(t.chrstr);
	}
	
	@Override
	public int hashCode()
	{
		return this.chrstr.hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.chrstr;
	}
	
	// ------------------ STATIC MEMBERS --------------------------- //

	private static HashMap<String,Chromosome> buffer=new HashMap<String,Chromosome>();
	private static Chromosome defaultChromosome=new Chromosome("__none__");
	/**
	 * Obtain an instance of a default chromosome (which is no chromosome
	 * @return
	 */
	public static Chromosome getDefaultChromosome()
	{
		return defaultChromosome;
	}
	/**
	 * Obtain a new instance of a chromosome
	 * @param chromosome
	 * @return
	 */
	public static Chromosome getChromosome(String chromosome)
	{
		if(buffer.containsKey(chromosome)) return buffer.get(chromosome);
		
		Chromosome chr= new Chromosome(chromosome);
		buffer.put(chromosome,chr);
		return chr;
	}
	
	public static ArrayList<Chromosome> getChromosome()
	{
		return new ArrayList<Chromosome>(buffer.values());
	}

	public static void resetChromosomes()
	{
		buffer=new HashMap<String,Chromosome>();
	}

}
