package mim2.shared;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by robertkofler on 11/02/2018.
 */
public class SnapshotManager {

    public static SnapshotManager getSnapshotManager(String snapshots, String strsnapshotssync, String strsnapshotsdir, String strsnapshotsgpf)
    {
        // set defaults
        HashSet<Integer> snapshotsSync=parseOutputGenerations(snapshots);
        HashSet<Integer> snapshotsDir=parseOutputGenerations(snapshots);
        HashSet<Integer> snapshotsGPF=parseOutputGenerations(snapshots);

        // per default also record the base population for all three outputs
        snapshotsDir.add(0); snapshotsSync.add(0); snapshotsGPF.add(0);

        // override defaults (even base population recording will be overriden)
        if(strsnapshotssync!=null) snapshotsSync=parseOutputGenerations(strsnapshotssync);
        if(strsnapshotsdir!=null) snapshotsDir=parseOutputGenerations(strsnapshotsdir);
        if(strsnapshotsgpf!=null) snapshotsGPF=parseOutputGenerations(strsnapshotsgpf);

        return new SnapshotManager(snapshotsSync,snapshotsDir,snapshotsGPF);


    }

    private static  HashSet<Integer> parseOutputGenerations(String outputGenerationsRaw)
    {
        if(outputGenerationsRaw==null || outputGenerationsRaw.equals("")) return new HashSet<Integer>();
        // Parse a String consistent of a comma-separated list of numbers, to a array of integers

        String [] tmp;
        if(outputGenerationsRaw.contains(","))
        {
            tmp=outputGenerationsRaw.split(",");
        }
        else
        {
            tmp=new String[1];
            tmp[0]=outputGenerationsRaw;
        }
        // Convert everything to int
        HashSet<Integer> ti=new HashSet<Integer>();
        for(String s :tmp)
        {
            ti.add(Integer.parseInt(s));
        }

        return ti;
    }


    private final HashSet<Integer> snapshotsSync;
    private final HashSet<Integer> snapshotsDir;
    private final HashSet<Integer> snapshotsGPF;
    private final HashSet<Integer> global;
    private final int maximumGeneration;

    public SnapshotManager(HashSet<Integer> snapshotsSync, HashSet<Integer> snapshotsDir, HashSet<Integer> snapshotsGPF)
    {
        this.snapshotsSync=new HashSet<Integer>(snapshotsSync);
        this.snapshotsDir= new HashSet<Integer>(snapshotsDir);
        this.snapshotsGPF=new HashSet<Integer>(snapshotsGPF);

        HashSet<Integer> tototal=new HashSet<Integer>();
        for(int i: this.snapshotsSync) tototal.add(i);
        for(int i: this.snapshotsDir) tototal.add(i);
        for(int i: this.snapshotsGPF) tototal.add(i);
        global=tototal;

        int maxg=0;
        for(int i: global)if(i>maxg)maxg=i;

        if(!(maxg>0)) throw new IllegalArgumentException("Invalid snapshots; provide at least one timepoint larger than zero");
        this.maximumGeneration=maxg;
    }

    public int getMaximumGeneration(){return this.maximumGeneration;}
    public boolean recordSync(int generation)
    {
        if(this.snapshotsSync.contains(generation))return true;
        else return false;
    }

    public boolean recordHaplotype(int generation)
    {
        if(this.snapshotsDir.contains(generation))return true;
        else return false;
    }

    public boolean recordGPF(int generation)
    {
        if(this.snapshotsGPF.contains(generation))return true;
        else return false;
    }



}
