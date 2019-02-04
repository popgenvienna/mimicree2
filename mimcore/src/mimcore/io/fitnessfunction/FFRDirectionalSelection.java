package mimcore.io.fitnessfunction;

import mimcore.data.gpf.fitness.*;

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
				if(a.length!=6 && a.length!=16) throw new IllegalArgumentException("Every entry of the directional selection fitness function file must have  6 or 16 columns (tab separated)");
				int generation=Integer.parseInt(a[0]);
				IFitnessCalculator selReg=null;
				if(a.length==6) selReg=parseNoSex(a);
				else if(a.length==16)selReg=parseSex(a);
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

	private FitnessFunctionQuantitativeDirectionalSelection parseNoSex(String[] toparse)
	{

		double minFitness=Double.parseDouble(toparse[1]);
		double maxFitness=Double.parseDouble(toparse[2]);
		double s=Double.parseDouble(toparse[3]);
		double r=Double.parseDouble(toparse[4]);
		double beta=Double.parseDouble(toparse[5]);
		if(minFitness<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitness<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitness<minFitness) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(s<=0) throw new IllegalArgumentException("The s value must be larger than zero");
		return new FitnessFunctionQuantitativeDirectionalSelection(minFitness,maxFitness,s,r,beta);

	}

	private FitnessCalculatorSexSpecific parseSex(String[] toparse)
	{

		double minFitnessM=Double.parseDouble(toparse[1]);
		double maxFitnessM=Double.parseDouble(toparse[2]);
		double sM=Double.parseDouble(toparse[3]);
		double rM=Double.parseDouble(toparse[4]);
		double betaM=Double.parseDouble(toparse[5]);
		if(minFitnessM<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessM<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessM<minFitnessM) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(sM<=0) throw new IllegalArgumentException("The s value must be larger than zero");
		IFitnessCalculator selregM= new FitnessFunctionQuantitativeDirectionalSelection(minFitnessM,maxFitnessM,sM,rM,betaM);

		double minFitnessF=Double.parseDouble(toparse[6]);
		double maxFitnessF=Double.parseDouble(toparse[7]);
		double sF=Double.parseDouble(toparse[8]);
		double rF=Double.parseDouble(toparse[9]);
		double betaF=Double.parseDouble(toparse[10]);
		if(minFitnessF<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessF<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessF<minFitnessF) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(sF<=0) throw new IllegalArgumentException("The s value must be larger than zero");
		IFitnessCalculator selregF= new FitnessFunctionQuantitativeDirectionalSelection(minFitnessF,maxFitnessF,sF,rF,betaF);

		double minFitnessH=Double.parseDouble(toparse[11]);
		double maxFitnessH=Double.parseDouble(toparse[12]);
		double sH=Double.parseDouble(toparse[13]);
		double rH=Double.parseDouble(toparse[14]);
		double betaH=Double.parseDouble(toparse[15]);
		if(minFitnessH<0) throw new IllegalArgumentException("The minimum fitness must be zero or larger");
		if(maxFitnessH<0) throw new IllegalArgumentException("The maximum fitness must be zero or larger");
		if(maxFitnessH<minFitnessH) throw new IllegalArgumentException("The maximum fitness must be equal or larger than the minimum fitness");
		if(sH<=0) throw new IllegalArgumentException("The s value must be larger than zero");
		IFitnessCalculator selregH= new FitnessFunctionQuantitativeDirectionalSelection(minFitnessH,maxFitnessH,sH,rH,betaH);

		return new FitnessCalculatorSexSpecific(selregM,selregF,selregH);

	}

}
