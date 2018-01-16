package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.gpf.quantitative.AdditiveSNPeffect;
import mimcore.data.gpf.quantitative.GenotypeCalculator;
import mimcore.data.gpf.quantitative.IAdditiveSNPeffect;
import mimcore.data.sex.Sex;
import mimcore.io.SNPQuantitativeEffectSizeReader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Test_SNPQuantitativeEffectSizeReader {


	public static SNPQuantitativeEffectSizeReader getSexReader() {
		String input =
				"1R\t1\tC/A\t2.0\t1.1\t3.0\t0.0\t-2.0\t-1.0\n" +
						"2R\t2\tA/C\t2.1\t1.2\n";
		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPQuantitativeEffectSizeReader("fakefile",br, SharedFactory.getNullLogger());
	}
	

	public static SNPQuantitativeEffectSizeReader getReader()
	{
		String input=
						"1R\t1\tC/A\t2.0\t1.1\n"+
						"2R\t2\tA/C\t2.1\t1.2\n"+
						"3R\t3\tC/T\t2.2\t1.3\n"+
						"4\t4\tG/C\t2.3\t1.4\n";

		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPQuantitativeEffectSizeReader("fakefile",br, SharedFactory.getNullLogger());

	}
	
	@Test
	public void position_of_first_SNP_in_file()
	{
		ArrayList<IAdditiveSNPeffect> s= getReader().readAdditiveFitness().getAdditiveSNPeffects();

		assertEquals(s.get(0).getPosition().chromosome().toString(),"1R");
		assertEquals(s.get(0).getPosition().position(),1);

	}

	@Test
	public void position_of_second_SNP_in_file()
	{
		ArrayList<IAdditiveSNPeffect> s= getReader().readAdditiveFitness().getAdditiveSNPeffects();

		assertEquals(s.get(1).getPosition().chromosome().toString(),"2R");
		assertEquals(s.get(1).getPosition().position(),2);

	}

	@Test
	public void position_of_third_SNP_in_file()
	{
		ArrayList<IAdditiveSNPeffect> s= getReader().readAdditiveFitness().getAdditiveSNPeffects();

		assertEquals(s.get(2).getPosition().chromosome().toString(),"3R");
		assertEquals(s.get(2).getPosition().position(),3);

	}


	@Test
	public void position_of_fourth_SNP_in_file()
	{
		ArrayList<IAdditiveSNPeffect> s= getReader().readAdditiveFitness().getAdditiveSNPeffects();

		assertEquals(s.get(3).getPosition().chromosome().toString(),"4");
		assertEquals(s.get(3).getPosition().position(),4);

	}

	@Test
	public void effect_of_first_SNP_in_file()
	{
		ArrayList<IAdditiveSNPeffect> s= getReader().readAdditiveFitness().getAdditiveSNPeffects();
		Sex m=Sex.Male;

		char[] geno={'C','C'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno,m),2.0,0.0000001);

		char[] geno1={'C','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno1,m),1.1,0.000001);

		char[] geno2={'A','A'};
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno2,m),-2.0,0.000001);

	}


	@Test
	public void effect_of_last_SNP_in_file()
	{
		ArrayList<IAdditiveSNPeffect> s= getReader().readAdditiveFitness().getAdditiveSNPeffects();
		Sex m=Sex.Male;

		char[] geno={'G','G'};
		assertEquals(s.get(3).getEffectSizeOfGenotype(geno,m),2.3,0.0000001);

		char[] geno1={'C','G'};
		assertEquals(s.get(3).getEffectSizeOfGenotype(geno1,m),1.4,0.000001);

		char[] geno2={'C','C'};
		assertEquals(s.get(3).getEffectSizeOfGenotype(geno2,m),-2.3,0.000001);

	}


	@Test
	public void sex_specific()
	{
		SNPQuantitativeEffectSizeReader sr=getSexReader();
		GenotypeCalculator gc=sr.readAdditiveFitness();
		ArrayList<IAdditiveSNPeffect> s= gc.getAdditiveSNPeffects();


		char[] geno={'A','A'};
		char[] geno1={'C','A'};
		char[] geno2={'C','C'};

		Sex m=Sex.Male;
		// "1R	1	C/A	2.0	1.1	3.0	0.0	-2.0	-1.0\
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno,m),-2.0,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno1,m),1.1,0.000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno2,m),2.0,0.000001);

		m=Sex.Female;
		// "1R	1	C/A	2.0	1.1	3.0	0.0	-2.0	-1.0\
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno,m),-3.0,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno1,m),0.0,0.000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno2,m),3.0,0.000001);


		m=Sex.Hermaphrodite;
		// "1R	1	C/A	2.0	1.1	3.0	0.0	-2.0	-1.0\
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno,m),2.0,0.0000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno1,m),-1.0,0.000001);
		assertEquals(s.get(0).getEffectSizeOfGenotype(geno2,m),-2.0,0.000001);

	}


	@Test
	public void sex_specific_mixed()
	{
		SNPQuantitativeEffectSizeReader sr=getSexReader();
		GenotypeCalculator gc=sr.readAdditiveFitness();
		ArrayList<IAdditiveSNPeffect> s= gc.getAdditiveSNPeffects();


		char[] geno={'A','A'};
		char[] geno1={'C','A'};
		char[] geno2={'C','C'};

		Sex m=Sex.Male;
		//"2R\t2\tA/C\t2.1\t1.2\n";
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno,m),2.1,0.0000001);
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno1,m),1.2,0.000001);
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno2,m),-2.1,0.000001);

		m=Sex.Female;
		// "1R	1	C/A	2.0	1.1	3.0	0.0	-2.0	-1.0\
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno,m),2.1,0.0000001);
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno1,m),1.2,0.000001);
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno2,m),-2.1,0.000001);


		m=Sex.Hermaphrodite;
		// "1R	1	C/A	2.0	1.1	3.0	0.0	-2.0	-1.0\
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno,m),2.1,0.0000001);
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno1,m),1.2,0.000001);
		assertEquals(s.get(1).getEffectSizeOfGenotype(geno2,m),-2.1,0.000001);

	}


	

	

}
