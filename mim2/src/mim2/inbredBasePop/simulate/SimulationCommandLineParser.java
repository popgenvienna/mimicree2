package mim2.inbredBasePop.simulate;


import mimcore.misc.MimicreeThreadPool;

import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SimulationCommandLineParser {
	
	/**
	 * Parse the command line arguments and return the results
	 * @param args the command line arguments
	 * @return
	 */
	public static void runMimicreeSimulations(LinkedList<String> args)
	{


		String haplotypeFile="";
		String recombinationFile="";
		String outputFile="";
		int isofemalelines=0;
		int sizeBasePop=0;
		int sizeisofemaleLine=0;
		int geninbreeding=0;
		Integer seed=null;
		boolean detailedLog=false;
		int threadCount=1;

	
		/*

		sb.append("--isofemale-lines				(int) the number of isofemale lines\n");
		sb.append("--N-base-population			(int) census size of base population\n");
		sb.append("--N-isofemale-line				(int) census size of isofemale lines (min: 2)\n");
		sb.append("--gen-inbreeding			(int) generations of inbreeding\n");
		 */
        while(args.size() > 0)
        {
            String cu=args.remove(0);

			if(cu.equals("--detailed-log"))
			{
				detailedLog=true;
			}

			else if(cu.equals("--version"))
			{
				printVersion();
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
            else if(cu.equals("--isofemale-lines"))
            {
            	isofemalelines=Integer.parseInt(args.remove(0));
            }
            else if(cu.equals("--N-base-population"))
            {
				sizeBasePop=Integer.parseInt(args.remove(0));

            }
			else if(cu.equals("--N-isofemale-line"))
			{
				sizeisofemaleLine=Integer.parseInt(args.remove(0));
			}
            else if(cu.equals("--gen-inbreeding"))
            {
            	geninbreeding=Integer.parseInt(args.remove(0));
            }
            else if(cu.equals("--output-file"))
            {
            	outputFile=args.remove(0);
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
		Logger logger= Logger.getLogger("Mimicree Logger");
		java.util.logging.ConsoleHandler mimhandler =new java.util.logging.ConsoleHandler();
		mimhandler.setLevel(Level.INFO);
		if(detailedLog)mimhandler.setLevel(Level.FINEST);
		mimhandler.setFormatter(new mimcore.misc.MimicreeLogFormatter());
		logger.addHandler(mimhandler);
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);


		MimicreeThreadPool.setThreads(threadCount);
        InbredBasePopFramework mimframe= new InbredBasePopFramework(haplotypeFile,recombinationFile,outputFile,
				isofemalelines,sizeBasePop,sizeisofemaleLine,geninbreeding,logger);
        
       // mimframe.run();
		logger.info("Thank you for using inbreBasePop MimicrEE");
		System.exit(0);
	}
	
	
	public static void printHelpMessage()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("--haplotypes-g0				(file) the haplotype file; must only contain 2 haplotypes\n");
		sb.append("--recombination-rate			(file) the recombination rate for windows of fixed size\n");
		sb.append("--isofemale-lines				(int) the number of isofemale lines\n");
		sb.append("--N-base-population			(int) census size of base population\n");
		sb.append("--N-isofemale-line				(int) census size of isofemale lines (min: 2)\n");
		sb.append("--gen-inbreeding			(int) generations of inbreeding\n");
		sb.append("--output-file				(file) the output file\n");
		sb.append("--detailed-log				print detailed log messages\n");
		sb.append("--threads				the number of threads to use\n");
		sb.append("--help					print the help\n");
		System.out.print(sb.toString());
		System.exit(1);
	}

	public static void printVersion()
	{
		String version="inbredbasepop version 1.01; build "+String.format("%tc",new Date(System.currentTimeMillis()));
		System.out.println(version);
		System.exit(1);
	}


	

}
