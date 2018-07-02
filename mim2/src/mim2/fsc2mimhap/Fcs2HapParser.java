package mim2.fsc2mimhap;

import mim2.CommandFormater;
import mimcore.misc.MimicreeLogFactory;
import mimcore.misc.MimicreeThreadPool;

import java.util.LinkedList;
import java.util.logging.Logger;


public class Fcs2HapParser {

    public static void parseCommandline(LinkedList<String> args)
    {
        String inputFile="";
        String outputFile="";
        String chrname="";
        boolean haploid=false;

        // print help if not enough arguments
        if(args.size()<1) printHelpMessage();
        for(String s: args){if(s.equals("--help")) printHelpMessage();}

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
            else if(cu.equals("--chrname"))
            {
                chrname=args.remove(0);
            }
            else if(cu.equals("--haploid"))
            {
                haploid=true;
            }
            else
            {
                throw new IllegalArgumentException("Do not recognize command line option "+cu);
            }
        }

        Logger logger= MimicreeLogFactory.getLogger(false);
        MimicreeThreadPool.setThreads(1);

        Fcs2HapFramework fc2h=new Fcs2HapFramework(inputFile,outputFile,chrname,haploid,logger);
        fc2h.run();
    }

    public static void printHelpMessage()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("arp2mimhap - convert Arlequin input data into MimicrEE2 haplotypes\n");
        sb.append("example usage:\njava -jar mim2.jar arp2mimhap --input input.arp --output output.mimhap.gz --chrname 2L \n\n");
        sb.append("Mandatory parameters:\n");
        sb.append(CommandFormater.format("--input","an arp file; haplotypes in Arlequin format",null));
        sb.append(CommandFormater.format("--output","the output file: MimimcrEE2 haplotypes",null));
        sb.append(CommandFormater.format("--chrname","chromosome name to be used in the MimicrEE2 haplotype file",null));
        sb.append("\nOptional parameters:\n");
        sb.append(CommandFormater.format("--haploid","generate haploid genomes instead of diploid ones",null));
        sb.append(CommandFormater.format("--help","print the help",null));
        System.out.print(sb.toString());
        System.exit(1);
    }
}
