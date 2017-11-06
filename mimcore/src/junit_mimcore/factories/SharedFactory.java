package junit_mimcore.factories;

import mimcore.data.Chromosome;
import mimcore.data.GenomicPosition;
import mimcore.data.haplotypes.SNP;

import java.util.logging.Logger;

/**
 * Created by robertkofler on 06/11/2017.
 */
public class SharedFactory {
    private static Logger logger;

    // Static constructor
    static
    {
        // Create logger
        logger=java.util.logging.Logger.getLogger("MimicrEE2 Null Logger");
        logger.setUseParentHandlers(false);
    }



    public static Logger getNullLogger()
    {
        return logger;
    }
}
