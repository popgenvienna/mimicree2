package mim2;

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
				mim2.qt.SimulationCommandLineParser.runQTSimulations(rawarguments);
			}
			else if(subtask.toLowerCase().equals("qs"))
			{
				// TODO
			}
			else if(subtask.toLowerCase().equals("sc"))
			{
				// TODO fitness
			}
			else if(subtask.toLowerCase().equals("stat-qt-pgf"))
			{
				// TODO not sure if I should support this
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
			sb.append(CommandFormater.format("sc","simulate selection for loci having given selection coefficient",null));
			sb.append(CommandFormater.format("qt","simulate truncating selection for a quantitative trait",null));
			sb.append(CommandFormater.format("qs","simulate stabilizing selection for a quantitative trait",null));


			sb.append("\n== Secondary tasks ==\n");
			sb.append(CommandFormater.format("todo","todo",null));
			sb.append("\nMimicrEE2 Version "+getVersionNumber()+"\n");



			//sb.append(String.format("%-22s%s","filter","filter TE insertions\n"));
			return sb.toString();
		}


		public static String getVersionNumber()
		{
			return "v0.54";
		}


	}
