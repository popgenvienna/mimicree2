package mim2.inbredBasePop;

import java.util.Arrays;
import java.util.LinkedList;


/**
 * @author robertkofler
 *
 */
public class inbredBasePop {

	/**
	 * @param args
	 * 	 */
	public static void main(String[] args) 
	{

		// Parse command lines to determine the analysis mode and the LOG
		LinkedList<String> rawarguments=new LinkedList<String>(Arrays.asList(args));
		mim2.inbredBasePop.simulate.SimulationCommandLineParser.runMimicreeSimulations(rawarguments);
	}



	
	

}
