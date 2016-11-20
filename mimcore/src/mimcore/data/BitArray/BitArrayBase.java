package mimcore.data.BitArray;

/** 
 * Share resources for all BitArrays
 * @author robertkofler
 *
 */
class BitArrayBase {
	
	// The following byte-sequence allows to address every bit separately (out of 8 bits)
	public static byte[] bitdec = {1 , 2 , 4 , 8 , 16 , 32 , 64 , -128};
	

}
