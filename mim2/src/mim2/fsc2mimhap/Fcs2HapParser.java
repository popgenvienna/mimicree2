package mim2.fsc2mimhap;

import java.util.LinkedList;
import java.util.logging.Logger;


public class Fcs2HapParser {

    public static void parseCommandline(Logger logger, LinkedList<String> args)
    {
        String inputFile="";
        String outputFile="";
        String chromosome="";
        int haplotypeCount=0;

        while(args.size()>0)
        {
            String cu=args.remove(0);

            if(cu.equals("--input"))
            {
                inputFile=args.remove(0);
            }
            else if(cu.equals("--output"))
            {
                outputFile=args.remove(0);
            }
            else if(cu.equals("--chromosome"))
            {
                chromosome=args.remove(0);
            }
            else if(cu.equals("--help"))
            {
                printHelp();
            }
            else
            {
                throw new IllegalArgumentException("Do not recognize command line option "+cu);
            }
        }


        Fcs2HapFramework fc2h=new Fcs2HapFramework(inputFile,outputFile,chromosome,logger);
        fc2h.run();
    }

    public static void printHelp()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("mode fcs2hap: convert fastcoalsim outpout to MimicrEEToolShed haplotypes\n");
        sb.append("--input					    the fastsimcoal file\n");
        sb.append("--chromosome                 the chromosome\n");
        sb.append("--output					the output file (sync)\n");
		sb.append("--mode					the analysis mode of operation; see manual for supported modes\n");
		sb.append("--version					the analysis mode of operation; see manual for supported modes\n");
		sb.append("--detailed-log				print detailed log messages\n");
		sb.append("--help					print the help\n");
		System.out.print(sb.toString());
        System.exit(1);
    }
}
