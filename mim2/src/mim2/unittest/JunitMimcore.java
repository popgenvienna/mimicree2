package mim2.unittest;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.*;

import static org.junit.runner.JUnitCore.runClasses;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;


/**
 * Created by robertkofler on 02/11/2017.
 */
public class JunitMimcore {

  public static void runTests() {

    JUnitCore jc=new JUnitCore();
    jc.addListener(new ExecutionListener());
    Result results=jc.run(Computer.serial(),junit_mimcore.data.basic.Test_BitArray.class, junit_mimcore.data.basic.Test_BitArrayBuilder.class, junit_mimcore.data.basic.Test_Chromosome.class,
            junit_mimcore.data.Test_FitnessFunctionContainer.class, junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class, junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class);



  int i =0;

  }
  //org.junit.runner.JUnitCore.runClasses(junit_mimcore.data.basic.Test_BitArray.class,junit_mimcore.data.basic.Test_BitArrayBuilder.class,junit_mimcore.data.basic.Test_Chromosome.class,
  //           junit_mimcore.data.Test_FitnessFunctionContainer.class, junit_mimcore.data.Test_FitnessFunctionQuantitativeGauss.class,junit_mimcore.data.Test_FitnessFunctionArbitraryLandscape.class);
}