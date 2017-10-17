package mim2.depr_qt_hap;


import mim2.CommandFormater;
import mim2.qt.SimulationMode;
import mimcore.misc.MimicreeLogFactory;
import mimcore.misc.MimicreeThreadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


public class SimulationCommandLineParser {
	
	/**
	 * Parse the command line arguments and return the results
	 * @param args the command line arguments
	 * @return
	 */
	public static void runQTSimulations(LinkedList<String> args)
	{

		String haplotypeFile="";
		String recombinationFile="";
		String effectSizeFile="";            //
		String outputDir="";
		String outputGenRaw="";
		String selectionRegimFile="";         //
		String chromosomeDefinition="";
		Integer seed=null;
		int replicateRuns=1;                   //
		double ve=0.0;
		boolean detailedLog=false;
		int threadCount=1;


		// print help if not enough arguments
		if(args.size()<1) printHelpMessage();
        while(args.size() > 0)
        {
            String cu=args.remove(0);

			if(cu.equals("--detailed-log"))
			{
				detailedLog=true;
			}
			else if(cu.equals("--threads"))
			{

				threadCount=Integer.parseInt(args.removeFirst());
			}
            else if(cu.equals("--haplotypes-g0"))
            {
                haplotypeFile = args.remove(0);
            }
            else if(cu.equals("--recombination-rate"))
            {
            	recombinationFile=args.remove(0);
            }
            else if(cu.equals("--effect-size"))
            {
            	effectSizeFile=args.remove(0);
            }
            else if(cu.equals("--selection-regime"))
            {
            	selectionRegimFile=args.remove(0);
            }
			else if(cu.equals("--ve"))
			{
				ve=Double.parseDouble(args.remove(0));
			}
            else if(cu.equals("--chromosome-definition"))
            {
            	chromosomeDefinition=args.remove(0);
            }
            else if(cu.equals("--output-mode"))
            {
            	outputGenRaw=args.remove(0);
            }
            else if(cu.equals("--replicate-runs"))
            {
            	replicateRuns=Integer.parseInt(args.remove(0));
            }
            else if(cu.equals("--output-dir"))
            {
            	outputDir=args.remove(0);
            }
            else if(cu.equals("--help"))
            {
            	printHelpMessage();
            }
            else
            {
                throw new IllegalArgumentException("Do not recognize command line option "+cu);
            }
        }

		// Create a logger to System.err
		Logger logger= MimicreeLogFactory.getLogger(detailedLog);

    
        // Parse the string with the generations
        SimulationMode simMode = parseOutputGenerations(outputGenRaw);

		MimicreeThreadPool.setThreads(threadCount);
        mim2.depr_qt_hap.QtSimulationFrameworkHaplotype mimframe= new QtSimulationFrameworkHaplotype(haplotypeFile,recombinationFile,chromosomeDefinition,
				effectSizeFile,ve,selectionRegimFile,outputDir,simMode,replicateRuns,logger);
        
        mimframe.run();
	}
	
	
	public static void printHelpMessage()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("qt-hap: Simulate truncating selection for a quantitative trait; output detailed haplotypes\n");
		sb.append(CommandFormater.format("--haplotypes-g0","the haplotype file",null));
		sb.append(CommandFormater.format("--recombination-rate","the recombination rate for windows of fixed size",null));
		sb.append(CommandFormater.format("--effect-size","the causative SNPs and their effect sizes",null));
		sb.append(CommandFormater.format("--ve","environmental variance","0.0"));
		sb.append(CommandFormater.format("--chromosome-definition","which chromosomes parts constitute a chromosome",null));
		sb.append(CommandFormater.format("--output-mode","a coma separated list of generations to output",null));
		sb.append(CommandFormater.format("--replicate-runs","how often should the simulation be repeated",null));
		sb.append(CommandFormater.format("--output-dir","the output directory",null));
		sb.append(CommandFormater.format("--selection-regime","the selection regime",null));
		sb.append(CommandFormater.format("--detailed-log","print detailed log messages",null));
		sb.append(CommandFormater.format("--threads","the number of threads to use",null));
		sb.append(CommandFormater.format("--help","print the help",null));
		System.out.print(sb.toString());
		System.exit(1);
	}



	
	public static SimulationMode parseOutputGenerations(String outputGenerationsRaw)
	{
		// Parse a String consistent of a comma-separated list of numbers, to a array of integers
		SimulationMode simMode;
			String [] tmp;
			if(outputGenerationsRaw.contains(","))
			{
				tmp=outputGenerationsRaw.split(",");
			}
			else
			{
				tmp=new String[1];
				tmp[0]=outputGenerationsRaw;
			}
			// Convert everything to int
			ArrayList<Integer> ti=new ArrayList<Integer>();
			for(String s :tmp)
			{
				ti.add(Integer.parseInt(s));
			}
			simMode=SimulationMode.Timestamp;
			simMode.setTimestamps(ti);
		return simMode;
	}
}
