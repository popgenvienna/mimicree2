package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDimRet;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDirectionalSelection;
import mimcore.data.gpf.fitness.IFitnessCalculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class FFRDirectionalSelection {

	private BufferedReader bf;
	public FFRDirectionalSelection(BufferedReader br)
	{
		this.bf=br;
	}
	
	/**
	 * read the selection regime
	 * @return
	 */
	public FitnessFunctionContainer readFitnessFunction()
	{

		HashMap<Integer,IFitnessCalculator> res=new HashMap<Integer, IFitnessCalculator>();
		String line;
		try
		{
			while((line=bf.readLine())!=null)
			{
				String[] a=line.split("\\s+");
				if(a.length!=6) throw new IllegalArgumentException("Every entry of the directional selection fitness function file must have exactly six columns (tab separated)");
				int generation=Integer.parseInt(a[0]);
				double minFitness=Double.parseDouble(a[1]);
				double maxFitness=Double.parseDouble(a[2]);
				double s=Double.parseDouble(a[3]);
				double r=Double.parseDouble(a[4]);
				double beta=Double.parseDouble(a[5]);
				if(minFitness<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
				if(maxFitness<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
				if(maxFitness<minFitness) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
				if(s<=0) throw new IllegalArgumentException("The s value must be larger than zero");


				IFitnessCalculator selReg=new FitnessFunctionQuantitativeDirectionalSelection(minFitness,maxFitness,s,r,beta);
				res.put(generation,selReg);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		try
		{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return new FitnessFunctionContainer(res);
	}

}
