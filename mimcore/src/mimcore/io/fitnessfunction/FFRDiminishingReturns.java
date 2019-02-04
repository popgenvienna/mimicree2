package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class FFRDiminishingReturns {

	private BufferedReader bf;
	public FFRDiminishingReturns(BufferedReader br)
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
				if(a.length!=5 && a.length!=13) throw new IllegalArgumentException("Every entry in the diminishing returns epistasis fitness function file must have  5 or 13 columns (sex specific)");
				int generation=Integer.parseInt(a[0]);
				IFitnessCalculator selReg=null;
				if(a.length==5) selReg=parseNoSex(a);
				else if(a.length==13)selReg=parseSex(a);
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


	private FitnessFunctionQuantitativeDimRet parseNoSex(String[] toparse)
	{

		double minFitness=Double.parseDouble(toparse[1]);
		double maxFitness=Double.parseDouble(toparse[2]);
		double alpha=Double.parseDouble(toparse[3]);
		double beta=Double.parseDouble(toparse[4]);
		if(minFitness<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitness<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitness<minFitness) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(alpha<=0) throw new IllegalArgumentException("The alpha value must be larger than zero");
		return new FitnessFunctionQuantitativeDimRet(minFitness,maxFitness,alpha,beta);
	}

	private FitnessCalculatorSexSpecific parseSex(String[] toparse)
	{

		double minFitnessM=Double.parseDouble(toparse[1]);
		double maxFitnessM=Double.parseDouble(toparse[2]);
		double alphaM=Double.parseDouble(toparse[3]);
		double betaM=Double.parseDouble(toparse[4]);
		if(minFitnessM<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessM<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessM<minFitnessM) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(alphaM<=0) throw new IllegalArgumentException("The alpha value must be larger than zero");
		FitnessFunctionQuantitativeDimRet selregM= new FitnessFunctionQuantitativeDimRet(minFitnessM,maxFitnessM,alphaM,betaM);


		double minFitnessF=Double.parseDouble(toparse[5]);
		double maxFitnessF=Double.parseDouble(toparse[6]);
		double alphaF=Double.parseDouble(toparse[7]);
		double betaF=Double.parseDouble(toparse[8]);
		if(minFitnessF<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessF<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessF<minFitnessF) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(alphaF<=0) throw new IllegalArgumentException("The alpha value must be larger than zero");
		FitnessFunctionQuantitativeDimRet selregF= new FitnessFunctionQuantitativeDimRet(minFitnessF,maxFitnessF,alphaF,betaF);


		double minFitnessH=Double.parseDouble(toparse[9]);
		double maxFitnessH=Double.parseDouble(toparse[10]);
		double alphaH=Double.parseDouble(toparse[11]);
		double betaH=Double.parseDouble(toparse[12]);
		if(minFitnessH<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessH<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessH<minFitnessH) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(alphaH<=0) throw new IllegalArgumentException("The alpha value must be larger than zero");
		FitnessFunctionQuantitativeDimRet selregH= new FitnessFunctionQuantitativeDimRet(minFitnessH,maxFitnessH,alphaH,betaH);

		return new FitnessCalculatorSexSpecific(selregM,selregF,selregH);


	}

}
