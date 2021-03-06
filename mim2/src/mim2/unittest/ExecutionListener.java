package mim2.unittest;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import sun.security.krb5.internal.crypto.Des;

/**
 * Created by robertkofler on 02/11/2017.
 */
public class ExecutionListener extends RunListener {


    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     */
    public void testFinished(Description description) throws java.lang.Exception {
       // System.out.println("OK:  " + description.getMethodName()+ "    ("+description.getClassName()+")");
        System.out.println("OK:  " +description.getClassName()+ " - "+ description.getMethodName());


    }

    /**
     * Called when an atomic test fails.
     */
    public void testFailure(Failure failure) throws java.lang.Exception {
        Description description=failure.getDescription();
        System.out.println("Execution of test case failed: "+description.getClassName()+ " " + description.getMethodName()+" "+ failure.getMessage());
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated with Ignore.
     */
    public void testIgnored(Description description) throws java.lang.Exception {
        System.out.println("Execution of test case ignored : " + description.getMethodName());
    }

    public void testRunFinished(Result result) throws java.lang.Exception {
        System.out.println("\nFinished testing");
        System.out.println(result.getFailureCount()+ " tests failed");
        System.out.println(result.getRunCount() +" tests were OK");
    }
}