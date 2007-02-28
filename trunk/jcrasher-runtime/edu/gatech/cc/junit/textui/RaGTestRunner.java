/*
 * RaGTestRunner.java
 * 
 * Copyright 2003 Christoph Csallner and Yannis Smaragdakis.
 */
package edu.gatech.cc.junit.textui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestResult;
import junit.runner.TestSuiteLoader;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;
import edu.gatech.cc.junit.FilteringTestCase;
import edu.gatech.cc.junit.NoExitSecurityManager;
import edu.gatech.cc.junit.framework.GroupedTestResult;
import edu.gatech.cc.junit.reinit.CopyCLinitClassLoader;

/**
 * RaGTestRunner
 * 
 * Re-Initializing and Grouping JUnit TestRunner.
 * Extends the junit.textui.TestRunner without modifications.
 * Requires the JCrasher Runtime.
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 * @version	$Id: $
 */
public class RaGTestRunner extends TestRunner {

	public static final String ANNOTATED_LIST_FILENAME = "annotated.txt";  
	public static boolean DO_REINIT = true;
	
	public static final int GROUP_CLASSIC = 1;  //SPE: same stack-trace down to test0.
	public static final int GROUP_FOCUSED = 2;  //same top-element.
	public static int GROUP_MODE = GROUP_CLASSIC;
	
	
	
	/**
	 * Constructors
	 */
	public RaGTestRunner(ResultPrinter printer) {	//called third, calls super.
		super(printer);
	}	
	public RaGTestRunner(PrintStream writer) {		//called second.
		this(new GroupedResultPrinter(writer));
	}
	public RaGTestRunner() {									//called first, by main.
		this(System.out);												// default out-print-stream
	}
	
	
	@Override
	protected TestResult createTestResult() {
		return new GroupedTestResult();
	}


	@Override
	public TestSuiteLoader getLoader() {
		/* Added check for global flag.
		 * Old version always returned CopyCLinitClassLoader. */
		if (DO_REINIT) 
			return new CopyCLinitClassLoader();
		
		return super.getLoader();
	}
	
	/*
	 * Where JUnit writes exceptions to
	 */	
	protected PrintStream getExceptionsStream() {
		return System.out;
	}

	/*
	 * Where RaGTestRunner writes results to
	 */	 	
	protected PrintStream getResultsStream() {
		return System.out;
	}
	
	
	/*
	 * Stuff from main - to be reused by subclasses.
	 */
	protected void run(String args[]) {
		setPrinter(new GroupedResultPrinter(getExceptionsStream()));
		
		/* Test run time measurement. */
		long startTime= System.currentTimeMillis();

		/* Adapted from super. */		
		GroupedTestResult r = null;
		try {
			r = (GroupedTestResult) start(args);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			//System.exit(EXCEPTION_EXIT);
		}
		
		/* Test run time measurement. */
		long endTime= System.currentTimeMillis();
		long runTime= endTime-startTime;
				
		//test-class; #tests run; #filtered errors or exceptions; #total reported e or e; exe [ms];	
		getResultsStream().println("Suite name: " +args[0]);
		if (r!=null) {
			//getResultsStream().println("Test cases run: " +r.runCount());
			getResultsStream().println("Exceptions and Errors after filtering (E): " +r.prototypeFailureCount());
			getResultsStream().println("Exceptions and Errors total (e):  " +r.errorCount());
		}
		getResultsStream().println("Run time: " +runTime +"ms");
//		getResultsStream().println(
//				";"+args[0]+
//				";"+r.runCount()+
//				";"+r.prototypeFailureCount()+
//				";"+r.errorCount()+
//				";"+runTime
//			);
	}		
	
	
	/**
	 * Convert all parameter types to simple names
	 */
	protected static String simpleParamTypes(String line) {
		String res = line.substring(0, line.indexOf('(')+1);
		String paramString = line.substring(line.indexOf('(')+1, line.length()-1);
		String[] params = paramString.split(",");
		for (int i=0; i<params.length; i++) {
			int dot = params[i].lastIndexOf('.');
			if (dot>0) {
				params[i] = params[i].substring(dot+1);
			}
			if (i>0) {res+=",";}
			res += params[i]; 
		}
		return res +")";
	}
	
	
	/*
	 * Read annotated.txt
	 * Convert param type to simple names
	 */
	protected static void parseAnnotatedList() {
		try {
			BufferedReader file = new BufferedReader(
					new FileReader(ANNOTATED_LIST_FILENAME));
			String line = file.readLine();
			while (line != null) {
				FilteringTestCase.ANNOTATED_LIST.add(simpleParamTypes(line));
				line = file.readLine();
			}
		}
		catch (IOException  e) {
			System.out.println("Could not open "+ANNOTATED_LIST_FILENAME);
		}
	}
	
	
	/**
	 * Main
	 */
	public static void main(String args[]) {
		System.setSecurityManager(new NoExitSecurityManager()); //forbid System.exit(int)
		
		/* we interpret first arguments only. */
		boolean foundArg = true;
		
		while (args.length>0 && foundArg) {
			foundArg = false;
			
			/* parse annotated.txt and don't use heuristic for these methods */
			if (args[0].equals("-annotated")) {
				foundArg = true;
				parseAnnotatedList();
			}
			if (args[0].equals("-directCallOnly")) {  //restrict reporting to exceptions thrown by testee
				FilteringTestCase.DIRECT_CALL_ONLY = true;
				foundArg = true;
			}						
			if (args[0].equals("-noreinit")) {
				DO_REINIT = false;  //turn off reinitialization
				foundArg = true;
			}
			
			if (args[0].equals("-focus")) {
				GROUP_MODE = GROUP_FOCUSED;
				foundArg = true;
			}			
			if (args[0].equals("-all")) {
				FilteringTestCase.FILTER_MODE = FilteringTestCase.REPORT_ALL;
				foundArg = true;
			}			
			if (args[0].equals("-selective")) {
				FilteringTestCase.FILTER_MODE = FilteringTestCase.REPORT_SELECTIVE;
				foundArg = true;
			}
			if (args[0].equals("-showErrors")) {  //show java.lang.Error to user
				FilteringTestCase.SHOW_ERRORS = true;
				foundArg = true;
			}		
			if (foundArg) {  //remove from list.
				String[] allArgs = args;
				args = new String[allArgs.length-1];
				System.arraycopy(allArgs, 1, args, 0, args.length);				
			}
		}
		new RaGTestRunner().run(args);
	}
}