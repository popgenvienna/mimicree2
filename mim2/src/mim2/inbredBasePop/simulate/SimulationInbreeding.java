package mim2.inbredBasePop.simulate;

import mimcore.data.MatePair;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.DiploidGenome;


import java.util.ArrayList;

import java.util.logging.Logger;

public class SimulationInbreeding {
	private final MatePair matepair;
	private final int isofemaleLines;
	private final int sizeBasePop;
	private final int sizeisofemaleLine;
	private final int geninbreeding;
	private final RecombinationGenerator recGenerator;
	private Logger logger;

	//matepair, isofemaleLines, sizeBasePop, sizeisofemaleLine, geninbreeding, recGenerator, this.logger
	public SimulationInbreeding(MatePair mp, int isofemaleLines, int sizeBasePop, int sizeisofemaleLine, int geninbreeding, RecombinationGenerator recgen, Logger logger)
	{
		this.matepair=mp;
		this.isofemaleLines=isofemaleLines;
		this.sizeBasePop=sizeBasePop;
		this.sizeisofemaleLine=sizeisofemaleLine;
		this.geninbreeding=geninbreeding;
		this.recGenerator=recgen;
		this.logger=logger;
		
	}

	private ArrayList<Integer> getTargetSizes(int popsize, int lines)
	{

		ArrayList<Integer> toret=new ArrayList<Integer>();

		// deal with perfect match
		if(popsize%lines==0)
		{
			  int div=popsize/lines;
			for(int i=0; i<lines;i++) toret.add(div);
			return toret;
		}


		// deal with the imperfect match
		double rat=((double)popsize)/((double)lines);
		int ceil=(int)Math.ceil(rat);
		int floor=(int)Math.floor(rat);
		int movingcount=popsize;
		while(movingcount%floor!=0)
		{
			toret.add(ceil);
			movingcount-=ceil;
		}
		while(movingcount>0)
		{
			toret.add(floor);
			movingcount-=floor;
		}
		if(movingcount!=0)throw new IllegalStateException("Something went wrong for computing the target size of the isofemal lines");
		if(toret.size()!=lines) throw new IllegalStateException("Something went wrong");
		return toret;
	}


	/*
	public ArrayList<DiploidGenome> run()
	{
		ArrayList<DiploidGenome> dipGenomes=  new ArrayList<DiploidGenome>();
		ArrayList<Integer> targets=getTargetSizes(this.sizeBasePop,this.isofemaleLines);

		for(int k=0; k<this.isofemaleLines; k++)
		{
			this.logger.info("Starting simulation of isofemale line number " + (k+1));
			ArrayList<DiploidGenome> tempGen = new SimulationInbreedingSingleIsofemaleLine(this.matepair,this.sizeisofemaleLine,this.geninbreeding,targets.get(k),this.recGenerator,this.logger,k+1).run();
			dipGenomes.addAll(tempGen);
		}
		if(dipGenomes.size()!=this.sizeBasePop) throw new IllegalStateException("Something went wrong..");
		return dipGenomes;
	}
	*/
}
