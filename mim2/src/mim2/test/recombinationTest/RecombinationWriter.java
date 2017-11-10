package mim2.test.recombinationTest;

import mimcore.data.misc.Tuple;
import mimcore.data.statistic.GPF;
import mimcore.data.statistic.GPFCollection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RecombinationWriter {
	public final String outputFile;
	public BufferedWriter bf;

	public RecombinationWriter(String outputFile)
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
	
	public void write(ArrayList<Tuple<Double,Integer>> toWrite)
	{



		for(Tuple<Double,Integer> t: toWrite)
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
