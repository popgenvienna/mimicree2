package mimcore.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/29/15.
 */
public class MimicreeLogFactory {

	private static Logger nullLogger=null;
	private static Logger popte2Logger=null;
	/*
	private constructor; not instances
	 */
	private MimicreeLogFactory(){}


	/**
	 * Default logger for PoPoolation TE2
	 * @param detailedLog
	 * @return
	 */
	public static Logger getLogger(boolean detailedLog) {
		if(popte2Logger==null) {
			Logger logger = Logger.getLogger("MimicrEE2 Logger");
			java.util.logging.ConsoleHandler tehandler = new java.util.logging.ConsoleHandler();
			tehandler.setLevel(Level.INFO);
			if (detailedLog) tehandler.setLevel(Level.FINEST);
			tehandler.setFormatter(new MimicreeLogFormatter());
			logger.addHandler(tehandler);
			logger.setUseParentHandlers(false);
			logger.setLevel(Level.ALL);
			popte2Logger=logger;
		}
		return popte2Logger;
	}

	/**
	 * Return a logger which is not producing output
	 * Useful for testing purposes
	 * @return
	 */
	public static Logger getNullLogger()
	{
		if(nullLogger ==null)
		{
			Logger logger = Logger.getLogger("MimicrEE2 debug logger");
			java.util.logging.ConsoleHandler tehandler = new java.util.logging.ConsoleHandler();
			tehandler.setLevel(Level.OFF);
			tehandler.setFormatter(new MimicreeLogFormatter());
			logger.addHandler(tehandler);
			logger.setUseParentHandlers(false);
			logger.setLevel(Level.OFF);
			nullLogger=logger;
		}

		return nullLogger;
	}


}
