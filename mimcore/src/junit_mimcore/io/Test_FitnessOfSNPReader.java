package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.gpf.fitness.IFitnessOfSNP;
import mimcore.data.sex.Sex;
import mimcore.io.w.SNPFitnessReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Test_FitnessOfSNPReader {


	

	public static SNPFitnessReader getReaderW()
	{
		String input=	"[w]\n"+
						"2R\t1\tC/A\t0.8\t1.0\t1.1\n"+
						"2R\t2\tT/A\t0.9\t1.0\t1.1\n"+
						"2R\t3\tC/A\t1.0\t1.0\t1.1\n"+
						"2L\t5\tG/C\t1.1\t1.0\t1.1\n";


		;
		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPFitnessReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static SNPFitnessReader getSexReaderW()
	{
		String input=	"[w]\n"+
						"2R\t1\tC/A\t0.1\t0.2\t0.3\t0.4\t0.5\t0.6\t0.7\t0.8\t0.9\n";

		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPFitnessReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static SNPFitnessReader getReaderS()
	{
		String input=	"[s]\n"+
				"2R\t1\tC/A\t0.1\t0.5\n"+
				"2R\t2\tT/A\t0.2\t1.0\n";

		;
		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPFitnessReader("fakefile",br, SharedFactory.getNullLogger());

	}

	public static SNPFitnessReader getSexReaderS()
	{
		String input=	"[s]\n"+
				"2R\t1\tC/A\t0.1\t0.2\t0.3\t0.4\t0.5\t0.6\n";

		;
		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPFitnessReader("fakefile",br, SharedFactory.getNullLogger());

	}
	
	@Test
	public void fitness_of_first_SNP_in_file_w()
	{
		SNPFitnessReader r=getReaderW();
		ArrayList<IFitnessOfSNP> s2= r.getSNPFitness().getSNPs();
		ArrayList<FitnessOfSNP> s=new ArrayList<FitnessOfSNP>();
		for(IFitnessOfSNP fs:s2)s.add((FitnessOfSNP)fs);

		assertEquals(s.get(0).getPosition().chromosome().toString(),"2R");
		assertEquals(s.get(0).getPosition().position(),1);
		assertEquals(s.get(0).get_achar(),'C');
		assertEquals(s.get(0).get_Achar(),'A');
		assertEquals(s.get(0).waa(),0.8,0.0000001);
		assertEquals(s.get(0).waA(),1.0,0.0000001);
		assertEquals(s.get(0).wAA(),1.1,0.0000001);
	}

	@Test
	public void fitness_of_middle_SNP_in_file_w()
	{
		SNPFitnessReader r=getReaderW();
		ArrayList<IFitnessOfSNP> s2= r.getSNPFitness().getSNPs();
		ArrayList<FitnessOfSNP> s=new ArrayList<FitnessOfSNP>();
		for(IFitnessOfSNP fs:s2)s.add((FitnessOfSNP)fs);

		assertEquals(s.get(1).getPosition().chromosome().toString(),"2R");
		assertEquals(s.get(1).getPosition().position(),2);
		assertEquals(s.get(1).get_achar(),'T');
		assertEquals(s.get(1).get_Achar(),'A');
		assertEquals(s.get(1).waa(),0.9,0.0000001);
		assertEquals(s.get(1).waA(),1.0,0.0000001);
		assertEquals(s.get(1).wAA(),1.1,0.0000001);
	}

	@Test
	public void fitness_of_last_SNP_in_file_w()
	{
		SNPFitnessReader r=getReaderW();
		ArrayList<IFitnessOfSNP> s2= r.getSNPFitness().getSNPs();
		ArrayList<FitnessOfSNP> s=new ArrayList<FitnessOfSNP>();
		for(IFitnessOfSNP fs:s2)s.add((FitnessOfSNP)fs);

		assertEquals(s.get(3).getPosition().chromosome().toString(),"2L");
		assertEquals(s.get(3).getPosition().position(),5);
		assertEquals(s.get(3).get_achar(),'G');
		assertEquals(s.get(3).get_Achar(),'C');
		assertEquals(s.get(3).waa(),1.1,0.0000001);
		assertEquals(s.get(3).waA(),1.0,0.0000001);
		assertEquals(s.get(3).wAA(),1.1,0.0000001);
	}



	@Test
	public void fitness_of_first_SNP_in_file_s()
	{
		SNPFitnessReader r=getReaderS();
		ArrayList<IFitnessOfSNP> s2= r.getSNPFitness().getSNPs();
		// 				"2R\t1\tC/A\t0.1\t0.5\n"
		//				"2R\t2\tT/A\t0.2\t1.0\n"
		ArrayList<FitnessOfSNP> s=new ArrayList<FitnessOfSNP>();
		for(IFitnessOfSNP fs:s2)s.add((FitnessOfSNP)fs);


		assertEquals(s.get(0).getPosition().chromosome().toString(),"2R");
		assertEquals(s.get(0).getPosition().position(),1);
		assertEquals(s.get(0).get_achar(),'C');
		assertEquals(s.get(0).get_Achar(),'A');
		assertEquals(s.get(0).waa(),1.0,0.0000001);
		assertEquals(s.get(0).waA(),1.05,0.0000001);
		assertEquals(s.get(0).wAA(),1.1,0.0000001);
	}

	@Test
	public void fitness_of_last_SNP_in_file_s()
	{
		SNPFitnessReader r=getReaderS();
		ArrayList<IFitnessOfSNP> s2= r.getSNPFitness().getSNPs();
		ArrayList<FitnessOfSNP> s=new ArrayList<FitnessOfSNP>();
		for(IFitnessOfSNP fs:s2)s.add((FitnessOfSNP)fs);



		assertEquals(s.get(1).getPosition().chromosome().toString(),"2R");
		assertEquals(s.get(1).getPosition().position(),2);
		assertEquals(s.get(1).get_achar(),'T');
		assertEquals(s.get(1).get_Achar(),'A');
		assertEquals(s.get(1).waa(),1.0,0.0000001);
		assertEquals(s.get(1).waA(),1.2,0.0000001);
		assertEquals(s.get(1).wAA(),1.2,0.0000001);
	}

	@Test
	public void fitness_of_first_genotype_in_file_s()
	{
		SNPFitnessReader r=getReaderS();
		Sex m= Sex.Male;
		ArrayList<IFitnessOfSNP> s2= r.getSNPFitness().getSNPs();
		ArrayList<FitnessOfSNP> s=new ArrayList<FitnessOfSNP>();
		for(IFitnessOfSNP fs:s2)s.add((FitnessOfSNP)fs);
		//"2R	1	C/A	0.1	0.5
		char[] genoAA={'A','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),1.1,0.0000001);
		char[] genoCA={'C','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),1.05,0.0000001);
		char[] genoAC={'A','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),1.05,0.0000001);
		char[] genoCC={'C','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),1.0,0.0000001);

	}

	@Test
	public void fitness_of_sexspecific_genotype_in_file_s()
	{
		//"2R\t1\tC/A\t0.1\t0.2\t0.3\t0.4\t0.5\t0.6\n";
		SNPFitnessReader r=getSexReaderS();
		Sex m= Sex.Male;
		ArrayList<IFitnessOfSNP> s= r.getSNPFitness().getSNPs();

		//"2R	1	C/A	0.1	0.5
		char[] genoAA={'A','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),1.1,0.0000001);
		char[] genoCA={'C','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),1.02,0.0000001);
		char[] genoAC={'A','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),1.02,0.0000001);
		char[] genoCC={'C','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),1.0,0.0000001);

		m= Sex.Female;
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),1.3,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),1.12,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),1.12,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),1.0,0.0000001);

		m= Sex.Hermaphrodite;
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),1.5,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),1.3,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),1.3,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),1.0,0.0000001);

	}

	@Test
	public void fitness_of_sexspecific_genotype_in_file_w()
	{

		SNPFitnessReader r=getSexReaderW();

		ArrayList<IFitnessOfSNP> s= r.getSNPFitness().getSNPs();

		Sex m= Sex.Male;
		char[] genoAA={'A','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),0.3,0.0000001);
		char[] genoCA={'C','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),0.2,0.0000001);
		char[] genoAC={'A','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),0.2,0.0000001);
		char[] genoCC={'C','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),0.1,0.0000001);

		m= Sex.Female;
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),0.6,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),0.5,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),0.5,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),0.4,0.0000001);

		m= Sex.Hermaphrodite;
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAA,m),0.9,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCA,m),0.8,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoAC,m),0.8,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(genoCC,m),0.7,0.0000001);

	}

	

}
