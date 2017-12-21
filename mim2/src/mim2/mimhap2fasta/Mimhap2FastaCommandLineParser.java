package mim2.mimhap2fasta;


import mim2.CommandFormater;
import mim2.shared.CommandLineParseHelp;
import mim2.shared.SimulationMode;
import mim2.w.WSimulationFramework;
import mimcore.misc.MimicreeLogFactory;
import mimcore.misc.MimicreeThreadPool;

import java.util.LinkedList;
import java.util.logging.Logger;


public class Mimhap2FastaCommandLineParser {
	
	/**
	 * Parse the command line arguments and return the results
	 * @param args the command line arguments
	 * @return
	 */
	public static void runConversion(LinkedList<String> args)
	{

		String referenceFile="";
		String mimhapFile="";
		String outputFasta="";
		boolean stringent=false;
		boolean detailedLog=false;




		// print help if not enough arguments
		if(args.size()<1) printHelpMessage();
		for(String s: args){if(s.equals("--help")) printHelpMessage();}

        while(args.size() > 0)
        {
            String cu=args.remove(0);

			if(cu.equals("--mimhap"))
			{
				mimhapFile=args.remove(0);
			}

            else if(cu.equals("--reference"))
            {
                referenceFile = args.remove(0);
            }
            else if(cu.equals("--output-fasta"))
            {
            	outputFasta=args.remove(0);
            }
            else if(cu.equals("--stringent"))
            {
				stringent=true;
            }

            else
            {
                throw new IllegalArgumentException("Do not recognize command line option "+cu);
            }
        }

		// Create a logger to System.err
		Logger logger= MimicreeLogFactory.getLogger(detailedLog);



		MimicreeThreadPool.setThreads(1);
        Mimhap2FastaFramework mimframe= new Mimhap2FastaFramework(referenceFile,mimhapFile,outputFasta,stringent,logger);
        
        mimframe.run();
	}
	
	
	public static void printHelpMessage()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("mimhap2fasta: convert MimicrEE2 haplotypes into fasta sequences\n");
		sb.append(CommandFormater.format("--mimhap","the haplotype file",null));
		sb.append(CommandFormater.format("--reference","the reference genome",null));
		sb.append(CommandFormater.format("--output-fasta","the output file; creates a sequence for each chromosome of each haploid genome",null));
		sb.append(CommandFormater.format("--stringent","report an error if ref.chars do not match",null));
		sb.append(CommandFormater.format("--help","print the help",null));
		System.out.print(sb.toString());
		System.exit(1);
	}



}
