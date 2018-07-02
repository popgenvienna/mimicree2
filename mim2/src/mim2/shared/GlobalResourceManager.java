package mim2.shared;

import mimcore.data.Chromosome;
import mimcore.data.DiploidGenome;
import mimcore.data.Mutator.IMutator;
import mimcore.data.Mutator.MutatorGenomeWideRate;
import mimcore.data.PopulationSizeContainer;
import mimcore.data.SexedDiploids;
import mimcore.data.migration.IMigrationRegime;
import mimcore.data.migration.MigrationRegime;
import mimcore.data.migration.MigrationRegimeNoMigration;
import mimcore.data.recombination.CrossoverGenerator;
import mimcore.data.recombination.RandomAssortmentGenerator;
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
    private static SnapshotManager snapshotManager;
    private static ResultRecorder rr;
    private static boolean haploidss;
    private static boolean clonals;



    //  java.util.logging.Logger logger

    public static void setGlobalResources(Logger mylogger, String haplotypeFile,  String recombinationFile,String populationSizeFile,
                                          String chromosomeDefinition, String sexInfoFile,String migrationRegimeFile, double mutationRate, String outputSync,
                                          String outputGPF, String outputDir, SnapshotManager snapman, int replicateRuns,boolean haploids, boolean clonal)
    {
        logger=mylogger;
        haploidss=haploids;
        clonals=clonal;
        // Set the output
        setOutput(outputSync,outputGPF,outputDir);

        // Set sex, recombination, and load base population
        if(clonal) setClonalSexRecGenomes(haplotypeFile,recombinationFile,chromosomeDefinition,sexInfoFile,haploids);
        else setSexRecGenomes(haplotypeFile,recombinationFile,chromosomeDefinition,sexInfoFile,haploids);

        // set population size, migration, mutation,
        setPopMigMutModeReps(populationSizeFile,migrationRegimeFile,mutationRate,snapman,replicateRuns,haploids,clonal);
        rr=null;
    }

    private static void setPopMigMutModeReps(String populationSizeFile, String migrationRegimeFile, double mutationRate, SnapshotManager snapman, int replicateRuns,boolean haploids,boolean clonal)
    {
        // migration regime
        if(migrationRegimeFile == null)logger.info("No migration regime file found; Proceeding without migration");
        else if (! new File(migrationRegimeFile).exists()) throw new IllegalArgumentException("Migration regime file does not exist; "+ migrationRegimeFile);
        if(mutationRate<0.0 || mutationRate>1.0)throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0");
        if(!(replicateRuns>0)) throw new IllegalArgumentException("At least one replicate run should be provided; Provided by the user "+replicateRuns);
        if((populationSizeFile != null) && (!new File(populationSizeFile).exists())) throw new IllegalArgumentException("Population size file does not exist; "+populationSizeFile);

        // Migration regime; If none specified no migration
        migrationRegime=new MigrationRegimeNoMigration();
        if(migrationRegimeFile != null) migrationRegime=new MigrationRegimeReader(migrationRegimeFile,logger,basePopulation,sexInfo.getSexAssigner(),haploids,clonal).readMigrationRegime();

        populationSizeContainer=new PopulationSizeContainer(basePopulation.size());
        if(populationSizeFile!=null) populationSizeContainer=new PopulationSizeReader(populationSizeFile,logger).readPopulationSizes();
        mutator=new MutatorGenomeWideRate(mutationRate);

        snapshotManager=snapman;
        replicateRunss=replicateRuns;


    }

    private static void setOutput(String outputSync, String outputGPF, String outputDir)
    {

        if((outputDir == null) && (outputSync==null)&& (outputGPF==null)) throw new IllegalArgumentException("No output was provided; Provide either an output directory or an output sync file or an output GPF file");

        if(outputGPF == null) logger.info("No output genotype/phenotype/fitness file provided; will not record GPF");
        else try {new File(outputGPF).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create GPF output file "+outputGPF);}

        if(outputDir== null) logger.info("No output director found; Will not record haplotypes");
        else if(! new File(outputDir).exists()) throw new IllegalArgumentException("The provided output directory does not exist "+outputDir);

        if(outputSync == null) logger.info("No output sync file was provided; Will not record allele frequencies");
        else try {new File(outputSync).createNewFile();} catch(IOException e) {throw new IllegalArgumentException("Can not create output sync file "+outputSync);}

        outputSyncs=outputSync;
        outputDirs=outputDir;
        outputGPFs=outputGPF;
    }


    /**
     * Simulations of clonal evolution requires an entirely different setup
     * no recombination rate must be provided
     * no sex chromosomes must be provided
     */
    private static void setClonalSexRecGenomes(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String sexInfoFile, boolean haploids)
    {
        if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
        if(recombinationFile!=null) throw new IllegalArgumentException("It is not allowed to provide a recombination file for simulation of clonal evolution" + recombinationFile);
        if(sexInfoFile != null) throw new IllegalArgumentException("It is not allowed to provide a sex info file for simulations of clonal evolution "+sexInfoFile);

        sexInfo=SexInfo.getClonalEvolutionSexInfo();

        DiploidGenomeReader dgr =new DiploidGenomeReader(haplotypeFile,sexInfo.getSexAssigner(),haploids,logger);
        SexedDiploids sd=dgr.readGenomes();
        if(sd.countMales()>0) throw new IllegalArgumentException("It is not allowed to specify male-haplotypes for clonal evolution, solely hermaphrodites or no sex is allowed");
        if(sd.countFemales()>0) throw new IllegalArgumentException("It is not allowed to specify female-haplotypes for clonal evolution, solely hermaphrodites or no sex is allowed");
        sexInfo.setHemizygousSite(sd.getDiploids().get(0).getHaplotypeA().getSNPCollection());

        ArrayList<Chromosome> chromosomes=sd.getDiploids().get(0).getHaplotypeA().getSNPCollection().getChromosomes();
        recombinationGenerator = new RecombinationGenerator(CrossoverGenerator.getDefault(), new RandomAssortmentGenerator(chromosomes,true));

        basePopulation=sd.updateSexChromosome(sexInfo);

    }





    private static void setSexRecGenomes(String haplotypeFile, String recombinationFile, String chromosomeDefinition, String sexInfoFile,boolean haploids)
    {
        if(! new File(haplotypeFile).exists()) throw new IllegalArgumentException("Haplotype file does not exist "+haplotypeFile);
        if(recombinationFile!=null && (!new File(recombinationFile).exists())) throw new IllegalArgumentException("The provided recombination file does not exist " + recombinationFile);
        if((sexInfoFile != null) && (!new File(sexInfoFile).exists())) throw new IllegalArgumentException("Sex defintion file does not exist; "+sexInfoFile);


        sexInfo=SexInfo.getDefaultSexInfo();
        if(sexInfoFile!=null) sexInfo=new SexReader(sexInfoFile,logger).readSexInfo();
        if(haploids && (!sexInfo.isValidHaploid())) throw new IllegalArgumentException("Sex definition file invalid for haploid simulations, eg. hemizygous sex chromsomes make no sense for haploids");

        DiploidGenomeReader dgr =new DiploidGenomeReader(haplotypeFile,sexInfo.getSexAssigner(),haploids,logger);
        SexedDiploids sd=dgr.readGenomes();

        // Load the recombination rate
        if(recombinationFile!=null) recombinationGenerator = new RecombinationGenerator(new RecombinationRateReader(recombinationFile,logger).getRecombinationRate(),
                new ChromosomeDefinitionReader(chromosomeDefinition).getRandomAssortmentGenerator());
        else {
            logger.info("No file with recombination rate provided; will use default recombination rate of 0.0");
            ArrayList<Chromosome> chromosomes=sd.getDiploids().get(0).getHaplotypeA().getSNPCollection().getChromosomes();
            recombinationGenerator = new RecombinationGenerator(CrossoverGenerator.getDefault(), new RandomAssortmentGenerator(chromosomes,true));

        }

        // set the sites of hemizygous chromosomes
        sexInfo.setHemizygousSite(sd.getDiploids().get(0).getHaplotypeA().getSNPCollection());
        logger.info("Updating sex chromosomes of base population (i.e. hemizygous chromosomes)");
       basePopulation=sd.updateSexChromosome(sexInfo);
       if(!recombinationGenerator.isValid(basePopulation.getDiploids())) throw new IllegalArgumentException("Recombination rate file is not valid; an entry needs to be provided for each chromosome of the base population");
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
    public static SnapshotManager getSnapshotManager(){return snapshotManager;}

    public static ResultRecorder getResultRecorder()
    {
        if(rr==null)rr =new ResultRecorder(outputGPFs,outputSyncs,outputDirs,sexInfo,snapshotManager,haploidss,logger);
        return rr;
    }

    public static boolean getHaploid(){return haploidss;}
    public static boolean getClonal(){return clonals;}
}
