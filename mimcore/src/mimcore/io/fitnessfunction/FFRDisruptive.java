package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.FitnessFunctionContainer;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeDisruptive;
import mimcore.data.gpf.fitness.FitnessFunctionQuantitativeGauss;
import mimcore.data.gpf.fitness.IFitnessCalculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class FFRDisruptive {

	private BufferedReader bf;
	public FFRDisruptive(BufferedReader br)
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
				String[] a=line.split("\t");
				if(a.length!=5) throw new IllegalArgumentException("Every entry in the disruptive fitness function file must have exactly five columns (tab separated)");
				int generation=Integer.parseInt(a[0]);
				double minFitness=Double.parseDouble(a[1]);
				double maxFitness=Double.parseDouble(a[2]);
				double meanOfPeak=Double.parseDouble(a[3]);
				double stdDevOfPeak=Double.parseDouble(a[4]);
				if(minFitness<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
				if(maxFitness<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
				if(maxFitness<minFitness) throw new IllegalArgumentException("The maximum fitness must equal or larger than the minimum fitness");
				if(stdDevOfPeak<=0) throw new IllegalArgumentException("The standard deviation of the fitness function must be larger than zero");

				IFitnessCalculator selReg=new FitnessFunctionQuantitativeDisruptive(minFitness,maxFitness,meanOfPeak,stdDevOfPeak);
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
