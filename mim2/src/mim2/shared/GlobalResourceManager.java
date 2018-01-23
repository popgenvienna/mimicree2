package mim2.shared;

import mimcore.data.DiploidGenome;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Mutator.MutatorGenomeWideRate;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.SexedDiploids;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.RecombinationGenerator;
import mimcore.data.sex.ISexAssigner;
import mimcore.data.sex.SexAssignerDirect;
import mimcore.data.sex.SexInfo;
import mimcore.io.ChromosomeDefinitionReader;
import mimcore.io.DiploidGenomeReader;
import mimcore.io.PopulationSizeReader;
import mimcore.io.SexReader;
import mimcore.io.migrationRegime.MigrationRegimeReader;
import mimcore.io.recombination.RecombinationRateReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 22/01/2018.
 */
public class GlobalResourceManager {
    private static SexInfo sexInfo;
    private static SexedDiploids basePopulation;
    private static RecombinationGenerator recombinationGenerator;
    private static IMigrationRegime migrationRegime;
    private static PopulationSizeContainer populationSizeContainer;
    private static IMutator mutator;
    private static String outputSyncs;
    private static String outputGPFs;
    private static String outputDirs;
    private static Logger logger;
    private static int replicateRunss;
    private static SimulationMode simulationModes;
    private static ResultRecorder rr;



    //  java.util.logging.Logger logger

    public static void setGlobalResources(Logger mylogger, String haplotypeFile,  String recombinationFile,String populationSizeFile,
                                          String chromosomeDefinition, String sexInfoFile,String migrationRegimeFile, double mutationRate, String outputSync, String outputGPF, String outputDir, SimulationMode simMode, int replicateRuns)
    {
        logger=mylogger;
        setOutput(outputSync,outputGPF,outputDir);
        setSexRecGenomes(haplotypeFile,recombinationFile,chromosomeDefinition,sexInfoFile);
        setPopMigMutModeReps(populationSizeFile,migrationRegimeFile,mutationRate,simMode,replicateRuns);
        rr=null;
    }

    private static void setPopMigMutModeReps(String populationSizeFile, String migrationRegimeFile, double mutationRate, SimulationMode simMode, int replicateRuns)
    {
        // migration regime
        if(migrationRegimeFile == null)logger.info("No migration regime file found; Proceeding without migration");
        else if (! new File(migrationRegimeFile).exists()) throw new IllegalArgumentException("Migration regime file does not exist; "+ migrationRegimeFile);
        if(mutationRate<0.0 || mutationRate>1.0)throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0");
        if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);
        if((populationSizeFile != null) && (!new File(populationSizeFile).exists())) throw new IllegalArgumentException("Population size file does not exist; "+populationSizeFile);

        // Migration regime; If none specified no migration
        migrationRegime=new MigrationRegimeNoMigration();
        if(migrationRegimeFile != null) migrationRegime=new MigrationRegimeReader(migrationRegimeFile,logger,basePopulation,sexInfo.getSexAssigner()).readMigrationRegime();

        populationSizeContainer=new PopulationSizeContainer(basePopulation.size());
        if(populationSizeFile!=null) populationSizeContainer=new PopulationSizeReader(populationSizeFile,logger).readPopulationSizes();
        mutator=new MutatorGenomeWideRate(mutationRate);

        simulationModes=simMode;
        replicateRunss=replicateRuns;


    }

    private static void setOutput(String outputSync, String outputGPF, String outputDir)
    {
        if(outputGPF == null) logger.info("No output genotype/phenotype/fitness file provided; will not record GPF");
        else try {new File(outputGPF).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create GPF output file "+outputGPF);}

        if((outputDir == null) && (outputSync==null)) throw new IllegalArgumentException("No output was provided; Provide either an output directory or an output sync file or both");

        if(outputDir== null) logger.info("No output director found; Will not record haplotypes");
        else if(! new File(outputDir).exists()) throw new IllegalArgumentException("The provided output directory does not exist "+outputDir);

        if(outputSync == null) logger.info("No output sync file was provided; Will not record allele frequencies");
        else try {new File(outputSync).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create output sync file "+outputSync);}

        outputSyncs=outputSync;
        outputDirs=outputDir;
        outputGPFs=outputGPF;
    }

    private static void setSexRecGenomes(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String sexInfoFile)
    {
        if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
        if(! new File(recombinationFile).exists()) throw new IllegalArgumentException("Recombination file does not exist " + recombinationFile);
        if((sexInfoFile != null) && (!new File(sexInfoFile).exists())) throw new IllegalArgumentException("Sex defintion file does not exist; "+sexInfoFile);


        sexInfo=SexInfo.getDefaultSexInfo();
        if(sexInfoFile!=null) sexInfo=new SexReader(sexInfoFile,logger).readSexInfo();

        // Load the data
       recombinationGenerator = new RecombinationGenerator(new RecombinationRateReader(recombinationFile,logger).getRecombinationRate(),
                new ChromosomeDefinitionReader(chromosomeDefinition).getRandomAssortmentGenerator());

       DiploidGenomeReader dgr =new DiploidGenomeReader(haplotypeFile,sexInfo.getSexAssigner(),logger);
       basePopulation=dgr.readGenomes();
       if(!recombinationGenerator.isValid(basePopulation.getDiploids())) throw new IllegalArgumentException("Recombination rate file is not valid; an entry needs to be provided for each chromosome of the base population");

        // set the sites of hemizygous chromosomes
       sexInfo.setHemizygousSite(basePopulation.getDiploids().get(0).getHaplotypeA().getSNPCollection());
    }

    public static SexInfo getSexInfo(){return sexInfo;}
    public static SexedDiploids getBasePopulation(){return basePopulation;}
    public static RecombinationGenerator getRecombinationGenerator(){return recombinationGenerator;}
    public static IMigrationRegime getMigrationRegime(){return migrationRegime;}
    public static PopulationSizeContainer getPopulationSizeContainer(){return populationSizeContainer;}
    public static IMutator getMutator(){return mutator;}
    public static String getOutputSync(){return outputSyncs;}
    public static String getOutputGPF(){return outputGPFs;}
    public static String getOutputDir(){return outputDirs;}
    public static Logger getLogger(){return logger;}
    public static int getReplicateRuns(){return replicateRunss;}
    public static SimulationMode getSimulationMode(){return simulationModes;}

    public static ResultRecorder getResultRecorder()
    {
        if(rr==null)rr =new ResultRecorder(outputGPFs,outputSyncs,outputDirs,sexInfo,simulationModes,logger);
        return rr;
    }
}
