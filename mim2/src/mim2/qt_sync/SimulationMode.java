package mim2.qt_sync;

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



}
