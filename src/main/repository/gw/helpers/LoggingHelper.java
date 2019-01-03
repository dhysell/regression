package repository.gw.helpers;

import org.slf4j.MDC;

/**
 * Class to handle setting/removing MDC on per test case basis. This helps
 * us log each test case into it's own log file. Please see
 * @link <a href="http://logback.qos.ch/manual/appenders.html#SiftingAppender">Sifting Appender Documentation</a>
 * @link <a href="http://logback.qos.ch/manual/mdc.html">Mapped Diagnostic Context Documentation</a>
 */
public class LoggingHelper {
	public static final String TEST_NAME = "testname";
	 
	/**
	 * Adds the test name to Mapped Diagnostic Context so that the Sifting Appender can use it and log the new
	 * log events to different files
	 * @param name name of the new log file
	 * @throws Exception
	 */
	public static void startTestLogging(String name) {
		MDC.put(TEST_NAME, name);
	}
	 
	/**
	 * Removes the key (log file name) from Mapped Diagnostic Context
	 * @return name of the log file, if one existed in Mapped Diagnostic Context
	 */
	public static String stopTestLogging() {
		String name = MDC.get(TEST_NAME);
		MDC.remove(TEST_NAME);
		return name;
	}
	
	public static String getLoggerInstanceName() {
		return MDC.get(TEST_NAME);
	}
}
