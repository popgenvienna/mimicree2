package junit_mimcore.data;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.Population;
import mimcore.data.Specimen;
import mimcore.data.gpf.fitness.FitnessOfSNP;
import mimcore.data.gpf.survival.SelectionRegimeReplicateSpecific;
import mimcore.data.gpf.survival.SurvivalRegimeTruncatingSelection;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


/**
 * Created by robertkofler on 30/10/2017.
 */
public class Test_SurvivalRegimeTruncatingSelection {
    public static Population getPopulation()
    {
        ArrayList<Specimen> specs=new ArrayList<Specimen>();
        specs.add(new Specimen(0.0,0.1,0.0,null));
        specs.add(new Specimen(0.0,0.2,0.0,null));
        specs.add(new Specimen(0.0,0.3,0.0,null));
        specs.add(new Specimen(0.0,0.4,0.0,null));
        specs.add(new Specimen(0.0,0.5,0.0,null));
        specs.add(new Specimen(0.0,0.6,0.0,null));
        specs.add(new Specimen(0.0,0.7,0.0,null));
        specs.add(new Specimen(0.0,0.8,0.0,null));
        specs.add(new Specimen(0.0,0.9,0.0,null));
        specs.add(new Specimen(0.0,1.0,0.0,null));
        return new Population(specs);

    }


    @Test
    public void generation_1_survivors_08() {
            Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.8);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),8);
    }

    @Test
    public void generation_1_survivors_m08() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,-0.8);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),8);
    }

    @Test
    public void generation_1_survivors_05() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.5);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),5);
    }

    @Test
    public void generation_1_survivors_01() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.1);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),1);
    }

    @Test
    public void generation_1_survivors_m01() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.1);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),1);
    }


    @Test
    public void generation_10_survivors_01() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.1);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,10,1);
        assertEquals(ps.size(),1);
    }

    @Test
    public void generation_2_survivors_01() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.1);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,2,1);
        assertEquals(ps.size(),1);
    }

    @Test
    public void correct_survivor_one_survivor() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.1);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,2,1);
        assertEquals(ps.getSpecimen().get(0).quantPhenotype(),1.0,0.0000001);
        assertEquals(ps.getSpecimen().get(0).quantGenotype(),0.0,0.0000001);
        assertEquals(ps.getSpecimen().get(0).fitness(),0.0,0.0000001);
    }


    @Test
    public void correct_survivors_three_survivors() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,0.3);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),3);
        assertEquals(ps.getSpecimen().get(0).quantPhenotype(),1.0,0.0000001);
        assertEquals(ps.getSpecimen().get(1).quantPhenotype(),0.9,0.0000001);
        assertEquals(ps.getSpecimen().get(2).quantPhenotype(),0.8,0.0000001);

    }


    @Test
    public void correct_survivors_three_survivors_minus() {
        Population p=getPopulation();
        ArrayList<HashMap<Integer,Double>> sr=new ArrayList<HashMap<Integer,Double>>();
        sr.add(new HashMap<Integer,Double>());
        sr.get(0).put(1,-0.3);
        SurvivalRegimeTruncatingSelection srt=new SurvivalRegimeTruncatingSelection(new SelectionRegimeReplicateSpecific(sr));

        Population ps=srt.getSurvivors(p,1,1);
        assertEquals(ps.size(),3);
        assertEquals(ps.getSpecimen().get(0).quantPhenotype(),0.1,0.0000001);
        assertEquals(ps.getSpecimen().get(1).quantPhenotype(),0.2,0.0000001);
        assertEquals(ps.getSpecimen().get(2).quantPhenotype(),0.3,0.0000001);

    }







}
