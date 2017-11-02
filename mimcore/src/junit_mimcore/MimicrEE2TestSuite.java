package junit_mimcore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author robertkofler
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({junit_mimcore.data.basic.Test_BitArray.class,junit_mimcore.data.basic.Test_BitArrayBuilder.class,junit_mimcore.data.basic.Test_Chromosome.class,
junit_mimcore.data.Test_FitnessFunctionContainer.class, junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class,junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class})
public class MimicrEE2TestSuite {

}
