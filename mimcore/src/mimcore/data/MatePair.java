package mimcore.data;

import mimcore.data.BitArray.BitArrayBuilder;
import mimcore.data.Mutator.IMutator;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNPCollection;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by robertkofler on 8/28/14.
 */
public class MatePair {
	private final Specimen s1;
	private final Specimen s2;

	public MatePair(Specimen s1, Specimen s2)
	{
		this.s1=s1;
		this.s2=s2;
	}


	public DiploidGenome getChildHaploid(RecombinationGenerator recGenerator, Sex sex, IMutator mutator, SexInfo si, Random random)
	{
		// Mating of haploids,
		// first make a novel fake Diploid, continaing the first haplotype of each individual
		DiploidGenome chimera=new DiploidGenome(s1.getGenome().getHaplotypeA(),s2.getGenome().getHaplotypeA());
		Specimen chimeraSpec=new Specimen(Sex.Hermaphrodite,0.0,0.0,0.0,chimera);

		// than get a gamete allowing the two haplotypes to recombine (if desired)
		SNPCollection snpCollection= chimera.getHaplotypeA().getSNPCollection();
		BitArrayBuilder haploid=chimeraSpec.getGamete(recGenerator,mutator,random);

		// generate the new genome, just take the single haplotype twice, allowing to continue to operate with diploids despite haploids being simulated
		HaploidGenome novelGenome=new HaploidGenome(haploid.getBitArray(),snpCollection);
		DiploidGenome toret =new DiploidGenome(novelGenome,novelGenome);

		return toret;
	}


	public DiploidGenome getChild(RecombinationGenerator recGenerator, Sex sex, IMutator mutator, SexInfo si, Random random)
	{
		SNPCollection snpCollection=s1.getGenome().getHaplotypeA().getSNPCollection();
		BitArrayBuilder babsemen=s1.getGamete(recGenerator, mutator, random);
		BitArrayBuilder babegg	=s2.getGamete(recGenerator, mutator, random);

		// take sex chromosomes into account; make hemizygous ones homozygous
		ArrayList<Integer> hemizygousIndizi= si.getHemizygousSites(sex);
		if(hemizygousIndizi.size()>0) homozygotify(babsemen,babegg,hemizygousIndizi);





		HaploidGenome semen	=new HaploidGenome(babsemen.getBitArray(),snpCollection);
		HaploidGenome egg	=new HaploidGenome(babegg.getBitArray(),snpCollection);
		DiploidGenome fertilizedEgg=new DiploidGenome(semen,egg);
		return fertilizedEgg;
	}

	public Specimen getS1(){return this.s1;}
	public Specimen getS2(){return this.s2;}



	private void homozygotify(BitArrayBuilder bab1, BitArrayBuilder bab2, ArrayList<Integer> positions)
	{
		for(int i: positions)
		{
			boolean bab1hasit=bab1.hasBit(i);
			boolean bab2hasit= bab2.hasBit(i);
			if(bab1hasit!=bab2hasit) bab2.flipBit(i);
		}
	}
}
