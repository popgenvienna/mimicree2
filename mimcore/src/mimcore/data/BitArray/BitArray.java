package mimcore.data.BitArray;

/** 
 * Immutable representation of BitArray;
 * 
 * @author robertkofler
 *
 */
public class BitArray 
{
	
	private final byte[] bitar;
	private final int size;
	
	public BitArray(byte[] bitar, int size)
	{
		// Ensure immutability by cloning
		this.bitar=bitar.clone();
		this.size=size;
	}
	
	
	/**
	 * Is the bit at the given position set?
	 * @param position
	 * @return
	 */
	public boolean hasBit(int position)
	{
		if(position >=size ) throw new IndexOutOfBoundsException("Index in BitArray is out of range");
		// Calculate the components
		int real=(int)(position/8.0);
		int mod=position % 8;
		
		// Calculate the result of the logical and to see if the bit at the position is set
		int res= this.bitar[real] & BitArrayBase.bitdec[mod];
		
		if(res!=0) return true;
		return false;
	}
	
	/**
	 * Obtain the size of the BitArray
	 * @return
	 */
	public int size()
	{
		return this.size;
	}
	
}
