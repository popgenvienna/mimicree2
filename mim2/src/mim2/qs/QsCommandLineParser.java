package mim2.qs;


import mim2.CommandFormater;
import mim2.shared.CommandLineParseHelp;
import mim2.shared.SimulationMode;
import mimcore.misc.MimicreeLogFactory;
import mimcore.misc.MimicreeThreadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


public class QsCommandLineParser {
	
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
		String outputSync=null;
		String outputGPF=null;
		String outputDir=null;
		String outputGenRaw="";
		String gaussianFitnessFunctionFile=null;         // either or
		String fitnessFunctionFile=null;
		String migrationRegimeFile=null;
		String chromosomeDefinition="";
		Integer seed=null;
		int replicateRuns=1;                   //
		Double ve=null;
		Double heritability=null;
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
            else if(cu.equals("--gauss-gpf-function"))
            {
				gaussianFitnessFunctionFile=args.remove(0);
            }
			else if(cu.equals("--gpf-function"))
			{
				fitnessFunctionFile=args.remove(0);
			}
			else if(cu.equals("--migration-regime"))
			{
				migrationRegimeFile=args.remove(0);
			}
			else if(cu.equals("--ve"))
			{
				ve=Double.parseDouble(args.remove(0));
			}
			else if(cu.equals("--heritability"))
			{
				heritability=Double.parseDouble(args.remove(0));
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
            else if(cu.equals("--output-sync"))
            {
            	outputSync=args.remove(0);
            }
			else if(cu.equals("--output-dir"))
			{
				outputDir=args.remove(0);
			}
			else if(cu.equals("--output-gpf"))
			{
				outputGPF=args.remove(0);
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
        SimulationMode simMode = CommandLineParseHelp.parseOutputGenerations(outputGenRaw);

		MimicreeThreadPool.setThreads(threadCount);
        QsSimulationFramework mimframe= new QsSimulationFramework(haplotypeFile,recombinationFile,chromosomeDefinition,
				effectSizeFile,ve,heritability,gaussianFitnessFunctionFile,fitnessFunctionFile,migrationRegimeFile,outputSync,outputGPF,outputDir,simMode,replicateRuns,logger);
        
        mimframe.run();
	}
	
	
	public static void printHelpMessage()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("qt: Simulate truncating selection for a quantitative trait\n");
		sb.append(CommandFormater.format("--haplotypes-g0","the haplotype file",null));
		sb.append(CommandFormater.format("--recombination-rate","the recombination rate for windows of fixed size",null));
		sb.append(CommandFormater.format("--effect-size","the causative SNPs and their effect sizes",null));
		sb.append(CommandFormater.format( "--ve", "environmental variance; either --ve or --heritability needs to be provided", null));
		sb.append(CommandFormater.format("--heritability", "heritability; either --ve or --heritability needs to be provided", null));
		sb.append(CommandFormater.format("--chromosome-definition","which chromosomes parts constitute a chromosome",null));
		sb.append(CommandFormater.format("--output-mode","a coma separated list of generations to output",null));
		sb.append(CommandFormater.format("--replicate-runs","how often should the simulation be repeated",null));
		sb.append(CommandFormater.format("--output-sync","the output file (sync); --output-dir or --output-sync or both may be provided",null));
		sb.append(CommandFormater.format("--output-dir","the output directory for the haplotypes; --output-dir or --output-sync or both may be provided",null));
		sb.append(CommandFormater.format("--output-gpf","the output file for genotype/phenotype/gpf; optional",null));
		sb.append(CommandFormater.format("--gauss-gpf-function","a gaussian gpf function for mapping the phenotype to gpf; --gpf-function or --gauss-gpf-function may be provided",null));
		sb.append(CommandFormater.format("--gpf-function","a gpf function for mapping the phenotype to gpf; either --gpf-function or --gauss-gpf-function may be provided",null));
		sb.append(CommandFormater.format("--migration-regime","the migration regime; migration from the base population to the evolved populations",null));
		sb.append(CommandFormater.format("--detailed-log","print detailed log messages",null));
		sb.append(CommandFormater.format("--threads","the number of threads to use",null));
		sb.append(CommandFormater.format("--help","print the help",null));
		System.out.print(sb.toString());
		System.exit(1);
	}



}