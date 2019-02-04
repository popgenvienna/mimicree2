package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.*;

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
				String[] a=line.split("\\s+");
				if(a.length!=5 && a.length!=13) throw new IllegalArgumentException("Every entry in the disruptive fitness function file must have 5 or 13 columns (sex specific)");
				int generation=Integer.parseInt(a[0]);
				IFitnessCalculator selReg=null;
				if(a.length==5) selReg=parseNoSex(a);
				else if(a.length==13) selReg=parseSex(a);
				else throw new IllegalArgumentException("Fatal error");
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

	private FitnessFunctionQuantitativeDisruptive parseNoSex(String[] toparse)
	{
		double minFitness=Double.parseDouble(toparse[1]);
		double maxFitness=Double.parseDouble(toparse[2]);
		double meanOfPeak=Double.parseDouble(toparse[3]);
		double stdDevOfPeak=Double.parseDouble(toparse[4]);
		if(minFitness<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitness<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitness<minFitness) throw new IllegalArgumentException("The maximum fitness must equal or larger than the minimum fitness");
		if(stdDevOfPeak<=0) throw new IllegalArgumentException("The standard deviation of the fitness function must be larger than zero");
		return new FitnessFunctionQuantitativeDisruptive(minFitness,maxFitness,meanOfPeak,stdDevOfPeak);
	}

	private FitnessCalculatorSexSpecific parseSex(String[] toparse)
	{

		// MALE
		double minFitnessM=Double.parseDouble(toparse[1]);
		double maxFitnessM=Double.parseDouble(toparse[2]);
		double meanOfPeakM=Double.parseDouble(toparse[3]);
		double stdDevOfPeakM=Double.parseDouble(toparse[4]);
		if(minFitnessM<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessM<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessM<minFitnessM) throw new IllegalArgumentException("The maximum fitness must equal or larger than the minimum fitness");
		if(stdDevOfPeakM<=0) throw new IllegalArgumentException("The standard deviation of the fitness function must be larger than zero");
		IFitnessCalculator selregM= new FitnessFunctionQuantitativeDisruptive(minFitnessM,maxFitnessM,meanOfPeakM,stdDevOfPeakM);


		// FEMALE
		double minFitnessF=Double.parseDouble(toparse[5]);
		double maxFitnessF=Double.parseDouble(toparse[6]);
		double meanOfPeakF=Double.parseDouble(toparse[7]);
		double stdDevOfPeakF=Double.parseDouble(toparse[8]);
		if(minFitnessF<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessF<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessF<minFitnessF) throw new IllegalArgumentException("The maximum fitness must equal or larger than the minimum fitness");
		if(stdDevOfPeakF<=0) throw new IllegalArgumentException("The standard deviation of the fitness function must be larger than zero");
		IFitnessCalculator selregF= new FitnessFunctionQuantitativeDisruptive(minFitnessF,maxFitnessF,meanOfPeakF,stdDevOfPeakF);

		// HERMAPHRODITE
		double minFitnessH=Double.parseDouble(toparse[9]);
		double maxFitnessH=Double.parseDouble(toparse[10]);
		double meanOfPeakH=Double.parseDouble(toparse[11]);
		double stdDevOfPeakH=Double.parseDouble(toparse[12]);
		if(minFitnessH<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessH<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessH<minFitnessH) throw new IllegalArgumentException("The maximum fitness must equal or larger than the minimum fitness");
		if(stdDevOfPeakH<=0) throw new IllegalArgumentException("The standard deviation of the fitness function must be larger than zero");
		IFitnessCalculator selregH= new FitnessFunctionQuantitativeDisruptive(minFitnessH,maxFitnessH,meanOfPeakH,stdDevOfPeakH);

		return new FitnessCalculatorSexSpecific(selregM,selregF,selregH);

	}

}
