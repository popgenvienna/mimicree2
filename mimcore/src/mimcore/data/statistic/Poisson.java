package mimcore.data.statistic;

import java.util.Random;

/**
 * Created by robertkofler on 14/12/2017.
 */
public class Poisson {

    public static int getPoisson(double lambda, Random random) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= random.nextDouble();
        } while (p > L);

        return k - 1;
    }
}
