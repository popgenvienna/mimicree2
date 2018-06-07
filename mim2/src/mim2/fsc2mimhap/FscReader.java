package mim2.fsc2mimhap;


import mimcore.data.BitArray.BitArray;
import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNP;
import mimcore.data.haplotypes.SNPCollection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 *    Reading the output of fastsimcoal
 */
public class FscReader {
    private final String inputFile;
    private final String chromosomeName;
    private Logger logger;
    private BufferedReader br;
    private final char refChar='A';
    private final char derivedChar='C';

    public FscReader(String inputFile, String chromosomeName, Logger logger)
    {
        this.chromosomeName=chromosomeName;
        this.inputFile=inputFile;

		try
		{
       		 br=new BufferedReader(new FileReader(this.inputFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
        this.logger = logger;
    }


	/**
	 * Read Haplotypes from a fastsimcoal file
	 * @return a collection of haplotypes read from a fastsimcoalfile
	 */
    public ArrayList<Haplotype> getHaplotypes()
    {

        SNPCollection snpCol;
        ArrayList<Integer> snpPositions =new ArrayList<Integer>();
        ArrayList<BitArray> haplotypes=new ArrayList<BitArray>();
        String line="";
		this.logger.info("Start reading haplotypes for chromosome" + this.chromosomeName + "from fastsimcoal outputfile " + this.inputFile);
        boolean foundSegsites=false;
        boolean readHaplotypes=false;
        try
        {
            while((line=br.readLine())!=null)
            {

                if(readHaplotypes)
                {
                    // Obtain the haplotypes
                    haplotypes.add(parseHaplotypes(line));
                }
                else if(foundSegsites)
                {
					this.logger.info("Reading SNP positions");
                    snpPositions=parseSNPPositions(line);
                    readHaplotypes=true;
					this.logger.info("Reading haplotypes");
                }
                else if(line.startsWith("segsites")) foundSegsites = true;

            }

        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }

		this.logger.info("Formatting haplotypes");
        ArrayList<Haplotype> toret= processHaplotypes(snpPositions, haplotypes, Chromosome.getChromosome(this.chromosomeName));

		this.logger.info("Finished reading Haplotypes");
		return toret;
    }


	/**
	 * Convert the SNP positions and the bitarrays to haplotypes
	 * @param positions
	 * @param haplotypes
	 * @param chromosome
	 * @return
	 */
    private ArrayList<Haplotype> processHaplotypes(ArrayList<Integer> positions, ArrayList<BitArray> haplotypes, Chromosome chromosome)
    {

        ArrayList<SNP> snps=new ArrayList<SNP>();

        // Detect ancestral and derived allele
        // 0 is ancestral (refchar) 1 is derived most are zero and a few are 1 (according to popgen expectation)
        for(int i=0; i < positions.size();i++)
        {

            char ancestralAllele=this.refChar;  //A
			char derivedAllele=this.derivedChar; //C
            int counter=0;
            SNP s = new SNP(new GenomicPosition(chromosome,positions.get(i)),this.refChar,ancestralAllele,derivedAllele);
            snps.add(s);
        }


		// Finally create the haplotypes by combining a bitarray and a snpcollection
        SNPCollection snpCollection=new SNPCollection(snps);
		ArrayList<Haplotype> haps=new ArrayList<Haplotype>();
		for(BitArray ba: haplotypes)
		{
			Haplotype h= new Haplotype(ba,snpCollection);
			haps.add(h);
		}
		return haps;
    }


    private ArrayList<Integer> parseSNPPositions(String line)
    {
        if(line.equals("")) throw new IllegalArgumentException("There is no SNP definition after the segsites");
        String[] rawPositions=line.split("\\s+");

        ArrayList<Integer> positions=new ArrayList<Integer>();
		for(int i=1; i<rawPositions.length; i++)
		{
			// Starting with 1 as the first entry has to be ignored (="Positions:")
			String s=rawPositions[i];
            positions.add(Integer.parseInt(s));
        }
        return positions;
    }


    private BitArray parseHaplotypes(String line)
    {
        char[] t=line.toCharArray();
        BitArrayBuilder builder=new BitArrayBuilder(t.length);
        for(int i=0; i<t.length; i++)
        {
           	char s=t[i];
            if(s == '0')
            {
				// 1 in MimicrEE means that the SNP has the major allele; Switch between fastsimcoal
                 builder.setBit(i);
            }
            else if(s=='1')
            {
			    // 0 in MimicrEE means does not have the major allele, thus the minor allele

            }
            else throw new IllegalArgumentException("Can not parse character"+s);
        }
        return builder.getBitArray();
    }




}










