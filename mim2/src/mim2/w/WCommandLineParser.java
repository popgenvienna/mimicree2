package mim2.w;


import mim2.CommandFormater;
import mim2.shared.CommandLineParseHelp;
import mim2.shared.GlobalResourceManager;
import mim2.shared.SnapshotManager;
import mimcore.misc.MimicreeLogFactory;
import mimcore.misc.MimicreeThreadPool;

import java.util.LinkedList;
import java.util.logging.Logger;


public class WCommandLineParser {
	
	/**
	 * Parse the command line arguments and return the results
	 * @param args the command line arguments
	 * @return
	 */
	public static void runWSimulations(LinkedList<String> args)
	{

		String haplotypeFile="";
		String recombinationFile=null;
		String outputSync=null;
		String outputGPF=null;
		String outputDir=null;
		String snapshots=null;
		String snapshotsdir=null;
		String snapshotssync=null;
		String snapshotsgpf=null;
		String fitnessFile=null;            //
		String epistasisFile=null;            //
		String migrationRegimeFile=null;
		String populationSizeFile=null;
		String sexInfoFile=null;
		String chromosomeDefinition="";
		Integer seed=null;
		int replicateRuns=1;                   //
		boolean detailedLog=false;
		int threadCount=1;
		double mutationRate=0.0;
		boolean haploids=false;
		boolean clonal=false;




		// print help if not enough arguments
		if(args.size()<1) printHelpMessage();
		for(String s: args){if(s.equals("--help")) printHelpMessage();}

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
            else if(cu.equals("--fitness"))
            {
				fitnessFile=args.remove(0);
            }
            else if(cu.equals("--epistasis"))
            {
				epistasisFile=args.remove(0);
            }
			else if(cu.equals("--migration-regime"))
			{
				migrationRegimeFile=args.remove(0);
			}
            else if(cu.equals("--chromosome-definition"))
            {
            	chromosomeDefinition=args.remove(0);
            }
			else if(cu.equals("--snapshots"))
			{
				snapshots=args.remove(0);
			}
			else if(cu.equals("--snapshots-dir"))
			{
				snapshotsdir=args.remove(0);
			}
			else if(cu.equals("--snapshots-sync"))
			{
				snapshotssync=args.remove(0);
			}
			else if(cu.equals("--snapshots-gpf"))
			{
				snapshotsgpf=args.remove(0);
			}
			else if(cu.equals("--population-size"))
			{
				populationSizeFile=args.remove(0);
			}
			else if(cu.equals("--mutation-rate"))
			{
				mutationRate=Double.parseDouble(args.remove(0));
			}
            else if(cu.equals("--replicate-runs"))
            {
            	replicateRuns=Integer.parseInt(args.remove(0));
            }
			else if(cu.equals("--sex"))
			{
				sexInfoFile=args.remove(0);
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
			else if(cu.equals("--haploid"))
			{
				haploids=true;
			}
			else if(cu.equals("--clonal"))
			{
				clonal=true;
			}
            else
            {
                throw new IllegalArgumentException("Do not recognize command line option "+cu);
            }
        }

		// Create a logger to System.err
		Logger logger= MimicreeLogFactory.getLogger(detailedLog);

    
        // Parse the string with the generations
        SnapshotManager snapman  = SnapshotManager.getSnapshotManager(snapshots,snapshotssync,snapshotsdir,snapshotsgpf);

		MimicreeThreadPool.setThreads(threadCount);
		logger.info("Starting simulations using fitness effects of SNPs (w)");
		GlobalResourceManager.setGlobalResources(logger,haplotypeFile,recombinationFile,populationSizeFile,chromosomeDefinition,sexInfoFile,migrationRegimeFile,mutationRate,outputSync,outputGPF,outputDir,snapman,replicateRuns,haploids,clonal);
		WSimulationFramework mimframe= new WSimulationFramework(fitnessFile,epistasisFile);
        
        mimframe.run();
	}
	
	
	public static void printHelpMessage()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("w - simulate selection for loci having a given fitness effect\n");
		sb.append("example usage:\n");
		sb.append("java -jar mim2.jar w --haplotypes-g0 basepop.mimhap.gz --fitness selectedloci.txt --snapshots 100 --ouput-dir simres\n\n");

		sb.append("Mandatory parameters:\n");
		sb.append(CommandFormater.format("--haplotypes-g0","the haplotype file",null));

		sb.append("\nSemi-mandatory parameters:\n");
		sb.append(CommandFormater.format("--snapshots","a comma separated list of generations to output; per default applies to all outputs; at least one of the four --snapshots* need to be provided",null));
		sb.append(CommandFormater.format("--snapshots-sync","use a distinct list of output generations for --output-sync",null));
		sb.append(CommandFormater.format("--snapshots-dir","use a distinct list of output generations for --output-dir",null));
		sb.append(CommandFormater.format("--snapshots-gpf","use a distinct list of output generations for --output-gpf",null));
		sb.append(CommandFormater.format("--output-sync","the output file (sync); at least one of the three --output* must be provided",null));
		sb.append(CommandFormater.format("--output-dir","the output directory for the haplotypes",null));
		sb.append(CommandFormater.format("--output-gpf","the output file for genotype/phenotype/fitness",null));

		sb.append("\nOptional parameters:\n");
		sb.append(CommandFormater.format("--fitness","fitness effects of selected SNPs",null));
		sb.append(CommandFormater.format("--epistasis","fitness effects of pairs of SNPs ",null));
		sb.append(CommandFormater.format("--population-size","a file with the population size during the simulations",null));
		sb.append(CommandFormater.format("--chromosome-definition"," define which chromosomes parts constitute a chromosome",null));
		sb.append(CommandFormater.format("--replicate-runs","how often should the simulation be repeated","1"));
		sb.append(CommandFormater.format("--recombination-rate","a file with the recombination rate for windows of fixed size",null));
		sb.append(CommandFormater.format("--sex","a file specifying the sex ratios",null));
		sb.append(CommandFormater.format("--migration-regime","the migration regime; migration from the base population to the evolved populations",null));
		sb.append(CommandFormater.format("--mutation-rate","the mutation rate per site","0.0"));
		sb.append(CommandFormater.format("--haploid","perform haploid simulations; precludes specifying hemizygous sex chromosomes",null));
		sb.append(CommandFormater.format("--clonal","simulate clonal evolution; precludes specifying --recombination rate and --sex; may be performed for diploids and haploids",null));
		sb.append(CommandFormater.format("--detailed-log","print detailed log messages",null));
		sb.append(CommandFormater.format("--threads","the number of threads to use",null));
		sb.append(CommandFormater.format("--help","print the help",null));
		System.out.print(sb.toString());
		System.exit(1);
	}



}
