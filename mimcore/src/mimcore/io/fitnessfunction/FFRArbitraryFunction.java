package mimcore.io.fitnessfunction;

import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import mimcore.data.gpf.fitness.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FFRArbitraryFunction {

	private BufferedReader bf;

	public FFRArbitraryFunction(BufferedReader br) {
		this.bf = br;
	}

	/**
	 * read the selection regime
	 *
	 * @return
	 */
	public FitnessFunctionContainer readFitnessFunction() {

		HashMap<Integer, ArrayList<ArrayList<ArbitraryLandscapeEntry>>> data = new HashMap<Integer, ArrayList<ArrayList<ArbitraryLandscapeEntry>>>();
		String line;
		try {
			while ((line = bf.readLine()) != null) {
				String[] a = line.split("\\s+");
				if (a.length != 3 && a.length!=5) throw new IllegalArgumentException("Every entry in the fitness function file must have exactly 3 or 7 columns (sex specific)");
				int generation = Integer.parseInt(a[0]);

				ArbitraryLandscapeEntry[] entries=null;
				if(a.length==3) entries=parseNoSex(a);
				else if(a.length==5) entries=parseSex(a);
				else throw new IllegalArgumentException("Fatal error");

				ArrayList<ArbitraryLandscapeEntry> tostore=new ArrayList<ArbitraryLandscapeEntry>();
				for(ArbitraryLandscapeEntry ae:entries)tostore.add(ae);

				if (!data.containsKey(generation)) data.put(generation, new ArrayList<ArrayList<ArbitraryLandscapeEntry>>());
				data.get(generation).add(tostore);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		try {
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// map the entries to the Fitness functions
		HashMap<Integer, IFitnessCalculator> res = new HashMap<Integer, IFitnessCalculator>();
		for (Map.Entry<Integer, ArrayList<ArrayList<ArbitraryLandscapeEntry>>> entry : data.entrySet()) {
			Integer key = entry.getKey();

			IFitnessCalculator fc=parseSingleGeneration(entry.getValue());
			res.put(key, fc);
		}

		return new FitnessFunctionContainer(res);
	}

	private IFitnessCalculator parseSingleGeneration(ArrayList<ArrayList<ArbitraryLandscapeEntry>>entries)
	{
		ArrayList<ArbitraryLandscapeEntry> ms=new ArrayList<ArbitraryLandscapeEntry>();
		ArrayList<ArbitraryLandscapeEntry> fs=new ArrayList<ArbitraryLandscapeEntry>();
		ArrayList<ArbitraryLandscapeEntry> hs=new ArrayList<ArbitraryLandscapeEntry>();

		for(ArrayList<ArbitraryLandscapeEntry> point:entries)
		{
			ms.add(point.get(0)); // zero are males
			fs.add(point.get(1)); // next females
			hs.add(point.get(2)); // finally hermaphrodites
		}

		FitnessFunctionArbitraryLandscape m=new FitnessFunctionArbitraryLandscape(ms);
		FitnessFunctionArbitraryLandscape f =new FitnessFunctionArbitraryLandscape(fs);
		FitnessFunctionArbitraryLandscape h =new FitnessFunctionArbitraryLandscape(hs);

		return new FitnessCalculatorSexSpecific(m,f,h);
	}

	private ArbitraryLandscapeEntry[] parseNoSex(String[] toparse) {

		double phenotypicValue = Double.parseDouble(toparse[1]);
		double fitness = Double.parseDouble(toparse[2]);
		if (fitness < 0) throw new IllegalArgumentException("Fitness must be equal or larger than zero");
		ArbitraryLandscapeEntry ae = new ArbitraryLandscapeEntry(phenotypicValue, fitness);
		ArbitraryLandscapeEntry[] toret=new ArbitraryLandscapeEntry[3];
		toret[0]=ae; toret[1]=ae; toret[2]=ae;
		return toret;
	}

	private ArbitraryLandscapeEntry[] parseSex(String[] toparse)
	{
		double phenotypicValue = Double.parseDouble(toparse[1]);
		double fitnessM = Double.parseDouble(toparse[2]);
		if (fitnessM < 0) throw new IllegalArgumentException("Fitness of male must be equal or larger than zero");
		ArbitraryLandscapeEntry aeM = new ArbitraryLandscapeEntry(phenotypicValue, fitnessM);


		double fitnessF = Double.parseDouble(toparse[3]);
		if (fitnessF < 0) throw new IllegalArgumentException("Fitness of female must be equal or larger than zero");
		ArbitraryLandscapeEntry aeF = new ArbitraryLandscapeEntry(phenotypicValue, fitnessF);


		double fitnessH = Double.parseDouble(toparse[4]);
		if (fitnessH < 0) throw new IllegalArgumentException("Fitness of hermaphrodite must be equal or larger than zero");
		ArbitraryLandscapeEntry aeH = new ArbitraryLandscapeEntry(phenotypicValue, fitnessH);


		ArbitraryLandscapeEntry[] toret=new ArbitraryLandscapeEntry[3];
		toret[0]=aeM; toret[1]=aeF; toret[2]=aeH;
		return toret;


	}

}
