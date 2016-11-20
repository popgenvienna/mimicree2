package qmimcore.data.fitness;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Collections;

/**
 * Created by robertkofler on 6/4/14.
 */
public class SelectionRegimeDefault implements ISelectionRegime {
	private final HashMap<Integer,Double> sr;
	private final double def;


	public SelectionRegimeDefault(HashMap<Integer, Double> input)
		{
			LinkedList<Integer> keys = new LinkedList<Integer>(input.keySet());
			Collections.sort(keys);
			int lastGenerations=keys.peekLast();
			Double defSelection=null;

			HashMap<Integer,Double> store=new HashMap<Integer, Double>();
			for(int i=1;i<=lastGenerations; i++)
			{
				if(input.containsKey(i))defSelection=input.get(i);
				if(defSelection==null) throw new IllegalArgumentException("Did not provide selection coefficient for the first generations");
				store.put(i,defSelection);

			}
			def=defSelection;
			sr=store;
		}


	/**
	 * Return the selection intensity (eg.: keep 50%) for the given generation
	 * @param generation
	 * @return
	 */
	public double getSelectionIntensity(int generation, int replicate)
	{
		if(generation<1)throw new IllegalArgumentException("No generations can be smaller 1");
		if(sr.containsKey(generation))
		{
			return sr.get(generation);
		}
		else
		{
			return def;
		}

	}
}

