package qmimcore.data.fitness;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Created by robertkofler on 6/4/14.
 */
public class SelectionRegimeReplicateSpecific implements ISelectionRegime {
	private final ArrayList<HashMap<Integer,Double>> sr;
	private final ArrayList<Double> def;


	public SelectionRegimeReplicateSpecific(ArrayList<HashMap<Integer, Double>> input)
		{

			ArrayList<HashMap<Integer,Double>> store =new ArrayList<HashMap<Integer, Double>>();
			ArrayList<Double> defs=new ArrayList<Double>();
			for(HashMap<Integer,Double> hm:input)
			{
				LinkedList<Integer> keys = new LinkedList<Integer>(hm.keySet());
				Collections.sort(keys);
				int lastGenerations=keys.peekLast();
				Double defSelection=null;

				HashMap<Integer,Double> tmp=new HashMap<Integer, Double>();
				for(int i=1;i<=lastGenerations; i++)
				{
					if(hm.containsKey(i))defSelection=hm.get(i);
					if(defSelection==null) throw new IllegalArgumentException("Did not provide selection coefficient for the first generations");
					tmp.put(i,defSelection);

				}
				defs.add(defSelection);
				store.add(tmp);
			}
			def=defs;
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
		if(sr.get(replicate-1).containsKey(generation))
		{
			return sr.get(replicate-1).get(generation);
		}
		else
		{
			return def.get(replicate-1);
		}
	}
}

