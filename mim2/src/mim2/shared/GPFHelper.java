package mim2.shared;

import mimcore.data.DiploidGenome;
import mimcore.data.gpf.quantitative.GenotypeCalculator;
import mimcore.data.gpf.quantitative.IGenotypeCalculator;
import mimcore.data.gpf.quantitative.PhenotypeCalculator;
import mimcore.data.sex.Sex;
import mimcore.data.sex.SexAssigner;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 25/10/2017.
 */
public class GPFHelper {

    public static PhenotypeCalculator getPhenotypeCalculator(ArrayList<DiploidGenome> dipGenomes, ArrayList<Sex> sexes, IGenotypeCalculator genotypeCalculator, Double ve, Double heritability, Logger logger)
    {
        // if environmental variance exists than use it directly
        if(ve!=null) {
            logger.info("Enviromental variance was provided; Will proceed with VE="+ve);
            return new PhenotypeCalculator(ve);
        }

        logger.info("No environmental variance was provided; Will compute VE from the heritability and the genotypic variance in the base population");

        // if not compute the environmental variance from the heritability
        ArrayList<Double> genotypes=new ArrayList<Double>();
        LinkedList<Sex> sl=new LinkedList<Sex>(sexes);
        for(DiploidGenome dg: dipGenomes)
        {
            genotypes.add(genotypeCalculator.getGenotype(dg,sl.remove(0)));
        }

        double mean=0.0;
        for(double d: genotypes)
        {
            mean+=d;
        }
        mean= mean/((double)genotypes.size());

        double variance=0.0;
        for(double d: genotypes)
        {
            double ss=Math.pow((d - mean), 2.0);
            variance+=ss;
        }
        variance= variance/((double)genotypes.size());
        // now we have the mean and the variance of the genotypes;


        double vecompute=PhenotypeCalculator.computeVEfromVGandH2(variance,heritability);

        logger.info("Estimated genotypic variance VG="+variance+" ; Using a heritability h2="+heritability+" ; Will proceed with VE="+vecompute);
        return new PhenotypeCalculator(vecompute);
    }
}
