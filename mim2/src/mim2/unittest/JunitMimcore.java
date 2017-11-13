package mim2.unittest;



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
            junit_mimcore.data.basic.Test_SNP.class,
            junit_mimcore.data.basic.Test_SNPCollection.class,
            junit_mimcore.data.Test_FitnessFunctionContainer.class,
            junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class,
            junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class,
            junit_mimcore.data.Test_FitnessOfSNP.class,
            junit_mimcore.data.basic.Test_PACReducer.class,
            junit_mimcore.io.Test_FitnessOfSNPReader.class,
            junit_mimcore.data.Test_FitnessOfEpistaticPair.class,
            junit_mimcore.io.Test_EpistasisFitnessReader.class,
            junit_mimcore.data.Test_AdditiveSNPEffect.class,
            junit_mimcore.io.Test_SNPQuantitativeEffectSizeReader.class,
            junit_mimcore.io.Test_HaplotypeReader.class,
            junit_mimcore.io.Test_RecombinationRateReader.class,
            junit_mimcore.io.Test_MigrationRegimeReader.class);



  int i =0;

  }
  //org.junit.runner.JUnitCore.runClasses(junit_mimcore.data.basic.Test_BitArray.class,junit_mimcore.data.basic.Test_BitArrayBuilder.class,junit_mimcore.data.basic.Test_Chromosome.class,
  //           junit_mimcore.data.Test_FitnessFunctionContainer.class, junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class,junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class);
}
