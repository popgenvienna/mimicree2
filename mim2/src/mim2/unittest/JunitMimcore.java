package mim2.unittest;


import junit_mimcore.data.Test_FitnessOfEpistaticPair;
import org.junit.runner.*;

import static org.junit.runner.JUnitCore.runClasses;


/**
 * Created by robertkofler on 02/11/2017.
 */
public class JunitMimcore {

  public static void runTests() {

    JUnitCore jc=new JUnitCore();
    jc.addListener(new ExecutionListener());
    Result results=jc.run(Computer.serial(),
            junit_mimcore.data.basic.Test_BitArray.class,
            junit_mimcore.data.basic.Test_BitArrayBuilder.class,
            junit_mimcore.data.basic.Test_Chromosome.class,
            junit_mimcore.data.Test_FitnessFunctionContainer.class,
            junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class,
            junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class,
            junit_mimcore.data.Test_FitnessOfSNP.class,
            junit_mimcore.io.Test_FitnessOfSNPReader.class,
            Test_FitnessOfEpistaticPair.class );



  int i =0;

  }
  //org.junit.runner.JUnitCore.runClasses(junit_mimcore.data.basic.Test_BitArray.class,junit_mimcore.data.basic.Test_BitArrayBuilder.class,junit_mimcore.data.basic.Test_Chromosome.class,
  //           junit_mimcore.data.Test_FitnessFunctionContainer.class, junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class,junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class);
}
