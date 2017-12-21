package mim2.test.mutationTest;

import mimcore.data.misc.Tuple;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MutationWriter {
	public final String outputFile;
	public BufferedWriter bf;

	public MutationWriter(String outputFile)
	{
		this.outputFile=outputFile;
		try
		{
			bf=new BufferedWriter(new FileWriter(outputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void write(ArrayList<Tuple<String,Integer>> toWrite)
	{



		for(Tuple<String,Integer> t: toWrite)
		{

				StringBuilder sb=new StringBuilder();
				sb.append(t.x); sb.append("\t");
				sb.append(t.y);
				try
				{
					bf.write(sb.toString()+"\n");
				}
				catch(IOException e)
				{
					e.printStackTrace();
					System.exit(0);
				}


		}

		
		try{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	

	

}
