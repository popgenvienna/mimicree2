package mim2.shared;

import java.util.ArrayList;

public enum SimulationMode {


	Timestamp;

	private ArrayList<Integer> timestamps;

	public void setTimestamps(ArrayList<Integer> timestamps)
	{
		this.timestamps=new ArrayList<Integer>(timestamps);
	}

	public ArrayList<Integer> getTimestamps()
	{
		return new ArrayList<Integer>(this.timestamps);
	}

	public int getMaximumGenerations()
	{
		int max=0;
		for(int i:this.timestamps)
		{
			if(i>max) max=i;
		}
		return max;
	}



}
