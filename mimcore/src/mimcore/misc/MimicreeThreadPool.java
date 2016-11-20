package mimcore.misc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class MimicreeThreadPool {

	private MimicreeThreadPool(){}

	
	private static int threadCount;
	private static ExecutorService executor  =null;
	private static ArrayList<Random> randoms=null;

	public static int getThreads()
	{
		return threadCount;	
	}
	
	
	public static ExecutorService getExector()
	{
		if(executor==null)throw new IllegalArgumentException("qMimicrEEThreadPool needs to be initated before usage");
		return executor;
	}
	
	public static void setThreads(int threads)
	{
		threadCount=threads;
		randoms=new ArrayList<Random>();
		for(int i=0; i<threads; i++) randoms.add(new Random(System.currentTimeMillis()+i));
		executor=Executors.newFixedThreadPool(threads);
	}

	public static Random getRandomForThread(int count)
	{
		int index=count%threadCount;
		return randoms.get(index);
	}

	
}
