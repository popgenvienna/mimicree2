package mim2;

import mim2.mimhap2fasta.Mimhap2FastaCommandLineParser;
import mim2.mimhap2fasta.Mimhap2FastaFramework;
import mim2.qs.QsCommandLineParser;
import mim2.qt.QtCommandLineParser;
import mim2.test.TestMain;
import mim2.unittest.JunitMimcore;
import mim2.w.WCommandLineParser;

import java.util.Arrays;
import java.util.LinkedList;

public class Main {


		public static void main(String[] args) {
			LinkedList<String> rawarguments=new LinkedList<String>(Arrays.asList(args));
			if(rawarguments.size()<1) {
				System.out.println("Not enough arguments");
				System.out.print(getgeneralHelp());
				System.exit(1);
			}



			String subtask=rawarguments.remove(0);
			if(subtask.toLowerCase().equals("qt"))
			{
				QtCommandLineParser.runQTSimulations(rawarguments);
			}
			else if(subtask.toLowerCase().equals("qff"))
			{
				QsCommandLineParser.runQSSimulations(rawarguments);
			}
			else if(subtask.toLowerCase().equals("w"))
			{
				WCommandLineParser.runWSimulations(rawarguments);
			}
			else if(subtask.toLowerCase().equals("stat-qt-pgf"))
			{
				// TODO not sure if I should support this
			}
			else if(subtask.toLowerCase().equals("__test__"))
			{
				TestMain.testMain();
			}
			else if(subtask.toLowerCase().equals("unit-tests"))
			{
				JunitMimcore.runTests();
			}
			else if(subtask.toLowerCase().equals("mimhap2fasta"))
			{
				Mimhap2FastaCommandLineParser.runConversion(rawarguments);
			}
			else
			{
				System.out.println("Unknown sub-task %s".format(subtask));
				System.out.print(getgeneralHelp());
				System.exit(1);
			}

			System.exit(0);

		}

		public static String getgeneralHelp()
		{
			StringBuilder sb=new StringBuilder();
			sb.append("Usage: java -Xmx4g -jar mim2.jar [subtask] [parameters of subtask]\n\n");
			sb.append("== Main tasks ==\n");
			sb.append(CommandFormater.format("w","simulate selection for loci having a given absolute fitness (w..fitness)",null));
			sb.append(CommandFormater.format("qt","simulate truncating selection for a quantitative trait (qt..quantitative truncating)",null));
			sb.append(CommandFormater.format("qff","simulate a quantitative trait mapping to fitness (qff..quantitative fitness function)",null));


			sb.append("\n== Secondary tasks ==\n");
			sb.append(CommandFormater.format("mimhap2fasta","convert the MimcrEE2 haplotypes into fasta",null));
			sb.append(CommandFormater.format("unit-tests","run the unit-tests; unit-tests validate proper behaviour of MimicrEE2 components",null));


			sb.append("\nMimicrEE2 Version "+getVersionNumber()+"\n");



			//sb.append(String.format("%-22s%s","filter","filter TE insertions\n"));
			return sb.toString();
		}


		public static String getVersionNumber()
		{
			return "v0.20.1";
		}


	}
