package junit_mimcore.io;

import junit_mimcore.factories.SharedFactory;
import mimcore.data.gpf.fitness.FitnessOfSNP;
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

	public static SNPFitnessReader getReaderS()
	{
		String input=	"[s]\n"+
				"2R\t1\tC/A\t0.1\t0.5\n"+
				"2R\t2\tT/A\t0.2\t1.0\n";

		;
		BufferedReader br=new BufferedReader(new StringReader(input));
		return new SNPFitnessReader("fakefile",br, SharedFactory.getNullLogger());

	}
	
	@Test
	public void fitness_of_first_SNP_in_file_w()
	{
		SNPFitnessReader r=getReaderW();
		ArrayList<FitnessOfSNP> s= r.getSNPFitness().getSNPs();

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
		ArrayList<FitnessOfSNP> s= r.getSNPFitness().getSNPs();

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
		ArrayList<FitnessOfSNP> s= r.getSNPFitness().getSNPs();

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
		ArrayList<FitnessOfSNP> s= r.getSNPFitness().getSNPs();
		// 				"2R\t1\tC/A\t0.1\t0.5\n"
		//				"2R\t2\tT/A\t0.2\t1.0\n"


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
		ArrayList<FitnessOfSNP> s= r.getSNPFitness().getSNPs();
		// 				"2R\t1\tC/A\t0.1\t0.5\n"
		//				"2R\t2\tT/A\t0.2\t1.0\n"


		assertEquals(s.get(1).getPosition().chromosome().toString(),"2R");
		assertEquals(s.get(1).getPosition().position(),2);
		assertEquals(s.get(1).get_achar(),'T');
		assertEquals(s.get(1).get_Achar(),'A');
		assertEquals(s.get(1).waa(),1.0,0.0000001);
		assertEquals(s.get(1).waA(),1.2,0.0000001);
		assertEquals(s.get(1).wAA(),1.2,0.0000001);
	}

	

}
