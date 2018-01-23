package mim2.shared;

import mimcore.data.Population;
import mimcore.data.sex.SexInfo;
import mimcore.data.statistic.GPFCollection;
import mimcore.data.statistic.GPFReducer;
import mimcore.data.statistic.PACReducer;
import mimcore.data.statistic.PopulationAlleleCount;
import mimcore.io.HaplotypeMultiWriter;
import mimcore.io.misc.GPFWriter;
import mimcore.io.misc.ISummaryWriter;
import mimcore.io.misc.SyncWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 23/01/2018.
 */
public class ResultRecorder {

    private final String outputGPF;
    private final String outputSync;
    private final String outputDir;
    private ArrayList<PopulationAlleleCount> pacs;
    private ArrayList<GPFCollection> gpfs;
    private final HashSet<Integer> outputGenerations;
    private final Logger logger;
    private final SexInfo sexInfo;
    public ResultRecorder(String outputGPF, String outputSync, String outputDir, SexInfo sexInfo, SimulationMode simulationMode, Logger logger)
    {
        this.outputDir=outputDir;
        this.outputGPF=outputGPF;
        this.outputSync=outputSync;
        this.pacs=new ArrayList<PopulationAlleleCount>();
        this.gpfs=new ArrayList<GPFCollection>();
        HashSet<Integer> toOutput=new HashSet<Integer>();
        toOutput.add(0); // always record base population!
        for(Integer i : GlobalResourceManager.getSimulationMode().getTimestamps())
        {
            toOutput.add(i);
        }
        outputGenerations=toOutput;
        this.logger=logger;
        this.sexInfo=sexInfo;
    }


    public void record(int generation, int replicate, Population population)
    {
        if(!this.outputGenerations.contains(generation))return;
        recordPAC(population,generation,replicate);
        recordGPF(population,generation,replicate);
        recordHap(population,generation,replicate);

    }


    private void recordPAC(Population toRecord,int generation, int replicate)
    {
        // No output file no action
        if(this.outputSync==null) return;
        this.logger.info("Recording allele frequences at generation "+generation+" of replicate "+replicate);
        pacs.add(new PACReducer(toRecord).reduce());
    }

    private void recordGPF(Population toRecord, int generation, int replicate)
    {
        // No output file no action
        if(this.outputGPF==null) return;
        this.logger.info("Recording genotype/phenotype/fitness at generation "+generation+" of replicate "+replicate);
        gpfs.add(new GPFReducer(toRecord,replicate,generation).reduce());
    }


    private void recordHap(Population toRecord, int generation, int replicate)
    {
        // No output file no action
        if(this.outputDir==null) return;
        this.logger.info("Recording haplotypes at generation "+generation+" of replicate "+replicate);
        new HaplotypeMultiWriter(toRecord, this.outputDir,generation, replicate, this.logger).write();
    }

    public void finishWriting()
    {
        // Finally write as yet unwritten results
        if(this.outputSync!=null) {
            ISummaryWriter sw = new SyncWriter(this.outputSync, this.logger);
            sw.write(this.pacs);
        }
        if(this.outputGPF!=null){
            GPFWriter gw=new GPFWriter(this.outputGPF,this.logger);
            gw.write(gpfs);
        }
    }

}
